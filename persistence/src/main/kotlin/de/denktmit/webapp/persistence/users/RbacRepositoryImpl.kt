package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.jooq.generated.tables.references.*
import de.denktmit.webapp.persistence.jooqRead
import de.denktmit.webapp.persistence.jooqWrite
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Record2
import org.jooq.Records
import org.jooq.impl.DSL.arrayAggDistinct
import org.jooq.impl.DSL.row
import org.jooq.util.postgres.PostgresDSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
@Transactional(readOnly = true)
class RbacRepositoryImpl(
    @PersistenceContext
    private val em: EntityManager,
) : RbacRepository {

    override fun findOneByMail(mail: String): RbacMapping? {
        return findAllByMails(setOf(mail)).firstOrNull()
    }

    override fun findAllByMails(mails: Set<String>): List<RbacMapping> {
        // https://stackoverflow.com/questions/63686796/executing-jooq-statements-inside-hibernate-transaction
        val rbacMapping = em.jooqRead { dslContext ->

            val groupsField: Field<Record2<Long?, String?>> = PostgresDSL.rowField(
                row(GROUPS.GROUP_ID, GROUPS.GROUP_NAME)
            )
            val authoritiesField: Field<Record2<Long?, String?>> = PostgresDSL.rowField(
                row(AUTHORITIES.AUTHORITY_ID, AUTHORITIES.AUTHORITY)
            )

            dslContext.select(
                row(*USERS.fields()),
                arrayAggDistinct(groupsField),
                arrayAggDistinct(authoritiesField),
            ).from(USERS)
                .leftJoin(GROUP_MEMBERS).on(USERS.USER_ID.eq(GROUP_MEMBERS.USER_ID))
                .leftJoin(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.GROUP_ID))
                .leftJoin(GROUP_AUTHORITIES).on(GROUPS.GROUP_ID.eq(GROUP_AUTHORITIES.GROUP_ID))
                .leftJoin(AUTHORITIES).on(GROUP_AUTHORITIES.AUTHORITY_ID.eq(AUTHORITIES.AUTHORITY_ID))
                .where(USERS.MAIL.`in`(mails))
                .groupBy(*USERS.fields())
                .fetch(Records.mapping { userRecord, groupRecords, authRecords ->
                    rbacMapping(userRecord, groupRecords, authRecords)
                })
        }

        return rbacMapping
    }

    private fun rbacMapping(
        userRecord: Record,
        groupRecords: Array<Record2<Long?, String?>>,
        authRecords: Array<Record2<Long?, String?>>
    ): RbacMapping {
        val user = userRecord.into(User::class.java)
        val groups = groupRecords.mapNotNull { record ->
            if (record.component1() != null && record.component2() != null) {
                record.into(Group::class.java)
            } else {
                null
            }
        }.toSet()
        val auths = authRecords.mapNotNull { record ->
            if (record.component1() != null && record.component2() != null) {
                record.into(Authority::class.java)
            } else {
                null
            }
        }.toSet()
        return RbacMapping(user, groups, auths)
    }

    @Transactional
    override fun setUserGroups(mail: String, groupNames: Set<String>) {
        em.jooqWrite { dslContext ->
            dslContext
                .deleteFrom(GROUP_MEMBERS)
                .whereExists(dslContext
                    .selectOne()
                    .from(USERS)
                    .where(USERS.MAIL.eq(mail))
                    .and(GROUP_MEMBERS.USER_ID.eq(USERS.USER_ID)))
                .execute()

            dslContext
                .insertInto(GROUP_MEMBERS, GROUP_MEMBERS.USER_ID, GROUP_MEMBERS.GROUP_ID)
                .select(
                    dslContext.select(USERS.USER_ID, GROUPS.GROUP_ID)
                        .from(USERS)
                        .crossJoin(GROUPS)
                        .where(USERS.MAIL.eq(mail))
                        .and(GROUPS.GROUP_NAME.`in`(groupNames))
                ).execute()
        }
    }
}

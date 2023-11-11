package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.jooq.generated.tables.references.*
import de.denktmit.webapp.persistence.jooqRead
import de.denktmit.webapp.persistence.users.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.DSL.arrayAggDistinct
import org.jooq.impl.DSL.row
import org.jooq.util.postgres.PostgresDSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class RbacRepositoryImpl(
    @PersistenceContext
    private val em: EntityManager,
) : RbacRepository {

    @Transactional
    override fun findByMail(mail: String): RbacMapping? {
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
                .where(USERS.MAIL.eq(mail))
                .groupBy(*USERS.fields())
                .fetchOne(Records.mapping { userRecord, groupRecords, authRecords ->
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
        val groups = groupRecords.map {
            it.into(Group::class.java)
        }.toSet()
        val auths = authRecords.map {
            it.into(Authority::class.java)
        }.toSet()
        return RbacMapping(user, groups, auths)
    }

}

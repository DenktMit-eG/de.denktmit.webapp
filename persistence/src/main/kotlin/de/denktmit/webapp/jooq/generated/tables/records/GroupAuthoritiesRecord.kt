/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables.records


import de.denktmit.webapp.jooq.generated.tables.GroupAuthoritiesTable
import de.denktmit.webapp.jooq.generated.tables.interfaces.IGroupAuthorities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

import org.jooq.Record2
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "group_authorities",
    schema = "public"
)
open class GroupAuthoritiesRecord() : UpdatableRecordImpl<GroupAuthoritiesRecord>(GroupAuthoritiesTable.GROUP_AUTHORITIES), IGroupAuthorities {

    @get:Column(name = "group_id", nullable = false)
    @get:NotNull
    open override var groupId: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    @get:Column(name = "authority_id", nullable = false)
    @get:NotNull
    open override var authorityId: Long?
        set(value): Unit = set(1, value)
        get(): Long? = get(1) as Long?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record2<Long?, Long?> = super.key() as Record2<Long?, Long?>

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IGroupAuthorities) {
        this.groupId = from.groupId
        this.authorityId = from.authorityId
        resetChangedOnNotNull()
    }

    override fun <E : IGroupAuthorities> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised GroupAuthoritiesRecord
     */
    constructor(groupId: Long? = null, authorityId: Long? = null): this() {
        this.groupId = groupId
        this.authorityId = authorityId
        resetChangedOnNotNull()
    }
}

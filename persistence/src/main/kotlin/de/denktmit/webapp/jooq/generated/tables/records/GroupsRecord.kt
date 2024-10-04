/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables.records


import de.denktmit.webapp.jooq.generated.tables.GroupsTable
import de.denktmit.webapp.jooq.generated.tables.interfaces.IGroups

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record2
import org.jooq.Row2
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "groups",
    schema = "public"
)
open class GroupsRecord() : UpdatableRecordImpl<GroupsRecord>(GroupsTable.GROUPS), Record2<Long?, String?>, IGroups {

    @get:Id
    @get:Column(name = "group_id", nullable = false)
    @get:NotNull
    open override var groupId: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    @get:Column(name = "group_name", nullable = false, length = 50)
    @get:NotNull
    @get:Size(max = 50)
    open override var groupName: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>
    override fun valuesRow(): Row2<Long?, String?> = super.valuesRow() as Row2<Long?, String?>
    override fun field1(): Field<Long?> = GroupsTable.GROUPS.GROUP_ID
    override fun field2(): Field<String?> = GroupsTable.GROUPS.GROUP_NAME
    override fun component1(): Long? = groupId
    override fun component2(): String? = groupName
    override fun value1(): Long? = groupId
    override fun value2(): String? = groupName

    override fun value1(value: Long?): GroupsRecord {
        set(0, value)
        return this
    }

    override fun value2(value: String?): GroupsRecord {
        set(1, value)
        return this
    }

    override fun values(value1: Long?, value2: String?): GroupsRecord {
        this.value1(value1)
        this.value2(value2)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IGroups) {
        this.groupId = from.groupId
        this.groupName = from.groupName
        resetChangedOnNotNull()
    }

    override fun <E : IGroups> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised GroupsRecord
     */
    constructor(groupId: Long? = null, groupName: String? = null): this() {
        this.groupId = groupId
        this.groupName = groupName
        resetChangedOnNotNull()
    }
}

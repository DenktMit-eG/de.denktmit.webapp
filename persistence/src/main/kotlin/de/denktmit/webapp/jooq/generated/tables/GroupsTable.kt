/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables


import de.denktmit.webapp.jooq.generated.Public
import de.denktmit.webapp.jooq.generated.keys.GROUPS_PKEY
import de.denktmit.webapp.jooq.generated.tables.records.GroupsRecord

import java.util.function.Function

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row2
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GroupsTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, GroupsRecord>?,
    aliased: Table<GroupsRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<GroupsRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.groups</code>
         */
        val GROUPS: GroupsTable = GroupsTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<GroupsRecord> = GroupsRecord::class.java

    /**
     * The column <code>public.groups.group_id</code>. Unique primary key for
     * the Group
     */
    val GROUP_ID: TableField<GroupsRecord, Long?> = createField(DSL.name("group_id"), SQLDataType.BIGINT.nullable(false), this, "Unique primary key for the Group")

    /**
     * The column <code>public.groups.group_name</code>. Group's unique name
     */
    val GROUP_NAME: TableField<GroupsRecord, String?> = createField(DSL.name("group_name"), SQLDataType.VARCHAR(50).nullable(false), this, "Group's unique name")

    private constructor(alias: Name, aliased: Table<GroupsRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<GroupsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.groups</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.groups</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.groups</code> table reference
     */
    constructor(): this(DSL.name("groups"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, GroupsRecord>): this(Internal.createPathAlias(child, key), child, key, GROUPS, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<GroupsRecord> = GROUPS_PKEY
    override fun `as`(alias: String): GroupsTable = GroupsTable(DSL.name(alias), this)
    override fun `as`(alias: Name): GroupsTable = GroupsTable(alias, this)
    override fun `as`(alias: Table<*>): GroupsTable = GroupsTable(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): GroupsTable = GroupsTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): GroupsTable = GroupsTable(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): GroupsTable = GroupsTable(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}

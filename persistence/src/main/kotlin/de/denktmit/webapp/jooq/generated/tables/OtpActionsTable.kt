/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables


import de.denktmit.webapp.jooq.generated.Public
import de.denktmit.webapp.jooq.generated.keys.OTP_ACTIONS_PKEY
import de.denktmit.webapp.jooq.generated.tables.records.OtpActionsRecord

import java.time.Instant
import java.util.UUID
import java.util.function.Function

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row5
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
open class OtpActionsTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, OtpActionsRecord>?,
    aliased: Table<OtpActionsRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<OtpActionsRecord>(
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
         * The reference instance of <code>public.otp_actions</code>
         */
        val OTP_ACTIONS: OtpActionsTable = OtpActionsTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<OtpActionsRecord> = OtpActionsRecord::class.java

    /**
     * The column <code>public.otp_actions.otp_action_id</code>. Unique
     * identifier for the OTP
     */
    val OTP_ACTION_ID: TableField<OtpActionsRecord, UUID?> = createField(DSL.name("otp_action_id"), SQLDataType.UUID.nullable(false), this, "Unique identifier for the OTP")

    /**
     * The column <code>public.otp_actions.target</code>. The actions target
     * descriptor, e.g. 'User'
     */
    val TARGET: TableField<OtpActionsRecord, String?> = createField(DSL.name("target"), SQLDataType.VARCHAR(255).nullable(false), this, "The actions target descriptor, e.g. 'User'")

    /**
     * The column <code>public.otp_actions.action</code>. The action descriptor,
     * e.g. 'activate'
     */
    val ACTION: TableField<OtpActionsRecord, String?> = createField(DSL.name("action"), SQLDataType.VARCHAR(25).nullable(false), this, "The action descriptor, e.g. 'activate'")

    /**
     * The column <code>public.otp_actions.identifier</code>. The actions target
     * identifier, most likely a database id
     */
    val IDENTIFIER: TableField<OtpActionsRecord, Long?> = createField(DSL.name("identifier"), SQLDataType.BIGINT.nullable(false), this, "The actions target identifier, most likely a database id")

    /**
     * The column <code>public.otp_actions.valid_until</code>. Timestamp when
     * the OTP expires
     */
    val VALID_UNTIL: TableField<OtpActionsRecord, Instant?> = createField(DSL.name("valid_until"), SQLDataType.INSTANT.nullable(false), this, "Timestamp when the OTP expires")

    private constructor(alias: Name, aliased: Table<OtpActionsRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<OtpActionsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.otp_actions</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.otp_actions</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.otp_actions</code> table reference
     */
    constructor(): this(DSL.name("otp_actions"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, OtpActionsRecord>): this(Internal.createPathAlias(child, key), child, key, OTP_ACTIONS, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<OtpActionsRecord> = OTP_ACTIONS_PKEY
    override fun `as`(alias: String): OtpActionsTable = OtpActionsTable(DSL.name(alias), this)
    override fun `as`(alias: Name): OtpActionsTable = OtpActionsTable(alias, this)
    override fun `as`(alias: Table<*>): OtpActionsTable = OtpActionsTable(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): OtpActionsTable = OtpActionsTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): OtpActionsTable = OtpActionsTable(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): OtpActionsTable = OtpActionsTable(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row5<UUID?, String?, String?, Long?, Instant?> = super.fieldsRow() as Row5<UUID?, String?, String?, Long?, Instant?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (UUID?, String?, String?, Long?, Instant?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (UUID?, String?, String?, Long?, Instant?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}

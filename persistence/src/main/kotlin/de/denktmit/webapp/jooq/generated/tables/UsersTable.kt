/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables


import de.denktmit.webapp.jooq.generated.Public
import de.denktmit.webapp.jooq.generated.keys.GROUP_MEMBERS__FK_GROUP_MEMBER_USER
import de.denktmit.webapp.jooq.generated.keys.OTP_ACTIONS__FK_OTP_ACTION_USER
import de.denktmit.webapp.jooq.generated.keys.USERS_PKEY
import de.denktmit.webapp.jooq.generated.tables.GroupMembersTable.GroupMembersPath
import de.denktmit.webapp.jooq.generated.tables.GroupsTable.GroupsPath
import de.denktmit.webapp.jooq.generated.tables.OtpActionsTable.OtpActionsPath
import de.denktmit.webapp.jooq.generated.tables.records.UsersRecord

import java.time.Instant

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Check
import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
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
open class UsersTable(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, UsersRecord>?,
    parentPath: InverseForeignKey<out Record, UsersRecord>?,
    aliased: Table<UsersRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<UsersRecord>(
    alias,
    Public.PUBLIC,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>public.users</code>
         */
        val USERS: UsersTable = UsersTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UsersRecord> = UsersRecord::class.java

    /**
     * The column <code>public.users.user_id</code>. Unique primary key for the
     * user
     */
    val USER_ID: TableField<UsersRecord, Long?> = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "Unique primary key for the user")

    /**
     * The column <code>public.users.mail</code>. User's unique email address
     */
    val MAIL: TableField<UsersRecord, String?> = createField(DSL.name("mail"), SQLDataType.VARCHAR(255).nullable(false), this, "User's unique email address")

    /**
     * The column <code>public.users.mail_verified</code>. Flag to track, if
     * User's e-mail been verified
     */
    val MAIL_VERIFIED: TableField<UsersRecord, Boolean?> = createField(DSL.name("mail_verified"), SQLDataType.BOOLEAN.nullable(false), this, "Flag to track, if User's e-mail been verified")

    /**
     * The column <code>public.users.password</code>. User's hashed or encrypted
     * password
     */
    val PASSWORD: TableField<UsersRecord, String?> = createField(DSL.name("password"), SQLDataType.VARCHAR(500).nullable(false), this, "User's hashed or encrypted password")

    /**
     * The column <code>public.users.disabled</code>. User's disabled status
     * (true or false) based on administrative actions
     */
    val DISABLED: TableField<UsersRecord, Boolean?> = createField(DSL.name("disabled"), SQLDataType.BOOLEAN.nullable(false), this, "User's disabled status (true or false) based on administrative actions")

    /**
     * The column <code>public.users.locked_until</code>. User's locked status
     * (true or false) based on temporary circumstances, e.g. failed logins
     */
    val LOCKED_UNTIL: TableField<UsersRecord, Instant?> = createField(DSL.name("locked_until"), SQLDataType.INSTANT.nullable(false), this, "User's locked status (true or false) based on temporary circumstances, e.g. failed logins")

    /**
     * The column <code>public.users.account_valid_until</code>. User's account
     * expiry date, e.g. for paid time-limited access
     */
    val ACCOUNT_VALID_UNTIL: TableField<UsersRecord, Instant?> = createField(DSL.name("account_valid_until"), SQLDataType.INSTANT.nullable(false), this, "User's account expiry date, e.g. for paid time-limited access")

    /**
     * The column <code>public.users.credentials_valid_until</code>. User's
     * credentials expiry date, e.g. to enforce password change after some time
     */
    val CREDENTIALS_VALID_UNTIL: TableField<UsersRecord, Instant?> = createField(DSL.name("credentials_valid_until"), SQLDataType.INSTANT.nullable(false), this, "User's credentials expiry date, e.g. to enforce password change after some time")

    private constructor(alias: Name, aliased: Table<UsersRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<UsersRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<UsersRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.users</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.users</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.users</code> table reference
     */
    constructor(): this(DSL.name("users"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, UsersRecord>?, parentPath: InverseForeignKey<out Record, UsersRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, USERS, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class UsersPath : UsersTable, Path<UsersRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, UsersRecord>?, parentPath: InverseForeignKey<out Record, UsersRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<UsersRecord>): super(alias, aliased)
        override fun `as`(alias: String): UsersPath = UsersPath(DSL.name(alias), this)
        override fun `as`(alias: Name): UsersPath = UsersPath(alias, this)
        override fun `as`(alias: Table<*>): UsersPath = UsersPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<UsersRecord> = USERS_PKEY

    private lateinit var _groupMembers: GroupMembersPath

    /**
     * Get the implicit to-many join path to the
     * <code>public.group_members</code> table
     */
    fun groupMembers(): GroupMembersPath {
        if (!this::_groupMembers.isInitialized)
            _groupMembers = GroupMembersPath(this, null, GROUP_MEMBERS__FK_GROUP_MEMBER_USER.inverseKey)

        return _groupMembers;
    }

    val groupMembers: GroupMembersPath
        get(): GroupMembersPath = groupMembers()

    private lateinit var _otpActions: OtpActionsPath

    /**
     * Get the implicit to-many join path to the <code>public.otp_actions</code>
     * table
     */
    fun otpActions(): OtpActionsPath {
        if (!this::_otpActions.isInitialized)
            _otpActions = OtpActionsPath(this, null, OTP_ACTIONS__FK_OTP_ACTION_USER.inverseKey)

        return _otpActions;
    }

    val otpActions: OtpActionsPath
        get(): OtpActionsPath = otpActions()

    /**
     * Get the implicit many-to-many join path to the <code>public.groups</code>
     * table
     */
    val groups: GroupsPath
        get(): GroupsPath = groupMembers().groups()
    override fun getChecks(): List<Check<UsersRecord>> = listOf(
        Internal.createCheck(this, DSL.name("users_mail_check"), "((TRIM(BOTH FROM mail) <> ''::text))", true)
    )
    override fun `as`(alias: String): UsersTable = UsersTable(DSL.name(alias), this)
    override fun `as`(alias: Name): UsersTable = UsersTable(alias, this)
    override fun `as`(alias: Table<*>): UsersTable = UsersTable(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): UsersTable = UsersTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): UsersTable = UsersTable(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): UsersTable = UsersTable(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): UsersTable = UsersTable(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): UsersTable = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): UsersTable = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): UsersTable = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): UsersTable = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): UsersTable = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): UsersTable = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): UsersTable = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): UsersTable = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): UsersTable = where(DSL.notExists(select))
}

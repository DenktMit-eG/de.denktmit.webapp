/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.keys


import de.denktmit.webapp.jooq.generated.tables.AuthoritiesTable
import de.denktmit.webapp.jooq.generated.tables.GroupAuthoritiesTable
import de.denktmit.webapp.jooq.generated.tables.GroupMembersTable
import de.denktmit.webapp.jooq.generated.tables.GroupsTable
import de.denktmit.webapp.jooq.generated.tables.OtpActionsTable
import de.denktmit.webapp.jooq.generated.tables.UsersTable
import de.denktmit.webapp.jooq.generated.tables.records.AuthoritiesRecord
import de.denktmit.webapp.jooq.generated.tables.records.GroupAuthoritiesRecord
import de.denktmit.webapp.jooq.generated.tables.records.GroupMembersRecord
import de.denktmit.webapp.jooq.generated.tables.records.GroupsRecord
import de.denktmit.webapp.jooq.generated.tables.records.OtpActionsRecord
import de.denktmit.webapp.jooq.generated.tables.records.UsersRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val AUTHORITIES_PKEY: UniqueKey<AuthoritiesRecord> = Internal.createUniqueKey(AuthoritiesTable.AUTHORITIES, DSL.name("authorities_pkey"), arrayOf(AuthoritiesTable.AUTHORITIES.AUTHORITY_ID), true)
val GROUP_AUTHORITIES_PKEY: UniqueKey<GroupAuthoritiesRecord> = Internal.createUniqueKey(GroupAuthoritiesTable.GROUP_AUTHORITIES, DSL.name("group_authorities_pkey"), arrayOf(GroupAuthoritiesTable.GROUP_AUTHORITIES.GROUP_ID, GroupAuthoritiesTable.GROUP_AUTHORITIES.AUTHORITY_ID), true)
val GROUP_MEMBERS_PKEY: UniqueKey<GroupMembersRecord> = Internal.createUniqueKey(GroupMembersTable.GROUP_MEMBERS, DSL.name("group_members_pkey"), arrayOf(GroupMembersTable.GROUP_MEMBERS.GROUP_ID, GroupMembersTable.GROUP_MEMBERS.USER_ID), true)
val GROUPS_PKEY: UniqueKey<GroupsRecord> = Internal.createUniqueKey(GroupsTable.GROUPS, DSL.name("groups_pkey"), arrayOf(GroupsTable.GROUPS.GROUP_ID), true)
val OTP_ACTIONS_PKEY: UniqueKey<OtpActionsRecord> = Internal.createUniqueKey(OtpActionsTable.OTP_ACTIONS, DSL.name("otp_actions_pkey"), arrayOf(OtpActionsTable.OTP_ACTIONS.ACTION_ID), true)
val OTP_ACTIONS_TOKEN_KEY: UniqueKey<OtpActionsRecord> = Internal.createUniqueKey(OtpActionsTable.OTP_ACTIONS, DSL.name("otp_actions_token_key"), arrayOf(OtpActionsTable.OTP_ACTIONS.TOKEN), true)
val USERS_PKEY: UniqueKey<UsersRecord> = Internal.createUniqueKey(UsersTable.USERS, DSL.name("users_pkey"), arrayOf(UsersTable.USERS.USER_ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val GROUP_AUTHORITIES__FK_GROUP_AUTHORITY_AUTHORITY: ForeignKey<GroupAuthoritiesRecord, AuthoritiesRecord> = Internal.createForeignKey(GroupAuthoritiesTable.GROUP_AUTHORITIES, DSL.name("fk_group_authority_authority"), arrayOf(GroupAuthoritiesTable.GROUP_AUTHORITIES.AUTHORITY_ID), de.denktmit.webapp.jooq.generated.keys.AUTHORITIES_PKEY, arrayOf(AuthoritiesTable.AUTHORITIES.AUTHORITY_ID), true)
val GROUP_AUTHORITIES__FK_GROUP_AUTHORITY_GROUP: ForeignKey<GroupAuthoritiesRecord, GroupsRecord> = Internal.createForeignKey(GroupAuthoritiesTable.GROUP_AUTHORITIES, DSL.name("fk_group_authority_group"), arrayOf(GroupAuthoritiesTable.GROUP_AUTHORITIES.GROUP_ID), de.denktmit.webapp.jooq.generated.keys.GROUPS_PKEY, arrayOf(GroupsTable.GROUPS.GROUP_ID), true)
val GROUP_MEMBERS__FK_GROUP_MEMBER_GROUP: ForeignKey<GroupMembersRecord, GroupsRecord> = Internal.createForeignKey(GroupMembersTable.GROUP_MEMBERS, DSL.name("fk_group_member_group"), arrayOf(GroupMembersTable.GROUP_MEMBERS.GROUP_ID), de.denktmit.webapp.jooq.generated.keys.GROUPS_PKEY, arrayOf(GroupsTable.GROUPS.GROUP_ID), true)
val GROUP_MEMBERS__FK_GROUP_MEMBER_USER: ForeignKey<GroupMembersRecord, UsersRecord> = Internal.createForeignKey(GroupMembersTable.GROUP_MEMBERS, DSL.name("fk_group_member_user"), arrayOf(GroupMembersTable.GROUP_MEMBERS.USER_ID), de.denktmit.webapp.jooq.generated.keys.USERS_PKEY, arrayOf(UsersTable.USERS.USER_ID), true)
val OTP_ACTIONS__FK_OTP_ACTION_USER: ForeignKey<OtpActionsRecord, UsersRecord> = Internal.createForeignKey(OtpActionsTable.OTP_ACTIONS, DSL.name("fk_otp_action_user"), arrayOf(OtpActionsTable.OTP_ACTIONS.USER_ID), de.denktmit.webapp.jooq.generated.keys.USERS_PKEY, arrayOf(UsersTable.USERS.USER_ID), true)

/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.sequences


import de.denktmit.webapp.jooq.generated.Public

import org.jooq.Sequence
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType



/**
 * The sequence <code>public.authorities_seq</code>
 */
val AUTHORITIES_SEQ: Sequence<Long> = Internal.createSequence("authorities_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), 10100, 50, null, null, false, null)

/**
 * The sequence <code>public.groups_seq</code>
 */
val GROUPS_SEQ: Sequence<Long> = Internal.createSequence("groups_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), 10100, 50, null, null, false, null)

/**
 * The sequence <code>public.users_seq</code>
 */
val USERS_SEQ: Sequence<Long> = Internal.createSequence("users_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), 10050, 50, null, null, false, null)

/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables.interfaces


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "group_authorities",
    schema = "public"
)
interface IGroupAuthorities : Serializable {
    @get:Column(name = "group_id", nullable = false)
    @get:NotNull
    var groupId: Long?
    @get:Column(name = "authority_id", nullable = false)
    @get:NotNull
    var authorityId: Long?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IGroupAuthorities
     */
    fun from(from: IGroupAuthorities)

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IGroupAuthorities
     */
    fun <E : IGroupAuthorities> into(into: E): E
}

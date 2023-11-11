/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables.interfaces


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "authorities",
    schema = "public"
)
interface IAuthorities : Serializable {
    @get:Id
    @get:Column(name = "authority_id", nullable = false)
    @get:NotNull
    var authorityId: Long?
    @get:Column(name = "authority", nullable = false, length = 50)
    @get:NotNull
    @get:Size(max = 50)
    var authority: String?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IAuthorities
     */
    fun from(from: IAuthorities)

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IAuthorities
     */
    fun <E : IAuthorities> into(into: E): E
}

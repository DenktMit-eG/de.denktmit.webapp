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
    name = "groups",
    schema = "public"
)
interface IGroups : Serializable {
    @get:Id
    @get:Column(name = "group_id", nullable = false)
    @get:NotNull
    var groupId: Long?
    @get:Column(name = "group_name", nullable = false, length = 50)
    @get:NotNull
    @get:Size(max = 50)
    var groupName: String?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IGroups
     */
    fun from(from: IGroups)

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IGroups
     */
    fun <E : IGroups> into(into: E): E
}

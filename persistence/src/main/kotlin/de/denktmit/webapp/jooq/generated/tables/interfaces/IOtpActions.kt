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
import java.time.Instant
import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "otp_actions",
    schema = "public"
)
interface IOtpActions : Serializable {
    @get:Id
    @get:Column(name = "otp_action_id", nullable = false)
    @get:NotNull
    var otpActionId: UUID?
    @get:Column(name = "target", nullable = false, length = 255)
    @get:NotNull
    @get:Size(max = 255)
    var target: String?
    @get:Column(name = "action", nullable = false, length = 25)
    @get:NotNull
    @get:Size(max = 25)
    var action: String?
    @get:Column(name = "identifier", nullable = false)
    @get:NotNull
    var identifier: Long?
    @get:Column(name = "valid_until", nullable = false)
    @get:NotNull
    var validUntil: Instant?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IOtpActions
     */
    fun from(from: IOtpActions)

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IOtpActions
     */
    fun <E : IOtpActions> into(into: E): E
}
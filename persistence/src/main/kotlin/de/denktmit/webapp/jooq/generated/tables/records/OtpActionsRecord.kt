/*
 * This file is generated by jOOQ.
 */
package de.denktmit.webapp.jooq.generated.tables.records


import de.denktmit.webapp.jooq.generated.tables.OtpActionsTable
import de.denktmit.webapp.jooq.generated.tables.interfaces.IOtpActions

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.time.Instant
import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record5
import org.jooq.Row5
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "otp_actions",
    schema = "public"
)
open class OtpActionsRecord() : UpdatableRecordImpl<OtpActionsRecord>(OtpActionsTable.OTP_ACTIONS), Record5<UUID?, String?, String?, Long?, Instant?>, IOtpActions {

    @get:Id
    @get:Column(name = "otp_action_id", nullable = false)
    @get:NotNull
    open override var otpActionId: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    @get:Column(name = "target", nullable = false, length = 255)
    @get:NotNull
    @get:Size(max = 255)
    open override var target: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    @get:Column(name = "action", nullable = false, length = 25)
    @get:NotNull
    @get:Size(max = 25)
    open override var action: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    @get:Column(name = "identifier", nullable = false)
    @get:NotNull
    open override var identifier: Long?
        set(value): Unit = set(3, value)
        get(): Long? = get(3) as Long?

    @get:Column(name = "valid_until", nullable = false)
    @get:NotNull
    open override var validUntil: Instant?
        set(value): Unit = set(4, value)
        get(): Instant? = get(4) as Instant?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row5<UUID?, String?, String?, Long?, Instant?> = super.fieldsRow() as Row5<UUID?, String?, String?, Long?, Instant?>
    override fun valuesRow(): Row5<UUID?, String?, String?, Long?, Instant?> = super.valuesRow() as Row5<UUID?, String?, String?, Long?, Instant?>
    override fun field1(): Field<UUID?> = OtpActionsTable.OTP_ACTIONS.OTP_ACTION_ID
    override fun field2(): Field<String?> = OtpActionsTable.OTP_ACTIONS.TARGET
    override fun field3(): Field<String?> = OtpActionsTable.OTP_ACTIONS.ACTION
    override fun field4(): Field<Long?> = OtpActionsTable.OTP_ACTIONS.IDENTIFIER
    override fun field5(): Field<Instant?> = OtpActionsTable.OTP_ACTIONS.VALID_UNTIL
    override fun component1(): UUID? = otpActionId
    override fun component2(): String? = target
    override fun component3(): String? = action
    override fun component4(): Long? = identifier
    override fun component5(): Instant? = validUntil
    override fun value1(): UUID? = otpActionId
    override fun value2(): String? = target
    override fun value3(): String? = action
    override fun value4(): Long? = identifier
    override fun value5(): Instant? = validUntil

    override fun value1(value: UUID?): OtpActionsRecord {
        set(0, value)
        return this
    }

    override fun value2(value: String?): OtpActionsRecord {
        set(1, value)
        return this
    }

    override fun value3(value: String?): OtpActionsRecord {
        set(2, value)
        return this
    }

    override fun value4(value: Long?): OtpActionsRecord {
        set(3, value)
        return this
    }

    override fun value5(value: Instant?): OtpActionsRecord {
        set(4, value)
        return this
    }

    override fun values(value1: UUID?, value2: String?, value3: String?, value4: Long?, value5: Instant?): OtpActionsRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IOtpActions) {
        otpActionId = from.otpActionId
        target = from.target
        action = from.action
        identifier = from.identifier
        validUntil = from.validUntil
        resetChangedOnNotNull()
    }

    override fun <E : IOtpActions> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised OtpActionsRecord
     */
    constructor(otpActionId: UUID? = null, target: String? = null, action: String? = null, identifier: Long? = null, validUntil: Instant? = null): this() {
        this.otpActionId = otpActionId
        this.target = target
        this.action = action
        this.identifier = identifier
        this.validUntil = validUntil
        resetChangedOnNotNull()
    }
}
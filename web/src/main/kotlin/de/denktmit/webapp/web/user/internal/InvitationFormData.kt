package de.denktmit.webapp.web.user.internal

import de.denktmit.webapp.persistence.rbac.GROUP_NAME_ADMINS
import de.denktmit.webapp.persistence.rbac.GROUP_NAME_USERS
import de.denktmit.webapp.web.user.internal.UniqueEmail
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class InvitationFormData(

    @get:NotBlank
    @get:Size(min = 5, max = 255)
    @get:Email
    @get:UniqueEmail
    val emailAddress: String = "",

    @Pattern(regexp = "${GROUP_NAME_ADMINS}|${GROUP_NAME_USERS}")
    val groupName: String = "",

)
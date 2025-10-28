package de.denktmit.webapp.business.user

import de.denktmit.webapp.common.DataTable
import de.denktmit.webapp.persistence.users.RbacMapping
import de.denktmit.webapp.persistence.users.User
import java.net.URI
import java.security.Principal

interface UserService {
    fun userData(): DataTable
    fun userDataV2(): List<RbacMapping>

    fun isEmailExisting(mail: String): Boolean

    fun findOneByMail(mail: String): User?

    fun encodePassword(password: WipeableCharSequence): String

    /**
     * Creates a new user with the provided information.
     *
     * @param mail The email address of the user.
     * @param password The password for the user, represented as a [WipeableCharSequence].
     * @param mailVerificationUri The full URI for email verification to be called with query parameter 'token'
     * @param mailVerified Indicates whether the user's email is already verified. Default is `false`.
     *
     * @return A [Result] object containing the created [User] if successful, or an error otherwise.
     */
    fun createUser(
        mail: String,
        password: WipeableCharSequence,
        mailVerificationUri: URI,
        mailVerified: Boolean = false
    ): UserSavingResult

    /**
     * Updates an existing user with the new values.
     *
     * @param user The updated user object
     *
     * @return An [UserSavingResult] object
     */
    fun updateUser(
        unsavedUser: User
    ): UserSavingResult

    /**
     * Invites a user to join a group with the provided information.
     *
     * @param mail The email address of the user.
     * @param groupName The name of the group to which the user is invited.
     * @param invitationAcceptUri The full URI to accept invitations to be called with query parameter 'token'
     * @param mailVerified Indicates whether the user's email is already verified. Default is `true`.
     *
     * @return A [Result] object containing the invited [User] if successful, or an error otherwise.
     */
    fun inviteUser(
        mail: String,
        groupName: String,
        invitationAcceptUri: URI,
        mailVerified: Boolean = true
    ): UserSavingResult

    fun updatePassword(
        principal: Principal,
        oldPassword: WipeableCharSequence,
        newPassword: WipeableCharSequence
    ): UserSavingResult

    sealed class UserSavingResult {
        data class Persisted(val user: User): UserSavingResult()
        data class EmailAlreadyExists(val user: User): UserSavingResult()
        data class UserNotFound(val mail: String): UserSavingResult()

        data class BadCredentialsProvided(val user: User): UserSavingResult()
    }
}



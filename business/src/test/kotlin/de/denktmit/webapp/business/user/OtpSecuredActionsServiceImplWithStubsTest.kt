package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.user.OtpActionResult.Success
import de.denktmit.webapp.business.user.UserService.UserSavingResult.Persisted
import de.denktmit.webapp.persistence.testdata.OtpActions
import de.denktmit.webapp.persistence.testdata.Rbac
import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import de.denktmit.webapp.springconfig.MailConfigProperties
import de.denktmit.webapp.testutils.softAssert
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import java.time.Duration
import java.util.*

class OtpSecuredActionsServiceImplWithStubsTest {

    private val config = BusinessContextConfigProperties(MailConfigProperties("junit@example.com"))
    private lateinit var publisher: ApplicationEventPublisher
    private lateinit var userRepo: Users.RepoStub
    private lateinit var rbacRepo: Rbac.RepoStub
    private lateinit var otpRepo: OtpActions.RepoStub
    private lateinit var userService: UserService
    private lateinit var otpService: OtpSecuredActionsServiceImpl

    @BeforeEach
    fun setUp() {
        publisher = mockk(relaxed = true)
        userRepo = Users.RepoStub()
        rbacRepo = Rbac.RepoStub()
        userService = UserServiceImpl(publisher, NoOpPasswordEncoder.getInstance(), userRepo, rbacRepo)
        otpRepo = OtpActions.RepoStub()
        otpService = OtpSecuredActionsServiceImpl(config, publisher, userService, otpRepo)
    }

    @Test
    fun `isTokenValid should return true if token is valid and false otherwise`() {
        OtpActions.all.forEach { otpAction ->
            val resultWithValidToken = otpService.isTokenValid(otpAction.token, otpAction.action, otpAction.validUntil.minusSeconds(1))
            val resultWithTokenExceeded = otpService.isTokenValid(otpAction.token, otpAction.action, otpAction.validUntil)
            val resultWithUnknownAction = otpService.isTokenValid(otpAction.token, "i-dont-exist", otpAction.validUntil.minusSeconds(1))
            val resultWithUnknownToken = otpService.isTokenValid(UUID.fromString("ba4d8b54-9ac4-466a-a940-00e6b5de269d"), otpAction.action, otpAction.validUntil.minusSeconds(1))
            softAssert {
                assertThat(resultWithValidToken).isTrue
                assertThat(resultWithTokenExceeded).isFalse
                assertThat(resultWithUnknownAction).isFalse
                assertThat(resultWithUnknownToken).isFalse
            }
        }
    }

    @Test
    fun `createOtpAction should overwrite old action for existing user and actionstring`() {
        val otpAction = OtpActions.facade00
        val createdAction = otpService.createOtpAction(otpAction.user, otpAction.action, Duration.ofSeconds(100))
        val expectedActions = OtpActions.all.filter { it != otpAction }.toMutableSet().plus(createdAction)
        assertThat(otpRepo.data).containsExactlyInAnyOrder(*expectedActions.toTypedArray())
    }

    @Test
    fun `createOtpAction should add new token for new actionstring but existing user`() {
        val otpAction = OtpActions.facade00
        val createdAction = otpService.createOtpAction(otpAction.user, "junit-action", Duration.ofSeconds(100))
        val expectedActions = OtpActions.all.plus(createdAction)
        assertThat(otpRepo.data).containsExactlyInAnyOrder(*expectedActions.toTypedArray())
    }

    @Test
    fun `createOtpAction should add new token for new user but existing actionstring`() {
        val otpAction = OtpActions.facade00
        val createdAction = otpService.createOtpAction(Users.createUser(), otpAction.action, Duration.ofSeconds(100))
        val expectedActions = OtpActions.all.plus(createdAction)
        assertThat(otpRepo.data).containsExactlyInAnyOrder(*expectedActions.toTypedArray())
    }

    @Test
    fun `createOtpAction should add new token for new user and actionstring`() {
        val createdAction = otpService.createOtpAction(Users.createUser(), "junit-action", Duration.ofSeconds(100))
        val expectedActions = OtpActions.all.plus(createdAction)
        assertThat(otpRepo.data).containsExactlyInAnyOrder(*expectedActions.toTypedArray())
    }

    @Test
    fun `validateEmailAddress should resolve the token and on success update the user`() {
        val otpAction = OtpActions.facade00
        val result = otpService.validateEmailAddress(otpAction.token)
        if (result as? Success == null) {
            fail { "Expected result to be a ${Success::class.java} but was $result" }
        }
        if (result.payload as? Persisted == null ) {
            fail { "Expected result.payload to be a ${Persisted::class.java} but was ${result.payload}" }
        } else {
            val persisted = (result.payload as Persisted)
            softAssert {
                assertThat(otpRepo.data.any { it.token == otpAction.token }).isFalse
                assertThat(persisted.user.mailVerified).isTrue
            }
        }

    }

//    @Test
//    fun `validateEmailAddress should return TokenNotResolved on invalid token`() {
//        // Vars
//        val otpAction = OtpActions.facade00
//        val validationTime = otpAction.validUntil.minusSeconds(1)
//
//        // Setup token resolution
//        setupTokenResolverMocks(otpAction, validationTime, false)
//
//        // Run and assert
//        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
//            OtpActionResult.Failed(
//                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
//            )
//        val result = otpService.validateEmailAddress(otpAction.token, validationTime)
//        assertThat(result).isEqualTo(expectedResult)
//
//        // Verify token resolution and confirm all mocks
//        verifyTokenResolverMocks(otpAction, validationTime, false)
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `acceptInvitation should resolve the token and on success update the user`() {
//        // Vars
//        val otpAction = OtpActions.adobe000
//        val validationTime = otpAction.validUntil.minusSeconds(1)
//        val newPassword = WipeableCharSequence("password".toCharArray())
//
//        // Setup token resolution
//        setupTokenResolverMocks(otpAction, validationTime, true)
//
//        // Setup test specifics mocks
//        val userToUpdate = slot<UserEntity>()
//        val savingResult = Persisted(Users.createUser())
//        every { userService.encodePassword(any()) } returns "{noop}password"
//        every { userService.updateUser(capture(userToUpdate)) } returns savingResult
//
//        // Run and assert
//        val expectedResult: OtpActionResult<UserSavingResult, CredentialsExpiredException> = Success(savingResult)
//        val result = otpService.acceptInvitation(otpAction.token, newPassword, validationTime)
//        softAssert {
//            assertThat(result).isEqualTo(expectedResult)
//            assertThat(otpAction.user.mailVerified).isFalse
//            assertThat(otpAction.user.credentialsValidUntil).isEqualTo(FAR_PAST)
//            assertThat(userToUpdate.captured.password).isEqualTo("{noop}password")
//            assertThat(userToUpdate.captured.mailVerified).isTrue
//            assertThat(userToUpdate.captured.credentialsValidUntil).isEqualTo(FAR_FUTURE)
//        }
//
//        // Verify test specific mocks
//        verify(exactly = 1) { userService.encodePassword(any()) }
//        verify(exactly = 1) { userService.updateUser(userToUpdate.captured) }
//
//        // Verify token resolution and confirm all mocks
//        verifyTokenResolverMocks(otpAction, validationTime, true)
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `acceptInvitation should return TokenNotResolved on invalid token`() {
//        // Vars
//        val otpAction = OtpActions.adobe000
//        val validationTime = otpAction.validUntil.minusSeconds(1)
//        val newPassword = WipeableCharSequence("password".toCharArray())
//
//        // Setup token resolution
//        setupTokenResolverMocks(otpAction, validationTime, false)
//
//        // Run and assert
//        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
//            OtpActionResult.Failed(
//                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
//            )
//        val result = otpService.acceptInvitation(otpAction.token, newPassword, validationTime)
//        assertThat(result).isEqualTo(expectedResult)
//
//        // Verify token resolution and confirm all mocks
//        verifyTokenResolverMocks(otpAction, validationTime, false)
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `resetPassword should resolve the token and on success update the user`() {
//        // Vars
//        val otpAction = OtpActions.decade00
//        val validationTime = otpAction.validUntil.minusSeconds(1)
//        val newPassword = WipeableCharSequence("password".toCharArray())
//
//        // Setup token resolution
//        setupTokenResolverMocks(otpAction, validationTime, true)
//
//        // Setup test specifics mocks
//        val userToUpdate = slot<UserEntity>()
//        val savingResult = Persisted(Users.createUser())
//        every { userService.encodePassword(any()) } returns "{noop}password"
//        every { userService.updateUser(capture(userToUpdate)) } returns savingResult
//
//        // Run and assert
//        val expectedResult: OtpActionResult<UserSavingResult, CredentialsExpiredException> = Success(savingResult)
//        val result = otpService.resetPassword(otpAction.token, newPassword, validationTime)
//        softAssert {
//            assertThat(result).isEqualTo(expectedResult)
//            assertThat(userToUpdate.captured.password).isEqualTo("{noop}password")
//        }
//
//        // Verify test specific mocks
//        verify(exactly = 1) { userService.encodePassword(any()) }
//        verify(exactly = 1) { userService.updateUser(userToUpdate.captured) }
//
//        // Verify token resolution and confirm all mocks
//        verifyTokenResolverMocks(otpAction, validationTime, true)
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `resetPassword should return TokenNotResolved on invalid token`() {
//        val otpAction = OtpActions.decade00
//        val validationTime = otpAction.validUntil.minusSeconds(1)
//        val newPassword = WipeableCharSequence("password".toCharArray())
//        setupTokenResolverMocks(otpAction, validationTime, false)
//
//        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
//            OtpActionResult.Failed(
//                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
//            )
//        val result = otpService.resetPassword(otpAction.token, newPassword, validationTime)
//        assertThat(result).isEqualTo(expectedResult)
//
//        verifyTokenResolverMocks(otpAction, validationTime, false)
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `startPasswordRecovery should store new action for user and publish event`() {
//        val otpAction = OtpActions.decade00
//        val event = slot<PasswordRecoveryRequestedEvent>()
//        every { userService.findOneByMail(otpAction.user.mail) } returns otpAction.user
//        every { otpRepo.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) } returns 0
//        every { otpRepo.save(any()) } returns otpAction
//        justRun { publisher.publishEvent(capture(event)) }
//
//        otpService.startPasswordRecovery(otpAction.user.mail, URI.create("https://example.com/recover-password"))
//        softAssert {
//            assertThat(event.captured.user).isEqualTo(otpAction.user)
//            assertThat(event.captured.passwordResetUri).isEqualTo(URI.create("https://example.com/recover-password?token=${otpAction.token}"))
//            assertThat(event.captured.validUntil).isEqualTo(otpAction.validUntil)
//        }
//
//        verify(exactly = 1) { userService.findOneByMail(otpAction.user.mail) }
//        verify(exactly = 1)  { otpRepo.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) }
//        verify(exactly = 1)  { otpRepo.save(any()) }
//        verify(exactly = 1)  { publisher.publishEvent(capture(event)) }
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    @Test
//    fun `startPasswordRecovery should publish nothing if user not found`() {
//        val otpAction = OtpActions.decade00
//        every { userService.findOneByMail(otpAction.user.mail) } returns null
//
//        otpService.startPasswordRecovery(otpAction.user.mail, URI.create("https://example.com/recover-password"))
//
//        verify(exactly = 1) { userService.findOneByMail(otpAction.user.mail) }
//        confirmVerified(publisher, otpRepo, userService)
//    }
//
//    private fun setupTokenResolverMocks(
//        otpAction: OtpAction,
//        validationTime: Instant,
//        resolvesSuccessful: Boolean
//    ) {
//        every {
//            otpRepo.findOneByTokenAndActionAndValidUntilAfter(
//                otpAction.token,
//                otpAction.action,
//                validationTime
//            )
//        } returns if (resolvesSuccessful) otpAction else null
//        justRun { otpRepo.delete(otpAction) }
//    }
//
//    private fun verifyTokenResolverMocks(
//        otpAction: OtpAction,
//        validationTime: Instant,
//        resolvesSuccessful: Boolean
//    ) {
//        verify(exactly = 1) {
//            otpRepo.findOneByTokenAndActionAndValidUntilAfter(
//                otpAction.token,
//                otpAction.action,
//                validationTime
//            )
//        }
//        verify(exactly = if (resolvesSuccessful) 1 else 0) { otpRepo.delete(otpAction) }
//    }
}
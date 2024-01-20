package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.communication.PasswordRecoveryRequestedEvent
import de.denktmit.webapp.business.user.OtpActionResult.Success
import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.business.user.UserService.UserSavingResult.Persisted
import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.Constants.FAR_PAST
import de.denktmit.webapp.persistence.users.OtpAction
import de.denktmit.webapp.persistence.testdata.OtpActions
import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import de.denktmit.webapp.springconfig.MailConfigProperties
import de.denktmit.webapp.testutils.softAssert
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.CredentialsExpiredException
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.util.*

class OtpSecuredActionsServiceImplTest {

    private val config = BusinessContextConfigProperties(MailConfigProperties("junit@example.com"))
    private lateinit var publisher: ApplicationEventPublisher
    private lateinit var otpRepository: OtpActions.RepoStub
    private lateinit var userService: UserService
    private lateinit var service: OtpSecuredActionsServiceImpl

    @BeforeEach
    fun setUp() {
        publisher = mockk(relaxed = true)
        userService = mockk()
        otpRepository = mockk()
        service = OtpSecuredActionsServiceImpl(config, publisher, userService, otpRepository)
    }

    @Test
    fun `isTokenValid should return response from underlying repo`() {
        validateExistsByIdAndActionAndValidUntilAfter(OtpActions.adobe000, Instant.now(), true)
        validateExistsByIdAndActionAndValidUntilAfter(OtpActions.adobe000, Instant.now(), false)
    }

    private fun validateExistsByIdAndActionAndValidUntilAfter(
        otpAction: OtpAction,
        validationTime: Instant,
        expectedOutcome: Boolean
    ) {
        every {
            otpRepository.existsByTokenAndActionAndValidUntilAfter(otpAction.token, otpAction.action, validationTime)
        } returns expectedOutcome

        val result = service.isTokenValid(otpAction.token, otpAction.action, validationTime)
        softAssert {
            assertThat(result).isEqualTo(expectedOutcome)
        }

        verify(exactly = 1) {
            otpRepository.existsByTokenAndActionAndValidUntilAfter(
                otpAction.token,
                otpAction.action,
                validationTime
            )
        }
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `createOtpAction should store new action and overwrite old ones of for same user and type`() {
        val otpAction = OtpActions.adobe000
        val actionToCreate = slot<OtpAction>()
        every { otpRepository.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) } returns 1
        every { otpRepository.save(capture(actionToCreate)) } returns otpAction

        val expectedDuration = Duration.ofSeconds(100)
        val result = service.createOtpAction(otpAction.user, otpAction.action, expectedDuration)
        softAssert {
            assertThat(actionToCreate.captured.user).isEqualTo(otpAction.user)
            assertThat(actionToCreate.captured.action).isEqualTo(otpAction.action)
            assertThat(actionToCreate.captured.token).isInstanceOf(UUID::class.java)
            assertThat(actionToCreate.captured.validUntil).isBetween(Instant.now(), Instant.now().plusSeconds(1000))
            assertThat(result).isEqualTo(otpAction)
        }

        verify(exactly = 1) { otpRepository.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) }
        verify(exactly = 1) { otpRepository.save(actionToCreate.captured) }
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `validateEmailAddress should resolve the token and on success update the user`() {
        // Vars
        val otpAction = OtpActions.facade00
        val validationTime = otpAction.validUntil.minusSeconds(1)

        // Setup token resolution
        setupTokenResolverMocks(otpAction, validationTime, true)

        // Setup test specifics mocks
        val userToUpdate = slot<User>()
        val savingResult = Persisted(Users.createUser())
        every { userService.updateUser(capture(userToUpdate)) } returns savingResult

        // Run and assert
        val expectedResult: OtpActionResult<UserSavingResult, CredentialsExpiredException> = Success(savingResult)
        val result = service.validateEmailAddress(otpAction.token, validationTime)
        assertThat(result).isEqualTo(expectedResult)

        // Verify test specifics mocks
        verify(exactly = 1) { userService.updateUser(userToUpdate.captured) }

        // Verify token resolution and confirm all mocks
        verifyTokenResolverMocks(otpAction, validationTime, true)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `validateEmailAddress should return TokenNotResolved on invalid token`() {
        // Vars
        val otpAction = OtpActions.facade00
        val validationTime = otpAction.validUntil.minusSeconds(1)

        // Setup token resolution
        setupTokenResolverMocks(otpAction, validationTime, false)

        // Run and assert
        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
            OtpActionResult.Failed(
                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
            )
        val result = service.validateEmailAddress(otpAction.token, validationTime)
        assertThat(result).isEqualTo(expectedResult)

        // Verify token resolution and confirm all mocks
        verifyTokenResolverMocks(otpAction, validationTime, false)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `acceptInvitation should resolve the token and on success update the user`() {
        // Vars
        val otpAction = OtpActions.adobe000
        val validationTime = otpAction.validUntil.minusSeconds(1)
        val newPassword = WipeableCharSequence("password".toCharArray())

        // Setup token resolution
        setupTokenResolverMocks(otpAction, validationTime, true)

        // Setup test specifics mocks
        val userToUpdate = slot<User>()
        val savingResult = Persisted(Users.createUser())
        every { userService.encodePassword(any()) } returns "{noop}password"
        every { userService.updateUser(capture(userToUpdate)) } returns savingResult

        // Run and assert
        val expectedResult: OtpActionResult<UserSavingResult, CredentialsExpiredException> = Success(savingResult)
        val result = service.acceptInvitation(otpAction.token, newPassword, validationTime)
        softAssert {
            assertThat(result).isEqualTo(expectedResult)
            assertThat(otpAction.user.mailVerified).isFalse
            assertThat(otpAction.user.credentialsValidUntil).isEqualTo(FAR_PAST)
            assertThat(userToUpdate.captured.password).isEqualTo("{noop}password")
            assertThat(userToUpdate.captured.mailVerified).isTrue
            assertThat(userToUpdate.captured.credentialsValidUntil).isEqualTo(FAR_FUTURE)
        }

        // Verify test specific mocks
        verify(exactly = 1) { userService.encodePassword(any()) }
        verify(exactly = 1) { userService.updateUser(userToUpdate.captured) }

        // Verify token resolution and confirm all mocks
        verifyTokenResolverMocks(otpAction, validationTime, true)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `acceptInvitation should return TokenNotResolved on invalid token`() {
        // Vars
        val otpAction = OtpActions.adobe000
        val validationTime = otpAction.validUntil.minusSeconds(1)
        val newPassword = WipeableCharSequence("password".toCharArray())

        // Setup token resolution
        setupTokenResolverMocks(otpAction, validationTime, false)

        // Run and assert
        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
            OtpActionResult.Failed(
                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
            )
        val result = service.acceptInvitation(otpAction.token, newPassword, validationTime)
        assertThat(result).isEqualTo(expectedResult)

        // Verify token resolution and confirm all mocks
        verifyTokenResolverMocks(otpAction, validationTime, false)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `resetPassword should resolve the token and on success update the user`() {
        // Vars
        val otpAction = OtpActions.decade00
        val validationTime = otpAction.validUntil.minusSeconds(1)
        val newPassword = WipeableCharSequence("password".toCharArray())

        // Setup token resolution
        setupTokenResolverMocks(otpAction, validationTime, true)

        // Setup test specifics mocks
        val userToUpdate = slot<User>()
        val savingResult = Persisted(Users.createUser())
        every { userService.encodePassword(any()) } returns "{noop}password"
        every { userService.updateUser(capture(userToUpdate)) } returns savingResult

        // Run and assert
        val expectedResult: OtpActionResult<UserSavingResult, CredentialsExpiredException> = Success(savingResult)
        val result = service.resetPassword(otpAction.token, newPassword, validationTime)
        softAssert {
            assertThat(result).isEqualTo(expectedResult)
            assertThat(userToUpdate.captured.password).isEqualTo("{noop}password")
        }

        // Verify test specific mocks
        verify(exactly = 1) { userService.encodePassword(any()) }
        verify(exactly = 1) { userService.updateUser(userToUpdate.captured) }

        // Verify token resolution and confirm all mocks
        verifyTokenResolverMocks(otpAction, validationTime, true)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `resetPassword should return TokenNotResolved on invalid token`() {
        val otpAction = OtpActions.decade00
        val validationTime = otpAction.validUntil.minusSeconds(1)
        val newPassword = WipeableCharSequence("password".toCharArray())
        setupTokenResolverMocks(otpAction, validationTime, false)

        val expectedResult: OtpActionResult<UserSavingResult, TokenNotResolved> =
            OtpActionResult.Failed(
                TokenNotResolved(otpAction.token, otpAction.action, validationTime)
            )
        val result = service.resetPassword(otpAction.token, newPassword, validationTime)
        assertThat(result).isEqualTo(expectedResult)

        verifyTokenResolverMocks(otpAction, validationTime, false)
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `startPasswordRecovery should store new action for user and publish event`() {
        val otpAction = OtpActions.decade00
        val event = slot<PasswordRecoveryRequestedEvent>()
        every { userService.findOneByMail(otpAction.user.mail) } returns otpAction.user
        every { otpRepository.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) } returns 0
        every { otpRepository.save(any()) } returns otpAction
        justRun { publisher.publishEvent(capture(event)) }

        service.startPasswordRecovery(otpAction.user.mail, URI.create("https://example.com/recover-password"))
        softAssert {
            assertThat(event.captured.user).isEqualTo(otpAction.user)
            assertThat(event.captured.passwordResetUri).isEqualTo(URI.create("https://example.com/recover-password?token=${otpAction.token}"))
            assertThat(event.captured.validUntil).isEqualTo(otpAction.validUntil)
        }

        verify(exactly = 1) { userService.findOneByMail(otpAction.user.mail) }
        verify(exactly = 1)  { otpRepository.deleteByUserMailAndAction(otpAction.user.mail, otpAction.action) }
        verify(exactly = 1)  { otpRepository.save(any()) }
        verify(exactly = 1)  { publisher.publishEvent(capture(event)) }
        confirmVerified(publisher, otpRepository, userService)
    }

    @Test
    fun `startPasswordRecovery should publish nothing if user not found`() {
        val otpAction = OtpActions.decade00
        every { userService.findOneByMail(otpAction.user.mail) } returns null

        service.startPasswordRecovery(otpAction.user.mail, URI.create("https://example.com/recover-password"))

        verify(exactly = 1) { userService.findOneByMail(otpAction.user.mail) }
        confirmVerified(publisher, otpRepository, userService)
    }

    private fun setupTokenResolverMocks(
        otpAction: OtpAction,
        validationTime: Instant,
        resolvesSuccessful: Boolean
    ) {
        every {
            otpRepository.findOneByTokenAndActionAndValidUntilAfter(
                otpAction.token,
                otpAction.action,
                validationTime
            )
        } returns if (resolvesSuccessful) otpAction else null
        justRun { otpRepository.delete(otpAction) }
    }

    private fun verifyTokenResolverMocks(
        otpAction: OtpAction,
        validationTime: Instant,
        resolvesSuccessful: Boolean
    ) {
        verify(exactly = 1) {
            otpRepository.findOneByTokenAndActionAndValidUntilAfter(
                otpAction.token,
                otpAction.action,
                validationTime
            )
        }
        verify(exactly = if (resolvesSuccessful) 1 else 0) { otpRepository.delete(otpAction) }
    }
}
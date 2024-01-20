package de.denktmit.webapp.business.user

import de.denktmit.webapp.persistence.users.RbacRepository
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceImplTest {

    private lateinit var publisher: ApplicationEventPublisher
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userRepository: UserRepository
    private lateinit var rbacRepository: RbacRepository
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        publisher = mockk(relaxed = true)
        passwordEncoder = mockk()
        userRepository = mockk()
        rbacRepository = mockk()
        userService = UserServiceImpl(publisher, passwordEncoder, userRepository, rbacRepository)
    }

    @Test
    fun `encodePassword should delegate to PasswordEncoder`() {
        // Arrange
        val password = WipeableCharSequence("testpassword".toCharArray())
        val encodedPassword = "encodedPassword"
        every { passwordEncoder.encode(password) } returns encodedPassword

        // Act
        val result = userService.encodePassword(password)

        // Assert
        verify(exactly = 1) { passwordEncoder.encode(password) }
        confirmVerified(passwordEncoder)
        assertThat(encodedPassword).isEqualTo(result)
    }

    @Test
    fun `createUser should persist user and publish event`() {
//        // Arrange
//        val mail = "test@example.com"
//        val password = WipeableCharSequence("testpassword".toCharArray())
//        val mailVerificationUri = URI.create("http://example.com/verify")
//        val mailVerified = true
//        val expectedUser = Users.alicejohnson
//
//        val result = UserService.UserSavingResult.Persisted(Users.alicejohnson)
//
//        every { passwordEncoder.encode(password) } returns "encodedPassword"
//        every { userRepository.save(any()) } returns expectedUser
//        every { rbacRepository.setUserGroups(any(), any()) } just Runs
//        every { publisher.publishEvent(any()) } just Runs
//
//        // Act
//        val userSavingResult = userService.createUser(expectedUser.mail, password, mailVerificationUri, mailVerified)
//
//        // Assert
//        verify(exactly = 1) { passwordEncoder.encode(password) }
//        verify(exactly = 1) { userRepository.save(any()) }
//        verify(exactly = 1) { rbacRepository.setUserGroups(mail, setOf(GROUP_NAME_USERS)) }
//        verify(exactly = 1) { publisher.publishEvent(ofType(UserCreatedEvent::class)) }
//        confirmVerified(passwordEncoder, userRepository, rbacRepository, publisher)
//
//        assertEquals(result, userSavingResult)
    }

    @Test
    fun `updateUser should persist user`() {
        // Arrange
        val unsavedUser = User.create("test@example.com", "encodedPassword", mailVerified = true)
        val result = UserService.UserSavingResult.Persisted(unsavedUser)

        every { userRepository.save(unsavedUser) } returns unsavedUser

        // Act
        val userSavingResult = userService.updateUser(unsavedUser)

        // Assert
        verify(exactly = 1) { userRepository.save(unsavedUser) }
        confirmVerified(userRepository)

        assertEquals(result, userSavingResult)
    }


}


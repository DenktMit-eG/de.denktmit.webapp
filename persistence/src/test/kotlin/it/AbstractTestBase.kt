package it

import de.denktmit.webapp.testutils.PostgresTestContextInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PersistenceTestContext::class])
@ContextConfiguration(initializers = [PostgresTestContextInitializer::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Transactional(readOnly = true)
annotation class IntegrationTestConfiguration
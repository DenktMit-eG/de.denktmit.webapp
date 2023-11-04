package it

import de.denktmit.webapp.testutils.PostgresTestContextInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ContextConfiguration(initializers = [PostgresTestContextInitializer::class])
@EnableAutoConfiguration
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestContext::class])
@TestPropertySource(locations = ["classpath:application-test.yaml"])
abstract class AbstractTestBase
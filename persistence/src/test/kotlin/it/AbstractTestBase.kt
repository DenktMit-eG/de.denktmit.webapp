package it

import de.denktmit.testsupport.spring.PostgresTestContextInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PersistenceTestContext::class])
@ContextConfiguration(initializers = [PostgresTestContextInitializer::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
abstract class AbstractTestBase {

    init {
        PostgresTestContextInitializer.DEFAULT_DB_NAME = "webapp"
        PostgresTestContextInitializer.DEFAULT_FLYWAY_MIGRATE = false
    }

}

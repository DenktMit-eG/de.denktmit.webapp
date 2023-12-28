package it

import de.denktmit.webapp.WebappApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [WebappApplication::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
class WebappApplicationIT {

	@Test
	fun contextLoads() {
	}

}

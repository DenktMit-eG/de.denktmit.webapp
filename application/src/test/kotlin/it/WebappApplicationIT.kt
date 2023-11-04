package it

import de.denktmit.webapp.WebappApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [WebappApplication::class])
class WebappApplicationIT {

	@Test
	fun contextLoads() {
	}

}

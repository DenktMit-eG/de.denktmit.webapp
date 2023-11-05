package de.denktmit.webapp.common

import de.denktmit.webapp.testutils.softAssert
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Year

class CalendarWeekTest {

    @Test
    fun createWithInvalidValues() {
        val result = assertThrows(IllegalArgumentException::class.java) {
            CalendarWeek.create(Year.now(), 70)
        }
        assertThat(result.message).isEqualTo("The weekNumber must be between greater 0 and lower or equal to 53, but was 70")
    }

    @Test
    fun createWithValidValues() {
        val result = CalendarWeek.create(Year.of(2020), 50)
        softAssert {
            assertThat(result.weekNumber).isEqualTo(50)
            assertThat(result.year).isEqualTo(Year.of(2020))
        }
    }
}
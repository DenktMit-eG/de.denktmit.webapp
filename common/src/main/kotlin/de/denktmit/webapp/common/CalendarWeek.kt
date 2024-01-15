package de.denktmit.webapp.common

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.temporal.WeekFields
import java.util.*

class CalendarWeek private constructor(
    val year: Year,
    val weekNumber: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val calendarWeeksInYear: Int
): Comparable<CalendarWeek> {

    companion object {
        private const val CALENDAR_YEAR_MIN_WEEKS = 52
        private const val CALENDAR_YEAR_MAX_WEEKS = 53

        fun create(year: Year, weekNumber: Int): CalendarWeek {
            val weeksCount = calendarWeeksInYear(year)
            if (weekNumber < 1 || weekNumber > weeksCount) {
                throw IllegalArgumentException("The weekNumber must be between greater 0 and lower or equal to ${CALENDAR_YEAR_MAX_WEEKS}, but was $weekNumber")
            }
            val firstDayOfWeek = calculateDayOfCalendarWeek(year, weekNumber, DayOfWeek.MONDAY)
            WeekFields.of(Locale.GERMANY)
            val lastDayOfWeek = firstDayOfWeek.with(DayOfWeek.SUNDAY)
            return CalendarWeek(year, weekNumber, firstDayOfWeek, lastDayOfWeek, weeksCount)
        }

        private fun calendarWeeksInYear(year: Year): Int {
            val firstThursday = calculateDayOfCalendarWeek(year, 1, DayOfWeek.THURSDAY)
            val thursdayAfterMinCalendarWeeks = firstThursday.plusWeeks(CALENDAR_YEAR_MIN_WEEKS.toLong())
            return if (thursdayAfterMinCalendarWeeks.year > firstThursday.year) {
                CALENDAR_YEAR_MIN_WEEKS
            } else {
                CALENDAR_YEAR_MAX_WEEKS
            }
        }

        private fun calculateDayOfCalendarWeek(year: Year, weekNumber: Int, dayOfWeek: DayOfWeek): LocalDate {
            return year.atDay(1)
                .with(WeekFields.ISO.weekOfYear(), weekNumber.toLong())
                .with(dayOfWeek)
        }

    }

    fun previousWeek(): CalendarWeek {
        return if (weekNumber == 1) {
            val previousYear = year.minusYears(1)
            return create(previousYear, calendarWeeksInYear(previousYear))
        } else {
            create(year, weekNumber - 1)
        }

    }

    fun nextWeek(): CalendarWeek {
        return if (weekNumber == calendarWeeksInYear) {
            create(year.plusYears(1), 1)
        } else {
            create(year, weekNumber + 1)
        }
    }

    override fun compareTo(other: CalendarWeek): Int {
        if (this == other) return 0
        if (this.year != other.year) {
            return this.year.compareTo(other.year)
        }
        return this.weekNumber.compareTo(other.weekNumber)
    }


    override fun toString(): String {
        return "CalendarWeek(year=$year, weekNumber=$weekNumber, startDate=$startDate, endDate=$endDate)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CalendarWeek

        if (year != other.year) return false
        if (weekNumber != other.weekNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year.hashCode()
        result = 31 * result + weekNumber
        return result
    }

}
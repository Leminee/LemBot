package tech.goodquestion.lembot.library.parser

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class LocalDateTimeDurationCalculator private constructor(){

    companion object Duration{

        @JvmStatic
        fun getDurationUntilInSeconds(localDateTime: LocalDateTime): Long {

            return ChronoUnit.SECONDS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInMinutes(localDateTime: LocalDateTime): Long {

            return ChronoUnit.MINUTES.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInHours(localDateTime: LocalDateTime): Long {

            return ChronoUnit.HOURS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInDays(localDateTime: LocalDateTime): Long {

            return ChronoUnit.DAYS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInWeeks(localDateTime: LocalDateTime): Long {

            return ChronoUnit.WEEKS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInMonths(localDateTime: LocalDateTime): Long {

            return ChronoUnit.MONTHS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getDurationUntilInYears(localDateTime: LocalDateTime): Long {

            return ChronoUnit.YEARS.between(LocalDateTime.now(), localDateTime)
        }

        @JvmStatic
        fun getPeriodUntil(localDateTime: LocalDateTime): String {

            val period = java.time.Period.between(LocalDateTime.now().toLocalDate(), localDateTime.toLocalDate())
            val years = abs(period.years)
            val months = abs(period.months)
            val days = abs(period.days)

            return "$years years $months months $days days"
        }
    }
}
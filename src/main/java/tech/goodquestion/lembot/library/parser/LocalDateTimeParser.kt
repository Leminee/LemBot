package tech.goodquestion.lembot.library.parser

import java.lang.IllegalArgumentException
import kotlin.math.*
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class LocalDateTimeParser private constructor() {

    companion object Parser {

        private var localDateTime: LocalDateTime? = null
        private const val MAX_EPOCH_DAY_VALUE = 365241780471

        private val specialDays = mapOf(
            "silvester" to LocalDateTime.of(LocalDateTime.now().year, 12, 31, 0, 0, 0),
            "christmas" to LocalDateTime.of(LocalDateTime.now().year, 12, 25, 0, 0, 0),
            "halloween" to LocalDateTime.of(LocalDateTime.now().year, 10, 31, 0, 0, 0),
            "now" to LocalDateTime.now(),
            "tomorrow" to LocalDateTime.now().plusDays(1),
            "today" to LocalDateTime.now(),
            "yesterday" to LocalDateTime.now().minusDays(1),
            "valentine" to LocalDateTime.of(LocalDateTime.now().year, 2, 14, 0, 0, 0),
            "corona" to LocalDateTime.of(2019, 12, 1, 0, 0, 0),
            "perm" to LocalDateTime.MAX
        )


        @JvmStatic
        fun parse(humanInput: String): LocalDateTime? {


            val cleanedHumanInput = removeSpecialCharacters(humanInput).lowercase()

            for (specialDay: String in specialDays.keys) {

                if (cleanedHumanInput.contains(specialDay)) {
                    return specialDays[specialDay]
                }
            }

            var years: Long = 0
            var months: Long = 0
            var weeks: Long = 0
            var days: Long = 0
            var hours: Long = 0
            var minutes: Long = 0
            var seconds: Long = 0

            val args: MutableList<String> = cleanedHumanInput.trim().split(" ") as MutableList<String>

            if (args.size == 1) throw IllegalArgumentException("no number found")

            args.removeAll(listOf(""))

            if (containsOutOfRangeValue(humanInput)) throw NumberFormatException("number too large")

            if (findWeekDay(humanInput.lowercase(Locale.getDefault())) != null) {

                val foundWeekDay:DayOfWeek? = findWeekDay(humanInput.lowercase(Locale.getDefault()))


                localDateTime = LocalDateTime.now().with(TemporalAdjusters.next(foundWeekDay))


                return localDateTime

            }


            for (arg: String in args) {

                try {

                    if (arg.startsWith("y")) years = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("mo")) months = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("w")) weeks = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("d")) days = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("h")) hours = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("mi")) minutes = args[args.indexOf(arg) - 1].toLong()
                    if (arg.startsWith("s")) seconds = args[args.indexOf(arg) - 1].toLong()

                } catch (numberFormatException: NumberFormatException) {

                    throw NumberFormatException("invalid input")

                }
            }

            if (years >= LocalDateTime.MAX.year) throw DateTimeException("years out of range")
            if (months / 12 >= LocalDateTime.MAX.year) throw DateTimeException("months out of range")
            if (days >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("days out of range")
            if (hours / 24 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("hours out of range")
            if (minutes / 1440 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("minutes out of range")
            if (seconds / 86400 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("seconds out of range")


            localDateTime = LocalDateTime.now()
                .plusYears(abs(years))
                .plusMonths(abs(months))
                .plusWeeks(abs(weeks))
                .plusDays(abs(days))
                .plusHours(abs(hours))
                .plusMinutes(abs(minutes))
                .plusSeconds(abs(seconds))

            return localDateTime
        }


        fun LocalDateTime.toGermanFormat(): String = this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))

        fun LocalDateTime.getPeriodUntil(): String {

            val period: Period = Period.between(LocalDateTime.now().toLocalDate(), this.toLocalDate())
            val years = abs(period.years)
            val months = abs(period.months)
            val days = abs(period.days)

            return "$years years $months months $days days"
        }

        private fun removeSpecialCharacters(string: String): String {

            return string.replace("[-+^]*".toRegex(), "")
        }

        private fun findWeekDay(input: String): DayOfWeek?{

            val foundWeekDay: DayOfWeek

            for (weekday in DayOfWeek.values()) {

                if (input.contains(weekday.toString().lowercase(Locale.getDefault()))) {

                    foundWeekDay = weekday

                    return foundWeekDay

                }


            }

            return null

        }

        private fun containsOutOfRangeValue(input:String):Boolean {

            val pattern: Pattern = Pattern.compile("\\d{19,}")
            val matcher: Matcher = pattern.matcher(input)

            return matcher.find()
        }


        fun getSpecialDays(): Set<String> {

            return specialDays.keys
        }
    }

}
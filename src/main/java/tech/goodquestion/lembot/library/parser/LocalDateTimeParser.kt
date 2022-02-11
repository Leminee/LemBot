package tech.goodquestion.lembot.library

import java.lang.IllegalArgumentException
import kotlin.math.*
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

            var years:Long = 0
            var months:Long = 0
            var weeks:Long = 0
            var days:Long = 0
            var hours:Long= 0
            var minutes:Long = 0
            var seconds:Long = 0


            val args: MutableList<String> = cleanedHumanInput.trim().split(" ") as MutableList<String>


            args.removeAll(listOf(""))

            for (arg: String in args) {

                try {

                    if (arg.startsWith("y"))  years = args[args.indexOf(arg) - 1].toLong()
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
            if (months/12 >= LocalDateTime.MAX.year)  throw DateTimeException("months out of range")
            if (days >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("days out of range")
            if (hours/24 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("hours out of range")
            if (minutes/1440 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("minutes out of range")
            if (seconds/86400 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("seconds out of range")



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


        @JvmStatic
        fun LocalDateTime.toGermanFormat(): String = this.format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm"))


        private fun removeSpecialCharacters(string: String): String {

            return string.replace("[-+^]*".toRegex(), "")
        }

        private fun containsOutOfRangeValues(input:String):Boolean {

            val args: List<String> = input.trim().split("")

            return false

            return true
        }


        fun getSpecialDays(): Set<String> {

            return specialDays.keys
        }
    }

}
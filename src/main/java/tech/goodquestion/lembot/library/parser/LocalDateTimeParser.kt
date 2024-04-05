package tech.goodquestion.lembot.library.parser

import java.lang.IllegalArgumentException
import java.time.*
import kotlin.math.*
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
            "perm" to LocalDateTime.MAX,
            "newyear" to LocalDateTime.of(LocalDateTime.now().year, 1, 1, 0, 0, 0),
            "easter" to calculateEasterDate(LocalDateTime.now().year), // Funktion zum Berechnen des Osterdatums
            "independenceday" to LocalDateTime.of(LocalDateTime.now().year, 7, 4, 0, 0, 0),
            "thanksgiving" to calculateThanksgivingDate(LocalDateTime.now().year), // Funktion zum Berechnen des Thanksgiving-Datums
            "earthDay" to LocalDateTime.of(LocalDateTime.now().year, 4, 22, 0, 0, 0),
            "aprilFools" to LocalDateTime.of(LocalDateTime.now().year, 4, 1, 0, 0, 0),
            "fallberlinwall" to LocalDateTime.of(1989, 11, 9, 0, 0, 0), // Mauerfall in Berlin
            "pearlharbor" to LocalDateTime.of(1941, 12, 7, 0, 0, 0), // Angriff auf Pearl Harbor
            "moonlanding" to LocalDateTime.of(1969, 7, 20, 0, 0, 0), // Mondlandung
            "berlinairlift" to LocalDateTime.of(1948, 6, 24, 0, 0, 0), // Berliner Luftbrücke
            "endWW2" to LocalDateTime.of(1945, 9, 2, 0, 0, 0), // Ende des Zweiten Weltkriegs
            "startww1" to LocalDateTime.of(1914, 7, 28, 0, 0, 0), // Beginn des Ersten Weltkriegs
            "hiroshima" to LocalDateTime.of(1945, 8, 6, 0, 0, 0), // Atombombenabwurf auf Hiroshima
            "nagasaki" to LocalDateTime.of(1945, 8, 9, 0, 0, 0), // Atombombenabwurf auf Nagasaki
            "tsunami2004" to LocalDateTime.of(2004, 12, 26, 0, 0, 0), // Tsunami im Indischen Ozean 2004
            "challengerdisaster" to LocalDateTime.of(1986, 1, 28, 0, 0, 0), // Challenger-Katastrophe
            "fukushima" to LocalDateTime.of(2011, 3, 11, 0, 0, 0), // Fukushima-Katastrophe
            "holocaust" to LocalDateTime.of(1945, 1, 27, 0, 0, 0), // Befreiung des Konzentrationslagers Auschwitz
            "russianRevolution" to LocalDateTime.of(1917, 10, 25, 0, 0, 0), // Russische Revolution
            "naziGermany" to LocalDateTime.of(1945, 5, 8, 0, 0, 0), // Ende des Dritten Reiches
            "september11" to LocalDateTime.of(2001, 9, 11, 0, 0, 0), // 11. September Anschläge
            "londonBombings" to LocalDateTime.of(2005, 7, 7, 0, 0, 0), // Londoner U-Bahn- und Busanschläge 2005
            "parissttacks" to LocalDateTime.of(2015, 11, 13, 0, 0, 0), // Anschläge in Paris 2015
            "christchurchshooting" to LocalDateTime.of(2019, 3, 15, 0, 0, 0), // Christchurch-Moschee-Angriffe
            "charliehebdo" to LocalDateTime.of(2015, 1, 7, 0, 0, 0), // Charlie Hebdo-Anschlag
            "oklahomabombing" to LocalDateTime.of(1995, 4, 19, 0, 0, 0), // Bombenanschlag von Oklahoma City
            "neujahr" to LocalDateTime.of(LocalDateTime.now().year, 1, 1, 0, 0, 0), // Neujahr
            "tagderarbeit" to LocalDateTime.of(LocalDateTime.now().year, 5, 1, 0, 0, 0), // Tag der Arbeit
            "tagdereinheit" to LocalDateTime.of(1990, 10, 3, 0, 0, 0), // Tag der Deutschen Einheit
            "weihnachten" to LocalDateTime.of(LocalDateTime.now().year, 12, 25, 0, 0, 0), // Weihnachten
            "ostern" to calculateEasterDate(LocalDateTime.now().year), // Ostern
            "pfingsten" to calculateEasterDate(LocalDateTime.now().year).plusDays(49), // Pfingsten
            "cannabislegalisierung" to LocalDateTime.of(2024, 3, 22, 0, 0, 0)
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

            if (args.size == 1) throw IllegalArgumentException("keine Zahl gefunden")

            args.removeAll(listOf(""))

            if (containsOutOfRangeValue(humanInput)) throw NumberFormatException("Zahl zu lang")

            if (findWeekDay(humanInput.lowercase(Locale.getDefault())) != null) {

                val foundWeekDay: DayOfWeek? = findWeekDay(humanInput.lowercase(Locale.getDefault()))


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

                    throw NumberFormatException("Input nicht valid")

                }
            }

            if (years >= LocalDateTime.MAX.year) throw DateTimeException("Erlaubte Zahl für Jahre überschrieten")
            if (months / 12 >= LocalDateTime.MAX.year) throw DateTimeException("Erlaubte Zahl für Monate überschrieten")
            if (days >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("Erlaubte Zahl für Tage überschrieten")
            if (hours / 24 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("Erlaubte Zahl für Stunden überschrieten")
            if (minutes / 1440 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("Erlaubte Zahl für Minuten überschrieten")
            if (seconds / 86400 >= MAX_EPOCH_DAY_VALUE) throw DateTimeException("Erlaubte Zahl für Sekunden überschrieten")


            localDateTime = LocalDateTime.now().plusYears(abs(years)).plusMonths(abs(months)).plusWeeks(abs(weeks))
                .plusDays(abs(days)).plusHours(abs(hours)).plusMinutes(abs(minutes)).plusSeconds(abs(seconds))

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

        private fun findWeekDay(input: String): DayOfWeek? {

            val foundWeekDay: DayOfWeek

            for (weekday in DayOfWeek.values()) {

                if (input.contains(weekday.toString().lowercase(Locale.getDefault()))) {

                    foundWeekDay = weekday

                    return foundWeekDay

                }


            }

            return null

        }

        private fun containsOutOfRangeValue(input: String): Boolean {

            val pattern: Pattern = Pattern.compile("\\d{19,}")
            val matcher: Matcher = pattern.matcher(input)

            return matcher.find()
        }


        fun getSpecialDays(): Set<String> {

            return specialDays.keys
        }


        private fun calculateEasterDate(year: Int): LocalDateTime {
            val a = year % 19
            val b = year / 100
            val c = year % 100
            val d = b / 4
            val e = b % 4
            val f = (b + 8) / 25
            val g = (b - f + 1) / 3
            val h = (19 * a + b - d - g + 15) % 30
            val i = c / 4
            val k = c % 4
            val l = (32 + 2 * e + 2 * i - h - k) % 7
            val m = (a + 11 * h + 22 * l) / 451
            val month = (h + l - 7 * m + 114) / 31
            val day = ((h + l - 7 * m + 114) % 31) + 1
            return LocalDateTime.of(year, month, day, 0, 0, 0)
        }

        private fun calculateThanksgivingDate(year: Int): LocalDateTime {
            val november = LocalDateTime.of(year, 11, 1, 0, 0, 0)
            var thanksgiving = november.with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY))
            if (thanksgiving.month != Month.NOVEMBER) {
                thanksgiving = november.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY))
            }
            return thanksgiving
        }
    }
}
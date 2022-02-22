package tech.goodquestion.lembot.library.parser

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateTimeFormatter private constructor() {

    companion object Formatter {

        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'um' HH:mm")

        @JvmStatic
        fun toGermanFormat(localDateTime: LocalDateTime): String {

            val germanFormat: DateTimeFormatter = dateFormatter.withLocale(Locale.GERMAN)

            return localDateTime.format(germanFormat)
        }


        @JvmStatic
        fun toUSFormat(localDateTime: LocalDateTime): String {

            val uSFormat: DateTimeFormatter = dateFormatter.withLocale(Locale.US)

            return localDateTime.format(uSFormat)
        }


        @JvmStatic
        fun formatTime(localTime: LocalTime): String? {

            val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val time: DateTimeFormatter = dateFormatter.withLocale(Locale.GERMAN)

            return localTime.format(time)

        }

    }
}
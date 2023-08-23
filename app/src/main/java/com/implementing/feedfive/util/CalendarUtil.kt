package com.implementing.feedfive.util

import com.implementing.feedfive.R
import com.implementing.feedfive.getString
import com.implementing.feedfive.model.CalendarEvent

const val CALENDAR_FREQ_DAILY = "DAILY"
const val CALENDAR_FREQ_WEEKLY = "WEEKLY"
const val CALENDAR_FREQ_MONTHLY = "MONTHLY"
const val CALENDAR_FREQ_YEARLY = "YEARLY"
const val CALENDAR_FREQ_NEVER = "NEVER"

fun String.toUIFrequency(): String {
    return when (this) {
        CALENDAR_FREQ_DAILY -> getString(R.string.every_day)
        CALENDAR_FREQ_WEEKLY -> getString(R.string.every_week)
        CALENDAR_FREQ_MONTHLY -> getString(R.string.every_month)
        CALENDAR_FREQ_YEARLY -> getString(R.string.every_year)
        else -> getString(R.string.do_not_repeat)
    }
}

fun CalendarEvent.getEventDuration(): String {
    return "P${(end - start) / 1000}S"
}

fun String.extractEndFromDuration(start: Long): Long {
    return try {
        val duration = this.substring(1, this.length - 1).toLong() * 1000
        start + duration
    }catch (e: Exception) {
        start
    }
}

fun CalendarEvent.getEventRule(): String {
   return buildString {
       append("FREQ=${frequency}")
       // will be implemented later
//       if (until > 0) {
//           val formattedUntil = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).format(until)
//           append(";UNTIL=${formattedUntil}")
//       }
//       if (count > 0) {
//           append(";COUNT=${count}")
//       }
//       if (interval > 0) {
//           append(";INTERVAL=${interval}")
//       }
   }
}

fun String.extractFrequency(): String {
    return if (this.contains("FREQ=")) {
        val freq = this.substringAfter("FREQ=")
        freq.substringBefore(";")
    } else CALENDAR_FREQ_NEVER
}


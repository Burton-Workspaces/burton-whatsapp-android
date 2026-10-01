package com.burton.chat.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUi {
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
    private val dateFormatter = DateTimeFormatter.ofPattern("M/d/yy", Locale.US)
    private val weekdayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.US)

    fun chatListTimestamp(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String {
        val time = Instant.ofEpochMilli(epochMillis).atZone(zoneId)
        val date = time.toLocalDate()
        val today = LocalDate.now(zoneId)
        return when (date) {
            today -> timeFormatter.format(time)
            today.minusDays(1) -> "Yesterday"
            else -> if (date.isAfter(today.minusDays(7))) {
                weekdayFormatter.format(time)
            } else {
                dateFormatter.format(time)
            }
        }
    }

    fun messageTimestamp(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String {
        val time = Instant.ofEpochMilli(epochMillis).atZone(zoneId)
        return timeFormatter.format(time)
    }
}

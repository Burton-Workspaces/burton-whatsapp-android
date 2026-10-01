package com.burton.chat.core.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeUiTest {
    private val zone = ZoneId.of("UTC")

    @Test
    fun `formats today as time`() {
        val noon = LocalDate.now(zone).atTime(12, 0).atZone(zone).toInstant().toEpochMilli()
        assertEquals("12:00 PM", DateTimeUi.chatListTimestamp(noon, zone))
    }

    @Test
    fun `formats message timestamps`() {
        val time = ZonedDateTime.of(2026, 10, 1, 9, 5, 0, 0, zone).toInstant().toEpochMilli()
        assertEquals("9:05 AM", DateTimeUi.messageTimestamp(time, zone))
    }
}

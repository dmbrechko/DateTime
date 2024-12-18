package com.example.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.MonthDay
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster

fun Long.toLocalDateViaUTC(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

fun LocalDate.format(): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return formatter.format(this)
}

fun nextMonthDay(monthDay: MonthDay): TemporalAdjuster {
    return TemporalAdjuster { temporal: Temporal ->
        temporal.with(monthDay)
            .plus(if(MonthDay.from(temporal) > monthDay) 1 else 0, ChronoUnit.YEARS)
    }
}
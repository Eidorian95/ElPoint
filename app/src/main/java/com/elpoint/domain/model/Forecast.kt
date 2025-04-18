package com.elpoint.domain.model

import java.time.ZonedDateTime

data class Forecast(
    val hours: List<Hour>
)

data class Hour(
    val time: ZonedDateTime,
    val gust: Double?,
    val waterTemperature: Double?,
    val waveDirection: Double?,
    val waveHeight: Double?,
    val wavePeriod: Double?,
    val windSpeed: Double?,
    val windDirection: Double?
)

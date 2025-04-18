package com.elpoint.domain.model

data class Forecast(
    val hours: List<Hour>
)

data class Hour(
    val time: Long,
    val gust: Double?,
    val waterTemperature: Double?,
    val waveDirection: Double?,
    val waveHeight: Double?,
    val wavePeriod: Double?,
    val windSpeed: Double?,
    val windDirection: Double?
)

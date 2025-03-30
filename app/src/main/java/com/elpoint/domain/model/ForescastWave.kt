package com.elpoint.domain.model

data class ForecastWave(
    val timestamps: List<TimestampForecast>,
    val units: Units?,
    val warning: String?
)

data class TimestampForecast(
    val timestamp: Long,
    val swell1: WaveData?,
    val waves: WaveData?,
    val wWaves: WaveData?
)

data class WaveData(
    val direction: Direction?,
    val height: Double?,
    val period: Double?
)

data class Units(
    val wavesDirectionUnit: String?,
    val wavesHeightUnit: String?,
    val wavesPeriodUnit: String?,
    val swell1DirectionUnit: String?,
    val swell1HeightUnit: String?,
    val swell1PeriodUnit: String?,
)
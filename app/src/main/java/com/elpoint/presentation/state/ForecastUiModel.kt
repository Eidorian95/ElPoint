package com.elpoint.presentation.state

class ForecastUiModel(
    val currentForecast: HourlyForecastUI?,
    val nextDaysForecast: List<DayForecastUI>,
)

data class DayForecastUI(
    val day: String,
    val hourlyForecast: List<HourlyForecastUI>
)
data class HourlyForecastUI(
    val time: String,
    val waves: WaveDataUI,
    val winds: WindDataUI
)

data class WaveDataUI(
    val direction: DirectionUI,
    val height: String,
    val period: String
)

data class WindDataUI(
    val direction: DirectionUI,
    val speed: String,
    val type: String
)

data class DirectionUI(
    val cardinal: String,
)
package com.elpoint.presentation.state

class ForecastUiModel(
    val currentForecast: HourlyForecastUI?,
    val nextHoursForecast: List<HourlyForecastUI>
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
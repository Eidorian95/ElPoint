package com.elpoint.presentation.state

internal data class ForecastUiModel(
    val currentForecast: HourlyForecastUI?,
    val nextDaysForecast: List<DayForecastUI>,
)

internal data class DayForecastUI(
    val day: String,
    val hourlyForecast: List<HourlyForecastUI>
)
internal data class HourlyForecastUI(
    val time: String,
    val waves: WaveDataUI,
    val winds: WindDataUI
)

internal data class WaveDataUI(
    val direction: DirectionUI,
    val height: String,
    val period: String
)

internal data class WindDataUI(
    val direction: DirectionUI,
    val speed: String,
    val type: String
)

internal  data class DirectionUI(
    val cardinal: String,
)
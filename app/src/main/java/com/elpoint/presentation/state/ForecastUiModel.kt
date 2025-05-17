package com.elpoint.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ForecastUiModel(
    val currentForecast: HourlyForecastUI?,
    val nextDaysForecast: List<DayForecastUI>,
)

@Immutable
internal data class DayForecastUI(
    val day: String,
    val hourlyForecast: List<HourlyForecastUI>
)

@Immutable
internal data class HourlyForecastUI(
    val time: String,
    val waves: WaveDataUI,
    val winds: WindDataUI
)

@Immutable
internal data class WaveDataUI(
    val direction: DirectionUI,
    val height: String,
    val period: String
)

@Immutable
internal data class WindDataUI(
    val direction: DirectionUI,
    val speed: String,
    val type: String
)

@Immutable
internal  data class DirectionUI(
    val cardinal: String,
)
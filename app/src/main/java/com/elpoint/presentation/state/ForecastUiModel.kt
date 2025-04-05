package com.elpoint.presentation.state

class ForecastUiModel(
    val currentForecast: HourlyForecastUI?,
    val nextHoursForecast: List<HourlyForecastUI>
)

data class HourlyForecastUI(
    val time: String,  // "15:00" (hora en formato 24h)
    val waves: WaveDataUI?,
    val winds: WindDataUI?
)

data class WaveDataUI(
    val direction: DirectionUI?, // Para dibujar la flecha
    val height: String,  // "1.5 m"
    val period: String  // "10 s"
)

data class WindDataUI(
    val direction: DirectionUI?, // Para dibujar la flecha
    val speed: String,  // "1.5 m"
    val type: String  // "10 s"
)

data class DirectionUI(
    val direction: String,
)
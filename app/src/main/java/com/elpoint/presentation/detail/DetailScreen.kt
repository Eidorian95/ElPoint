package com.elpoint.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.elpoint.presentation.state.DayForecastUI
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI


@Composable
internal fun DetailInformationScreen(forecast: ForecastUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CurrentHeader(forecast.currentForecast)
        HourlyForecastPager(forecast.nextDaysForecast)
    }
}

@Preview
@Composable
private fun HourlyForecastPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        DetailInformationScreen(
            forecast = ForecastUiModel(
                currentForecast = getMock(),
                nextDaysForecast = getDaysMock()
            )
        )
    }
}

private fun getMock() = HourlyForecastUI(
    time = "6", waves = WaveDataUI(
        direction = DirectionUI("N"),
        height = "1.5m",
        period = "12s"
    ), winds = WindDataUI(
        direction = DirectionUI("S"),
        speed = "27km/h",
        type = "CROSS-SHORE"
    )
)

private fun getDaysMock() = listOf(
    DayForecastUI(
        day = "Lunes", hourlyForecast = listOf(
            HourlyForecastUI(
                time = "6", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "9", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "12", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "15", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "18", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "20", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            )
        )
    ),
    DayForecastUI(
        day = "Martes", hourlyForecast = listOf(
            HourlyForecastUI(
                time = "6", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "9", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "12", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "15", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "18", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            ),
            HourlyForecastUI(
                time = "20", waves = WaveDataUI(
                    direction = DirectionUI("N"),
                    height = "1.5m",
                    period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"),
                    speed = "27km/h",
                    type = "CROSS-SHORE"
                )
            )
        )
    )
)




package com.elpoint.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI

@Composable
fun DetailInformationScreen(forecast: ForecastUiModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        CurrentHeader(forecast.currentForecast)
        HourlyForecast(forecast.nextDaysForecast)
    }
}

@Preview
@Composable
private fun HourlyForecastPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        DetailInformationScreen(
            forecast = ForecastUiModel(
                currentForecast = emptyList<HourlyForecastUI>().first(),
                nextDaysForecast = emptyList()
            )
        )
    }
}

/*
@Composable
private fun getMock() = listOf(
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
*/


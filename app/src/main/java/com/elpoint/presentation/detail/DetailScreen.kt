package com.elpoint.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI

@Composable
fun DetailInformationScreen(forecast: ForecastUiModel) {
    Column {
        CurrentHeader(forecast.currentForecast)
        HourlyForecast(forecast.nextHoursForecast)
    }
}

@Preview
@Composable
private fun HourlyForecastPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        DetailInformationScreen(
            forecast = ForecastUiModel(
                currentForecast = getMock().first(),
                nextHoursForecast = getMock()
            )
        )
    }
}

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


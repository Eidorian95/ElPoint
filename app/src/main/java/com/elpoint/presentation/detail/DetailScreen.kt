package com.elpoint.presentation.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elpoint.R
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI
import com.elpoint.ui.theme.ElPointTheme

@Composable
internal fun DetailScreen(state: State<ForecastState>) {
    when (val value = state.value) {
        ForecastState.Loading -> {
            Text(text = "Loading")
        }

        is ForecastState.Success -> {
            DetailInformation(value.forecast)
        }

        else -> {
            Text(text = "Error")
        }
    }
}

@Composable
private fun DetailInformation(forecast: ForecastUiModel) {
    Column {
        CurrentHeader(forecast.currentForecast)
        HourlyForecast(forecast.nextHoursForecast)
    }
}

@Composable
fun HourlyForecast(nextHoursForecast: List<HourlyForecastUI>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            //iconos de hora, ola y viento
        }
        nextHoursForecast.forEachIndexed { _, forecast ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.size( 24.dp)){
                    Text( text = forecast.time, modifier = Modifier.align(Alignment.Center))
                }
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.waves?.direction?.cardinal ?: "-")
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.waves?.height.orEmpty())
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.waves?.period.orEmpty())
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.winds?.direction?.cardinal ?: "-")
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.winds?.speed.orEmpty())
                Text(modifier = Modifier.padding(horizontal = 8.dp),text = forecast.winds?.type.orEmpty())
            }
        }

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

@Preview
@Composable
private fun HourlyForecastPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        DetailInformation(forecast = ForecastUiModel(currentForecast = getMock().first(), nextHoursForecast = getMock()))
    }
}

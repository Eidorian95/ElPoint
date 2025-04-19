package com.elpoint.presentation.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elpoint.R
import com.elpoint.presentation.state.HourlyForecastUI


@Composable
fun HourlyForecast(nextHoursForecast: List<HourlyForecastUI>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .background(Color(0xFF71b3e8)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeader(icon = R.drawable.baseline_access_time_24)
            TableHeader(icon = R.drawable.waves,0.8f)
            TableHeader(icon = R.drawable.arrowup)
            TableHeader(icon = R.drawable.baseline_access_time_24, 0.8f)
            TableHeader(icon = R.drawable.winds, 1f)
            TableHeader(icon = R.drawable.arrowup)
        }

        // Rows
        nextHoursForecast.forEachIndexed { index, forecast ->
            val background = if(index % 2 == 0) Color(0xFFf0f0f0) else Color.LightGray
            Row(
                modifier = Modifier.fillMaxWidth().background(background),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TableCell(text = forecast.time)
                TableCell(text = forecast.waves.height, 0.8f)
                TableCell(text = forecast.waves.direction.cardinal)
                TableCell(text = forecast.waves.period, 0.8f)
                TableCell(text = forecast.winds.speed, 1f)
                TableCell(text = forecast.winds.direction.cardinal)
            }
        }
    }
}

@Composable
private fun RowScope.TableHeader(@DrawableRes icon: Int, weight: Float = 0.6f) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float = 0.6f) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 16.sp, maxLines = 1)
    }
}

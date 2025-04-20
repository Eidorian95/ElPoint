package com.elpoint.presentation.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elpoint.R
import com.elpoint.presentation.state.HourlyForecastUI

@Composable
internal fun CurrentHeader(currentForecast: HourlyForecastUI?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            .background(Color(0xFF71b3e8))
    ) {
        Column {
            Text(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = currentForecast?.time.plus("h"),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                val waves  = currentForecast?.waves
                val winds  = currentForecast?.winds
                CurrentForecast(
                    icon = R.drawable.waves,
                    direction = waves?.direction?.cardinal ?: "-",
                    subtitleLeft = waves?.height ?: "-",
                    subtitleRight = waves?.period ?: "-",
                )
                Divider(
                    modifier = Modifier
                        .height(200.dp)
                        .width(1.dp)
                        .padding(bottom = 16.dp),
                    color = Color.White
                )
                CurrentForecast(
                    modifier = Modifier.padding(start = 16.dp),
                    icon = R.drawable.winds,
                    direction = winds?.direction?.cardinal ?: "-",
                    subtitleLeft = winds?.speed ?: "-",
                    subtitleRight = "CROSS-OFF",
                )
            }
        }
    }
}


@Composable
private fun RowScope.CurrentForecast(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    direction: String,
    subtitleLeft: String,
    subtitleRight: String,
) {
    Column(
        modifier = modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(48.dp),
            painter = painterResource(id = icon),
            contentDescription = ""
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = direction,
                color = Color.White,
                fontSize = 82.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                modifier = Modifier.size(82.dp),
                painter = painterResource(id = R.drawable.arrowup),
                contentDescription = ""
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = subtitleLeft,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(
                modifier = Modifier
                    .height(16.dp)
                    .width(1.dp),
                color = Color.White
            )
            Text(
                modifier = Modifier,
                text = subtitleRight,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

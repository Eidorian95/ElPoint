package com.elpoint.presentation.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elpoint.R
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI

private val HeaderBackgroundColor = Color(0xFF71b3e8)
private val HeaderShape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
private val TextColorWhite = Color.White
private val DividerColorWhite = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrentHeader(
    currentForecast: HourlyForecastUI?,
    pointName: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val headerTextColor = Color.White
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(HeaderBackgroundColor)
            .clip(HeaderShape)
            .padding(bottom = 16.dp)
            .systemBarsPadding()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pointName,
                        color = TextColorWhite,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ahora: ${currentForecast?.time.orEmpty()} hs",
                        color = TextColorWhite,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                IconButton(modifier= Modifier.align(Alignment.Top),onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Guardar en Favoritos",
                        tint = if (isFavorite) Color.Red.copy(alpha = 0.8f) else headerTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                val waves = currentForecast?.waves
                val winds = currentForecast?.winds

                CurrentForecastItem(
                    icon = R.drawable.waves,
                    iconDesc = "Waves",
                    direction = waves?.direction?.cardinal ?: "-",
                    valuePrimary = waves?.height ?: "-",
                    valueSecondary = waves?.period ?: "-",
                    modifier = Modifier.weight(1f)
                )

                Divider(
                    modifier = Modifier
                        .height(150.dp)
                        .width(2.dp)
                        .align(Alignment.CenterVertically), color = DividerColorWhite
                )

                CurrentForecastItem(
                    icon = R.drawable.winds,
                    iconDesc = "Winds",
                    direction = winds?.direction?.cardinal ?: "-",
                    valuePrimary = winds?.speed ?: "-",
                    valueSecondary = winds?.type ?: "-",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun CurrentForecastItem(
    @DrawableRes icon: Int,
    iconDesc: String,
    direction: String,
    valuePrimary: String,
    valueSecondary: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = iconDesc,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = direction,
                color = TextColorWhite,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.arrowup),
                contentDescription = "Direction indicator",
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = valuePrimary,
            color = TextColorWhite,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = valueSecondary,
            color = TextColorWhite.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    Box {
        CurrentHeader(
            pointName = "waikiki Miramar", currentForecast = HourlyForecastUI(
                time = "6", waves = WaveDataUI(
                    direction = DirectionUI("N"), height = "1.5m", period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"), speed = "27km/h", type = "CROSS-SHORE"
                )
            ), isFavorite = false, onFavoriteClick = {})
    }
}

@Preview
@Composable
private fun HeaderExtraLargeNamePreview() {
    Box {
        CurrentHeader(
            pointName = "waikiki Club de Mar, Miramar -35487, -3546",
            currentForecast = HourlyForecastUI(
                time = "6", waves = WaveDataUI(
                    direction = DirectionUI("N"), height = "1.5m", period = "12s"
                ), winds = WindDataUI(
                    direction = DirectionUI("S"), speed = "27km/h", type = "CROSS-SHORE"
                )
            ),
            isFavorite = true,
            onFavoriteClick = {})
    }
}
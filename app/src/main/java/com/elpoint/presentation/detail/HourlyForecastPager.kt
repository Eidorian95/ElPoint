package com.elpoint.presentation.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elpoint.presentation.state.DayForecastUI
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI
import kotlinx.coroutines.launch

private val RowBackgroundEven = Color(0xFFf0f0f0)
private val RowBackgroundOdd = Color.LightGray
private val TableHeaderBackgroundColor = Color(0xFF71b3e8)
private val TextColorWhite = Color.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ColumnScope.HourlyForecastPager(daysForecast: List<DayForecastUI>) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { daysForecast.size })
    val dayTitles = remember(daysForecast) { daysForecast.map { it.day } }


        if (dayTitles.isNotEmpty()) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                dayTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title, style = MaterialTheme.typography.labelLarge) }
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { pageIndex ->
            val dayData = daysForecast[pageIndex]
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                stickyHeader {
                    HourlyForecastHeader()
                }
                items(
                    items = dayData.hourlyForecast,
                    key = { "Forecast_time_${it.time}" }
                ) {
                    val backgroundColor =
                        if (dayData.hourlyForecast.indexOf(it) % 2 == 0) RowBackgroundEven else RowBackgroundOdd
                    HourlyForecastRowItem(
                        forecast = it,
                        backgroundColor = backgroundColor
                    )
                }
            }
        }
}
@Composable
private fun HourlyForecastHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(TableHeaderBackgroundColor)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableHeaderCell(text = "Hora")
        TableHeaderCell(text = "Alt.")
        TableHeaderCell(text = "Dir.")
        TableHeaderCell(text = "Per.")
        TableHeaderCell(text = "Vel.")
        TableHeaderCell(text = "Dir.")
    }
}
@Composable
private fun RowScope.TableHeaderCell(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        modifier = modifier
            .weight(1f)
            .padding(vertical = 8.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        style = MaterialTheme.typography.labelMedium,
        color = TextColorWhite,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun HourlyForecastRowItem(
    forecast: HourlyForecastUI,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp), // Consistent padding
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCellItem(text = forecast.time)
        TableCellItem(text = forecast.waves.height)
        TableCellItem(text = forecast.waves.direction.cardinal)
        TableCellItem(text = forecast.waves.period)
        TableCellItem(text = forecast.winds.speed)
        TableCellItem(text = forecast.winds.direction.cardinal)
    }
}

@Composable
private fun RowScope.TableCellItem(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HourlyPagerPreview() {
    Column {
        HourlyForecastPager(
            daysForecast = listOf(
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
            ))
    }
}


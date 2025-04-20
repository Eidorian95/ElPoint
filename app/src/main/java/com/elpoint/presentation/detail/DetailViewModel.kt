package com.elpoint.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Direction
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.model.Hour
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.presentation.state.DayForecastUI
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state: StateFlow<ForecastState> = _state

    fun fetchForecast(lat: Double = -38.2753982, lon: Double = -57.8324016) {
        viewModelScope.launch {
            try {
                val forecast = getForecastUseCase(lat, lon)
                _state.value = ForecastState.Success(forecast.toUIModel())
                Log.d("FORESCAST RESPONSE", "${forecast.hours}")
            } catch (e: Exception) {
                _state.value =
                    ForecastState.Error("Error fetching forecast: ${e.message}. Cause: ${e.cause}")
                Log.d("FORESCAST ERROR", e.message.orEmpty())
            }
        }
    }


    private fun Forecast.toUIModel(): ForecastUiModel {
        val now = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))
        val today = now.toLocalDate()
        val targetDates = (0..2).map { today.plusDays(it.toLong()) }

        val forecastsPerDay = hours
            .filter { it.time.toLocalDate() in targetDates }
            .groupBy { it.time.toLocalDate() }

        val current = forecastsPerDay[today]
            ?.firstOrNull { it.time.isAfter(now) || it.time.isEqual(now) }
            ?.toHourlyForecastUI()

        val nextDaysForecast = forecastsPerDay
            .toSortedMap()
            .map { (date, hourlyList) ->
                DayForecastUI(
                    day = date.format(DateTimeFormatter.ofPattern("EEEE dd", Locale("es"))),
                    hourlyForecast = hourlyList
                        .sortedBy { it.time.toEpochSecond() }
                        .map { it.toHourlyForecastUI() }
                )
            }

        return ForecastUiModel(
            currentForecast = current,
            nextDaysForecast = nextDaysForecast
        )
    }

    private fun Hour.toHourlyForecastUI(): HourlyForecastUI {
        return HourlyForecastUI(
            time = "${time.toHourFormat()}h",
            waves = WaveDataUI(
                direction = waveDirection.toDirectionUI(),
                height = "${waveHeight}m",
                period = "${wavePeriod}s"
            ),
            winds = WindDataUI(
                direction = windDirection.toDirectionUI(),
                speed = "${windSpeed}m/s",
                type = "OFF-SHORE"
            )
        )
    }

    private fun Double?.toDirectionUI(): DirectionUI {
        return DirectionUI(
            cardinal = Direction.fromDegrees(this?.toInt() ?: 0).abbreviation,
        )
    }

    private fun ZonedDateTime.toHourFormat(): String {
        return this.format(DateTimeFormatter.ofPattern("HH"))
    }
}
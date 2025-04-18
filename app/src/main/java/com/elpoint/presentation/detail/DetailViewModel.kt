package com.elpoint.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Direction
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.model.Hour
import com.elpoint.domain.usecases.GetForecastUseCase
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
        val currentTimestamp = System.currentTimeMillis() / 1000

        val futureForecasts = this.hours
            .filter { it.time >= currentTimestamp }
            .sortedBy { it.time }

        val current = futureForecasts.firstOrNull()
        val targetHours = listOf(6, 9, 12, 15, 18, 20)

          val nextHoursForecast = targetHours.mapNotNull { targetHour ->
              futureForecasts.minByOrNull { forecast ->
                  val forecastHour = forecast.time.toHourOfDay()
                  kotlin.math.abs(forecastHour - targetHour)
              }
          }.distinctBy { it.time }
          return ForecastUiModel(
              currentForecast = current?.toHourlyForecastUI(),
              nextHoursForecast = nextHoursForecast.map { it.toHourlyForecastUI() }
          )
    }


   private fun Hour.toHourlyForecastUI(): HourlyForecastUI {
        return HourlyForecastUI(
            time = time.toHourFormat(),
            waves = WaveDataUI(
                direction = waveDirection?.toDirectionUI(),
                height = "${waveHeight}m",
                period = "${wavePeriod}s"
            ),
            winds = WindDataUI(
                direction = windDirection?.toDirectionUI(),
                speed = "${windSpeed}m/s",
                type = "OFF-SHORE"
            )
        )
    }

    private fun Double.toDirectionUI(): DirectionUI {
        return DirectionUI(
            cardinal = Direction.fromDegrees(this.toInt()).abbreviation,
        )
    }

    private fun Long.toHourFormat(): String {
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        return sdf.format(Date(this * 1000))
    }

    private fun Long.toHourOfDay(): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = this@toHourOfDay * 1000
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}
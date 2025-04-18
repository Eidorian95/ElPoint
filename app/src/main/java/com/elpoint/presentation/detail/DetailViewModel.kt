package com.elpoint.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Direction
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
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
               // _state.value = ForecastState.Success(forecast)
                Log.d("FORESCAST RESPONSE", "${forecast.hours}")
            } catch (e: Exception) {
                _state.value =
                    ForecastState.Error("Error fetching forecast: ${e.message}. Cause: ${e.cause}")
                Log.d("FORESCAST ERROR", e.message.orEmpty())
            }
        }
    }
/*

    private fun ForecastWave.toUIModel(): ForecastUiModel {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val futureForecasts = timestamps
            .filter { it.timestamp >= currentTimestamp }
            .sortedBy { it.timestamp }

        val current = futureForecasts.firstOrNull()
        val targetHours = listOf(6, 9, 12, 15, 18, 20)

        val nextHoursForecast = targetHours.mapNotNull { targetHour ->
            futureForecasts.minByOrNull { forecast ->
                val forecastHour = forecast.timestamp.toHourOfDay()
                kotlin.math.abs(forecastHour - targetHour)
            }
        }.distinctBy { it.timestamp }

        return ForecastUiModel(
            currentForecast = current?.toHourlyForecastUI(units),
            nextHoursForecast = nextHoursForecast.map { it.toHourlyForecastUI(units) }
        )
    }
*/

/*    private fun TimestampForecast.toHourlyForecastUI(units: Units?): HourlyForecastUI {
        return HourlyForecastUI(
            time = timestamp.toHourFormat(),
            waves = waves?.toWaveDataUI(units?.wavesHeightUnit, units?.wavesPeriodUnit),
            winds = wWaves?.toWindDataUi(units?.wavesHeightUnit, units?.wavesPeriodUnit)
        )
    }

    private fun WaveData.toWaveDataUI(heightUnit: String?, periodUnit: String?): WaveDataUI {
        return WaveDataUI(
            direction = direction?.toDirectionUI(),
            height = "${height ?: "-"} ${heightUnit.orEmpty()}",
            period = "${period ?: "-"} ${periodUnit.orEmpty()}"
        )
    }
    //TODO: cambiar la info de ola por el de viento con nueva API
    private fun WaveData.toWindDataUi(heightUnit: String?, periodUnit: String?): WindDataUI {
        return WindDataUI(
            direction = direction?.toDirectionUI(),
            speed = "27km/h",
            type = "CROSS_SHORE"
        )
    }*/

    private fun Direction.toDirectionUI(): DirectionUI {
        return DirectionUI(
            direction = this.abbreviation,
        )
    }

    private fun Long.toHourFormat(): String {
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        return sdf.format(Date(this * 1000))
    }

    fun Long.toHourOfDay(): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = this@toHourOfDay * 1000
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}
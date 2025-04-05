package com.elpoint.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Direction
import com.elpoint.domain.model.ForecastWave
import com.elpoint.domain.model.TimestampForecast
import com.elpoint.domain.model.Units
import com.elpoint.domain.model.WaveData
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state: StateFlow<ForecastState> = _state

    fun fetchForecast(lat: Double = -36.5759957, lon: Double = -56.6891419) {
        viewModelScope.launch {
            try {
                val forecast = getForecastUseCase(lat, lon)
                _state.value = ForecastState.Success(forecast.toUIModel())
            } catch (e: Exception) {
                _state.value =
                    ForecastState.Error("Error fetching forecast: ${e.message}. Cause: ${e.cause}")
            }
        }
    }

    private fun ForecastWave.toUIModel(): ForecastUiModel {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val sortedForecasts = timestamps.sortedBy { it.timestamp }

        val current = sortedForecasts.firstOrNull { it.timestamp >= currentTimestamp }
        val nextHours = sortedForecasts.filter { it.timestamp > currentTimestamp }

        return ForecastUiModel(
            currentForecast = current?.toHourlyForecastUI(units),
            nextHoursForecast = nextHours.map { it.toHourlyForecastUI(units) }
        )
    }

    private fun TimestampForecast.toHourlyForecastUI(units: Units?): HourlyForecastUI {
        return HourlyForecastUI(
            time = timestamp.toHourFormat(),
            swell1 = swell1?.toWaveDataUI(units?.swell1HeightUnit, units?.swell1PeriodUnit),
            waves = waves?.toWaveDataUI(units?.wavesHeightUnit, units?.wavesPeriodUnit),
            wWaves = wWaves?.toWaveDataUI(units?.wavesHeightUnit, units?.wavesPeriodUnit)
        )
    }

    private fun WaveData.toWaveDataUI(heightUnit: String?, periodUnit: String?): WaveDataUI {
        return WaveDataUI(
            direction = direction?.toDirectionUI(),
            height = "${height ?: "-"} ${heightUnit.orEmpty()}",
            period = "${period ?: "-"} ${periodUnit.orEmpty()}"
        )
    }

    private fun Direction.toDirectionUI(): DirectionUI {
        return DirectionUI(
            direction = this.abbreviation,
        )
    }

    private fun Long.toHourFormat(): String {
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        return sdf.format(Date(this * 1000))
    }

}
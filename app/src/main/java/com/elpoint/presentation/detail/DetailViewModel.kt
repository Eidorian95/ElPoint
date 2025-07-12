package com.elpoint.presentation.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Direction
import com.elpoint.domain.model.FavoriteSpot
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.model.Hour
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.domain.usecases.favorites.DeleteSpotUseCase
import com.elpoint.domain.usecases.favorites.GetFavoriteStatusUseCase
import com.elpoint.domain.usecases.favorites.SaveSpotUseCase
import com.elpoint.presentation.state.DayForecastUI
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastUiModel
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val saveSpotUseCase: SaveSpotUseCase,
    private val deleteSpotUseCase: DeleteSpotUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val lat = savedStateHandle.get<Double>("PLACE_LAT")
    private val lon = savedStateHandle.get<Double>("PLACE_LNG")
    private val name = savedStateHandle.get<String>("PLACE_NAME") ?: ""
    private val spotId = savedStateHandle.get<String>("SPOT_ID")

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state: StateFlow<ForecastState> = _state.asStateFlow()

    fun loadInitialData() {
        if (state.value !is ForecastState.Loading) return

        if (lat != null && lon != null) {
            fetchForecast(lat, lon)
        } else {
            _state.value = ForecastState.Error("Datos del spot incompletos.")
        }

        spotId?.let { id ->
            observeFavoriteStatus(id)
        }
    }

    private fun fetchForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val forecast = getForecastUseCase(lat = lat, lon = lon)
                val currentIsFavorite = (_state.value as? ForecastState.Success)?.isFavorite ?: false
                _state.value = ForecastState.Success(forecast.toUIModel(), currentIsFavorite)
                Log.d("FORECAST_DETAIL_TEST", "Success fetching forecast: $forecast")
            } catch (e: Exception) {
                _state.value = ForecastState.Error("Error fetching forecast: ${e.message}")
                Log.d("FORECAST_DETAIL_TEST", "Error fetching forecast: $e")
            }
        }
    }

    private fun observeFavoriteStatus(id: String) {
        viewModelScope.launch {
            getFavoriteStatusUseCase(id).collect { isFavorite ->
                val currentState = _state.value
                if (currentState is ForecastState.Success) {
                    _state.value = currentState.copy(isFavorite = isFavorite)
                }
            }
        }
    }

    fun onFavoriteToggleClicked() {
        viewModelScope.launch {
            val currentState = _state.value as? ForecastState.Success ?: return@launch
            val currentSpotId = spotId ?: return@launch

            if (currentState.isFavorite) {
                deleteSpotUseCase(currentSpotId)
            } else {
                val spotToSave = FavoriteSpot(
                    id = currentSpotId,
                    name = name,
                    lat = lat ?: 0.0,
                    lon = lon ?: 0.0
                )
                saveSpotUseCase(spotToSave)
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
            name = this@DetailViewModel.name,
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
package com.elpoint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.presentation.state.ForecastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state: StateFlow<ForecastState> = _state

    fun fetchForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val forecast = getForecastUseCase(lat, lon)
                _state.value = ForecastState.Success(forecast)
            } catch (e: Exception) {
                _state.value = ForecastState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
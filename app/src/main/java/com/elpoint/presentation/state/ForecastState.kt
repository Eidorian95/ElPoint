package com.elpoint.presentation.state

import com.elpoint.domain.model.Forecast


sealed class ForecastState {
    data object Loading : ForecastState()
    data class Success(val forecast: Forecast) : ForecastState()
    data class Error(val message: String) : ForecastState()
}
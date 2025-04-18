package com.elpoint.presentation.state

import com.elpoint.domain.model.ForecastWave


sealed class ForecastState {
    data object Loading : ForecastState()
    data class Success(val forecast: ForecastWave) : ForecastState()
    data class Error(val message: String) : ForecastState()
}
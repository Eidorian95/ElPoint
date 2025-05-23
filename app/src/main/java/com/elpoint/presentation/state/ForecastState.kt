package com.elpoint.presentation.state


internal sealed class ForecastState {
    data object Loading : ForecastState()
    data class Success(val forecast: ForecastUiModel) : ForecastState()
    data class Error(val message: String) : ForecastState()
}
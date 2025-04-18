package com.elpoint.domain.repository

import com.elpoint.domain.model.Forecast


internal interface ForecastRepository {
    suspend fun getForecast(lat: Double, lon: Double): Forecast
}
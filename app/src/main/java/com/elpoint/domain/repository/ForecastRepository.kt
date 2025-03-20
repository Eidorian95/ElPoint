package com.elpoint.domain.repository

import com.elpoint.domain.model.Forecast

interface ForecastRepository {
    suspend fun getForecast(lat: Double, lon: Double): Forecast
}
package com.elpoint.domain.repository

import com.elpoint.domain.model.ForecastWave


internal interface ForecastRepository {
    suspend fun getForecast(lat: Double, lon: Double): ForecastWave
}
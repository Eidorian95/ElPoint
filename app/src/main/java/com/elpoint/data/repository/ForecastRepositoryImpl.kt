package com.elpoint.data.repository

import com.elpoint.data.remote.ApiService
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.repository.ForecastRepository
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ForecastRepository {

    override suspend fun getForecast(lat: Double, lon: Double): Forecast {
        val response = apiService.getForecast(lat, lon, "API_KEY")
        return Forecast(
            temperature = response.temperature,
            windSpeed = response.windSpeed,
            waveHeight = response.waveHeight
        )
    }
}
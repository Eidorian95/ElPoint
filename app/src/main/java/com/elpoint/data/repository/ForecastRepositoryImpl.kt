package com.elpoint.data.repository

import com.elpoint.data.remote.ApiService
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.model.ForecastResponse
import com.elpoint.domain.model.Hour
import com.elpoint.domain.model.HoursDto
import com.elpoint.domain.repository.ForecastRepository
import javax.inject.Inject

internal class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ForecastRepository {

    override suspend fun getForecast(lat: Double, lon: Double): Forecast {
        val response = apiService.getForecast(
                lat = lat,
                lng = lon,
                source = "sg",
                params = "waterTemperature,wavePeriod,waveDirection,waveHeight,windSpeed,windDirection,gust"
        )
        return response.toDomainModel()
    }
}

private fun ForecastResponse.toDomainModel(): Forecast {
    return Forecast(
        hours = this.hours.toDomainModel()
    )
}

private fun List<HoursDto>.toDomainModel(): List<Hour> {
    return this.map {
        Hour(
            time = it.time,
            gust = it.gust?.sg,
            waterTemperature = it.waterTemperature?.sg,
            waveDirection = it.waveDirection?.sg,
            waveHeight = it.waveHeight?.sg,
            wavePeriod = it.wavePeriod?.sg,
            windSpeed = it.windSpeed?.sg,
            windDirection = it.windDirection?.sg
        )
    }
}
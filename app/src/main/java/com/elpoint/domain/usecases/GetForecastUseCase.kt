package com.elpoint.domain.usecases

import com.elpoint.domain.model.Forecast
import com.elpoint.domain.repository.ForecastRepository
import javax.inject.Inject


internal class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Forecast {
        return repository.getForecast(lat, lon)
    }
}
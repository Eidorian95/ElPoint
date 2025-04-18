package com.elpoint.data.remote

import com.elpoint.domain.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ApiService {
    @GET("weather/point")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("source") source: String,
        @Query("params") params: String,
    ): ForecastResponse
}
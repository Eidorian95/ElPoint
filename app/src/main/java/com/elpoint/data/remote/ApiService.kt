package com.elpoint.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("apikey") apiKey: String
    ): ForecastResponse
}


data class ForecastResponse(
    val temperature: Double,
    val windSpeed: Double,
    val waveHeight: Double
)
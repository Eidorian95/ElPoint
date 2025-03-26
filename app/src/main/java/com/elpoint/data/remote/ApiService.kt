package com.elpoint.data.remote

import com.elpoint.domain.model.ForecastWaveResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface ApiService {
    @POST("point-forecast/v2")
    suspend fun getForecast(
        @Body body: ForecastBody,
    ): ForecastWaveResponse
}

internal data class ForecastBody(
    val lat: Double,
    val lon: Double,
    val model: String,
    val levels: List<String>,
    val parameters: List<String>,
    val key: String = "JIqBWLUVopVpLlrVGiojCViSy7KphizP"
)

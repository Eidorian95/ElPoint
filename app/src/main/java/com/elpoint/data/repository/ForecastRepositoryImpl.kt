package com.elpoint.data.repository

import android.content.Context
import com.elpoint.data.remote.ApiService
import com.elpoint.domain.model.Forecast
import com.elpoint.domain.model.ForecastResponse
import com.elpoint.domain.model.Hour
import com.elpoint.domain.model.HoursDto
import com.elpoint.domain.repository.ForecastRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : ForecastRepository {

    override suspend fun getForecast(lat: Double, lon: Double): Forecast {
       /* val response = apiService.getForecast(
                lat = lat,
                lng = lon,
                source = "sg",
                params = "waterTemperature,wavePeriod,waveDirection,waveHeight,windSpeed,windDirection,gust"
        )*/
        val response = readJsonFromAssets(context, "forecast_mock.json")
        return response.toDomainModel()
    }
}
fun readJsonFromAssets(context: Context, fileName: String): ForecastResponse {
    val json =  context.assets.open(fileName).bufferedReader().use { it.readText() }
    val gson = Gson()
    return gson.fromJson(json, ForecastResponse::class.java)
}

private fun ForecastResponse.toDomainModel(): Forecast {
    return Forecast(
        hours = this.hours.toDomainModel()
    )
}

private fun List<HoursDto>.toDomainModel(): List<Hour> {
    return this.map {
        Hour(
            time = it.time.toZonedDateTimeUtc().toLocalZoned(),
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
private fun String.toZonedDateTimeUtc(): ZonedDateTime {
    return ZonedDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
private fun ZonedDateTime.toLocalZoned(): ZonedDateTime {
    return this.withZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"))
}
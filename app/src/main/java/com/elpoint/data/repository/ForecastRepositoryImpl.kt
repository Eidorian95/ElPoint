package com.elpoint.data.repository

import com.elpoint.data.remote.ApiService
import com.elpoint.data.remote.ForecastBody
import com.elpoint.domain.model.Direction
import com.elpoint.domain.model.ForecastWave
import com.elpoint.domain.model.ForecastWaveResponse
import com.elpoint.domain.model.TimestampForecast
import com.elpoint.domain.model.Units
import com.elpoint.domain.model.UnitsResponse
import com.elpoint.domain.model.WaveData
import com.elpoint.domain.repository.ForecastRepository
import javax.inject.Inject
import kotlin.math.round

internal class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ForecastRepository {

    override suspend fun getForecast(lat: Double, lon: Double): ForecastWave {
        val response = apiService.getForecast(
            body = ForecastBody(
                lat = lat,
                lon = lon,
                model = "gfsWave",
                levels = listOf("surface"),
                parameters = listOf("waves", "windWaves", "swell1", "swell2")
            )
        )
        return response.toDomainModel()
    }
}

private fun ForecastWaveResponse.toDomainModel(): ForecastWave {
    val timestamps = ts?.mapIndexed { index, timestamp ->
        TimestampForecast(
            timestamp = timestamp ?: 0L,
            swell1 = WaveData(
                direction = Direction.fromDegrees(
                    swell1DirectionSurface?.getOrNull(index)?.toInt() ?: 0
                ),
                height = swell1HeightSurface?.getHeightOrNull(index),
                period = swell1PeriodSurface?.getPeriodOrNull(index)
            ),
            waves = WaveData(
                direction = Direction.fromDegrees(
                    wavesDirectionSurface?.getOrNull(index)?.toInt() ?: 0
                ),
                height = wavesHeightSurface?.getHeightOrNull(index),
                period = wavesPeriodSurface?.getPeriodOrNull(index)
            ),
            wWaves = WaveData(
                direction = Direction.fromDegrees(
                    wwavesDirectionSurface?.getOrNull(index)?.toInt() ?: 0
                ),
                height = wwavesHeightSurface?.getHeightOrNull(index),
                period = wwavesPeriodSurface?.getPeriodOrNull(index)
            )
        )
    } ?: emptyList()

    return ForecastWave(
        timestamps = timestamps,
        units = units?.toDomainModel(),
        warning = warning
    )
}


fun UnitsResponse.toDomainModel() = Units(
    wavesDirectionUnit = wavesDirectionSurface.orEmpty(),
    wavesHeightUnit = wavesHeightSurface.orEmpty(),
    wavesPeriodUnit = wavesPeriodSurface.orEmpty(),
    swell1DirectionUnit = swell1DirectionSurface.orEmpty(),
    swell1HeightUnit = swell1HeightSurface.orEmpty(),
    swell1PeriodUnit = swell1PeriodSurface.orEmpty()

)

fun List<Double?>.getHeightOrNull(index:Int): Double? {
    return this.getOrNull(index)?.let { round(it * 10) / 10 }
}

fun List<Double?>.getPeriodOrNull(index:Int): Int? {
    return this.getOrNull(index)?.toInt()
}
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
                height = swell1HeightSurface?.getOrNull(index),
                period = swell1PeriodSurface?.getOrNull(index)
            ),
            waves = WaveData(
                direction = Direction.fromDegrees(
                    wavesDirectionSurface?.getOrNull(index)?.toInt() ?: 0
                ),
                height = wavesHeightSurface?.getOrNull(index),
                period = wavesPeriodSurface?.getOrNull(index)
            ),
            wWaves = WaveData(
                direction = Direction.fromDegrees(
                    wwavesDirectionSurface?.getOrNull(index)?.toInt() ?: 0
                ),
                height = wwavesHeightSurface?.getOrNull(index),
                period = wwavesPeriodSurface?.getOrNull(index)
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
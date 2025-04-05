package com.elpoint.domain.model


import com.google.gson.annotations.SerializedName

data class ForecastWaveResponse(
    @SerializedName("ts")
    val ts: List<Long?>?,
    @SerializedName("units")
    val units: UnitsResponse?,
    @SerializedName("warning")
    val warning: String?,
    @SerializedName("waves_direction-surface")
    val wavesDirectionSurface: List<Double?>?,
    @SerializedName("waves_height-surface")
    val wavesHeightSurface: List<Double?>?,
    @SerializedName("waves_period-surface")
    val wavesPeriodSurface: List<Double?>?,
    @SerializedName("wwaves_direction-surface")
    val wwavesDirectionSurface: List<Double?>?,
    @SerializedName("wwaves_height-surface")
    val wwavesHeightSurface: List<Double?>?,
    @SerializedName("wwaves_period-surface")
    val wwavesPeriodSurface: List<Double?>?
)


data class UnitsResponse(
    @SerializedName("swell1_direction-surface")
    val swell1DirectionSurface: String?,
    @SerializedName("swell1_height-surface")
    val swell1HeightSurface: String?,
    @SerializedName("swell1_period-surface")
    val swell1PeriodSurface: String?,
    @SerializedName("swell2_direction-surface")
    val swell2DirectionSurface: String?,
    @SerializedName("swell2_height-surface")
    val swell2HeightSurface: String?,
    @SerializedName("swell2_period-surface")
    val swell2PeriodSurface: String?,
    @SerializedName("waves_direction-surface")
    val wavesDirectionSurface: String?,
    @SerializedName("waves_height-surface")
    val wavesHeightSurface: String?,
    @SerializedName("waves_period-surface")
    val wavesPeriodSurface: String?,
    @SerializedName("wwaves_direction-surface")
    val wwavesDirectionSurface: String?,
    @SerializedName("wwaves_height-surface")
    val wwavesHeightSurface: String?,
    @SerializedName("wwaves_period-surface")
    val wwavesPeriodSurface: String?
)
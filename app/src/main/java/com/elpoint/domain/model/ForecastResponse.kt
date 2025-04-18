package com.elpoint.domain.model


import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("hours")
    val hours: List<HoursDto>,
    @SerializedName("meta")
    val meta: MetaDto
)

data class HoursDto(
    @SerializedName("time")
    val time: String,
    @SerializedName("gust")
    val gust: SourceValueDto?,
    @SerializedName("waterTemperature")
    val waterTemperature: SourceValueDto?,
    @SerializedName("waveDirection")
    val waveDirection: SourceValueDto?,
    @SerializedName("waveHeight")
    val waveHeight: SourceValueDto?,
    @SerializedName("wavePeriod")
    val wavePeriod: SourceValueDto?,
    @SerializedName("windSpeed")
    val windSpeed: SourceValueDto?,
    @SerializedName("windDirection")
    val windDirection: SourceValueDto?
)
data class SourceValueDto(
    @SerializedName("sg")
    val sg: Double?
)

data class MetaDto(
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("dailyQuota")
    val dailyQuota: Int,
    @SerializedName("end")
    val end: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("params")
    val params: List<String>,
    @SerializedName("requestCount")
    val requestCount: Int,
    @SerializedName("source")
    val source: List<String>,
    @SerializedName("start")
    val start: String
)
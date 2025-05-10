package com.elpoint.presentation.state

internal data class UserPointsUiModel(
    val points: List<PointUiModel>, )

internal data class PointUiModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val alarm: Boolean,
    val currentForecast: HourlyForecastUI? = null,
)

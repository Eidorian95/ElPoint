package com.elpoint.presentation.state

data class UserPointsUiModel(
    val points: List<PointUiModel>,

    )

data class PointUiModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val alarm: Boolean,
    val currentForecast: HourlyForecastUI? = null,
)

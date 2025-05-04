package com.elpoint.domain.model

internal data class Point (
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val alarm: Boolean = false
)
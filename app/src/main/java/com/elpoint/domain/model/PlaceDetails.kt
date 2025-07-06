package com.elpoint.domain.model

import androidx.compose.runtime.Immutable
import com.google.android.gms.maps.model.LatLng

@Immutable
data class PlaceDetails(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

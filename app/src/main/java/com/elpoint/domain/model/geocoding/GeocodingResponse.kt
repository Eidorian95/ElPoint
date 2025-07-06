package com.elpoint.domain.model.geocoding

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(val results: List<GeocodingResult>)
data class GeocodingResult(
    @SerializedName("place_id") val placeId: String,
    @SerializedName("formatted_address") val formattedAddress: String,
    val geometry: Geometry
)
data class Geometry(val location: Location)
data class Location(val lat: Double, val lng: Double)

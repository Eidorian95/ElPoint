package com.elpoint.domain.repository

import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.model.PlaceSuggestion

interface PlacesRepository {
    suspend fun searchPlaces(query: String): Result<List<PlaceSuggestion>>
    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails>
    suspend fun getAddressFromCoordinates(lat: Double, lng: Double): Result<String>
}
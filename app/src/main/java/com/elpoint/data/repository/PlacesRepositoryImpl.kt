package com.elpoint.data.repository

import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.domain.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlacesRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): Result<List<PlaceSuggestion>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }

        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        return suspendCoroutine { continuation ->
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val suggestions =
                        response.autocompletePredictions.map { it.toPlaceSuggestion() }
                    continuation.resume(Result.success(suggestions))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }
    }

    override suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails> {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        return suspendCoroutine { continuation ->
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                if (place.location != null && place.displayName != null) {
                    val details = PlaceDetails(
                        name = place.displayName ?: "",
                        latitude = place.location?.latitude ?: 0.0 ,
                        longitude = place.location?.longitude ?: 0.0
                    )
                    continuation.resume(Result.success(details))
                } else {
                    continuation.resume(Result.failure(Exception("Los detalles del lugar están incompletos.")))
                }
            }.addOnFailureListener { exception -> continuation.resume(Result.failure(exception)) }
        }
    }

    private fun AutocompletePrediction.toPlaceSuggestion(): PlaceSuggestion {
        return PlaceSuggestion(
            placeId = this.placeId,
            primaryText = this.getPrimaryText(null).toString(),
            secondaryText = this.getSecondaryText(null).toString()
        )
    }
}
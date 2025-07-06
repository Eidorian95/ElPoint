package com.elpoint.domain.usecases.geocoding

import com.elpoint.domain.repository.PlacesRepository
import javax.inject.Inject

class GetPlaceFromCoordinatesUseCase @Inject constructor(private val repo: PlacesRepository) {
    suspend operator fun invoke(lat: Double, lng: Double) = repo.getPlaceFromCoordinates(lat, lng)
}

package com.elpoint.domain.usecases

import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.repository.PlacesRepository
import javax.inject.Inject

class GetPlaceDetailsUseCase @Inject constructor(private val placesRepository: PlacesRepository) {
    suspend operator fun invoke(placeId: String): Result<PlaceDetails> {
        return placesRepository.getPlaceDetails(placeId)
    }
}
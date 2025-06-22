package com.elpoint.domain.usecases

import com.elpoint.domain.repository.PlacesRepository
import javax.inject.Inject

class GetAddressFromCoordinatesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lng: Double
    ): Result<String> {
        return placesRepository.getAddressFromCoordinates(lat, lng)
    }
}
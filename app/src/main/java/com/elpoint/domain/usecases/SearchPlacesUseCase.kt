package com.elpoint.domain.usecases

import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository
) {
    operator fun invoke(query: String): Flow<Result<List<PlaceSuggestion>>> = flow {
        emit(placesRepository.searchPlaces(query))
    }
}
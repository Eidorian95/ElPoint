package com.elpoint.domain.usecases.favorites

import com.elpoint.domain.repository.UserSpotsRepository
import javax.inject.Inject

class DeleteSpotUseCase @Inject constructor(private val repo: UserSpotsRepository) {
    suspend operator fun invoke(spotId: String) = repo.deleteSpot(spotId)
}
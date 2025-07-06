package com.elpoint.domain.usecases.favorites

import com.elpoint.domain.model.FavoriteSpot
import com.elpoint.domain.repository.UserSpotsRepository
import javax.inject.Inject

class SaveSpotUseCase @Inject constructor(private val repo: UserSpotsRepository) {
    suspend operator fun invoke(spot: FavoriteSpot) = repo.saveSpot(spot)
}
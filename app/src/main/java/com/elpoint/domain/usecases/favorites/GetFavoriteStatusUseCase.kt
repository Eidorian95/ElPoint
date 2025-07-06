package com.elpoint.domain.usecases.favorites

import com.elpoint.domain.repository.UserSpotsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteStatusUseCase @Inject constructor(private val repo: UserSpotsRepository) {
    operator fun invoke(spotId: String): Flow<Boolean> = repo.isFavorite(spotId)
}
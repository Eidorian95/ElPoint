package com.elpoint.domain.repository

import com.elpoint.domain.model.FavoriteSpot
import kotlinx.coroutines.flow.Flow

interface UserSpotsRepository {
    fun isFavorite(spotId: String): Flow<Boolean>
    suspend fun saveSpot(spot: FavoriteSpot)
    suspend fun deleteSpot(spotId: String)
}
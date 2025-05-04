package com.elpoint.domain.usecases

import com.elpoint.domain.model.Point
import com.elpoint.domain.repository.UserPointsRepository
import javax.inject.Inject

internal class GetUserPointsUseCase @Inject constructor(
    private val userPointsRepository: UserPointsRepository
){
    suspend operator fun invoke(): List<Pair<String, Point>> {
        return userPointsRepository.getPoints()
    }
}
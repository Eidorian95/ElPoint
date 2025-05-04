package com.elpoint.data.repository

import com.elpoint.domain.model.Point
import com.elpoint.domain.repository.UserPointsRepository
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

internal class UserPointsRepositoryImpl @Inject constructor(
    private val pointReference: DatabaseReference
): UserPointsRepository {
    override fun getPoints(): Point {
        TODO("Not yet implemented")
    }

    override fun addPoint(point: Point): Boolean {
        TODO("Not yet implemented")
    }

    override fun deletePoint(): Point {
        TODO("Not yet implemented")
    }
}
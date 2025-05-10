package com.elpoint.domain.repository

import com.elpoint.domain.model.Point

internal interface UserPointsRepository {
    suspend fun getPoints():  List<Point>
    suspend fun addPoint(point:Point): Boolean
    suspend fun deletePoint(pointId: String): Boolean
}



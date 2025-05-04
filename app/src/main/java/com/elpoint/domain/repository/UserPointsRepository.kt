package com.elpoint.domain.repository

import com.elpoint.domain.model.Point

internal interface UserPointsRepository {
    fun getPoints(): Point
    fun addPoint(point:Point): Boolean
    fun deletePoint(): Point
}



package com.elpoint.data.repository

import android.util.Log
import com.elpoint.domain.model.Point
import com.elpoint.domain.repository.UserPointsRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserPointsRepositoryImpl @Inject constructor(
    private val pointReference: DatabaseReference
): UserPointsRepository {
    override suspend fun getPoints(): List <Point> = suspendCoroutine { continuation ->
        pointReference
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val points = snapshot.children.mapNotNull { child ->
                        child.getValue(Point::class.java)
                    }
                    Log.d("USER_POINTS", "SUCCESS")
                    continuation.resume(points)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("USER_POINTS", "ERROR")
                    continuation.resumeWithException(error.toException())
                }
            })
    }

    override suspend fun addPoint(point: Point): Boolean = suspendCoroutine { continuation ->
        val id = point.id.ifEmpty { UUID.randomUUID().toString() }
        val pointWithId = point.copy(id = id)

        pointReference
            .child(id)
            .setValue(pointWithId)
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener { continuation.resume(false) }
    }
    override suspend fun deletePoint(pointId: String): Boolean = suspendCoroutine { continuation ->
        pointReference
            .child(pointId)
            .removeValue()
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener { continuation.resume(false) }
    }
}
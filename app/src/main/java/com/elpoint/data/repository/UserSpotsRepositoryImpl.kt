package com.elpoint.data.repository

import com.elpoint.domain.model.FavoriteSpot
import com.elpoint.domain.repository.UserSpotsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

class UserSpotsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : UserSpotsRepository {

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    override fun isFavorite(spotId: String): Flow<Boolean> = callbackFlow {
        val userId = currentUserId
        if (userId == null) {
            trySend(false)
            close()
            return@callbackFlow
        }

        val ref = database.getReference("users").child(userId).child("favorites").child(spotId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun saveSpot(spot: FavoriteSpot) {
        currentUserId?.let { userId ->
            database.getReference("users").child(userId).child("favorites").child(spot.id).setValue(spot)
        }
    }

    override suspend fun deleteSpot(spotId: String) {
        currentUserId?.let { userId ->
            database.getReference("users").child(userId).child("favorites").child(spotId).removeValue()
        }
    }
}
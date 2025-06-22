package com.elpoint.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.elpoint.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

internal class PermissionDeniedException(message: String) : Exception(message)

internal class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationRepository {
    override suspend fun getCurrentLocation(): Result<Location> {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return Result.failure(PermissionDeniedException("Location permission not granted."))
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            continuation.resume(Result.success(location))
                        } else {
                            // TODO: Implementar una solicitud de ubicación fresca si lastLocation es nula
                            continuation.resume(Result.failure(Exception("Could not retrieve last location.")))
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(Result.failure(e))
                    }
            } catch (e: SecurityException) {
                continuation.resume(Result.failure(e))
            }
        }
    }
}
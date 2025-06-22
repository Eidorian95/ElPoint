package com.elpoint.domain.repository

import android.location.Location

internal interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
}
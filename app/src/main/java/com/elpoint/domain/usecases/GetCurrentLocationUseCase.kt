package com.elpoint.domain.usecases

import android.location.Location
import com.elpoint.domain.repository.LocationRepository
import javax.inject.Inject

internal class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<Location> {
        return locationRepository.getCurrentLocation()
    }
}

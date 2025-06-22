package com.elpoint.di

import android.content.Context
import android.location.Geocoder
import com.elpoint.data.repository.ForecastRepositoryImpl
import com.elpoint.data.repository.PlacesRepositoryImpl
import com.elpoint.data.repository.UserPointsRepositoryImpl
import com.elpoint.domain.repository.ForecastRepository
import com.elpoint.domain.repository.PlacesRepository
import com.elpoint.domain.repository.UserPointsRepository
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(
        impl: ForecastRepositoryImpl
    ): ForecastRepository

    @Binds
    @Singleton
    abstract fun bindUserPointsRepository(
        impl: UserPointsRepositoryImpl
    ): UserPointsRepository

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlacesRepositoryImpl
    ): PlacesRepository
}
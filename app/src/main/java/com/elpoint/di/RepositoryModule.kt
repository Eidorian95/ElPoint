package com.elpoint.di

import com.elpoint.data.repository.ForecastRepositoryImpl
import com.elpoint.data.repository.PlacesRepositoryImpl
import com.elpoint.data.repository.UserPointsRepositoryImpl
import com.elpoint.domain.repository.ForecastRepository
import com.elpoint.domain.repository.PlacesRepository
import com.elpoint.domain.repository.UserPointsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
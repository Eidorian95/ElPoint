package com.elpoint.di

import com.elpoint.data.repository.ForecastRepositoryImpl
import com.elpoint.domain.repository.ForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(
        impl: ForecastRepositoryImpl
    ): ForecastRepository
}
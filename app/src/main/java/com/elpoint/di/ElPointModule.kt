package com.elpoint.di

import com.elpoint.domain.repository.ForecastRepository
import com.elpoint.domain.repository.UserPointsRepository
import com.elpoint.domain.usecases.GetForecastUseCase
import com.elpoint.domain.usecases.GetUserPointsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal object ElPointModule {

    @Provides
    fun provideGetForecastUseCase(repository: ForecastRepository): GetForecastUseCase {
        return GetForecastUseCase(repository)
    }

    @Provides
    fun provideGetUserPoints(repository: UserPointsRepository): GetUserPointsUseCase {
        return GetUserPointsUseCase(repository)
    }
}
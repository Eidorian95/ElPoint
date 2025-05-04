package com.elpoint.di

import com.elpoint.data.remote.ApiService
import com.elpoint.data.remote.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitClient.create()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesFirebaseDB(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    @Singleton
    fun providesFirebasePointReference(dataBase:FirebaseDatabase): DatabaseReference {
        return dataBase.getReference("points")
    }
}
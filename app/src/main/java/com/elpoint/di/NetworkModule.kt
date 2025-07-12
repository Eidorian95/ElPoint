package com.elpoint.di

import android.content.Context
import android.location.Geocoder
import com.elpoint.data.remote.ApiService
import com.elpoint.data.remote.GooglePlacesApiService
import com.elpoint.data.repository.LocationRepositoryImpl
import com.elpoint.data.repository.UserSpotsRepositoryImpl
import com.elpoint.domain.repository.LocationRepository
import com.elpoint.domain.repository.UserSpotsRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    // --- Clientes OkHttp ---
    @Provides
    @Singleton
    @Named("google_client")
    fun provideGoogleOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @Named("stormglass_client") // Le damos otro nombre
    fun provideStormglassOkHttpClient(): OkHttpClient {
        val stormglassApiKey = "e754e418-1c63-11f0-88e2-0242ac130003-e754e4d6-1c63-11f0-88e2-0242ac130003"

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", stormglassApiKey)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    // --- Instancias de Retrofit ---

    @Provides
    @Singleton
    @Named("google_retrofit")
    fun provideGoogleRetrofit(@Named("google_client") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient) // Usa el cliente de Google
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("stormglass_retrofit")
    fun provideStormglassRetrofit(@Named("stormglass_client") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.stormglass.io/v2/")
            .client(okHttpClient) // Usa el cliente de Stormglass
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // --- Servicios de API ---

    @Provides
    @Singleton
    fun provideGooglePlacesApiService(@Named("google_retrofit") retrofit: Retrofit): GooglePlacesApiService {
        return retrofit.create(GooglePlacesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStormglassApiService(@Named("stormglass_retrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    // --- Servicios de Firebase ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
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


    // --- Servicios de Places ---
    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        return Places.createClient(context)
    }

    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(fusedLocationProviderClient: FusedLocationProviderClient, @ApplicationContext context: Context): LocationRepository {
        return LocationRepositoryImpl(fusedLocationProviderClient, context)
    }
    @Provides
    @Singleton
    fun provideUserSpotsRepository(auth: FirebaseAuth, db: FirebaseDatabase): UserSpotsRepository {
        return UserSpotsRepositoryImpl(auth, db)
    }
}
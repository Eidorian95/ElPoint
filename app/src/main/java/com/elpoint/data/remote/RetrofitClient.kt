package com.elpoint.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://api.stormglass.io/v2/"
    private const val API_KEY =
        "e754e418-1c63-11f0-88e2-0242ac130003-e754e4d6-1c63-11f0-88e2-0242ac130003"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    fun create(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
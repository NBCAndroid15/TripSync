package com.example.tripsync.data

import com.example.tripsync.util.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : TravelApiService by lazy {
        retrofit.create(TravelApiService::class.java)
    }
}
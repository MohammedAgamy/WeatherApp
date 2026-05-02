package com.agamy.weatherapp.data.remote

import com.agamy.weatherapp.data.model.Current
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
    ): Current

}
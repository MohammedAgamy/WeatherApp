package com.agamy.weatherapp.data.remote

import com.agamy.weatherapp.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
    ): WeatherModel


    @GET("forecast.json")
    suspend fun getHourWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: String,
    ): WeatherModel

 /*   @GET("timezone.json")
    suspend fun SearchLocation(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: String,
    ): WeatherModel
*/
}
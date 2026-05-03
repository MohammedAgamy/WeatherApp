package com.agamy.weatherapp.domain.repository

import com.agamy.weatherapp.data.model.WeatherModel

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherModel>
}
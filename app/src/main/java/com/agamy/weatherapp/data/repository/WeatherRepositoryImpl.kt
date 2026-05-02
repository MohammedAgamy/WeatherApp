package com.agamy.weatherapp.data.repository

import com.agamy.weatherapp.data.model.Current
import com.agamy.weatherapp.data.remote.ApiService
import com.agamy.weatherapp.domain.repository.WeatherRepository


class WeatherRepositoryImpl(
    private val apiService: ApiService
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<Current> {
        return try {
            val location = "$lat,$lon"  // ✅ WeatherAPI format
            Result.success(
                apiService.getCurrentWeather(
                    apiKey = "1c8a282d913a48cc851101825242004",
                    location = location
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

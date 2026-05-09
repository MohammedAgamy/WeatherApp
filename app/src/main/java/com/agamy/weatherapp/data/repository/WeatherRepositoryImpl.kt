package com.agamy.weatherapp.data.repository

import com.agamy.weatherapp.data.model.Forecastday
import com.agamy.weatherapp.data.model.Hour
import com.agamy.weatherapp.data.model.WeatherModel
import com.agamy.weatherapp.data.remote.ApiService
import com.agamy.weatherapp.domain.repository.WeatherRepository


class WeatherRepositoryImpl(
    private val apiService: ApiService
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<WeatherModel> {
        return try {
            val location = "$lat,$lon"
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

    override suspend fun getHourWeather(lat: Double, lon: Double): Result<List<Hour>> {
        return try {
            val location = "$lat,$lon"
            val response = apiService.getHourWeather(
                apiKey = "1c8a282d913a48cc851101825242004",
                location = location,
                days = "1"
            )
            // ✅ استخرج الـ hours من الـ response
            val hours = response.forecast.forecastday.firstOrNull()?.hour ?: emptyList()
            Result.success(hours)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeeklyForecast(
        lat: Double,
        lon: Double
    ): Result<List<Forecastday>> {
        return try {
            val response = apiService.getHourWeather(
                apiKey = "1c8a282d913a48cc851101825242004",
                location = "$lat,$lon",
                days = "7"    //  7 أيام
            )
            Result.success(response.forecast.forecastday)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchWeather(city: String): Result<WeatherModel> {
        return try {
            val response = apiService.getHourWeather(
                apiKey = "1c8a282d913a48cc851101825242004",
                location = city,
                days = "1"
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

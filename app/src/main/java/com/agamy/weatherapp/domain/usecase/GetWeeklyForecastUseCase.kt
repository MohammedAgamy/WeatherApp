package com.agamy.weatherapp.domain.usecase

import com.agamy.weatherapp.data.model.Forecastday
import com.agamy.weatherapp.domain.repository.WeatherRepository

class GetWeeklyForecastUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<List<Forecastday>> {
        return repository.getWeeklyForecast(lat, lon)
    }
}
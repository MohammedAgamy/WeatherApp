package com.agamy.weatherapp.domain.usecase

import com.agamy.weatherapp.data.model.WeatherModel
import com.agamy.weatherapp.domain.repository.WeatherRepository

class SearchWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): Result<WeatherModel> {
        if (city.isBlank()) return Result.failure(Exception("City name is empty"))
        return repository.searchWeather(city)
    }
}
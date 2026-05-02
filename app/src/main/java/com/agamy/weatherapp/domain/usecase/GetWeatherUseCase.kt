package com.agamy.weatherapp.domain.usecase

import com.agamy.weatherapp.data.model.Current
import com.agamy.weatherapp.domain.repository.WeatherRepository


class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Current> {
        return repository.getWeather(lat, lon)
    }
}
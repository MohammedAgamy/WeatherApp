package com.agamy.weatherapp.domain.usecase

import com.agamy.weatherapp.data.model.Hour
import com.agamy.weatherapp.data.model.WeatherModel
import com.agamy.weatherapp.domain.repository.WeatherRepository

class GetHourUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<List<Hour>> {
        return repository.getHourWeather(lat, lon)
    }
}
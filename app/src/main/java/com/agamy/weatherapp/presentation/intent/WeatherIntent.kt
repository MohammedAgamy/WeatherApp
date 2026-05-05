package com.agamy.weatherapp.presentation.intent

sealed class WeatherIntent {
    object LoadWeather : WeatherIntent()
    object RefreshWeather  : WeatherIntent()

    data class LoadWeatherWithLocation(
        val lat: Double,
        val lon: Double
    ) : WeatherIntent()
}
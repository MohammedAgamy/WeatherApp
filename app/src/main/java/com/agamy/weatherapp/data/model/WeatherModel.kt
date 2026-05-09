package com.agamy.weatherapp.data.model

data class WeatherModel(
    val forecast: Forecast,
    val current: CurrentX,
    val location: Location
)
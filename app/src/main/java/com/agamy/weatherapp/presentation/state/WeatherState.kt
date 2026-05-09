package com.agamy.weatherapp.presentation.state

import com.agamy.weatherapp.data.model.Hour
import com.agamy.weatherapp.data.model.WeatherModel

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(
        val current: WeatherModel,
        val hourlyForecast: List<Hour> = emptyList() // ✅ الاتنين مع بعض


    ) : WeatherState()

    data class Error(val massage: String) : WeatherState()
}
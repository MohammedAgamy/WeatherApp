package com.agamy.weatherapp.presentation.state

import com.agamy.weatherapp.data.model.WeatherModel

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success (val  current: WeatherModel) : WeatherState()
    data class Error (val  massage: String) : WeatherState()
}
package com.agamy.weatherapp.presentation.screen.search

import com.agamy.weatherapp.data.model.WeatherModel

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val results: List<WeatherModel>) : SearchState()
    data class Error(val message: String) : SearchState()
}
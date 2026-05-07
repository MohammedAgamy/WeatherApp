package com.agamy.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.agamy.weatherapp.domain.usecase.GetWeatherUseCase
import com.agamy.weatherapp.presentation.intent.WeatherIntent
import com.agamy.weatherapp.presentation.state.WeatherState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state

    private val intentChannel = Channel<WeatherIntent>(Channel.UNLIMITED)

    init {
        handelIntent()
    }


    fun sendIntent(intent: WeatherIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handelIntent() {

        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    WeatherIntent.LoadWeather -> fetchWeather()
                    WeatherIntent.RefreshWeather -> fetchWeather()
                    is WeatherIntent.LoadWeatherWithLocation  -> {
                        lat = intent.lat
                        lon = intent.lon
                        fetchWeather()
                    }
                }
            }
        }

    }


    private suspend fun fetchWeather() {
        _state.value = WeatherState.Loading

        getWeatherUseCase(lat, lon).fold(
            onSuccess = { weather ->
                _state.value = WeatherState.Success(weather)

            },
            onFailure = { error ->
                _state.value = WeatherState.Error(error.message ?: "Something went wrong")

            }
        )

    }
}

class WeatherViewModelFactory(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(getWeatherUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

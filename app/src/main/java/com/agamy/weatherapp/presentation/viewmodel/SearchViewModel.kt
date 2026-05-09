package com.agamy.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.agamy.weatherapp.domain.usecase.SearchWeatherUseCase
import com.agamy.weatherapp.presentation.screen.search.SearchIntent
import com.agamy.weatherapp.presentation.screen.search.SearchState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchWeatherUseCase: SearchWeatherUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    val state: StateFlow<SearchState> = _state

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query


    private val intentChannel = Channel<SearchIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
        viewModelScope.launch {
            _query
                .debounce(500)
                .filter { it.length >= 2 }   // ابدأ بعد حرفين على الأقل
                .distinctUntilChanged()
                .collect { query ->
                    fetchSearch(query)
                }
        }
    }


    fun sendIntent(intent: SearchIntent) {
        viewModelScope.launch { intentChannel.send(intent) }
    }
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            _state.value = SearchState.Idle
        }
    }
    private fun handleIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is SearchIntent.Search -> fetchSearch(intent.query)
                    is SearchIntent.Clear -> {
                        _query.value = ""
                        _state.value = SearchState.Idle
                    }
                }
            }
        }
    }

    private suspend fun fetchSearch(city: String) {
        _state.value = SearchState.Loading
        searchWeatherUseCase(city).fold(
            onSuccess = { weather ->
                _state.value = SearchState.Success(listOf(weather))
            },
            onFailure = { error ->
                _state.value = SearchState.Error(
                    error.message ?: "City not found"
                )
            }
        )
    }
}



class SearchViewModelFactory(
    private val searchWeatherUseCase: SearchWeatherUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(searchWeatherUseCase) as T
    }
}
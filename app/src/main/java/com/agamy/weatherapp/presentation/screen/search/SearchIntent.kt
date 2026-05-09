package com.agamy.weatherapp.presentation.screen.search

sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
    object Clear : SearchIntent()
}
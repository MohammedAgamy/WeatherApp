package com.agamy.weatherapp

sealed class Routes(val routes: String) {
    object SPLASH : Routes ("Splash")
    object HOME : Routes ("Home")
}
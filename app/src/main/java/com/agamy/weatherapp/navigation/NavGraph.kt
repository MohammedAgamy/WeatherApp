package com.agamy.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agamy.weatherapp.Routes
import com.agamy.weatherapp.presentation.home.HomeScreen
import com.agamy.weatherapp.presentation.splash.SplashScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController ,
        startDestination = Routes.SPLASH.routes
    ){
        composable(Routes.SPLASH.routes){
            SplashScreen(navController)
        }

        composable(Routes.HOME.routes){
            HomeScreen()
        }
    }
}


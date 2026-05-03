package com.agamy.weatherapp.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.agamy.weatherapp.R
import com.agamy.weatherapp.Routes
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {


    //lottie animation
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.weather_night_rain)
    )


    LaunchedEffect(Unit) {
        delay(3000L)

        navController.navigate(Routes.HOME.routes)
        {
            popUpTo(Routes.SPLASH.routes) {
                inclusive = true
            }
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Image(
            painter = painterResource(R.drawable.weather_back),
            contentDescription = "BackGround App",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        // display the lottie animation
        LottieAnimation(
            alignment = Alignment.Center,
            modifier = Modifier
                .size(300.dp)
                .padding(top = 60.dp),
            composition = composition
        )
    }
}
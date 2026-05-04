package com.agamy.weatherapp.presentation.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agamy.weatherapp.R
import com.agamy.weatherapp.data.RetrofitClient
import com.agamy.weatherapp.data.model.WeatherModel
import com.agamy.weatherapp.data.repository.WeatherRepositoryImpl
import com.agamy.weatherapp.domain.usecase.GetWeatherUseCase
import com.agamy.weatherapp.presentation.intent.WeatherIntent
import com.agamy.weatherapp.presentation.state.WeatherState
import com.agamy.weatherapp.presentation.viewmodel.WeatherViewModel
import com.agamy.weatherapp.presentation.viewmodel.WeatherViewModelFactory

// ── Colors ──
private val SheetBg = Color(0xFF1A1040)
private val PurpleLight = Color(0xC87B5EA7)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen() {


    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(
            GetWeatherUseCase(
                WeatherRepositoryImpl(RetrofitClient.apiService)
            )
        )
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(WeatherIntent.LoadWeather)
    }

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContainerColor = SheetBg,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetTonalElevation = 0.dp,
        sheetShadowElevation = 0.dp,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(PurpleLight)
                )
            }
        },
        sheetPeekHeight = 340.dp,
        sheetContent = {
            WeatherBottomSheetContent()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background
            Image(
                painter = painterResource(R.drawable.weather_back),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // House
            Image(
                painter = painterResource(R.drawable.weather_house),
                contentDescription = null,
                modifier = Modifier
                    .size(700.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 200.dp),
                contentScale = ContentScale.Crop
            )

            when (state) {
                is WeatherState.Error -> ErrorContent((state as WeatherState.Error).massage)
                WeatherState.Idle -> IdleContent()
                WeatherState.Loading -> LoadingContent()
                is WeatherState.Success -> WeatherContent((state as WeatherState.Success).current)
            }

        }
    }
}


// ── Models ──
data class HourWeather(
    val time: String,
    val temp: String,
    val type: WeatherType,
    val isNow: Boolean = false
)

data class DayWeather(
    val day: String,
    val high: String,
    val low: String,
    val type: WeatherType
)

enum class WeatherType(val emoji: String) {
    SUNNY("☀️"),
    CLOUDY("⛅"),
    RAINY("🌧️"),
    SNOWY("❄️")
}
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
private val PurpleDark = Color(0xFF4A3880)
private val PurpleMuted = Color(0xFF8A7BA8)
private val PurpleText = Color(0xFFB39DDB)
private val CardDark = Color(0xFF2A1F50)

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


@Composable
fun ErrorContent(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red, textAlign = TextAlign.Center)
    }
}
@Composable
fun IdleContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Loading weather...", color = Color.Gray)
    }
}

@Composable
fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun WeatherContent(weatherModel: WeatherModel) {
    // Top Info
    Column(
        modifier = Modifier
            // .align(Alignment.TopCenter)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = weatherModel.location.country,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${weatherModel.current.temp_c}°",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = weatherModel.current.condition.text,
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                "H: ${weatherModel.current.gust_kph}°",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
            Text(
                "L: ${weatherModel.current.gust_mph}°",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun WeatherBottomSheetContent() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Hourly Forecast", "Weekly Forecast")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        // ── Tabs ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedTab = index }
                ) {
                    Text(
                        text = title,
                        color = if (selectedTab == index) Color.White else PurpleMuted,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == index)
                            FontWeight.SemiBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (selectedTab == index) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(PurpleLight, PurpleText)
                                    ),
                                    shape = CircleShape
                                )
                        )
                    } else {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Content ──
        when (selectedTab) {
            0 -> HourlyForecastRow()
            1 -> WeeklyForecastRow()
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Bottom Actions ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Share
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(CardDark, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = PurpleMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            // + FAB
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PurpleLight, PurpleDark)
                        ),
                        shape = CircleShape
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            // List
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(CardDark, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List",
                    tint = PurpleMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Hourly Row ──
@Composable
fun HourlyForecastRow() {
    val hours = listOf(
        HourWeather("12 AM", "19°", WeatherType.CLOUDY),
        HourWeather("Now", "19°", WeatherType.RAINY, isNow = true),
        HourWeather("2 AM", "18°", WeatherType.CLOUDY),
        HourWeather("3 AM", "19°", WeatherType.SUNNY),
        HourWeather("4 AM", "19°", WeatherType.CLOUDY),
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(hours) { HourlyItem(it) }
    }
}

@Composable
fun HourlyItem(data: HourWeather) {
    val bg = if (data.isNow)
        Brush.linearGradient(listOf(PurpleLight, PurpleDark))
    else
        Brush.linearGradient(listOf(CardDark, CardDark))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .background(brush = bg, shape = RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp)
    ) {
        Text(text = data.time, color = PurpleText, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = data.type.emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = data.temp,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Weekly Row ──
@Composable
fun WeeklyForecastRow() {
    val days = listOf(
        DayWeather("Mon", "24°", "17°", WeatherType.SUNNY),
        DayWeather("Tue", "22°", "16°", WeatherType.CLOUDY),
        DayWeather("Wed", "19°", "14°", WeatherType.RAINY),
        DayWeather("Thu", "21°", "15°", WeatherType.CLOUDY),
        DayWeather("Fri", "26°", "18°", WeatherType.SUNNY),
        DayWeather("Sat", "23°", "16°", WeatherType.CLOUDY),
        DayWeather("Sun", "20°", "15°", WeatherType.SNOWY),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { day ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardDark, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day.day,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(40.dp)
                )
                Text(text = day.type.emoji, fontSize = 20.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = day.high,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = day.low,
                        color = PurpleMuted,
                        fontSize = 14.sp
                    )
                }
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
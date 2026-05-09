package com.agamy.weatherapp.presentation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.agamy.weatherapp.Routes
import com.agamy.weatherapp.data.model.Forecast
import com.agamy.weatherapp.data.model.Forecastday
import com.agamy.weatherapp.data.model.Hour
import com.agamy.weatherapp.data.model.WeatherModel
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours


// ── Colors ──
private val SheetBg = Color(0xFF1A1040)
private val PurpleLight = Color(0xC87B5EA7)
private val PurpleDark = Color(0xFF4A3880)
private val PurpleMuted = Color(0xFF8A7BA8)
private val PurpleText = Color(0xFFB39DDB)
private val CardDark = Color(0xFF2A1F50)

@Composable
fun WeatherBottomSheetContent(
    hourlyForecast: List<Hour> = emptyList(),
    weeklyForecast: List<Forecastday> = emptyList(),
    navController: NavController
) {
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
            0 -> HourlyForecastRow(hourlyForecast)
            1 -> WeeklyForecastRow(weeklyForecast)
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
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            onClick = {
                                navController.navigate(Routes.SEARCH.routes)
                            }
                        )
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
                    modifier = Modifier
                        .size(20.dp)

                )
            }
        }
    }
}


// ── Hourly Row ──
@Composable
fun HourlyForecastRow(hour: List<Hour>) {

    if (hour.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF7B5EA7), modifier = Modifier.size(32.dp))
        }
        return
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(hour) { index, item ->       // ✅ itemsIndexed
            HourlyItem(data = item, isNow = index == 0)
        }
    }
}

@Composable
fun HourlyItem(data: Hour, isNow: Boolean = false) {
    val bg = if (isNow)
        Brush.linearGradient(listOf(PurpleLight, PurpleDark))
    else
        Brush.linearGradient(listOf(CardDark, CardDark))
    val iconUrl = "https:${data.condition.icon}"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .background(brush = bg, shape = RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp)
    ) {

        val timeDisplay = if (isNow) "Now" else data.time.split(" ").lastOrNull() ?: data.time
        Text(text = timeDisplay, color = PurpleText, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(10.dp))
        AsyncImage(
            model = iconUrl,
            contentDescription = data.condition.text,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${data.temp_c.toInt()}°",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Weekly Row ──
@Composable
fun WeeklyForecastRow(days: List<Forecastday>) {
    if (days.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF7B5EA7), modifier = Modifier.size(32.dp))
        }
        return
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { forecastday ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardDark, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // اليوم - استخرجه من الـ date "2024-01-15"
                Text(
                    text = getDayName(forecastday.date),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(44.dp)
                )

                // الأيقونة
                AsyncImage(
                    model = "https:${forecastday.day.condition.icon}",
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )

                // درجات الحرارة
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${forecastday.day.maxtemp_c.toInt()}°",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${forecastday.day.mintemp_c.toInt()}°",
                        color = PurpleMuted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


// format handel
fun getDayName(dateStr: String): String {
    return try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH)
        val date = sdf.parse(dateStr)
        val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.ENGLISH)
        dayFormat.format(date!!)
    } catch (e: Exception) {
        dateStr
    }
}
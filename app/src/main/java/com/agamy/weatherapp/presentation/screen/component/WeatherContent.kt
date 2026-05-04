package com.agamy.weatherapp.presentation.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agamy.weatherapp.data.model.WeatherModel

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

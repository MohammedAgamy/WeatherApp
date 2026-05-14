# 🌤️ WeatherApp — Android Kotlin Jetpack Compose

A modern Android weather application built with **Kotlin** and **Jetpack Compose**, following **Clean Architecture** and **MVI pattern**.

---

## 📸 Screenshots

> Add your screenshots here

---

## 🚀 Features

- 📍 **Current Location Weather** — Auto-detect location via GPS
- 🔍 **City Search** — Search any city worldwide with debounce
- ⏱️ **Hourly Forecast** — Hour-by-hour weather for today
- 📅 **7-Day Forecast** — Weekly weather overview
- 🌙 **Splash Screen** — Lottie animation on launch
- 🗺️ **Navigation** — Multi-screen with Jetpack Navigation
- 🎨 **Custom Bottom Sheet** — Always-visible forecast sheet

---

## 🏗️ Architecture

This project follows **Clean Architecture** with **MVI (Model-View-Intent)** pattern.

```
📦 weatherapp
 ┣ 📂 data
 ┃ ┣ 📂 location         # GPS LocationProvider
 ┃ ┣ 📂 model            # Data classes (WeatherModel, Hour, Forecastday...)
 ┃ ┣ 📂 remote           # ApiService (Retrofit)
 ┃ ┣ 📂 repository       # WeatherRepositoryImpl
 ┃ ┗ 📄 RetrofitClient   # Retrofit singleton
 ┣ 📂 domain
 ┃ ┣ 📂 repository       # WeatherRepository interface
 ┃ ┗ 📂 usecase          # GetWeatherUseCase, GetHourUseCase,
 ┃                       # GetWeeklyForecastUseCase, SearchWeatherUseCase
 ┣ 📂 navigation
 ┃ ┗ 📄 NavGraph         # Navigation routes
 ┣ 📂 presentation
 ┃ ┣ 📂 intent           # WeatherIntent, SearchIntent
 ┃ ┣ 📂 state            # WeatherState, SearchState
 ┃ ┣ 📂 viewmodel        # WeatherViewModel, SearchViewModel
 ┃ ┗ 📂 screen
 ┃   ┣ 📂 splash         # SplashScreen
 ┃   ┣ 📂 home           # HomeScreen
 ┃   ┣ 📂 search         # SearchScreen, WeatherCityCard
 ┃   ┗ 📂 component      # WeatherScreen, WeatherContent,
 ┃                       # WeatherBottomSheetContent, HourlyItem...
 ┗ 📄 Routes             # App routes sealed class
```

---

## 🔄 MVI Flow

```
User Action (Intent)
      ↓
  ViewModel
      ↓
  UseCase
      ↓
  Repository
      ↓
  API / GPS
      ↓
  State Update
      ↓
    UI
```

---

## 🛠️ Tech Stack

| Library | Purpose |
|---|---|
| **Kotlin** | Primary language |
| **Jetpack Compose** | UI framework |
| **Jetpack Navigation** | Screen navigation |
| **Retrofit** | HTTP client |
| **Gson** | JSON parsing |
| **OkHttp** | Network interceptor |
| **Coil** | Image loading (weather icons) |
| **Lottie** | Splash screen animation |
| **Coroutines + Flow** | Async & state management |
| **FusedLocationProvider** | GPS location |
| **BottomSheetScaffold** | Always-visible forecast sheet |

---

## 🌐 API

This app uses [WeatherAPI.com](https://www.weatherapi.com/)

| Endpoint | Usage |
|---|---|
| `/current.json` | Current weather by lat/lon |
| `/forecast.json?days=1` | Hourly forecast |
| `/forecast.json?days=7` | 7-day weekly forecast |
| `/forecast.json?q={city}` | Search city weather |

### Get your free API key
1. Sign up at [weatherapi.com](https://www.weatherapi.com/signup.aspx)
2. Copy your API key from the dashboard
3. Replace `YOUR_API_KEY` in `WeatherRepositoryImpl.kt`

```kotlin
private const val API_KEY = "YOUR_API_KEY_HERE"
```

---

## ⚙️ Setup

### 1. Clone the repo
```bash
git clone https://github.com/MohammedAgamy/WeatherApp.git
cd WeatherApp
```

### 2. Add your API Key
Open `WeatherRepositoryImpl.kt` and replace:
```kotlin
apiKey = "YOUR_API_KEY_HERE"
```

### 3. Add dependencies in `build.gradle`
```kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coil
implementation("io.coil-kt:coil-compose:2.6.0")

// Location
implementation("com.google.android.gms:play-services-location:21.2.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Lottie
implementation("com.airbnb.android:lottie-compose:6.3.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### 4. Add permissions in `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### 5. Run the app
Build and run on your device or emulator via Android Studio.

---

## 📱 Screens

### 🌙 Splash Screen
- Lottie weather animation
- Auto-navigates to Home after 3 seconds

### 🏠 Home Screen
- Background weather image
- Current temperature, condition, city name
- Always-visible bottom sheet with:
  - **Hourly Forecast** tab (real API data)
  - **Weekly Forecast** tab (7-day)
  - FAB to navigate to Search

### 🔍 Search Screen
- Search any city worldwide
- Debounce 500ms (auto-search while typing)
- Shows city card with temperature, condition, local time

---

## 🗺️ Navigation

```
Splash → Home → Search
              ← Back
```

```kotlin
sealed class Routes(val routes: String) {
    object SPLASH : Routes("Splash")
    object HOME   : Routes("Home")
    object SEARCH : Routes("Search")
}
```

---

## 📂 Key Files

| File | Description |
|---|---|
| `WeatherViewModel.kt` | Handles weather + hourly + weekly fetch |
| `SearchViewModel.kt` | Handles search with debounce |
| `WeatherRepositoryImpl.kt` | All API calls implementation |
| `LocationProvider.kt` | GPS using FusedLocationProvider |
| `WeatherBottomSheetContent.kt` | Hourly + weekly forecast UI |
| `WeatherCityCard.kt` | Search result card UI |
| `NavGraph.kt` | Navigation setup |

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first.

---

## 📄 License

```
MIT License — feel free to use this project for learning or personal use.
```

---

## 👨‍💻 Author "MohammedAgamy"

Built with ❤️ using Kotlin + Jetpack Compose

> ⭐ Star this repo if you found it helpful!

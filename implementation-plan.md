MVP Implementation Plan — Weather App
Stack: Jetpack Compose · Retrofit · Room · Open-Meteo (no auth required)

Architecture Overview
UI (Compose) → ViewModel → Repository → Retrofit (remote)
→ Room (local)
Single screen with two tabs: Current Weather and History.

Task Breakdown

Task 1 — Project Setup

Add dependencies to build.gradle: Retrofit, Gson converter, Room, ViewModel Compose, Coroutines
Add INTERNET permission to AndroidManifest.xml


Task 2 — Data Layer: Remote

Create WeatherResponse data class matching Open-Meteo's JSON (temperature_2m, windspeed_10m, weathercode)
Create WeatherApiService interface with a single @GET for current weather

Endpoint: https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true


Create RetrofitInstance singleton


Task 3 — Data Layer: Local (Room)

Create WeatherLog entity: id, temperature, windspeed, weathercode, fetchedAt (timestamp)
Create WeatherLogDao with insert and getAll (ordered by most recent)
Create WeatherDatabase


Task 4 — Repository

Create WeatherRepository that:

Calls Retrofit to fetch current weather
Inserts the result into Room as a new WeatherLog
Exposes getAllLogs() as a Flow from Room




Task 5 — ViewModel

Create WeatherViewModel with:

uiState: loading / success / error
fetchWeather() — calls repository, updates state
logs: StateFlow collected from getAllLogs()


Hardcode São Carlos coordinates for MVP (lat=-22.01, lon=-47.89)


Task 6 — UI

MainActivity sets up the Compose scaffold
WeatherScreen with two tabs:

Current: button "Fetch Weather", shows temperature + windspeed + condition. Triggers fetchWeather()
History: LazyColumn listing all WeatherLog entries with timestamp




Task 7 — Wire Everything

Provide WeatherDatabase and WeatherRepository manually (no Hilt for simplicity)
Pass repository into WeatherViewModel via a ViewModelProvider.Factory


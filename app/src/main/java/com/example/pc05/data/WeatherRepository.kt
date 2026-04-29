package com.example.pc05.data

import com.example.pc05.data.local.WeatherLog
import com.example.pc05.data.local.WeatherLogDao
import com.example.pc05.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val apiService: WeatherApiService,
    private val weatherLogDao: WeatherLogDao
) {
    suspend fun fetchAndSaveWeather(lat: Double, lon: Double) {
        val response = apiService.getCurrentWeather(lat, lon)
        response.currentWeather?.let {
            val log = WeatherLog(
                temperature = it.temperature,
                windspeed = it.windspeed,
                weathercode = it.weathercode,
                fetchedAt = System.currentTimeMillis()
            )
            weatherLogDao.insertLog(log)
        }
    }

    fun getAllLogs(): Flow<List<WeatherLog>> = weatherLogDao.getAllLogs()
}

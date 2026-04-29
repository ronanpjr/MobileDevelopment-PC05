package com.example.pc05.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_logs")
data class WeatherLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temperature: Double,
    val windspeed: Double,
    val weathercode: Int,
    val fetchedAt: Long
)

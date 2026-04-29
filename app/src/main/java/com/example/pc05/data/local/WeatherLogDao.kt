package com.example.pc05.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WeatherLog)

    @Query("SELECT * FROM weather_logs ORDER BY fetchedAt DESC")
    fun getAllLogs(): Flow<List<WeatherLog>>
}

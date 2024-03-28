package com.example.weatherforecastapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow
@Dao
interface  WeatherDao {

    @Query("SELECT * FROM currentWeather")
     fun getCurrentWeather():Flow<FiveDaysForecast>
    @Query("DELETE FROM currentWeather WHERE currentDate =:date")
    suspend fun deleteCurrentWeather(date:String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCurrentWeather(weather: FiveDaysForecast)
}
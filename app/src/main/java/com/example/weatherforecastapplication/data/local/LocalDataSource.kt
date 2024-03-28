package com.example.weatherforecastapplication.data.local

import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    // Favourites
    fun getAllFavouritesWeather(): Flow<List<Favourites>>

    suspend fun addWeatherToFavourites(weather: Favourites)
    suspend fun deleteWeatherFromFavourites(weather: Favourites)

    // alerts
    fun getAlerts(): Flow<List<AlertRoom>>

    suspend fun saveAlert(alert: AlertRoom)
    suspend fun deleteAlert(alert: AlertRoom)

    // currentWeather
    fun getCurrentWeather():Flow<FiveDaysForecast>
    suspend fun deleteCurrentWeather(date:String)

    suspend fun addCurrentWeather(weather: FiveDaysForecast)


}
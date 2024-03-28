package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.models.WeatherParam
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {


    suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): Flow<FiveDaysForecast>

    suspend fun getAlertForWeather(weatherParam: WeatherParam):Flow<AlertResult>
    suspend fun saveAlert(alert: AlertRoom)
    suspend fun deleteAlert(alert: AlertRoom)
    fun getAlerts():Flow<List<AlertRoom>>

    fun getAllFavouritesWeather(): Flow<List<Favourites>>

    suspend fun addWeatherToFavourites(weather: Favourites)
    suspend fun deleteWeatherFromFavourites(weather: Favourites)

    fun getCurrentWeatherFromRoom(): Flow<FiveDaysForecast>
    suspend fun deleteCurrentWeather(date:String)

    suspend fun addCurrentWeather(weather: FiveDaysForecast)

}
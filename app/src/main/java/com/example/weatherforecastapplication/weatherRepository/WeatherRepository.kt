package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.favouritesFeature.model.Favourites
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.WeatherParam
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

   // suspend fun getCurrentWeatherFromRoom(id:Int): CurrentWeather
    suspend fun addWeatherToFavourites(weather: Favourites)
    suspend fun deleteWeatherFromFavourites(weather: Favourites)

}
package com.example.weatherforecastapplication.favouritesFeature.model

import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllFavouritesWeather(): Flow<List<Favourites>>

    suspend fun addWeatherToFavourites(weather: Favourites)
    suspend fun deleteWeatherFromFavourites(weather: Favourites)

    fun getAlerts(): Flow<List<AlertRoom>>

    suspend fun saveAlert(alert: AlertRoom)
    suspend fun deleteAlert(alert: AlertRoom)
}
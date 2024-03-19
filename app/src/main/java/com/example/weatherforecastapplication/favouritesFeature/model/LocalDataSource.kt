package com.example.weatherforecastapplication.favouritesFeature.model

import com.example.weatherforecastapplication.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllFavouritesWeather(): Flow<List<CurrentWeather>>

    suspend fun getCurrentWeatherFromRoom(id:Int): CurrentWeather
    suspend fun addWeatherToFavourites(weather: CurrentWeather)
    suspend fun deleteWeatherFromFavourites(weather: CurrentWeather)
}
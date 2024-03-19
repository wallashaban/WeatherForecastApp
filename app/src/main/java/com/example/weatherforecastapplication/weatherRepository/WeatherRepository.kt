package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.WeatherParam
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(
        weatherParam: WeatherParam
    ): CurrentWeather

    suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): FiveDaysForecast


    fun getAllFavouritesWeather(): Flow<List<CurrentWeather>>

    suspend fun getCurrentWeatherFromRoom(id:Int): CurrentWeather
    suspend fun addWeatherToFavourites(weather: CurrentWeather)
    suspend fun deleteWeatherFromFavourites(weather: CurrentWeather)

}
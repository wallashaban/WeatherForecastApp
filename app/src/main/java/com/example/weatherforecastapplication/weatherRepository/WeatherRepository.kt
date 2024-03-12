package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double,
                                  longitude: Double,
                                  apiKey: String):CurrentWeather

    suspend fun getCurrentWeatherUsingRoom(date:String)

    suspend fun getFiveDaysForecast(latitude: Double,
                                  longitude: Double,
                                  apiKey: String): FiveDaysForecast


}
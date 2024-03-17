package com.example.weatherforecastapplication.weatherRepository

import android.content.Context
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.WeatherParam

interface WeatherRepository {
    suspend fun getCurrentWeather(
        weatherParam: WeatherParam
    ): CurrentWeather

    suspend fun getCurrentWeatherUsingRoom(date: String)

    suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): FiveDaysForecast


}
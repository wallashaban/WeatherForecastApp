package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast

interface RemoteDataSource {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double,apiKey:String)
    :CurrentWeather
    suspend fun getFiveDaysForecast(latitude:Double,longitude:Double,apiKey:String)
            : FiveDaysForecast
}
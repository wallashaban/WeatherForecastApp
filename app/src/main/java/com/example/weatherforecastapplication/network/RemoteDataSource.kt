package com.example.weatherforecastapplication.network

import android.content.Context
import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast

interface RemoteDataSource {
    suspend fun getCurrentWeather(weatherParam: WeatherParam)
            : CurrentWeather

    suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
            : FiveDaysForecast
    suspend fun getAlertForWeather(weatherParam: WeatherParam)
            : AlertResult
}
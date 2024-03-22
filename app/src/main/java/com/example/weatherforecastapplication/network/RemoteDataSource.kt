package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getCurrentWeather(weatherParam: WeatherParam)
            : Flow<CurrentWeather>

    suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
            : Flow<FiveDaysForecast>
    suspend fun getAlertForWeather(weatherParam: WeatherParam)
            : Flow<AlertResult>
}
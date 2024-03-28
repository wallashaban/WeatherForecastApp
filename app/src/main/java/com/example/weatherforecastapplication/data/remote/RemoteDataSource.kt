package com.example.weatherforecastapplication.data.remote

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.models.WeatherParam
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getCurrentWeather(weatherParam: WeatherParam)
            : Flow<CurrentWeather>

    suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
            : Flow<FiveDaysForecast>
    suspend fun getAlertForWeather(weatherParam: WeatherParam)
            : Flow<AlertResult>
}
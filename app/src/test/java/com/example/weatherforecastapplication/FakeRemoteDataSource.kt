package com.example.weatherforecastapplication

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.models.WeatherParam
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource: RemoteDataSource {
    override suspend fun getCurrentWeather(weatherParam: WeatherParam): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun getFiveDaysForecast(weatherParam: WeatherParam): Flow<FiveDaysForecast> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertForWeather(weatherParam: WeatherParam): Flow<AlertResult> {
        TODO("Not yet implemented")
    }
}
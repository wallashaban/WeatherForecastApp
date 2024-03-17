package com.example.weatherforecastapplication.weatherRepository

import android.content.Context
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.RemoteDataSource
import com.example.weatherforecastapplication.network.WeatherParam

class WeatherRepositoryImpl private constructor(
    private var remoteDataSource: RemoteDataSource
) : WeatherRepository {


    companion object {
        @Volatile
        private var INSTANCE: WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource)
                : WeatherRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRepositoryImpl(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getCurrentWeather(
        weatherParam: WeatherParam
    ): CurrentWeather {
        return remoteDataSource.getCurrentWeather(weatherParam)
    }

    override suspend fun getCurrentWeatherUsingRoom(date: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): FiveDaysForecast {
        return remoteDataSource.getFiveDaysForecast(weatherParam)
    }
}
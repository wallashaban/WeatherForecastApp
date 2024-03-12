package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.RemoteDataSource

class WeatherRepositoryImpl private constructor(
   private var remoteDataSource: RemoteDataSource
):WeatherRepository{




    companion object{
        @Volatile
        private var INSTANCE:WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource)
        :WeatherRepositoryImpl{
            return INSTANCE?: synchronized(this){
                val instance = WeatherRepositoryImpl(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): CurrentWeather {
       return remoteDataSource.getCurrentWeather(latitude,longitude,apiKey)
    }

    override suspend fun getCurrentWeatherUsingRoom(date: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): FiveDaysForecast {
        return remoteDataSource.getFiveDaysForecast(latitude, longitude, apiKey)
    }
}
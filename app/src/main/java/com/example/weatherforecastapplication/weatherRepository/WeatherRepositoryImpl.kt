package com.example.weatherforecastapplication.weatherRepository

import android.content.Context
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSource
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.RemoteDataSource
import com.example.weatherforecastapplication.network.WeatherParam
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : WeatherRepository {


    companion object {
        @Volatile
        private var INSTANCE: WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource)
                : WeatherRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRepositoryImpl(remoteDataSource,
                    localDataSource)
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


    override suspend fun getCurrentWeatherFromRoom(id: Int): CurrentWeather {
       return localDataSource.getCurrentWeatherFromRoom(id)
    }

    override suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): FiveDaysForecast {
        return remoteDataSource.getFiveDaysForecast(weatherParam)
    }

    override fun getAllFavouritesWeather(): Flow<List<CurrentWeather>> {
       return localDataSource.getAllFavouritesWeather()
    }

    override suspend fun addWeatherToFavourites(weather: CurrentWeather) {
        localDataSource.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: CurrentWeather) {
        localDataSource.deleteWeatherFromFavourites(weather)
    }
}
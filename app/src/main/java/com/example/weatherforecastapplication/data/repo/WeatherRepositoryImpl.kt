package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.models.WeatherParam
import kotlinx.coroutines.flow.Flow

 class WeatherRepositoryImpl private constructor(
     private val remoteDataSource: RemoteDataSource,
     private val localDataSource: LocalDataSource
) : WeatherRepository {


    companion object {
        @Volatile
        private var INSTANCE: WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource)
                : WeatherRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRepositoryImpl(remoteDataSource,
                    localDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): Flow<FiveDaysForecast> {
        return remoteDataSource.getFiveDaysForecast(weatherParam)
    }

    override suspend fun getAlertForWeather(weatherParam: WeatherParam): Flow<AlertResult> {
        return remoteDataSource.getAlertForWeather(weatherParam)
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        localDataSource.saveAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        localDataSource.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<AlertRoom>> {
        return localDataSource.getAlerts()
    }

    override fun getAllFavouritesWeather(): Flow<List<Favourites>> {
       return localDataSource.getAllFavouritesWeather()
    }

    override fun getCurrentWeatherFromRoom(): Flow<FiveDaysForecast> {
        return localDataSource.getCurrentWeather()
    }

     override suspend fun deleteCurrentWeather(date:String) {
         localDataSource.deleteCurrentWeather(date)
     }

     override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
         localDataSource.addCurrentWeather(weather)
     }

     override suspend fun addWeatherToFavourites(weather: Favourites) {
        localDataSource.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        localDataSource.deleteWeatherFromFavourites(weather)
    }
}
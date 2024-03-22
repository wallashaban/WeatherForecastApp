package com.example.weatherforecastapplication.weatherRepository

import android.content.Context
import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.favouritesFeature.model.Favourites
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

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        localDataSource.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        localDataSource.deleteWeatherFromFavourites(weather)
    }
}
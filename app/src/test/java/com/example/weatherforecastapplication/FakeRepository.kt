package com.example.weatherforecastapplication

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow

class FakeRepository(private val localDataSource: LocalDataSource,
                     private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
    override suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
    : Flow<FiveDaysForecast> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertForWeather(weatherParam: WeatherParam): Flow<AlertResult> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        TODO("Not yet implemented")
    }

    override fun getAlerts(): Flow<List<AlertRoom>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavouritesWeather(): Flow<List<Favourites>> {
        TODO("Not yet implemented")
    }

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        TODO("Not yet implemented")
    }
}
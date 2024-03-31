package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.City
import com.example.weatherforecastapplication.data.models.Coord
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository(private val localDataSource: LocalDataSource,
                              private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
    override suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
    : Flow<FiveDaysForecast> = flow{
        val fiveDaysForecast =
            FiveDaysForecast(
                "2024/03/28",
                mutableListOf(),
                City("Cairo","EG",
                    Coord(latitude = 29.9792,
                        longitude = 31.1342)
                )
            )
        emit(fiveDaysForecast)
      // return remoteDataSource.getFiveDaysForecast(weatherParam)
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

     override fun getCurrentWeatherFromRoom(): Flow<FiveDaysForecast> {
         return localDataSource.getCurrentWeather()
     }
     override suspend fun deleteCurrentWeather(date: String) {
         localDataSource.deleteCurrentWeather(date)
     }
     override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
         localDataSource.addCurrentWeather(weather)
     }

     override suspend fun deleteAlertByDate(datetime: String) {
         TODO("Not yet implemented")
     }
 }
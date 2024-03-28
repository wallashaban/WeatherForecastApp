package com.example.weatherforecastapplication

import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow

class FakeLocalDataSource: LocalDataSource {
    override fun getAllFavouritesWeather(): Flow<List<Favourites>> {
        TODO("Not yet implemented")
    }

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        TODO("Not yet implemented")
    }

    override fun getAlerts(): Flow<List<AlertRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeather(): Flow<FiveDaysForecast> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather(weather: FiveDaysForecast) {
        TODO("Not yet implemented")
    }

    override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
        TODO("Not yet implemented")
    }
}
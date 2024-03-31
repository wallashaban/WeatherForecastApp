package com.example.weatherforecastapplication.data.local

import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.City
import com.example.weatherforecastapplication.data.models.Coord
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource: LocalDataSource {

   private val favourites = mutableListOf<Favourites>()
   private val alerts = mutableListOf<AlertRoom>()
    override fun getAllFavouritesWeather(): Flow<List<Favourites>> =flow{
        emit(favourites)
    }

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        favourites.add(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        favourites.remove(weather)
    }

    override fun getAlerts(): Flow<List<AlertRoom>> = flow{
        emit(alerts)
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        alerts.add(alert)
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        alerts.remove(alert)
    }

    override suspend fun deleteAlertByDate(datetime: String) {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeather(): Flow<FiveDaysForecast> = flow {
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
    }

    override suspend fun deleteCurrentWeather(date: String) {
        TODO("Not yet implemented")
    }


    override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
        TODO("Not yet implemented")
    }
}
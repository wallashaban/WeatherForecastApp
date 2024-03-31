package com.example.weatherforecastapplication.data.remote

import com.example.weatherforecastapplication.data.models.AlertResponse
import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.City
import com.example.weatherforecastapplication.data.models.Coord
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.models.WeatherParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource: RemoteDataSource {
    private val fiveDaysForecast =
        FiveDaysForecast(
            "2024/03/28",
            mutableListOf(),
            City("Cairo","EG",
                Coord(latitude = 29.9792,
                    longitude = 31.1342)
            )
        )
    override suspend fun getCurrentWeather(weatherParam: WeatherParam): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun getFiveDaysForecast
                (weatherParam: WeatherParam)
    : Flow<FiveDaysForecast> =flow{
       emit(fiveDaysForecast)
    }

    override suspend fun getAlertForWeather(weatherParam: WeatherParam): Flow<AlertResult> = flow{
        val alert =
            AlertResult(AlertResponse(mutableListOf()),"",29.9792,31.1342)
        emit(alert)
    }
}
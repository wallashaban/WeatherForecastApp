package com.example.weatherforecastapplication.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.workDataOf
import com.example.weatherforecastapplication.alertFeature.model.AlertResponse
import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.shared.BASE_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

object RetrofitHelper {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

object RemoteDataSourceImpl : RemoteDataSource {
    private val weatherServices: WeatherServices =
        RetrofitHelper.retrofit.create(WeatherServices::class.java)



    override suspend fun getCurrentWeather(
        weatherParam: WeatherParam
    ): Flow<CurrentWeather> = flow {
        val currentWeather =   weatherServices.getCurrentWeather(
            weatherParam.latitude,
            weatherParam.longitude,
            weatherParam.apiKey,
            weatherParam.units,
            weatherParam.lang
        )
            .body()!!
        Log.i("TAG", "getCurrentWeather: $currentWeather")
        emit(currentWeather)
    }

    override suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
    : Flow<FiveDaysForecast>
    = flow {
        val fiveDaysWeather =
            weatherServices.getFiveDaysForecast(
                weatherParam.latitude,
                weatherParam.longitude,
                weatherParam.apiKey,
                weatherParam.units,
                weatherParam.lang
            )
                .body()!!
        emit(fiveDaysWeather)
    }


    override suspend fun getAlertForWeather(weatherParam: WeatherParam)
            : Flow<AlertResult>
            = flow {
        val result =
            weatherServices.getAlertForWeather(
               weatherParam.latitude,// 33.44,
                weatherParam.longitude,
               weatherParam.apiKey,
                weatherParam.units,
                weatherParam.lang,
                )
        Log.i("TAG", "getAlertForWeather: ${result.body()}")
        emit(result.body()!!)
    }

}
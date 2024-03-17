package com.example.weatherforecastapplication.network

import android.content.Context
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.shared.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    ): CurrentWeather {
        return weatherServices.getCurrentWeather(weatherParam.latitude,
           weatherParam. longitude, weatherParam. apiKey,weatherParam. units,
            weatherParam.lang)
            .body()!!
    }

    override suspend fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ): FiveDaysForecast {
        return weatherServices.getFiveDaysForecast(weatherParam.latitude,
            weatherParam. longitude, weatherParam. apiKey,weatherParam. units,
            weatherParam.lang)
            .body()!!
    }
}
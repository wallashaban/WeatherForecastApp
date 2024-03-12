package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.Shared.BASE_URL
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
object RemoteDataSourceImpl : RemoteDataSource {
   private val weatherServices:WeatherServices =
       RetrofitHelper.retrofit.create(WeatherServices::class.java)

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): CurrentWeather {
        return weatherServices.getCurrentWeather(latitude, longitude, apiKey)
            .body()!!
    }

    override suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): FiveDaysForecast {
        return weatherServices.getFiveDaysForecast(latitude,longitude,apiKey)
            .body()!!
    }
}
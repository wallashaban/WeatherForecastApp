package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String ,
        @Query("lang") lang: String ,
    )
            : Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getFiveDaysForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    )
            : Response<FiveDaysForecast>

    @GET("data/3.0/onecall?exclude=hourly,daily,minutely,current")
   suspend fun getAlertForWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("unit") units: String,
        @Query("lang") lang: String
    ):Response<AlertResult>
}
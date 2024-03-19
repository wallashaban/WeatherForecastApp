package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.alertFeature.model.AlertResult
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
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

    @GET("forecast")
    suspend fun getFiveDaysForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    )
            : Response<FiveDaysForecast>

    @GET("data/3.0/onecall?exclude=hourly,daily,minutely,current")
    fun getAlertForWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ):AlertResult
}
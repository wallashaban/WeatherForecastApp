package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather")
    suspend fun getCurrentWeather( @Query("lat") latitude: Double,
                                   @Query("lon") longitude: Double,
                                   @Query("appid") apiKey: String)
    :Response<CurrentWeather>

    @GET("forecast")
    suspend fun getFiveDaysForecast( @Query("lat") latitude: Double,
                                   @Query("lon") longitude: Double,
                                   @Query("appid") apiKey: String)
            :Response<FiveDaysForecast>
}
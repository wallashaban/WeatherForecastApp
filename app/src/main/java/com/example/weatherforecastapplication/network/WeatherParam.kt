package com.example.weatherforecastapplication.network

import retrofit2.http.Query

data class WeatherParam(val latitude: Double,
                   val longitude: Double,
                   val apiKey: String,
                   val units: String,
                   val lang: String )
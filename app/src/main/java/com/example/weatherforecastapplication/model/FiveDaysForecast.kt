package com.example.weatherforecastapplication.model

import com.google.gson.annotations.SerializedName

data class FiveDaysForecast(
    val list: List<CurrentWeather>,
    val city: City,
    )

data class City(
    val name:String,
    val country:String,
    val coord: Coord,
    )

data class Coord(
    @SerializedName("lat")
    val latitude:Double,
    @SerializedName("lon")
    val longitude:Double,
    )
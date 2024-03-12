package com.example.weatherforecastapplication.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("dt_txt")
    val dateText:String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    @SerializedName("dt")
    val datetime: Int,
    @SerializedName("sys")
    val system: System,
    val base: String, val visibility: Int, val timezone: Int,
    @SerializedName("cod")
    val code: Int, val name: String, val id: Int
)

data class Wind(
    val speed: Double,
    @SerializedName("deg")
    val degree: Int,
    val gust: Double
)

data class Weather(val id: String, val main: String, val description: String, val icon: String)


data class Main(

    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val minTemp: Double,
    @SerializedName("temp_max")
    val maxTemp: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("sea_level")
    val seaLevel: Double,
    @SerializedName("grnd_level")
    val groundLevel: Double,
    @SerializedName("temp_kf")
    val tempKf:Double
)

data class Clouds(val all: Int)

data class System(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)


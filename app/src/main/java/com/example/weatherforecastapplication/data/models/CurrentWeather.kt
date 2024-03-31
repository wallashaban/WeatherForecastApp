package com.example.weatherforecastapplication.data.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

//@Fts4
data class CurrentWeather(
    val id: Int,
    @SerializedName("dt_txt")
    val dateText: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    @SerializedName("dt")
    val datetime: Int,
    @SerializedName("sys")
    val system: System,
    val base: String,
    val visibility: Int,
    val timezone: Int,
    @SerializedName("cod")
    val code: Int,
    var name: String,
)


class CoordConverter {
    @TypeConverter
    fun fromCoord(coord: Coord?): String? {
        val gson = Gson()
        return gson.toJson(coord)
    }

    @TypeConverter
    fun toCoord(coordString: String?): Coord? {
        val gson = Gson()
        return gson.fromJson(coordString, Coord::class.java)
    }
}
class WeatherListConverter {
    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
        val gson = Gson()
        return gson.toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(weatherString: String): List<Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(weatherString, type)
    }
}

class MainConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMain(main: Main?): String? {
        return gson.toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String?): Main? {
        return gson.fromJson(mainString, Main::class.java)
    }
}

class WindConverter {
    @TypeConverter
    fun fromWind(wind: Wind?): String? {
        val gson = Gson()
        return gson.toJson(wind)
    }

    @TypeConverter
    fun toWind(windString: String?): Wind? {
        val gson = Gson()
        return gson.fromJson(windString, Wind::class.java)
    }
}
class CloudsConverter {
    @TypeConverter
    fun fromClouds(clouds: Clouds?): String? {
        val gson = Gson()
        return gson.toJson(clouds)
    }

    @TypeConverter
    fun toClouds(cloudsString: String?): Clouds? {
        val gson = Gson()
        return gson.fromJson(cloudsString, Clouds::class.java)
    }
}
class SystemConverter {
    @TypeConverter
    fun fromSystem(system: System?): String? {
        val gson = Gson()
        return gson.toJson(system)
    }

    @TypeConverter
    fun toSystem(systemString: String?): System? {
        val gson = Gson()
        return gson.fromJson(systemString, System::class.java)
    }
}




data class Wind(
    var speed: Double,
    @SerializedName("deg")
    val degree: Int,
    val gust: Double
)

data class Weather(val id: String, val main: String, var description: String, val icon: String)


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
    val tempKf: Double
)

data class Clouds(val all: Int)

data class System(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)


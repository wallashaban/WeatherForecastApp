package com.example.weatherforecastapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity(tableName = "currentWeather")
data class FiveDaysForecast(
    @PrimaryKey
    var currentDate:String,
    val list: List<CurrentWeather>,
    val city: City,
    )


object CityConverter {
    @TypeConverter
    fun fromString(value: String?): City {
        return Gson().fromJson(value, City::class.java)
    }

    @TypeConverter
    fun fromCity(city: City?): String {
        return Gson().toJson(city)
    }
}


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


object CurrentWeatherConverter {
    @TypeConverter
    fun fromString(value: String?): List<CurrentWeather> {
        val listType: Type = object : TypeToken<List<CurrentWeather?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CurrentWeather?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
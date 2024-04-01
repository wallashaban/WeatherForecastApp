package com.example.weatherforecastapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.CityConverter
import com.example.weatherforecastapplication.data.models.CloudsConverter
import com.example.weatherforecastapplication.data.models.CoordConverter
import com.example.weatherforecastapplication.data.models.CurrentWeatherConverter
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.models.MainConverter
import com.example.weatherforecastapplication.data.models.SystemConverter
import com.example.weatherforecastapplication.data.models.WeatherListConverter
import com.example.weatherforecastapplication.data.models.WindConverter

@Database(entities = [Favourites::class, AlertRoom::class,
                     FiveDaysForecast::class], version = 9, exportSchema = false)
@TypeConverters(
    CurrentWeatherConverter::class, CoordConverter::class
, MainConverter::class, WeatherListConverter::class,
    WindConverter::class, CloudsConverter::class,
    SystemConverter::class, CityConverter::class)


abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherFavouritesDao(): FavouritesDao
    abstract fun getAlertsDao(): AlertsDao
    abstract fun getWeatherDao(): WeatherDao
    companion object{
        private var INSTANCE : WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context=context.applicationContext,
                    klass = WeatherDatabase::class.java,
                    name = "weather"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
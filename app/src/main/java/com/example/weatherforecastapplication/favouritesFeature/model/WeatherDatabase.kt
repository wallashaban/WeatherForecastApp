package com.example.weatherforecastapplication.favouritesFeature.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapplication.model.CloudsConverter
import com.example.weatherforecastapplication.model.CoordConverter
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.MainConverter
import com.example.weatherforecastapplication.model.SystemConverter
import com.example.weatherforecastapplication.model.WeatherListConverter
import com.example.weatherforecastapplication.model.WindConverter
import com.example.weatherforecastapplication.shared.FAVOURITES_TABLE

@Database(entities = arrayOf( CurrentWeather::class), version = 2, exportSchema = false)
@TypeConverters(WeatherListConverter::class,MainConverter::class,
    WindConverter::class,CloudsConverter::class,SystemConverter::class,
    CoordConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherFavouritesDao():FavouritesDao

    companion object{
        private var INSTANCE : WeatherDatabase? = null

        fun getInstance(context: Context):WeatherDatabase{
            return INSTANCE?: synchronized(this){
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
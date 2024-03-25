package com.example.weatherforecastapplication.favouritesFeature.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom

@Database(entities = [Favourites::class,AlertRoom::class], version = 7, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherFavouritesDao():FavouritesDao
    abstract fun getAlertsDao():AlertsDao
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
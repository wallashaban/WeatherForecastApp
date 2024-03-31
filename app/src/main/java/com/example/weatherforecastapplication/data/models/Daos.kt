package com.example.weatherforecastapplication.data.models

import android.content.Context
import com.example.weatherforecastapplication.data.local.AlertsDao
import com.example.weatherforecastapplication.data.local.FavouritesDao
import com.example.weatherforecastapplication.data.local.WeatherDao
import com.example.weatherforecastapplication.data.local.WeatherDatabase


class Daos(val context: Context)
{
     val favouritesDao: FavouritesDao =
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()

     val alertsDao: AlertsDao =
        WeatherDatabase.getInstance(context).getAlertsDao()

     val weatherDao: WeatherDao =
        WeatherDatabase.getInstance(context).getWeatherDao()

}

/*
  private lateinit var context: Context // it cause memory leak search more

     fun initialize(context: Context) {
         this.context = context.applicationContext
     }
    private val favouritesDao: FavouritesDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()
    }
    private val alertsDao: AlertsDao by lazy {
        WeatherDatabase.getInstance(context).getAlertsDao()
    }
    private val weatherDao: WeatherDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherDao()
    }*/

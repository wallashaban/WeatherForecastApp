package com.example.weatherforecastapplication.favouritesFeature.view

import com.example.weatherforecastapplication.model.CurrentWeather

interface OnDeleteClickListener {
    fun onFavClickListener(weather: CurrentWeather) //(weather:CurrentWeather)->Unit
}
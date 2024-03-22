package com.example.weatherforecastapplication.favouritesFeature.view

import com.example.weatherforecastapplication.favouritesFeature.model.Favourites
import com.example.weatherforecastapplication.model.CurrentWeather

interface OnDeleteClickListener {
    fun onFavClickListener(weather: Favourites) //(weather:Favourites)->Unit
}
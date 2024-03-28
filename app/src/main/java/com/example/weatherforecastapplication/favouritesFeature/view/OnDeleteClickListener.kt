package com.example.weatherforecastapplication.favouritesFeature.view

import com.example.weatherforecastapplication.data.models.Favourites

interface OnDeleteClickListener {
    fun onFavClickListener(weather: Favourites) //(weather:Favourites)->Unit
}
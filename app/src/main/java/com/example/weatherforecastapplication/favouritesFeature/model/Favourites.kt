package com.example.weatherforecastapplication.favouritesFeature.model

import androidx.room.Entity

@Entity(tableName = "favourites", primaryKeys = ["latitude","longitude"])
data class Favourites (
    val latitude:Double,
    val longitude:Double,
    val name:String,
    )
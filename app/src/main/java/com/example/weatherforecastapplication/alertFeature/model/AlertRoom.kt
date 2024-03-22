package com.example.weatherforecastapplication.alertFeature.model

import androidx.room.Entity

@Entity(tableName = "alerts", primaryKeys = ["latitude","longitude"])
data class AlertRoom (
    val name:String,
    val time:String,
    val date:String,
    val latitude:Double,
    val longitude:Double,
    )
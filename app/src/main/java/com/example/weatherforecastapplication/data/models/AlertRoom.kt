package com.example.weatherforecastapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "alerts")
data class AlertRoom (
    @PrimaryKey
    val id:UUID,
    val name:String,
    val time:String,
    val date:String,
    val latitude:Double,
    val longitude:Double,
    )
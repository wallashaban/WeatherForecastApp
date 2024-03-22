package com.example.weatherforecastapplication.alertFeature.model

data class AlarmItem( // contain the fields we want to send to the alarm manager to display a notification
    val time: Long,
    val message:String
)

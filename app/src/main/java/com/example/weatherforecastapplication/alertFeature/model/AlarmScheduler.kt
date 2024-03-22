package com.example.weatherforecastapplication.alertFeature.model

interface AlarmScheduler { // contain s the functions that make up an alarm scheduler
    fun scheduler(item: AlarmItem)

    fun cansel(item: AlarmItem)
}
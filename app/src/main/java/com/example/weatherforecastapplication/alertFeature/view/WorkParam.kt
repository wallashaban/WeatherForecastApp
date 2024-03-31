package com.example.weatherforecastapplication.alertFeature.view

import android.content.Context

data class WorkParam(
    val context:Context,
   val millis:Long,
    var lat:Double=0.0,
    var long:Double=0.0,
   val isNotification:Boolean,
    val datetime:String
)

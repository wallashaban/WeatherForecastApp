package com.example.weatherforecastapplication.data.models

import com.google.gson.annotations.SerializedName

data class AlertResult(
    val alertResponse: AlertResponse,
    val timezone:String,
    val lat: Double,
    val lon: Double,
    )
data class AlertResponse(val alerts: List<Alert>)
data class Alert (
    @SerializedName("sender_name")
    val senderName:String,
    val event:String,
    val start:Long,
    val end:Long,
    val description:String,
    val tags:List<String>
    )

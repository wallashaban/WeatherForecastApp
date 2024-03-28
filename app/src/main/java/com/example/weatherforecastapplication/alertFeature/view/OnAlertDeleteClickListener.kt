package com.example.weatherforecastapplication.alertFeature.view

import com.example.weatherforecastapplication.data.models.AlertRoom

interface OnAlertDeleteClickListener {
    fun onClickListener(alert: AlertRoom) // (alert):Unit
}
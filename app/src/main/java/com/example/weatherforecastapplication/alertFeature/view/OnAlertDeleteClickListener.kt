package com.example.weatherforecastapplication.alertFeature.view

import com.example.weatherforecastapplication.alertFeature.model.AlertRoom

interface OnAlertDeleteClickListener {
    fun onClickListener(alert: AlertRoom) // (alert):Unit
}
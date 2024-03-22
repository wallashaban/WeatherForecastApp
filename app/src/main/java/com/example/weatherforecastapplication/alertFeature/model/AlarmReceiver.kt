package com.example.weatherforecastapplication.alertFeature.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver

@SuppressLint("RestrictedApi")
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val alertNotificationService =
            AlertNotificationService(context)
        val message = intent?.getStringExtra("message")?:return
        println("Alarm Trigger : $message")
        alertNotificationService.showAlertNotification(message)
    }
}
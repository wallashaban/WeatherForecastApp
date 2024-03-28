package com.example.weatherforecastapplication.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.weatherforecastapplication.alertFeature.model.AlertNotificationService

class MyApp: Application() {
    val storage : Storage by lazy {
        Storage(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                AlertNotificationService.ALERT_CHANNEL_ID,
                "Weather Alert",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = ""
            val notificationManager = getSystemService(NOTIFICATION_SERVICE)
            as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.getLocalizedContext(base, Storage(base).getPreferredLocale()))
    }
}
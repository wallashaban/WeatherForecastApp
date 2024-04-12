package com.example.weatherforecastapplication.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.weatherforecastapplication.alertFeature.model.AlertNotificationService

class MyApp: Application() {
    val storage : Storage by lazy {
        Storage(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        applyMode(applicationContext)
        //Storage.getPreferredLocale(applicationContext)
       /* LocaleUtil
            .applyLocalizedContext(this,
                Storage.getPreferredLocale(this))*/
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
        Log.i("TAG", "attachBaseContext: ${Storage(base).getPreferredLocale()}")
        super.attachBaseContext(LocaleUtil.getLocalizedContext(base,
            Storage(base).getPreferredLocale()))
    }
}
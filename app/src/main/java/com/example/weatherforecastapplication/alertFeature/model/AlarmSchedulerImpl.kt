package com.example.weatherforecastapplication.alertFeature.model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId

class AlarmSchedulerImpl(private val context: Context) :AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    @SuppressLint("MissingPermission")
    override fun scheduler(item: AlarmItem) {
        val intent = Intent(context,AlarmReceiver::class.java).apply {
            putExtra("lat", item.latitude)
            putExtra("long", item.longitude)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
        )
    }
    override fun cansel(item: AlarmItem) {
        Log.i("TAG", "cansel: ")
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(context,AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
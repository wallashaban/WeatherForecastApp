package com.example.weatherforecastapplication.alertFeature.model

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R

class AlertNotificationService(private val context: Context) {

    private val notificationManager = context.
    getSystemService(Application.NOTIFICATION_SERVICE)
            as NotificationManager
    fun showAlertNotification(description:String,icon:Bitmap){
        notificationManager.notify(1,createAlertNotification(description,icon))
    }

    private fun createAlertNotification(description: String,
                                        icon:Bitmap): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivities(
            context,
            1,
            arrayOf(intent),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.cloud_notification)
            .setContentTitle("Weather Alert")
            .setContentText(description)
            .setCategory(Notification.CATEGORY_ALARM)
            .setLargeIcon(icon)
            .setContentIntent(pendingIntent)
            .build()
    }
    fun createForegroundServiceNotification() :Notification
    {
        val notificationBuilder = NotificationCompat.Builder(context,
            ALERT_CHANNEL_ID
        )
       return  notificationBuilder.setOngoing(true)
            .setContentTitle("Service running")
            .setContentText("Displaying over other apps")
            .setSmallIcon(R.drawable.cloud_notification)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }
    companion object{
        const val ALERT_CHANNEL_ID = "AlertChannel"
    }
}
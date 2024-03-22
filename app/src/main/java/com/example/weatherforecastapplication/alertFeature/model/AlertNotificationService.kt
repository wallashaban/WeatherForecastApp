package com.example.weatherforecastapplication.alertFeature.model

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R

class AlertNotificationService(private val context: Context) {

    val notificationManager = context.getSystemService(Application.NOTIFICATION_SERVICE)
            as NotificationManager
    fun showAlertNotification(message:String){
        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivities(
            context,
            1,
            arrayOf(intent),
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)PendingIntent.FLAG_IMMUTABLE else 0
            )
        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle("Alert")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(1,notification)
    }
    companion object{
        const val ALERT_CHANNEL_ID = "AlertChannel"
    }
}
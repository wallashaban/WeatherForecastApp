package com.example.weatherforecastapplication.alertFeature.model

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R

class AlertNotificationService(private val context: Context) {

    private val notificationManager = context.getSystemService(Application.NOTIFICATION_SERVICE)
            as NotificationManager

    @SuppressLint("RemoteViewLayout")
    val remoteViews = RemoteViews(context.packageName, R.layout.notification_layout)


    fun showAlertNotification(description:String){

       // remoteViews.setTextViewText(R.id.titleTextView, degree)
       // remoteViews.setTextViewText(R.id.firstPhraseTextView, description)
        //remoteViews.setTextViewText(R.id.secondPhraseTextView, address)
        notificationManager.notify(1,createAlertNotification(description))
    }

    private fun createAlertNotification(description: String): Notification {
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
           // .setContent(remoteViews)
            .setContentIntent(pendingIntent)
            .build()
    }
    companion object{
        const val ALERT_CHANNEL_ID = "AlertChannel"
    }
}
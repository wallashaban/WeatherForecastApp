package com.example.weatherforecastapplication.alertFeature.model

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.view.Window


class ForegroundService : Service() {
    private lateinit var  mediaPlayer:MediaPlayer
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this,R.raw.alarm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground() else startForeground(
            1,
            Notification()
        )
        val window = Window(this)
        window.open()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("TAG", "onStartCommand: ")
        mediaPlayer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy: Service")
        mediaPlayer.stop()
    }
    @SuppressLint("ForegroundServiceType")
    private fun startMyOwnForeground() {
/*       val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)*/
        val notificationBuilder = NotificationCompat.Builder(this,
            AlertNotificationService.ALERT_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Service running")
            .setContentText("Displaying over other apps")
            .setSmallIcon(R.drawable.cloud_notification)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }
}
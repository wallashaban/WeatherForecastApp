package com.example.weatherforecastapplication.alertFeature.model

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.view.Window


class ForegroundService : Service() {
    private lateinit var  mediaPlayer:MediaPlayer
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("TAG", "onCreate: Service")
        mediaPlayer = MediaPlayer.create(this,R.raw.alarm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
          val notification =
              AlertNotificationService(this)
                  .createForegroundServiceNotification()
                startForeground(2, notification)
        }
        else {
            startForeground(1, Notification())
        }

    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("TAG", "onStartCommand: ")
        val desc = intent.getStringExtra("description")
        val window = Window(this@ForegroundService,
            desc?:"The weather is fine :)")
        window.open()
        mediaPlayer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

}
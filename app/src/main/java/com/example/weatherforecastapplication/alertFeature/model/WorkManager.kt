package com.example.weatherforecastapplication.alertFeature.model

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.remote.RemoteDataSourceImpl
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.utils.API_KEY
import com.example.weatherforecastapplication.utils.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn


class MyWorkManager (val context: Context, params: WorkerParameters)
    : CoroutineWorker(context,params) {
        private  var isNotification = true
    private lateinit var date: String
    private lateinit var time: String
    override suspend fun doWork(): Result {
        val inputData = inputData
        val lat = inputData.getDouble("lat", 0.0)
        val lon = inputData.getDouble("lon", 0.0)
         isNotification = inputData.getBoolean("notification",true)

        fetchDataAndShowNotification(context,lat,lon)
        return Result.success()
    }

    private suspend fun fetchDataAndShowNotification(context: Context, lat: Double, long: Double) {
        val alertNotificationService =
            AlertNotificationService(context)

        val  remoteDataSource = RemoteDataSourceImpl

        val weatherParam = WeatherParam(
            lat,
            long,
            API_KEY,
            Storage.getCurrentUnit(context),
            Storage.getPreferredLocale(context),
        )

        remoteDataSource.getAlertForWeather(weatherParam)
            .flowOn(
            Dispatchers.IO
        ).collectLatest {

            if(it.alertResponse==null)
            {
                if(isNotification)
                {
                    alertNotificationService.showAlertNotification("The weather is fine " +
                            "no risks to face :)",
                        BitmapFactory.decodeResource(context.resources, R.drawable.sunny))
                }else
                {
                    startService(context,"The weather is fine " +
                            "no risks to face :)")
                }
            }else {
                if(isNotification) {
                    Log.i("TAG", "fetchDataAndShowNotification: ${it.alertResponse}")
                    alertNotificationService.showAlertNotification(
                        it.alertResponse.alerts[0].description,
                        BitmapFactory.decodeResource(context.resources, R.drawable.weather_icon)
                    )
                }
                else{
                    startService(context, description = it.alertResponse.alerts[0].description)
                }
            }
        }
    }
    fun startService(context: Context,description:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("TAG", "startService: ")
            if (Settings.canDrawOverlays(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   context.startForegroundService(Intent(context, ForegroundService::class.java))
                } else {
                   context.startService(Intent(context, ForegroundService::class.java))
                }
            }
        } else {
          val intent =  Intent(context, ForegroundService::class.java)
                intent.putExtra("description",description)
           context.startService(intent)
        }
    }
}
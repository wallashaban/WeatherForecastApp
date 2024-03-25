package com.example.weatherforecastapplication.alertFeature.model

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlin.math.log


class MyWorkManager (val context: Context, params: WorkerParameters)
    : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        val inputData = inputData
        val lat = inputData.getDouble("lat", 0.0)
        val lon = inputData.getDouble("lon", 0.0)
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
//                alertNotificationService.showAlertNotification("The weather is fine " +
//                       "no risks to face :)")
                Log.i("TAG", "fetchDataAndShowNotification: Dialog")
                startService(context)
            }else {
                Log.i("TAG", "fetchDataAndShowNotification: ${it.alertResponse}")
                alertNotificationService.showAlertNotification(it.alertResponse.alerts[0].description)
            }
        }
    }
    fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("TAG", "startService: ")
            if (Settings.canDrawOverlays(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   context. startForegroundService(Intent(context, ForegroundService::class.java))
                } else {
                   context. startService(Intent(context, ForegroundService::class.java))
                }
            }
        } else {
           context.startService(Intent(context, ForegroundService::class.java))
        }
    }
}
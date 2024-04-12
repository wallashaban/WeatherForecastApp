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
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.remote.RemoteDataSourceImpl
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
import com.example.weatherforecastapplication.utils.API_KEY
import com.example.weatherforecastapplication.utils.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext


class MyWorkManager(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private var isNotification = true
    override suspend fun doWork(): Result {
        Log.i("TAG", "doWork: ")
        withContext(Dispatchers.IO) {
            val inputData = inputData
            val lat = inputData.getDouble("lat", 0.0)
            val lon = inputData.getDouble("lon", 0.0)
            val datetime = inputData.getString("datetime")
            isNotification = inputData.getBoolean("notification", true)

            getWeatherAlert(context, lat, lon,datetime!!)
        }
        return Result.success()
    }

    private suspend fun getWeatherAlert(
        context: Context,
        lat: Double,
        long: Double,
        datetime:String
    ) {

        val repo = WeatherRepositoryImpl.getInstance(
            RemoteDataSourceImpl,
            LocalDataSourceImpl.getInstance(Daos(context))
        )

        val weatherParam = WeatherParam(
            lat,
            long,
            API_KEY,
            Storage.getCurrentUnit(context),
            Storage(context).getPreferredLocale(),
        )

        repo.getAlertForWeather(weatherParam)
            .collectLatest {

                if (it.alertResponse == null) {
                    if (isNotification) {
                        showNotification(context.getString(R.string.weatherDescription))
                    } else {
                        showDialog(context.getString(R.string.weatherDescription))
                    }
                } else {
                    if (isNotification) {
                        showNotification(
                            it.alertResponse.alerts[0].description,
                        )
                    } else {
                        showDialog(it.alertResponse.alerts[0].description)
                    }
                }
                repo.deleteAlertByDate(datetime)
            }
    }

    private fun showNotification(description: String) {
        val alertNotificationService =
            AlertNotificationService(context)
        alertNotificationService.showAlertNotification(
            description,
            BitmapFactory.decodeResource(context.resources, R.drawable.weather_icon)
        )
    }

    private fun showDialog(description: String) {
        startService(context, description)
    }

    private fun startService(context: Context, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val intent = Intent(context, DialogService::class.java)
                    intent.putExtra("description", description)
                    context.startForegroundService(intent)
                   // context.startForegroundService(Intent(context, DialogService::class.java))
                } else {
                    val intent = Intent(context, DialogService::class.java)
                    intent.putExtra("description", description)
                    context.startService(intent)
                }
            }
        } else {
            val intent = Intent(context, DialogService::class.java)
            intent.putExtra("description", description)
            context.startService(intent)
        }
    }
}
package com.example.weatherforecastapplication.alertFeature.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.shared.Storage
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        val lat = intent?.getDoubleExtra("lat",0.0)?:return
        val long = intent?.getDoubleExtra("long",0.0)?:return
        println("Alarm Trigger : $lat")
        GlobalScope.launch(Dispatchers.IO) {
            fetchDataAndShowNotification(context, lat, long)
        }
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

        remoteDataSource.getAlertForWeather(weatherParam).collectLatest {
          //  alertNotificationService.showAlertNotification(it.alertResponse .alerts[0].description)
        }



    }
}
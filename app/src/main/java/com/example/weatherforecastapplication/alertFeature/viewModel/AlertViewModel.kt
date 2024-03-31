package com.example.weatherforecastapplication.alertFeature.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecastapplication.alertFeature.model.MyWorkManager
import com.example.weatherforecastapplication.alertFeature.view.WorkParam
import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import com.example.weatherforecastapplication.utils.getAddressFromCoordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class AlertViewModel(private val _repo: WeatherRepository) : ViewModel() {
    private val _alerts = MutableStateFlow<ApiState<List<AlertRoom>>>(ApiState.Loading())
    val alerts = _alerts.asStateFlow()

    private val _weatherAlert = MutableStateFlow<ApiState<AlertResult>>(ApiState.Loading())
    val weatherAlert = _weatherAlert.asStateFlow()


    fun getAlertForWeather(weatherParam: WeatherParam){
        viewModelScope.launch {
            _repo.getAlertForWeather(weatherParam).catch {
                _weatherAlert.emit(ApiState.Failure(it))
            }.collectLatest {
                _weatherAlert.emit(ApiState.Success(it))
            }
        }
    }

     fun scheduleWork(
       workParam: WorkParam
        ) : OneTimeWorkRequest{
        val inputData = Data.Builder()
            .putDouble("lat", workParam.lat)
            .putDouble("lon", workParam.long)
            .putString("datetime",workParam.datetime)
            .putBoolean("notification",workParam.isNotification)
            .build()
        val currentTimeMillis = System.currentTimeMillis()
        val initialDelay = workParam.millis - currentTimeMillis
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        return OneTimeWorkRequestBuilder<MyWorkManager>()
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("alert")
            .build()
     }

    fun saveAlert(alert: AlertRoom){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.saveAlert(alert)
        }
    }
    fun deleteAlert(alert: AlertRoom){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteAlert(alert)
        }
    }

    fun getAlert(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getAlerts().catch {
                _alerts.emit(ApiState.Failure(it))
            }.collectLatest {
                _alerts.emit(ApiState.Success(it))
            }
        }
    }
    class Factory(private val _repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
                AlertViewModel(_repo) as T
            } else {
                throw IllegalArgumentException("ViewModel class not found")
            }
        }

    }
}
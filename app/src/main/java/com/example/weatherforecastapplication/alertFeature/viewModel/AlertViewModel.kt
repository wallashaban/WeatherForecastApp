package com.example.weatherforecastapplication.alertFeature.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertViewModel(private val _repo: WeatherRepository) : ViewModel() {
    private val _alerts = MutableStateFlow<ApiState<List<AlertRoom>>>(ApiState.Loading())
    val alerts = _alerts.asStateFlow()

    private val _weatherAlert = MutableStateFlow<ApiState<AlertResult>>(ApiState.Loading())
    val weatherAlert = _weatherAlert.asStateFlow()


    fun getAlertForWeather(weatherParam: WeatherParam){
        viewModelScope.launch {
            _repo.getAlertForWeather(weatherParam).catch {
                _weatherAlert.emit(ApiState.Failure(it))
                Log.i("TAG", "getAlertForWeather: ")
            }.collectLatest {
                Log.i("TAG", "getAlertForWeather: ")
                _weatherAlert.emit(ApiState.Success(it))
            }
        }
    }

    fun saveAlert(alert: AlertRoom){
        Log.i("AlertFragment", "saveAlert: before")
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("AlertFragment", "saveAlert:in ")
            _repo.saveAlert(alert)
            Log.i("AlertFragment", "saveAlert: after")
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
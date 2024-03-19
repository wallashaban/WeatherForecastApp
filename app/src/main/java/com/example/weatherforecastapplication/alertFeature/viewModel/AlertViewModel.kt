package com.example.weatherforecastapplication.alertFeature.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.network.RemoteDataSource
import com.example.weatherforecastapplication.network.WeatherParam
import kotlinx.coroutines.launch

class AlertViewModel(private val _remoteDataSource: RemoteDataSource) : ViewModel() {


    fun getAlertForWeather(weatherParam: WeatherParam){
        viewModelScope.launch {
            _remoteDataSource.getAlertForWeather(weatherParam)
        }
    }
    class Factory(private val _remoteDataSource: RemoteDataSource) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
                AlertViewModel(_remoteDataSource) as T
            } else {
                throw IllegalArgumentException("ViewModel class not found")
            }
        }

    }
}
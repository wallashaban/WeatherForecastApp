package com.example.weatherforecastapplication.weatherFeature.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(private val _repo: WeatherRepository) : ViewModel() {

    var unit:String? = null
    var wind:String? = null
    var isFirst:Boolean = true
    private var _currentWeather = MutableStateFlow<ApiState<FiveDaysForecast>>(ApiState.Loading())
    var currentWeather= _currentWeather.asStateFlow()
    private var _fiveDaysForecast = MutableStateFlow<ApiState<FiveDaysForecast>>(ApiState.Loading())
    var fiveDaysForecast= _fiveDaysForecast.asStateFlow()

    fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ) {
        viewModelScope.launch(Dispatchers.IO) {
           _repo.getFiveDaysForecast(weatherParam).catch {
               _fiveDaysForecast.emit(ApiState.Failure(it))
           }.collect {
               Log.i("TAG", "getFiveDaysForecast: api")
               _fiveDaysForecast.emit(ApiState.Success(it))
           }

        }
    }

    fun getCurrentWeatherUsingRoom(){
       viewModelScope.launch {
           _repo.getCurrentWeatherFromRoom()
               .catch {
                   _fiveDaysForecast.emit(ApiState.Failure(it))
                   Log.i("TAG", "getCurrentWeatherUsingRoom: ${it.message}")
               }
               .collect{
                   Log.i("TAG", "getCurrentWeatherUsingRoom: collect")
               _fiveDaysForecast.emit(ApiState.Success(it))
           }
       }
    }

    fun deleteCurrentWeather(date:String){
        viewModelScope.launch {
            _repo.deleteCurrentWeather(date)
        }
    }

    fun addCurrentWeather(weather: FiveDaysForecast){
        viewModelScope.launch {
            _repo.addCurrentWeather(weather)
        }
    }


    class Factory(private val _repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                WeatherViewModel(_repo) as T
            } else {
                throw IllegalArgumentException("ViewModel class not found")
            }
        }
    }
}
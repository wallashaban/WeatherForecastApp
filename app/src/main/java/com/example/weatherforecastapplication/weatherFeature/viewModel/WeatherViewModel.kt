package com.example.weatherforecastapplication.weatherFeature.viewModel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.weatherRepository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherViewModel(private val _repo: WeatherRepository) : ViewModel() {

    var unit:String? = null
    var wind:String? = null
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
               _fiveDaysForecast.emit(ApiState.Success(it))
           }

        }
    }

//    fun saveCurrentWeatherToRoom(weather: CurrentWeather){
//        viewModelScope.launch(Dispatchers.IO) {
//             _repo.addWeatherToFavourites(weather)
//        }
//    }

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
package com.example.weatherforecastapplication.weatherFeature.viewModel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.getCurrentMode
import com.example.weatherforecastapplication.shared.getCurrentWindUnit
import com.example.weatherforecastapplication.weatherRepository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val _repo: WeatherRepository) : ViewModel() {

    private var _currentWeather = MutableLiveData<CurrentWeather>()
    var currentWeather: LiveData<CurrentWeather> = _currentWeather
    private var _fiveDaysForecast = MutableLiveData<FiveDaysForecast>()
    var fiveDaysForecast: LiveData<FiveDaysForecast> = _fiveDaysForecast


    fun getCurrentWeather(
        weatherParam: WeatherParam
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _repo.getCurrentWeather(weatherParam)
            Log.i("TAG", "getCurrentWeather: $result")
           // if(getCurrentWindUnit(requireContext(this)) !="Meter/Sec") {
                val speed = result.wind.speed * 2.23694
                result.wind.speed = "%.2f".format(speed).toDouble()
          //  }
            _currentWeather.postValue(result)
        }
    }

    fun getFiveDaysForecast(
        weatherParam: WeatherParam
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _repo.getFiveDaysForecast(weatherParam)
            Log.i("TAG", "getFiveDaysForecast: $result")
            _fiveDaysForecast.postValue(result)
        }
    }

    fun saveCurrentWeatherToRoom(weather: CurrentWeather){
        viewModelScope.launch(Dispatchers.IO) {
             _repo.addWeatherToFavourites(weather)
        }
    }
}
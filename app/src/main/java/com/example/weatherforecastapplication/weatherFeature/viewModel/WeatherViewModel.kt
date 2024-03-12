package com.example.weatherforecastapplication.weatherFeature.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.weatherRepository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val _repo :WeatherRepository) :ViewModel() {

    private var _currentWeather = MutableLiveData<CurrentWeather>()
    var currentWeather:LiveData<CurrentWeather> = _currentWeather
    private var _fiveDaysForecast = MutableLiveData<FiveDaysForecast>()
    var fiveDaysForecast:LiveData<FiveDaysForecast> = _fiveDaysForecast

    fun getCurrentWeather(latitude: Double,
                          longitude: Double,
                          apiKey: String){
        viewModelScope.launch(Dispatchers.IO){
            val result = _repo.getCurrentWeather(latitude, longitude, apiKey)
            Log.i("TAG", "getCurrentWeather: $result")
            _currentWeather.postValue(result)
        }

    }
    fun getFiveDaysForecast(latitude: Double,
                            longitude: Double,
                            apiKey: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _repo.getFiveDaysForecast(latitude, longitude, apiKey)
            Log.i("TAG", "getFiveDaysForecast: $result")
            _fiveDaysForecast.postValue(result)
        }
    }
}
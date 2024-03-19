package com.example.weatherforecastapplication.favouritesFeature.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSource
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.weatherRepository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouritesViewModel(private val _localDataSource:LocalDataSource) :  ViewModel(){
    private var _favourites = MutableStateFlow<ApiState>(ApiState.Loading)
    var favourites  = _favourites.asStateFlow()

    fun getFavWeather(){
        viewModelScope.launch(Dispatchers.IO) {
             _localDataSource.getAllFavouritesWeather().catch {
                 _favourites.emit(ApiState.Failure(it))
             }.collect{
                 _favourites.emit(ApiState.Success(it))
             }

        }
    }
    fun addWeatherToFavourites(weather: CurrentWeather){
        viewModelScope.launch(Dispatchers.IO) {
            _localDataSource.addWeatherToFavourites(weather)
        }
    }

    fun deleteWeatherFromFavourites(weather: CurrentWeather){
        viewModelScope.launch(Dispatchers.IO) {
            _localDataSource.deleteWeatherFromFavourites(weather)
        }
    }

    class Factory(private val _localDataSource: LocalDataSource) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
                FavouritesViewModel(_localDataSource) as T
            } else {
                throw IllegalArgumentException("ViewModel class not found")
            }
        }
    }
}
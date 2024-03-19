package com.example.weatherforecastapplication.shared

import com.example.weatherforecastapplication.model.CurrentWeather

sealed class ApiState {
    class Success(val data:List<CurrentWeather>):ApiState()
    class Failure(val error:Throwable):ApiState()
    object Loading:ApiState()
}
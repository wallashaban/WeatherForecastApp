package com.example.weatherforecastapplication.utils


sealed class ApiState<T> {
    class Success<T>(val data:T):ApiState<T>()
    class Failure<T>(val error:Throwable):ApiState<T>()
    class Loading<T>:ApiState<T>()
}
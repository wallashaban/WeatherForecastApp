package com.example.weatherforecastapplication.shared


sealed class ApiState<T> {
    class Success<T>(val data:T):ApiState<T>()
    class Failure<T>(val error:Throwable):ApiState<T>()
    class Loading<T>:ApiState<T>()
}
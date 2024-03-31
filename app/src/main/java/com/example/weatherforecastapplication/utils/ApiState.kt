package com.example.weatherforecastapplication.utils


sealed class ApiState<T> {
    class Success<T>(val data:T):ApiState<T>()
    class Failure<T>(val error:Throwable):ApiState<T>()
    class Loading<T>:ApiState<T>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApiState<*>) return false

        return when (this) {
            is Success<*> -> other is Success<*> && this.data == other.data
            is Failure<*> -> other is Failure<*> && this.error == other.error
            is Loading -> other is Loading<*>
        }
    }

    override fun hashCode(): Int {
        return when (this) {
            is Success<*> -> data?.hashCode() ?: 0
            is Failure<*> -> error.hashCode()
            is Loading -> javaClass.hashCode()
        }
    }
}
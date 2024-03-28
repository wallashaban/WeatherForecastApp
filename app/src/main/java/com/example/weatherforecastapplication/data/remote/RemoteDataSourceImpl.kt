package com.example.weatherforecastapplication.data.remote

import android.util.Log
import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.utils.BASE_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

object RemoteDataSourceImpl : RemoteDataSource {
    private val weatherServices: WeatherServices =
        RetrofitHelper.retrofit.create(WeatherServices::class.java)



    override suspend fun getCurrentWeather(
        weatherParam: WeatherParam
    ): Flow<CurrentWeather> = flow {
        val currentWeather =   weatherServices.getCurrentWeather(
            weatherParam.latitude,
            weatherParam.longitude,
            weatherParam.apiKey,
            weatherParam.units,
            weatherParam.lang
        )
            .body()!!
        Log.i("TAG", "getCurrentWeather: $currentWeather")
        emit(currentWeather)
    }

    override suspend fun getFiveDaysForecast(weatherParam: WeatherParam)
    : Flow<FiveDaysForecast>
    = flow {
        val fiveDaysWeather =
            weatherServices.getFiveDaysForecast(
                weatherParam.latitude,
                weatherParam.longitude,
                weatherParam.apiKey,
                weatherParam.units,
                weatherParam.lang
            )
                .body()!!
        emit(fiveDaysWeather)
    }


    override suspend fun getAlertForWeather(weatherParam: WeatherParam)
            : Flow<AlertResult>
            = flow {
        val result =
            weatherServices.getAlertForWeather(
               weatherParam.latitude,// 33.44,
                weatherParam.longitude,
               weatherParam.apiKey,
                weatherParam.units,
                weatherParam.lang,
                )
        emit(result.body()!!)
    }

}
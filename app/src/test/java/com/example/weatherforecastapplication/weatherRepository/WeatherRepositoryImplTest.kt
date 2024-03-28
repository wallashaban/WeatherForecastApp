package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.FakeLocalDataSource
import com.example.weatherforecastapplication.FakeRemoteDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
import org.junit.Before


class WeatherRepositoryImplTest{
    private lateinit var fakeLocalDataSource: LocalDataSource
    private lateinit var fakeRemoteDataSource: RemoteDataSource
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp()
    {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = WeatherRepositoryImpl
            .getInstance(fakeRemoteDataSource,fakeLocalDataSource)
    }

}
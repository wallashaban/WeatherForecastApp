package com.example.weatherforecastapplication.favouritesFeature.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.local.WeatherDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class LocalDataSourceImplTest{

    private lateinit var database: WeatherDatabase
    private lateinit var localDataSource: LocalDataSource



    @Before
    fun setUp()
    {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java,
        )
            .build()
        localDataSource = LocalDataSourceImpl
            .getInstance(ApplicationProvider.getApplicationContext())
    }


}
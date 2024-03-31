package com.example.weatherforecastapplication.weatherFeature.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.data.local.FakeLocalDataSource
import com.example.weatherforecastapplication.data.models.City
import com.example.weatherforecastapplication.data.models.Coord
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.FakeRemoteDataSource
import com.example.weatherforecastapplication.data.repo.FakeRepository
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import com.example.weatherforecastapplication.utils.ApiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)

class WeatherViewModelTest{

    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun setUp()
    {
        repository = FakeRepository(FakeLocalDataSource(), FakeRemoteDataSource())
        viewModel = WeatherViewModel(repository)
    }
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Test
    fun getFiveDaysForecast_fiveDaysWeatherObject()= runTest{

        // When the initial state is loading
        var res = viewModel.fiveDaysForecast.first()
        // assert that the state is loading
        assertThat(res,`is`(ApiState.Loading()))

         val fiveDaysForecast =
            FiveDaysForecast(
                "2024/03/28",
                mutableListOf(),
                City("Cairo","EG",
                    Coord(latitude = 29.9792,
                        longitude = 31.1342)
                )
            )
       viewModel.getFiveDaysForecast(
            WeatherParam(0.0,0.0,"","","")
        )
        res = viewModel.fiveDaysForecast.first()

        assertThat(res, `is`(ApiState.Success(fiveDaysForecast)))

          //  res.first {

         //   true
       // }
    }
}
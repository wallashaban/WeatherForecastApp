package com.example.weatherforecastapplication.alertFeature.viewModel

import com.example.weatherforecastapplication.data.local.FakeLocalDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.AlertResult
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.remote.FakeRemoteDataSource
import com.example.weatherforecastapplication.data.repo.FakeRepository
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID


class AlertViewModelTest{
    private lateinit var viewModel: AlertViewModel
    private lateinit var repo : WeatherRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @Before
    fun setUp()
    {
        repo = FakeRepository(FakeLocalDataSource(),FakeRemoteDataSource())
        viewModel = AlertViewModel(repo)
    }

    val alert = AlertRoom(
        id= UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        datetime ="2024/03/28 05:00",
        latitude = 29.9792,
        name = "Cairo",
        longitude = 31.1342
    )
    val alert1 = AlertRoom(
        id= UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"),
        datetime = "2024/03/27 06:00",
        latitude = 29.9792,
        name = "BeniSuef",
        longitude = 31.1342
    )
    @Test
    fun saveAlert_getAlerts() = runBlockingTest{
        //When call add weather
        viewModel.saveAlert(alert)

        //Then ensure getFavWeather
        viewModel.getAlert()
        var res: ApiState<List<AlertRoom>>? = null
        viewModel.alerts.first {alerts->
            res = alerts
            true
        }
        MatcherAssert.assertThat(res, Matchers.`is`(Matchers.not(Matchers.nullValue())))
    }
    @Test
    fun deleteAlert_getAlerts() = runBlockingTest{
        //Given
        viewModel.saveAlert(alert)
        viewModel.saveAlert(alert1)

        //When call delete weather
        viewModel.deleteAlert(alert1)

        //Then ensure getFavWeather
        viewModel.getAlert()
        var res: ApiState<List<AlertRoom>>? = null
        viewModel.alerts.take(1).collect {alerts->
            res = alerts
        }
        MatcherAssert.assertThat(res, Matchers.`is`(Matchers.not(Matchers.nullValue())))
    }
    @Test
    fun getWeatherAlert() = mainCoroutineRule.runBlockingTest{
        //Given
       val weatherParam = WeatherParam(0.0,0.0,"","","")
        //When call add weather
        viewModel.getAlertForWeather(weatherParam = weatherParam)

        var res: ApiState<AlertResult>? = null
        viewModel.weatherAlert .first {alert->
            res = alert
            true
        }
        MatcherAssert.assertThat(res, Matchers.`is`(Matchers.not(Matchers.nullValue())))
    }

}
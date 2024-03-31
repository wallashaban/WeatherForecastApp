package com.example.weatherforecastapplication.weatherRepository

import com.example.weatherforecastapplication.data.local.FakeLocalDataSource
import com.example.weatherforecastapplication.data.remote.FakeRemoteDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.data.remote.RemoteDataSource
import com.example.weatherforecastapplication.data.repo.WeatherRepository
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue

class WeatherRepositoryImplTest {
    private lateinit var fakeLocalDataSource: LocalDataSource
    private lateinit var fakeRemoteDataSource: RemoteDataSource
    private lateinit var repository: WeatherRepository
    private val weather = Favourites(latitude = 29.9792, name = "Cairo", longitude = 31.1342)
    private val weather1 = Favourites(latitude = 32.9792, name = "Beni Suef", longitude = 31.1342)
    val alert = AlertRoom(
        id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        datetime = "2024/03/28 05:00",
        latitude = 29.9792,
        name = "Cairo",
        longitude = 31.1342
    )
    val alert1 = AlertRoom(
        id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"),
        datetime = "2024/03/27 05:00",
        latitude = 29.9792,
        name = "BeniSuef",
        longitude = 31.1342
    )
    private val favourites = mutableListOf<Favourites>(weather, weather1)
    private val alerts = mutableListOf<AlertRoom>(alert, alert1)

    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = WeatherRepositoryImpl
            .getInstance(fakeRemoteDataSource, fakeLocalDataSource)
    }
    @Test
    fun getAlerts() = runTest {
        repository.saveAlert(alert)
        repository.saveAlert(alert1)
        val result = repository.getAlerts()
        result.first {
            assertThat(it, `is`(alerts))
            true
        }

    }
    @Test
    fun getFavouriteWeather() = runTest {
        repository.addWeatherToFavourites(weather)
        repository.addWeatherToFavourites(weather1)
        val result = repository.getAllFavouritesWeather()
        result.first {
            assertThat(it, `is`(favourites))
            true
        }


    }

    @Test
    fun deleteWeatherFromFavourite_getFavWeather() = runTest{
        //Given
        repository.addWeatherToFavourites(weather)
        repository.addWeatherToFavourites(weather1)
        //When
        repository.deleteWeatherFromFavourites(weather1)
        //then
       val res =  repository.getAllFavouritesWeather()

        res.first{favouritesList->
            assertThat(favouritesList,
                `is`( not( nullValue())))
            assertThat(favouritesList[0].longitude,  `is`(weather.longitude))
            assertThat(favouritesList[0].latitude,  `is`(weather.latitude))
            assertThat(favouritesList[0].name,  `is`(weather.name))
            true
        }
    }
    @Test
    fun addWeatherToFavourite_getFavWeather() = runTest{
        repository.addWeatherToFavourites(weather)
        val res =  repository.getAllFavouritesWeather()

        res.first{favouritesList->
            assertThat(favouritesList,
                `is`( not( nullValue())))
            assertThat(favouritesList[0].longitude,  `is`(weather.longitude))
            assertThat(favouritesList[0].latitude,  `is`(weather.latitude))
            assertThat(favouritesList[0].name,  `is`(weather.name))
            true
        }
    }
    @Test
    fun saveAlert_getAlerts() = runTest {
        repository.saveAlert(alert)
        val res = repository.getAlerts()

        res.first { alertsList ->
            assertThat(
                alertsList,
                `is`(not(nullValue()))
            )
            assertThat(alertsList[0].longitude, `is`(alert.longitude))
            assertThat(alertsList[0].latitude, `is`(alert.latitude))
            assertThat(alertsList[0].name, `is`(alert.name))
            assertThat(alertsList[0].datetime, `is`(alert.datetime))
            assertThat(alertsList[0].id, `is`(alert.id))
            true
        }
    }

    @Test
    fun deleteAlert_getAlerts() = runTest {
        repository.saveAlert(alert)
        repository.saveAlert(alert1)
        repository.deleteAlert(alert1)
        val res = repository.getAlerts()

        res.first { alertsList ->
            assertThat(
                alertsList.size,
                `is`(1)
            )
            assertThat(
                alertsList,
                `is`(not(nullValue()))
            )
            assertThat(alertsList[0].longitude, `is`(alert.longitude))
            assertThat(alertsList[0].latitude, `is`(alert.latitude))
            assertThat(alertsList[0].name, `is`(alert.name))
            assertThat(alertsList[0].datetime, `is`(alert.datetime))
            assertThat(alertsList[0].id, `is`(alert.id))
            true
        }
    }

    @Test
    fun getFiveDaysForecast_weatherParam_fiveDaysForecastObject() = runTest {

        val res = repository.getFiveDaysForecast(
            WeatherParam(0.0,0.0,"","",""))
        res.first { weather ->
            assertThat(
                weather,
                `is`(not(nullValue()))
            )
            true
        }
    }

    @Test
    fun getAlertForWeather_weatherParam_alertResult() = runTest {
        val res = repository.getAlertForWeather(
            WeatherParam(0.0,0.0,"","",""))
        res.first { alert ->
            assertThat(
                alert,
                `is`(not(nullValue()))
            )
            true
        }
    }

    @Test
    fun getCurrentWeatherUsingRoom_weatherParam_fiveDaysForecastObject() = runTest {

        val res = repository.getCurrentWeatherFromRoom()
        res.first { weather ->
            assertThat(
                weather,
                `is`(not(nullValue()))
            )
            true
        }
    }

}
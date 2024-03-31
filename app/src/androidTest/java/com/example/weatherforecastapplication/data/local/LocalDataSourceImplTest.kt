package com.example.weatherforecastapplication.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.City
import com.example.weatherforecastapplication.data.models.Coord
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import java.util.UUID


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class LocalDataSourceImplTest{

    private lateinit var database: WeatherDatabase
    private lateinit var localDataSource: LocalDataSource

    private val weather = Favourites(latitude = 29.9792, name = "Cairo", longitude = 31.1342)
    private val weather1 = Favourites(latitude = 32.9792, name = "Beni Suef", longitude = 31.1342)
    @Before
    fun setUp()
    {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java,
        )
            .build()
        localDataSource = LocalDataSourceImpl.getInstance(
            Daos(ApplicationProvider.getApplicationContext(),)
        )
    }
    @Test
    fun addWeatherToFavourites_getFavouriteWeather() = runTest {
        //When
        // Add a weather to favourites
        localDataSource.addWeatherToFavourites(weather)

        //Then
        // get all favourites weather
        localDataSource.getAllFavouritesWeather().first { favouritesList ->
            assertThat(
                favouritesList,
             `is`( not( nullValue()))
            )
              assertThat(favouritesList[0].longitude,  `is`(weather.longitude))
              assertThat(favouritesList[0].latitude,  `is`(weather.latitude))
              assertThat(favouritesList[0].name,  `is`(weather.name))
            true
        }
    }
    @Test
    fun deleteWeatherToFavourites_getFavouriteWeather() = runTest {
        //Given
        // Add a weather to favourites
        localDataSource.addWeatherToFavourites(weather)
        localDataSource.addWeatherToFavourites(weather1)
        //When delete weather
        localDataSource.deleteWeatherFromFavourites(weather1)

        //Then
        // get all favourites weather
        localDataSource.getAllFavouritesWeather().first { favouritesList ->
              assertThat(favouritesList,
                `is`( not( nullValue())))
            assertThat(favouritesList[0].longitude,  `is`(weather.longitude))
            assertThat(favouritesList[0].latitude,  `is`(weather.latitude))
            assertThat(favouritesList[0].name,  `is`(weather.name))

            true
        }
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
    fun saveAlert_getAlerts() = runTest {
        //When
        // Add a weather to favourites
        localDataSource.saveAlert(alert)

        //Then
        // get all favourites weather
        localDataSource.getAlerts().first { alerts ->
            assertThat(
                alerts,
                `is`( not( nullValue()))
            )
            assertThat(alerts[0].longitude,  `is`(alert.longitude))
            assertThat(alerts[0].latitude,  `is`(alert.latitude))
            assertThat(alerts[0].name,  `is`(alert.name))
            assertThat(alerts[0].id,  `is`(alert.id))
            assertThat(alerts[0].datetime,  `is`(alert.datetime))
            true
        }
    }
    @Test
    fun deleteAlert_getAlerts() = runTest {
        //Given
        // Add an alert to favourites
        localDataSource.saveAlert(alert)
        localDataSource.saveAlert(alert1)
        //When delete alert
        localDataSource.deleteAlert(alert1)
        //Then
        // get all favourites weather
        localDataSource.getAlerts().first { alerts ->
            assertThat(
                alerts,
                `is`( not( nullValue()))
            )
            assertThat(alerts[0].longitude,  `is`(alert.longitude))
            assertThat(alerts[0].latitude,  `is`(alert.latitude))
            assertThat(alerts[0].name,  `is`(alert.name))
            assertThat(alerts[0].id,  `is`(alert.id))
            assertThat(alerts[0].datetime,  `is`(alert.datetime))
            true
        }
    }
    fun saveCurrentWeather_getCurrentWeather() = runTest {
        // Given
        // create object from current weather
        val currentWeather =
            FiveDaysForecast(
                "2024/03/28",
                mutableListOf(),
                City("Cairo","EG",
                    Coord(latitude = 29.9792,
                        longitude = 31.1342)
                ))
        //When
        // Add a weather to favourites
        localDataSource.addCurrentWeather(currentWeather)

        //Then
        // get  current weather
        localDataSource.getCurrentWeather().first { current ->
            assertThat(
                current,
                `is`( not( nullValue()))
            )
            assertThat(current.currentDate,  `is`(currentWeather.currentDate))
            assertThat(current.city,  `is`(currentWeather.city))
            assertThat(current.list,  `is`(currentWeather.list))
            true
        }
    }
}
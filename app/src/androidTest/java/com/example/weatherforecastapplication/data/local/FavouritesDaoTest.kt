package com.example.weatherforecastapplication.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecastapplication.data.models.Favourites
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class FavouritesDaoTest {
    private lateinit var database: WeatherDatabase
    private lateinit var favouritesDao: FavouritesDao

    @Before
    fun setUp()
    {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java,
            )
            .build()

        favouritesDao = database.getWeatherFavouritesDao()
    }

    //Given
    // create the object
    private val weather = Favourites(latitude = 29.9792, name = "Cairo", longitude = 31.1342)
    private val weather1 = Favourites(latitude = 32.9792, name = "Beni Suef", longitude = 31.1342)

    @Test
    fun addAndGetFavouriteWeather()= runTest {
        //When
        // Add a weather to favourites
        favouritesDao.addWeatherToFavourites(weather)

        //Then
        // get all favourites weather
        favouritesDao.getAllFavouritesWeather().first { favouritesList ->
            assertThat(favouritesList, `is`(not(nullValue())))
            assertThat(favouritesList[0].longitude, `is`(weather.longitude))
            assertThat(favouritesList[0].latitude, `is`(weather.latitude))
            assertThat(favouritesList[0].name, `is`(weather.name))
            true
        }
    }

    @Test
    fun deleteWeatherFromFavourites() = runTest {
        // Add a weather to favourites
        favouritesDao.addWeatherToFavourites(weather)
        favouritesDao.addWeatherToFavourites(weather1)

        //When
        // Delete the weather from favourites
        favouritesDao.deleteWeatherFromFavourites(weather)

        //Then
        // Ensure that the weather is deleted
        favouritesDao.getAllFavouritesWeather().first { favourites ->
            assertThat(favourites, `is`(not(nullValue())))
            true

        }
    }
    @After
    fun tearDown(){
        database.close()
    }
}
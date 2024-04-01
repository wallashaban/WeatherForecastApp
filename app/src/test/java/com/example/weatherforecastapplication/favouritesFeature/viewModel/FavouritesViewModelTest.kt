package com.example.weatherforecastapplication.favouritesFeature.viewModel

import com.example.weatherforecastapplication.data.local.FakeLocalDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.utils.ApiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Test


class FavouritesViewModelTest{
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var localDataSource :LocalDataSource

    @Before
    fun setUp()
    {
        localDataSource = FakeLocalDataSource()
        viewModel = FavouritesViewModel(localDataSource)
    }

    private val weather = Favourites(latitude = 29.9792, name = "Cairo", longitude = 31.1342)
    private val weather1 = Favourites(latitude = 32.9792, name = "Beni Suef", longitude = 31.1342)

    @Test
    fun addWeatherToFavourites_getFavWeather() = runBlockingTest{
        //When call add weather
        viewModel.addWeatherToFavourites(weather)

        //Then ensure getFavWeather
         viewModel.getFavWeather()
        var res: ApiState<List<Favourites>>? = null
        viewModel.favourites.first {favouritesList->
           res = favouritesList
            true
        }
        assertThat(res,`is`(not(nullValue())))
    }
    @Test
    fun deleteWeatherFromFavourites_getFavWeather() = runBlockingTest{
        //Given
        viewModel.addWeatherToFavourites(weather)
        viewModel.addWeatherToFavourites(weather1)

        //When call delete weather
        viewModel.deleteWeatherFromFavourites(weather1)

        //Then ensure getFavWeather
        viewModel.getFavWeather()
        var res: ApiState<List<Favourites>>? = null
        viewModel.favourites.take(1).collect {favouritesList->
            res = favouritesList
        }
        assertThat(res,`is`(not(nullValue())))
    }
}
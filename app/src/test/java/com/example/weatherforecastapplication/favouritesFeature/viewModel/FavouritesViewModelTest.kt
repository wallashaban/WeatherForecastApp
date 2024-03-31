package com.example.weatherforecastapplication.favouritesFeature.viewModel

import com.example.weatherforecastapplication.data.local.FakeLocalDataSource
import com.example.weatherforecastapplication.data.local.LocalDataSource
import com.example.weatherforecastapplication.data.models.Favourites
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
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
        val result = viewModel.getFavWeather()
        viewModel.favourites.first {
            true
        }
    }
}
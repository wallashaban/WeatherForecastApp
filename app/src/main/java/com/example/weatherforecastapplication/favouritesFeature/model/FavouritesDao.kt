package com.example.weatherforecastapplication.favouritesFeature.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favourites")
     fun getAllFavouritesWeather(): Flow<List<Favourites>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun addWeatherToFavourites(weather: Favourites)
    @Delete
   suspend fun deleteWeatherFromFavourites(weather: Favourites)
}
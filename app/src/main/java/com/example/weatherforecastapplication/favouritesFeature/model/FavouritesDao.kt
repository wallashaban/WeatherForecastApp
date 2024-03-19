package com.example.weatherforecastapplication.favouritesFeature.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.shared.FAVOURITES_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favourites")
     fun getAllFavouritesWeather(): Flow<List<CurrentWeather>>

    @Query("SELECT * FROM favourites WHERE id = (:id)")
   suspend fun getCurrentWeather(id:Int):CurrentWeather
    @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun addWeatherToFavourites(weather: CurrentWeather)
    @Delete
   suspend fun deleteWeatherFromFavourites(weather: CurrentWeather)
}
package com.example.weatherforecastapplication.favouritesFeature.model
import android.content.Context
import com.example.weatherforecastapplication.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(private val context: Context) : LocalDataSource {
    private val favouritesDao: FavouritesDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()
    }

    override fun getAllFavouritesWeather(): Flow<List<CurrentWeather>> {
        return favouritesDao.getAllFavouritesWeather()
    }

    override suspend fun getCurrentWeatherFromRoom(id: Int): CurrentWeather {
        return favouritesDao.getCurrentWeather(id)
    }

    override suspend fun addWeatherToFavourites(weather: CurrentWeather) {
        favouritesDao.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: CurrentWeather) {
        favouritesDao.deleteWeatherFromFavourites(weather)
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSourceImpl? = null
        fun getInstance(ctx: Context): LocalDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDataSourceImpl(ctx)
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.weatherforecastapplication.data.local
import android.content.Context
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(private val context: Context) : LocalDataSource {
    private val favouritesDao: FavouritesDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()
    }
    private val alertsDao: AlertsDao by lazy {
        WeatherDatabase.getInstance(context).getAlertsDao()
    }
    private val weatherDao: WeatherDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherDao()
    }

    override fun getAllFavouritesWeather(): Flow<List<Favourites>> {
        return favouritesDao.getAllFavouritesWeather()
    }

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        favouritesDao.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        favouritesDao.deleteWeatherFromFavourites(weather)
    }

    override fun getAlerts(): Flow<List<AlertRoom>> {
        return  alertsDao.getAlerts()
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        alertsDao.saveAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        alertsDao.deleteAlert(alert)
    }

    override fun getCurrentWeather(): Flow<FiveDaysForecast> {
       return weatherDao.getCurrentWeather()
    }

    override suspend fun deleteCurrentWeather(date:String) {
        return weatherDao.deleteCurrentWeather(date)
    }

    override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
        weatherDao.addCurrentWeather(weather)
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
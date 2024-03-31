package com.example.weatherforecastapplication.data.local
import android.util.Log
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(private val daos: Daos) : LocalDataSource {
  /*  private val favouritesDao: FavouritesDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()
    }
    private val alertsDao: AlertsDao by lazy {
        WeatherDatabase.getInstance(context).getAlertsDao()
    }
    private val weatherDao: WeatherDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherDao()
    }*/

    override fun getAllFavouritesWeather(): Flow<List<Favourites>> {
        return daos.favouritesDao.getAllFavouritesWeather()
    }

    override suspend fun addWeatherToFavourites(weather: Favourites) {
        daos.favouritesDao.addWeatherToFavourites(weather)
    }

    override suspend fun deleteWeatherFromFavourites(weather: Favourites) {
        daos.favouritesDao.deleteWeatherFromFavourites(weather)
    }

    override fun getAlerts(): Flow<List<AlertRoom>> {
        return  daos.alertsDao.getAlerts()
    }

    override suspend fun saveAlert(alert: AlertRoom) {
        daos.alertsDao.saveAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertRoom) {
        daos.alertsDao.deleteAlert(alert)
    }

    override suspend fun deleteAlertByDate(datetime: String) {
        daos.alertsDao.deleteAlertByDate(datetime)
    }

    override fun getCurrentWeather(): Flow<FiveDaysForecast> {
       return daos.weatherDao.getCurrentWeather()
    }

    override suspend fun deleteCurrentWeather(date:String) {
        return daos.weatherDao.deleteCurrentWeather(date)
    }

    override suspend fun addCurrentWeather(weather: FiveDaysForecast) {
        daos.weatherDao.addCurrentWeather(weather)
    }


    companion object {
        @Volatile
        private var INSTANCE: LocalDataSourceImpl? = null
        fun getInstance(daos: Daos): LocalDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDataSourceImpl(daos)
                INSTANCE = instance
                instance
            }
        }
    }
}
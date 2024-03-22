package com.example.weatherforecastapplication.favouritesFeature.model
import android.content.Context
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(private val context: Context) : LocalDataSource {
    private val favouritesDao: FavouritesDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherFavouritesDao()
    }
    private val alertsDao: AlertsDao by lazy {
        WeatherDatabase.getInstance(context).getAlertsDao()
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
        alertsDao.deleteDelete(alert)
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
package com.example.weatherforecastapplication.favouritesFeature.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertRoom>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlert(alert: AlertRoom)
    @Delete
    suspend fun deleteDelete(alert: AlertRoom)
}
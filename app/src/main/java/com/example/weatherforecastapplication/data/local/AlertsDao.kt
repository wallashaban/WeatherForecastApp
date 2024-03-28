package com.example.weatherforecastapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.data.models.AlertRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertRoom>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlert(alert: AlertRoom)
    @Delete
    suspend fun deleteAlert(alert: AlertRoom)


}
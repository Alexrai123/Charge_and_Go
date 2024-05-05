package com.example.licenta_copie.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.licenta_copie.Database.Entity.ChargingStation
import kotlinx.coroutines.flow.Flow

@Dao
interface ChargingStationDao {
    @Query("SELECT * from ChargingStation")
    fun getAllChargingStations(): Flow<List<ChargingStation>>
    @Query("SELECT * from ChargingStation WHERE id = :id")
    fun getChargingStationById(id: Int): Flow<ChargingStation>
    @Query("DELETE FROM ChargingStation WHERE id = :id")
    suspend fun deleteChargingStationById(id: Int)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(chargingStation: ChargingStation)
    @Update
    suspend fun update(chargingStation: ChargingStation)
    @Delete
    suspend fun delete(chargingStation: ChargingStation)
}
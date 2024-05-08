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
    @Query("SELECT * from ChargingStation WHERE name = :name")
    fun getChargingStationById(name: String): Flow<ChargingStation>
    @Query("SELECT * from ChargingStation WHERE name = :name")
    fun getChargingStationByName(name: String): Flow<ChargingStation>
    @Query("SELECT pricePerHour from ChargingStation where name = :name")
    fun getPricePerHourByName(name: String): Flow<Int>
    @Query("SELECT EXISTS(SELECT 1 FROM ChargingStation WHERE name = :stationName)")
    suspend fun existsByName(stationName: String): Boolean
    @Query("DELETE FROM ChargingStation WHERE name = :name")
    suspend fun deleteChargingStationByName(name: String)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(chargingStation: ChargingStation)
    @Update
    suspend fun update(chargingStation: ChargingStation)
    @Delete
    suspend fun delete(chargingStation: ChargingStation)
}
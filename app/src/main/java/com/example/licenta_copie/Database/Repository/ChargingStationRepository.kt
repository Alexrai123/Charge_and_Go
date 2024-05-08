package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.ChargingStation
import kotlinx.coroutines.flow.Flow

interface ChargingStationRepository {
    fun getAllChargingStationsStream(): Flow<List<ChargingStation>>
    fun getChargingStationByName(name: String): Flow<ChargingStation?>
    fun getPricePerHourByName(name: String): Flow<Int>
    suspend fun deleteChargingStationByName(name: String)
    suspend fun insertChargingStation(chargingStation: ChargingStation)
    suspend fun deleteChargingStation(chargingStation: ChargingStation)
    suspend fun updateChargingStation(chargingStation: ChargingStation)
}
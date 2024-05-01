package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.ChargingStation
import kotlinx.coroutines.flow.Flow

interface ChargingStationRepository {
    fun getAllChargingStationsStream(): Flow<List<ChargingStation>>
    fun getChargingStationById(id: Int): Flow<ChargingStation?>
    suspend fun insertChargingStation(chargingStation: ChargingStation)
    suspend fun deleteChargingStation(chargingStation: ChargingStation)
    suspend fun updateChargingStation(chargingStation: ChargingStation)
}
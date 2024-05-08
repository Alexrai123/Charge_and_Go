package com.example.licenta_copie.Database.OfflineRepository

import com.example.licenta_copie.Database.Dao.ChargingStationDao
import com.example.licenta_copie.Database.Entity.ChargingStation
import com.example.licenta_copie.Database.Repository.ChargingStationRepository
import kotlinx.coroutines.flow.Flow

class OfflineChargingStationRepository(private val chargingStationDao: ChargingStationDao) : ChargingStationRepository{
    override fun getAllChargingStationsStream(): Flow<List<ChargingStation>> = chargingStationDao.getAllChargingStations()
    override fun getChargingStationByName(name: String): Flow<ChargingStation?> = chargingStationDao.getChargingStationById(name)
    override fun getPricePerHourByName(name: String): Flow<Int> = chargingStationDao.getPricePerHourByName(name)
    override suspend fun deleteChargingStationByName(name: String) = chargingStationDao.deleteChargingStationByName(name)
    override suspend fun insertChargingStation(chargingStation: ChargingStation) = chargingStationDao.insert(chargingStation)
    override suspend fun deleteChargingStation(chargingStation: ChargingStation) = chargingStationDao.delete(chargingStation)
    override suspend fun updateChargingStation(chargingStation: ChargingStation) = chargingStationDao.update(chargingStation)
}
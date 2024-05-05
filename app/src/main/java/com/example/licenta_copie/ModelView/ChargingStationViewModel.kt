package com.example.licenta_copie.ModelView

import androidx.lifecycle.ViewModel
import com.example.licenta_copie.Database.Entity.ChargingStation
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import kotlinx.coroutines.flow.Flow

class ChargingStationViewModel(val repository: OfflineChargingStationRepository) : ViewModel() {
    val chargingStations: Flow<List<ChargingStation>> = repository.getAllChargingStationsStream()
    suspend fun insertChargingStation(chargingStation: ChargingStation) {
        repository.insertChargingStation(chargingStation)
    }
    suspend fun deleteChargingStation(chargingStation: ChargingStation) {
        repository.deleteChargingStation(chargingStation)
    }
    suspend fun updateCar(chargingStation: ChargingStation) {
        repository.updateChargingStation(chargingStation)
    }
}
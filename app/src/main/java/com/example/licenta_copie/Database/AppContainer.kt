package com.example.licenta_copie.Database

import android.content.Context
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.Database.Repository.CarRepository
import com.example.licenta_copie.Database.Repository.ChargingStationRepository
import com.example.licenta_copie.Database.Repository.ReservationRepository
import com.example.licenta_copie.Database.Repository.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val carRepository: CarRepository
    val reservationRepository: ReservationRepository
    val chargingStationRepository: ChargingStationRepository
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val userRepository : UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getDatabase(context).userDao())
    }
    override val carRepository : CarRepository by lazy {
        OfflineCarRepository(AppDatabase.getDatabase(context).carDao())
    }
    override val chargingStationRepository : ChargingStationRepository by lazy {
        OfflineChargingStationRepository(AppDatabase.getDatabase(context).chargingStationDao())
    }
    override val reservationRepository : ReservationRepository by lazy {
        OfflineReservationRepository(AppDatabase.getDatabase(context).reservationDao())
    }
}
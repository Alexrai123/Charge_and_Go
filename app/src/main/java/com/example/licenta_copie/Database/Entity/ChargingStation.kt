package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "ChargingStation")
data class ChargingStation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng: Double,
    val location: String,
    val chargingPower_kW: Int,
    val nrOfChargingPorts: Int,
    val pricePerHour: Int
)

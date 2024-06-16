package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "ChargingStation")
data class ChargingStation(
    @PrimaryKey
    var name: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var chargingPower_kW: Int = 0,
    var pricePerHour: Int = 0
)

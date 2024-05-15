package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Reservation", foreignKeys = [
    ForeignKey(
        entity = ChargingStation::class,
        parentColumns = ["name"],
        childColumns = ["nameOfChargingStation"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["idOfUser"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
])
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    var idReservation: Int = 0,
    var nameOfChargingStation: String = "",
    var idOfUser: Int = -1,
    var date: String = "",
    var StartChargeTime: String = "",
    var EndChargeTime: String = "",
    var totalCost: Double = 0.0
)

package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "Reservation", foreignKeys = [
    ForeignKey(
        entity = ChargingStation::class,
        parentColumns = ["id"],
        childColumns = ["idOfChargingStation"],
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
    var idOfChargingStation: Int = -1,
    var idOfUser: Int = -1,
    var StartChargeTime: String = "00:00",
    var EndChargeTime: String = "00:00",
    val totalCost: Int = 0
)

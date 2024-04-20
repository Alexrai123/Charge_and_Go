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
    val idReservation: Int = 0,
    val idOfChargingStation: Int,
    val idOfUser: Int,
    val StartChargeTime: String,
    val EndChargeTime: String,
    val totalCost: Int
)

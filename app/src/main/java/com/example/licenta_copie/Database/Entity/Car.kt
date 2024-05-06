package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Car", foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["ownerId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
])
data class Car(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var ownerId: Int = 0,
    var model: String = "",
    var licensePlate: String = "",
    var batteryCapacity: Int = 0
)
package com.example.licenta_copie.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var email: String = "",
    var phoneNumber: String = "",
    var password: String = "",
)

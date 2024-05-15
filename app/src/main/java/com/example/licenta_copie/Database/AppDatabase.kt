package com.example.licenta_copie.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.licenta_copie.Database.Dao.CarDao
import com.example.licenta_copie.Database.Dao.ChargingStationDao
import com.example.licenta_copie.Database.Dao.ReservationDao
import com.example.licenta_copie.Database.Dao.UserDao
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.Entity.ChargingStation
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.Entity.User

@Database(entities = [User::class, Car::class, ChargingStation::class, Reservation::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao
    abstract fun reservationDao(): ReservationDao
    abstract fun chargingStationDao(): ChargingStationDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context, AppDatabase::class.java, "App_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
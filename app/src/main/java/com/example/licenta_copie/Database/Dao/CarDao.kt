package com.example.licenta_copie.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.licenta_copie.Database.Entity.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * from Car")
    fun getAllCars(): Flow<List<Car>>
    @Query("SELECT * from Car WHERE ownerId = :ownerId")
    fun getCarByOwnerId(ownerId: Int): Flow<Car?>
    @Query("SELECT batteryCapacity FROM Car WHERE id = :id")
    fun getBatteryCapacityById(id: Int): Flow<Int>
    @Query("SELECT * from Car WHERE id = :id")
    fun getCarById(id: Int): Flow<Car>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(car: Car)
    @Update
    suspend fun update(car: Car)
    @Delete
    suspend fun delete(car: Car)
}
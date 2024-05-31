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
    @Query("SELECT * from Car WHERE ownerId = :ownerId")
    fun getCarsByOwnerId(ownerId: Int): Flow<List<Car>>
    @Query("SELECT EXISTS(SELECT 1 FROM Car WHERE id = :carId)")
    suspend fun existsById(carId: Int): Boolean
    @Query("SELECT COUNT(*) FROM Car WHERE ownerId = :ownerId")
    fun countCarsByOwnerId(ownerId: Int): Int
    @Query("SELECT batteryCapacity FROM Car WHERE id = :id")
    fun getBatteryCapacityById(id: Int): Flow<Int>
    @Query("SELECT * from Car WHERE id = :id")
    fun getCarById(id: Int): Flow<Car>
    @Query("DELETE FROM Car WHERE id = :id")
    suspend fun deleteCarById(id: Int)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(car: Car)
    @Update
    suspend fun update(car: Car)
    @Delete
    suspend fun delete(car: Car)
}
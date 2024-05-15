package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getAllCarsStream(): Flow<List<Car>>
    fun getCarByOwnerId(ownerId: Int): Flow<Car?>
    fun countCarsByOwnerId(ownerId: Int): Int
    fun getBatteryCapacityById(id: Int): Flow<Int>
    fun getCarById(id: Int): Flow<Car?>
    suspend fun deleteCarById(id: Int)
    suspend fun insertCar(car: Car)
    suspend fun deleteCar(car: Car)
    suspend fun updateCar(car: Car)
}
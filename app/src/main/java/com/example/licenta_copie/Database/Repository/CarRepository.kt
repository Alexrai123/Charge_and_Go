package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getAllCarsStream(): Flow<List<Car>>
    fun getCarByOwnerId(ownerId: Int): Flow<Car?>
    fun getCarsByOwnerId(ownerId: Int): Flow<List<Car>>
    suspend fun existsBylicensePlate(licensePlate: String): Boolean
    suspend fun existsById(carId: Int): Boolean
    fun countCarsByOwnerId(ownerId: Int): Int
    fun getBatteryCapacityById(id: Int): Flow<Int>
    fun getCarById(id: Int): Flow<Car?>
    suspend fun deleteCarById(id: Int)
    suspend fun insertCar(car: Car)
    suspend fun deleteCar(car: Car)
    suspend fun updateCar(car: Car)
}
package com.example.licenta_copie.Database.OfflineRepository

import com.example.licenta_copie.Database.Dao.CarDao
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.Repository.CarRepository
import kotlinx.coroutines.flow.Flow

class OfflineCarRepository(private val carDao: CarDao) : CarRepository {
    override fun getAllCarsStream(): Flow<List<Car>> = carDao.getAllCars()
    override fun getCarByOwnerId(ownerId: Int): Flow<Car?> = carDao.getCarByOwnerId(ownerId)
    override fun getCarsByOwnerId(ownerId: Int): Flow<List<Car>> = carDao.getCarsByOwnerId(ownerId)
    override suspend fun existsById(carId: Int): Boolean = carDao.existsById(carId)
    override suspend fun existsBylicensePlate(licensePlate: String): Boolean = carDao.existsBylicensePlate(licensePlate)
    override suspend fun existsByLicensePlateExcludingId(licensePlate: String, carId: Int): Boolean = carDao.existsByLicensePlateExcludingId(licensePlate, carId)
    override fun countCarsByOwnerId(ownerId: Int): Int = carDao.countCarsByOwnerId(ownerId)
    override fun getBatteryCapacityById(id: Int): Flow<Int> = carDao.getBatteryCapacityById(id)
    override fun getCarById(id: Int): Flow<Car?> = carDao.getCarById(id)
    override fun getCarByIdAndOwnerId(id: Int, ownerId: Int): Flow<Car?> = carDao.getCarByIdAndOwnerId(id, ownerId)
    override suspend fun deleteCarById(id: Int) = carDao.deleteCarById(id)
    override suspend fun insertCar(car: Car) = carDao.insert(car)

    override suspend fun deleteCar(car: Car) = carDao.delete(car)

    override suspend fun updateCar(car: Car) = carDao.update(car)
}
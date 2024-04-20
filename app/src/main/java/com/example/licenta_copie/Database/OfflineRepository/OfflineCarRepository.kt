package com.example.licenta_copie.Database.OfflineRepository

import com.example.licenta_copie.Database.Dao.CarDao
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.Repository.CarRepository
import kotlinx.coroutines.flow.Flow

class OfflineCarRepository(private val carDao: CarDao) : CarRepository {
    override fun getAllCarsStream(): Flow<List<Car>> = carDao.getAllCars()
    override fun getCarStream(id: Int): Flow<Car?> = carDao.getCarById(id)
    override suspend fun insertCar(car: Car) = carDao.insert(car)

    override suspend fun deleteCar(car: Car) = carDao.delete(car)

    override suspend fun updateCar(car: Car) = carDao.update(car)
}
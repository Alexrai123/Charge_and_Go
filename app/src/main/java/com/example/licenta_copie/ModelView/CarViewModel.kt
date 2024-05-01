package com.example.licenta_copie.ModelView

import androidx.lifecycle.ViewModel
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import kotlinx.coroutines.flow.Flow

class CarViewModel(val repository: OfflineCarRepository, filterId: Int) : ViewModel() {
    val cars: Flow<List<Car>> = repository.getAllCarsStream()
    val filterCar: Flow<Car?> = repository.getCarByOwnerId(filterId)
    suspend fun insertCar(car: Car) {
        repository.insertCar(car)
    }
    suspend fun deleteCar(car: Car) {
        repository.deleteCar(car)
    }
    suspend fun updateCar(car: Car) {
        repository.updateCar(car)
    }
}
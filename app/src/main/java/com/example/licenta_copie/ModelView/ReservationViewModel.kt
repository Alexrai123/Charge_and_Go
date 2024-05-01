package com.example.licenta_copie.ModelView

import androidx.lifecycle.ViewModel
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import kotlinx.coroutines.flow.Flow

class ReservationViewModel(val repository: OfflineReservationRepository) : ViewModel() {
    val reservations: Flow<List<Reservation>> = repository.getAllReservationsStream()
    suspend fun insertReservation(reservation: Reservation) {
        repository.insertReservation(reservation)
    }
    suspend fun deleteReservation(reservation: Reservation) {
        repository.deleteReservation(reservation)
    }
    suspend fun updateReservation(reservation: Reservation) {
        repository.updateReservation(reservation)
    }
}

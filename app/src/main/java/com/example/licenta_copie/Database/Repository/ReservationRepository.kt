package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.Reservation
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    fun getAllReservationsStream(): Flow<List<Reservation>>
    fun getReservationsByUserId(id: Int): Flow<List<Reservation>>
    fun getReservationById(id: Int): Flow<Reservation?>
    fun getAllReservationsByData(): Flow<List<Reservation>>
    suspend fun checkForOverlappingReservations(chargingStationName: String, newStartTime: String, newEndTime: String, newDate: String): Int
    suspend fun checkForOverlappingReservations(nameOfChargingStation: String, startChargeTime: String, endChargeTime: String, date: String, currentReservationId: Int): Int
    fun updateReservationDetails(idReservation: Int, newDate: String, newStartChargeTime: String, newEndChargeTime: String, totalCost: String)
    suspend fun deleteReservationById(id: Int)
    suspend fun insertReservation(reservation: Reservation)
    suspend fun deleteReservation(reservation: Reservation)
    suspend fun updateReservation(reservation: Reservation)
}
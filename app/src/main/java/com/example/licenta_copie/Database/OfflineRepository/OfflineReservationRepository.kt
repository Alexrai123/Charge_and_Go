package com.example.licenta_copie.Database.OfflineRepository

import com.example.licenta_copie.Database.Dao.ReservationDao
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.Repository.ReservationRepository
import kotlinx.coroutines.flow.Flow

class OfflineReservationRepository(private val reservationDao: ReservationDao) : ReservationRepository {
    override fun getAllReservationsStream(): Flow<List<Reservation>> = reservationDao.getAllReservations()
    override fun getReservationStream(id: Int): Flow<Reservation?> = reservationDao.getReservationById(id)
    override suspend fun insertReservation(reservation: Reservation) = reservationDao.insert(reservation)
    override suspend fun deleteReservation(reservation: Reservation) = reservationDao.delete(reservation)
    override suspend fun updateReservation(reservation: Reservation) = reservationDao.update(reservation)
}
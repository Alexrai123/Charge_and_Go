package com.example.licenta_copie.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.licenta_copie.Database.Entity.Reservation
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from Reservation ORDER BY idReservation ASC")
    fun getAllReservations(): Flow<List<Reservation>>
    @Query("SELECT * from Reservation WHERE idOfUser = :id")
    fun getReservationsByUserId(id: Int): Flow<List<Reservation>>
    @Query("SELECT * from Reservation WHERE idReservation = :id")
    fun getReservationById(id: Int): Flow<Reservation>
    @Query("SELECT * FROM Reservation ORDER BY date ASC")
    fun getAllReservationsByData(): Flow<List<Reservation>>
    @Query("UPDATE Reservation SET date = :newDate, StartChargeTime = :newStartChargeTime, EndChargeTime = :newEndChargeTime, totalCost = :totalCost WHERE idReservation = :idReservation")
    fun updateReservationDetails(idReservation: Int, newDate: String, newStartChargeTime: String, newEndChargeTime: String, totalCost: String)
    @Query("DELETE FROM Reservation WHERE idReservation = :id")
    suspend fun deleteReservationById(id: Int)
    @Query("""
    SELECT * FROM Reservation 
    WHERE nameOfChargingStation = :nameOfChargingStation 
      AND date = :date 
      AND idReservation != :currentReservationId
      AND ((StartChargeTime < :endChargeTime AND EndChargeTime > :startChargeTime)
      OR (StartChargeTime <= :startChargeTime AND EndChargeTime >= :endChargeTime))""")
    fun getOverlappingReservations(
        nameOfChargingStation: String,
        startChargeTime: String,
        endChargeTime: String,
        date: String,
        currentReservationId: Int
    ): List<Reservation>

    @Query("""
    SELECT COUNT(*) FROM Reservation 
    WHERE nameOfChargingStation = :chargingStationName 
    AND date = :newDate
    AND (
        (StartChargeTime < :newEndTime AND EndChargeTime > :newStartTime) OR 
        (StartChargeTime < :newStartTime AND EndChargeTime > :newEndTime) OR 
        (StartChargeTime > :newStartTime AND EndChargeTime < :newEndTime))""")
    suspend fun checkForOverlappingReservations(chargingStationName: String, newStartTime: String, newEndTime: String, newDate: String): Int
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(reservation: Reservation)
    @Update
    suspend fun update(reservation: Reservation)
    @Delete
    suspend fun delete(reservation: Reservation)
}
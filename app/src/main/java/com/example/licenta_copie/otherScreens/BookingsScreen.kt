package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.ModelView.ReservationViewModel
import com.example.licenta_copie.ModelView.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import kotlin.math.min

//**************************VALIDARI DATE**********************
fun isValidTime(time: String): Boolean {
    return try {
        LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        true
    } catch (e: DateTimeParseException) {
        false
    }
}
fun isValidDate(date: String): Boolean {
    return try {
        val parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val day = parsedDate.dayOfMonth
        val month = parsedDate.monthValue
        val year = parsedDate.year
        day in 1..31 && month in 1..12 && year >= 1900 && year <= LocalDate.now().plusYears(100).year
    } catch (e: DateTimeParseException) {
        false
    }
}

fun isValidTimeInterval(startTime: String, endTime: String): Boolean {
    return try {
        val start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
        val end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
        !start.isAfter(end) && !start.equals(end)
    } catch (e: DateTimeParseException) {
        false
    }
}

fun submitReservation(date: String, startTime: String, endTime: String): String {
    if (!isValidDate(date)) {
        return "Invalid date format. Please use 'dd-MM-yyyy'."
    }
    if (!isValidTime(startTime) || !isValidTime(endTime)) {
        return "Invalid time format. Please use 'HH:mm'."
    }
    if (!isValidTimeInterval(startTime, endTime)) {
        return "End time must be after start time."
    }
    return "Valid"
}
//*************************CALCULAT TIMP SI SUMA INCARCARE************************
fun calculateTimeDifference(startTime: String, endTime: String): Long {
    val start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
    val end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
    return ChronoUnit.MINUTES.between(start, end)
}
fun calculateChargingTime(batteryCapacity: Int, chargingPower: Int): Int {
    return (batteryCapacity / chargingPower) * 60  // Assuming linear charging, result in minutes
}
fun calculateTotalCost(timeReserved: Long, timeToCharge: Int, pricePerHour: Int): Double {
    val hoursReserved = timeReserved / 60.0
    val hoursToCharge = timeToCharge / 60.0
    val billedHours = min(hoursReserved, hoursToCharge)
    return billedHours * pricePerHour
}


@Composable
fun ReservationCard(reservation: Reservation){
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color(0xFF000000)),
        border = BorderStroke(1.dp, Color.Black)
    ){
        Column(modifier = Modifier.padding(5.dp)){
            //id rezervare
            Text(text = "Reservation ID: "+reservation.idReservation.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //id statie incarcare
            Text(text = "Charging Station ID: "+reservation.nameOfChargingStation)
            Spacer(modifier = Modifier.height(5.dp))
            //startCh-endCh
            Text(text = "Time: "+reservation.StartChargeTime+"-"+reservation.EndChargeTime)
            Spacer(modifier = Modifier.height(5.dp))
            //pret
            Text(text = "Price per hour: "+reservation.totalCost.toString()+" lei")
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Bookings(reservationViewModel: ReservationViewModel, showDialog: MutableState<Boolean>, sharedViewModel: SharedViewModel) {
    var nameChargingStation by remember { mutableStateOf("") }
    var idUser by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startChargeTime by remember { mutableStateOf("") }
    var endChargeTime by remember { mutableStateOf("") }
    val newReservation by remember { mutableStateOf(Reservation()) }
    val reservations by reservationViewModel.reservations.collectAsState(initial = emptyList())
    val notification = remember{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    Scaffold(//afiseaza lista doar pt user-ul ala
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 75.dp),
                onClick = { showDialog.value = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Reservation") }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(reservations.filter { reservations ->
                    reservations.idOfUser.toString() == sharedViewModel.user_id.value
                }) { reservation ->
                    ReservationCard(reservation = reservation)
                }
            }
        }
    )
    if(showDialog.value){
        idUser = sharedViewModel.user_id.value.toString()
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        Dialog(onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(455.dp)
                .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Add Reservation",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(//id of charging station
                        value = nameChargingStation,
                        onValueChange = {
                            nameChargingStation = it
                        },
                        label = { Text("Name of Charging Station") }
                    )
                        //data
                    TextField(
                        value = date,
                        onValueChange = {date = it},
                        label = { Text("Date") }
                    )
                    //startTime si endTime
                    TextField(
                        value = startChargeTime,
                        onValueChange = {startChargeTime = it},
                        label = { Text("Start Time") }
                    )
                    TextField(
                        value = endChargeTime,
                        onValueChange = {endChargeTime = it},
                        label = { Text("End Time") }
                    )
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                                nameChargingStation = ""
                                startChargeTime = ""
                                endChargeTime = ""
                                date = ""
                                showDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val exists = chargingStationRepository.existsByName(nameChargingStation)
                                if (exists) {
                                    val chargingStation = chargingStationRepository.getChargingStationByName(nameChargingStation).firstOrNull()
                                    if (chargingStation != null) {
                                        val overlapCount = reservationRepository.checkForOverlappingReservations(
                                            nameChargingStation, startChargeTime, endChargeTime
                                        )
                                        val validationResult = submitReservation(date, startChargeTime, endChargeTime)
                                        if (validationResult == "Valid") {
                                            if (overlapCount == 0) {
                                                val timeDifference = calculateTimeDifference(startChargeTime, endChargeTime)
                                                val car = sharedViewModel.user_id.value?.let { id ->
                                                    carRepository.getCarByOwnerId(id.toInt()).firstOrNull()
                                                }
                                                if (car != null) {
                                                    val chargingTime = calculateChargingTime(car.batteryCapacity, chargingStation.chargingPower_kW)
                                                    val totalCost = calculateTotalCost(timeDifference, chargingTime, chargingStation.pricePerHour)
                                                    newReservation.nameOfChargingStation = nameChargingStation
                                                    newReservation.idOfUser = idUser.toInt()
                                                    newReservation.date = date
                                                    newReservation.StartChargeTime = startChargeTime
                                                    newReservation.EndChargeTime = endChargeTime
                                                    newReservation.totalCost = totalCost.toInt()

                                                    reservationRepository.insertReservation(newReservation)
                                                    withContext(Dispatchers.Main) {
                                                        notification.value = "Reservation created successfully."
                                                        showDialog.value = false
                                                    }
                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        notification.value = "Failed to fetch car details."
                                                    }
                                                }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    notification.value = "Time slot not available. Please choose another time."
                                                }
                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                notification.value = validationResult
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            notification.value = "Charging station does not exist. Please enter a valid station name."
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Charging station does not exist. Please enter a valid station name."
                                    }
                                }
                            }
                        }) {
                            Text("Submit")
                        }
                        if (notification.value.isNotEmpty()) {
                            Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
                            notification.value = ""  // Resetting the notification after showing it
                        }
                    }
                }
            }
        }
    }
}


package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.ModelView.ReservationViewModel
import com.example.licenta_copie.ModelView.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

//id, id statie, *id user, start ch time, end ch time, cost

fun calculateChargingTime(
    batteryCapacity: Int, // Battery capacity in kWh
    initialSOC: Int = 0, // Initial state of charge in percentage
    desiredSOC: Int = 100, // Desired state of charge in percentage
    chargingPower: Double // Charging power in kW
): Double {
    val chargingTimeMinutes = (batteryCapacity * (desiredSOC - initialSOC) / chargingPower) * 60
    return chargingTimeMinutes
}
@Composable
fun ReservationCard(reservation: Reservation){
    Card(modifier = Modifier
        .padding(8.dp)
        .width(200.dp), shape = RoundedCornerShape(8.dp)){
        Column(modifier = Modifier.padding(16.dp)){
            //id rezervare
            Text(text = reservation.idReservation.toString())
            Spacer(modifier = Modifier.height(20.dp))
            //id statie incarcare
            Text(text = reservation.idReservation.toString())
            Spacer(modifier = Modifier.height(20.dp))
            //startCh-endCh
            Text(text = reservation.StartChargeTime+"-"+reservation.EndChargeTime)
            Spacer(modifier = Modifier.height(20.dp))
            //pret(treb calculat)
            Text(text = reservation.totalCost.toString()+"lei")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Bookings(reservationViewModel: ReservationViewModel, showDialog: MutableState<Boolean>, sharedViewModel: SharedViewModel) {
    val reservations by reservationViewModel.reservations.collectAsState(initial = emptyList())
    Scaffold(
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
                items(reservations) { reservation ->
                    ReservationCard(reservation = reservation)
                }
            }
        }
    )
    if(showDialog.value){ //ADAUGA ZIUA LUNA AN
        val newReservation by remember { mutableStateOf(Reservation()) }
        newReservation.idOfUser = sharedViewModel.user_id.value?.toInt() ?: -1
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        var carRepository = OfflineCarRepository(
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
                        value = newReservation.idOfChargingStation.toString(),
                        onValueChange = {
                            newReservation.idOfChargingStation = it.toIntOrNull() ?: 0
                        },
                        label = { Text("ID of Charging Station") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
//                    val idOfCar = sharedViewModel.car_id.value?.toInt() ?: -1
//                    val batteryCapacity: Int = carRepository.getBatteryCapacityById(idOfCar).first()
//                    val time = calculateChargingTime(batteryCapacity, chargingPower = 100.0)
//                    Text(text = "You need at least $time minutes to charge your car from 0% to 100%!")
//                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = newReservation.StartChargeTime,
                        onValueChange = { newReservation.StartChargeTime = it },
                        label = { Text("Start charging time") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = newReservation.EndChargeTime,
                        onValueChange = { newReservation.EndChargeTime = it },
                        label = { Text("End charging time") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
                    var pickedTime by remember { mutableStateOf(LocalTime.now()) }
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                //reservationRepository.insertReservation(newReservation)
                                showDialog.value = false
                            }
                        }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}


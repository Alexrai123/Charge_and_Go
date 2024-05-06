package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.ModelView.ReservationViewModel
import com.example.licenta_copie.ModelView.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            Text(text = "Charging Station ID: "+reservation.idReservation.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //startCh-endCh
            Text(text = "Time: "+reservation.StartChargeTime+"-"+reservation.EndChargeTime)
            Spacer(modifier = Modifier.height(5.dp))
            //pret(treb calculat), vine charge time de mai sus * chargePower din charging station(get charging power from id of charging station)
            Text(text = "Total Price: "+reservation.totalCost.toString()+" lei")
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
    var cost by remember { mutableStateOf("") }
    val newReservation by remember { mutableStateOf(Reservation()) }
    val reservations by reservationViewModel.reservations.collectAsState(initial = emptyList())
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
                    Row {
                        Button(onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    newReservation.nameOfChargingStation = nameChargingStation
                                    newReservation.idOfUser = idUser.toInt()
                                    newReservation.date = date
                                    newReservation.StartChargeTime = startChargeTime
                                    newReservation.EndChargeTime = endChargeTime
                                    newReservation.totalCost = 100
                                    reservationRepository.insertReservation(newReservation)
                                    showDialog.value = false
                                }
                            }
                        ) {
                            Text("Submit")
                        }
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
                    }
                }
            }
        }
    }
}


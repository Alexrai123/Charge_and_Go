package com.example.licenta_copie.Admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.ModelView.ReservationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ReservationCard(reservation: Reservation){
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color(0xFF000000)
        ),
        border = BorderStroke(1.dp, Color.Black)
    ){
        Column(modifier = Modifier.padding(5.dp)){
            //id
            Text(text = "Reservation ID: "+reservation.idReservation.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //nume statie incarcare
            Text(text = "Name of Charging Station: "+reservation.nameOfChargingStation)
            Spacer(modifier = Modifier.height(5.dp))
            //id user
            Text(text = "User ID: "+reservation.idOfUser.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //id car
            Text(text = "Car ID: "+reservation.idOfCar.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //date
            Text(text = "Date: "+reservation.date)
            Spacer(modifier = Modifier.height(5.dp))
            //startCh-endCh
            Text(text = "Time: "+reservation.StartChargeTime+"-"+reservation.EndChargeTime)
            Spacer(modifier = Modifier.height(5.dp))
            //pret
            //Text(text = "Total Price: "+reservation.totalCost+" lei")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reservations(reservationViewModel: ReservationViewModel, goBack:() -> Unit,
                 showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean> ){
    val reservations by reservationViewModel.reservations.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Reservations") },
                actions = {
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete reservation")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = {contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()) {
                //afisezi tabela Reservation
                LazyColumn(modifier = Modifier.padding(8.dp).fillMaxSize()){
                    items(reservations.size){ index ->
                        ReservationCard(reservation = reservations[index])
                    }
                }
            }
        }
    )
    if(showDialogDelete.value){
        var idDelete by remember { mutableStateOf("") }
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        Dialog(onDismissRequest = { showDialogDelete.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = idDelete,
                    onValueChange = { idDelete = it },
                    label = { Text(text = "ID") }
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(onClick = {
                        idDelete = ""
                        showDialogDelete.value = false}) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                reservationRepository.deleteReservationById(idDelete.toInt())
                                showDialogDelete.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Color.Red)){
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
    if(showDialogEdit.value){
        var id by remember { mutableStateOf("") }
        var nameOfChargingStation by remember { mutableStateOf("") }
        var idOfUser by remember { mutableStateOf("") }
        var idOfCar by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var startChargeTime by remember { mutableStateOf("") }
        var endChargeTime by remember { mutableStateOf("") }
        var totalCost by remember { mutableIntStateOf(0) }
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        val reservationEdit by remember { mutableStateOf(Reservation()) }
        LaunchedEffect(id) {
            reservationEdit.nameOfChargingStation = ""
            reservationEdit.idOfUser = 0
            reservationEdit.idOfCar = 0
            reservationEdit.date = ""
            reservationEdit.StartChargeTime = ""
            reservationEdit.EndChargeTime = ""
            reservationEdit.totalCost = 0.0
            if(id.isNotEmpty()){
                delay(500)
                val reservation = reservationRepository.getReservationById(id.toInt()).firstOrNull()
                reservation?.let {
                    reservationEdit.idReservation = it.idReservation
                    reservationEdit.nameOfChargingStation = it.nameOfChargingStation
                    reservationEdit.idOfUser = it.idOfUser
                    reservationEdit.idOfCar = it.idOfCar
                    reservationEdit.date = it.date
                    reservationEdit.StartChargeTime = it.StartChargeTime
                    reservationEdit.EndChargeTime = it.EndChargeTime
                    reservationEdit.totalCost = it.totalCost

                    nameOfChargingStation = it.nameOfChargingStation
                    idOfUser = it.idOfUser.toString()
                    idOfCar = it.idOfCar.toString()
                    date = it.date
                    startChargeTime = it.StartChargeTime
                    endChargeTime = it.EndChargeTime
                    totalCost = it.totalCost.toInt()
                }
                delay(500)
            }
        }
        Dialog(onDismissRequest = { showDialogEdit.value = false },
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
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = "Edit Reservation",
                    )
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("Id") }
                    )
                    TextField(
                        value = nameOfChargingStation,
                        onValueChange = { nameOfChargingStation = it },
                        label = { Text("Name of Charging Station") }
                    )
                    TextField(
                        value = idOfUser,
                        onValueChange = { idOfUser = it },
                        label = { Text("Id of User") }
                    )
                    TextField(
                        value = idOfCar,
                        onValueChange = { idOfCar = it },
                        label = { Text("Id of Car") }
                    )
                    TextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") }
                    )
                    TextField(
                        value = startChargeTime,
                        onValueChange = { startChargeTime = it },
                        label = { Text("Start Charge Time") }
                    )
                    TextField(
                        value = endChargeTime,
                        onValueChange = { endChargeTime = it },
                        label = { Text("End Charge Time") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            reservationEdit.nameOfChargingStation = ""
                            reservationEdit.idOfUser = 0
                            reservationEdit.idOfCar = 0
                            reservationEdit.date = ""
                            reservationEdit.StartChargeTime = ""
                            reservationEdit.EndChargeTime = ""
                            reservationEdit.totalCost = 0.0
                            showDialogEdit.value = false
                        }){
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 95.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    reservationEdit.nameOfChargingStation = nameOfChargingStation
                                    reservationEdit.idOfUser = idOfUser.toInt()
                                    reservationEdit.idOfCar = idOfCar.toInt()
                                    reservationEdit.date = date
                                    reservationEdit.StartChargeTime = startChargeTime
                                    reservationEdit.EndChargeTime = endChargeTime
                                    reservationEdit.totalCost = totalCost.toDouble()
                                    reservationRepository.updateReservation(reservationEdit)
                                    showDialogEdit.value = false
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
}
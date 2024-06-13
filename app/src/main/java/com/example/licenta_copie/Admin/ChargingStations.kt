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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.licenta_copie.Database.Entity.ChargingStation
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.ModelView.ChargingStationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ReservationCard(chargingStation: ChargingStation){
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
            //name
            Text(text = "Name: "+chargingStation.name)
            Spacer(modifier = Modifier.height(5.dp))
            //lat, lng
            Text(text = "Location: "+chargingStation.lat.toString()+", "+chargingStation.lng.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //charging power
            Text(text = "Charging Power (kW): "+chargingStation.chargingPower_kW.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //nr of charging ports
            Text(text = "Number of charging ports: "+chargingStation.nrOfChargingPorts.toString())
            //price per hour
            Text(text = "Price per hour: "+chargingStation.pricePerHour.toString())
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargingStations(chargingStationViewModel: ChargingStationViewModel, goBack:() -> Unit,
                     showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean>, showDialogAdd: MutableState<Boolean>){
    val chargingStations by chargingStationViewModel.chargingStations.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Charging Station") },
                actions = {
                    IconButton(onClick = { showDialogAdd.value = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add charging station")
                    }
                    IconButton(onClick = { showDialogEdit.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit charging station")
                    }
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete charging station ")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                })
        },
        content = {contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()) {
                //afisezi tabela ChargingStation
                LazyColumn(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()){
                    items(chargingStations.size){ index ->
                        ReservationCard(chargingStation = chargingStations[index])
                    }
                }
            }
        }
    )
    if(showDialogDelete.value){
        var nameDelete by remember { mutableStateOf("") }
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
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
                TextField(value = nameDelete,
                    onValueChange = { nameDelete = it },
                    label = { Text(text = "Name") }
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(onClick = {
                        nameDelete = ""
                        showDialogDelete.value = false}) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                chargingStationRepository.deleteChargingStationByName(nameDelete)
                                showDialogDelete.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Color.Red)){
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
    if(showDialogAdd.value){
        var lat by remember { mutableStateOf("") }
        var lng by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var chargingPower_kW by remember { mutableStateOf("") }
        var nrOfChargingPorts by remember { mutableStateOf("") }
        var pricePerHour by remember { mutableStateOf("") }
        val newChargingStation by remember { mutableStateOf(ChargingStation()) }
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        Dialog(onDismissRequest = { showDialogAdd.value = false },
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
                        text = "Add Charging Station",
                    )
                    TextField(
                        value = name,
                        onValueChange = {name = it},
                        label = { Text("Name") }
                    )
                    TextField(
                        value = lat,
                        onValueChange = {lat = it},
                        label = { Text("Latitude") }
                    )
                    TextField(
                        value = lng,
                        onValueChange = {lng = it},
                        label = { Text("Longitude") }
                    )
                    TextField(
                        value = chargingPower_kW,
                        onValueChange = {chargingPower_kW = it},
                        label = { Text("Charging Power (kW)") }
                    )
                    TextField(
                        value = nrOfChargingPorts,
                        onValueChange = {nrOfChargingPorts = it},
                        label = { Text("Number Of Charging Ports") }
                    )
                    TextField(
                        value = pricePerHour,
                        onValueChange = {pricePerHour = it},
                        label = { Text("Price Per Hour") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            lat = ""
                            lng = ""
                            name = ""
                            chargingPower_kW = ""
                            nrOfChargingPorts = ""
                            pricePerHour = ""
                            showDialogAdd.value = false
                        }
                        ) {
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 75.dp),
                            onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                newChargingStation.lat = lat.toDouble()
                                newChargingStation.lng = lng.toDouble()
                                newChargingStation.name = name
                                newChargingStation.chargingPower_kW = chargingPower_kW.toInt()
                                newChargingStation.nrOfChargingPorts = nrOfChargingPorts.toInt()
                                newChargingStation.pricePerHour = pricePerHour.toInt()
                                chargingStationRepository.insertChargingStation(newChargingStation)
                                showDialogAdd.value = false
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
    if(showDialogEdit.value){
        var name by remember { mutableStateOf("") }
        var lat by remember { mutableStateOf("") }
        var lng by remember { mutableStateOf("") }
        var chargingPower_kW by remember { mutableStateOf("") }
        var nrOfChargingPorts by remember { mutableStateOf("") }
        var pricePerHour by remember { mutableStateOf("") }
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        val chargingStationEdit by remember { mutableStateOf(ChargingStation()) }
        LaunchedEffect(name) {
            chargingStationEdit.lat = 0.0
            chargingStationEdit.lng = 0.0
            chargingStationEdit.chargingPower_kW = 0
            chargingStationEdit.nrOfChargingPorts = 0
            chargingStationEdit.pricePerHour = 0
            if(name.isNotEmpty()){
                delay(500)
                val chargingStation = chargingStationRepository.getChargingStationByName(name).firstOrNull()
                chargingStation?.let {
                    chargingStationEdit.name = it.name
                    chargingStationEdit.lat = it.lat
                    chargingStationEdit.lng = it.lng
                    chargingStationEdit.chargingPower_kW = it.chargingPower_kW
                    chargingStationEdit.nrOfChargingPorts = it.nrOfChargingPorts
                    chargingStationEdit.pricePerHour = it.pricePerHour

                    lat = it.lat.toString()
                    lng = it.lng.toString()
                    chargingPower_kW = it.chargingPower_kW.toString()
                    nrOfChargingPorts = it.nrOfChargingPorts.toString()
                    pricePerHour = it.pricePerHour.toString()
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
                        text = "Edit Charging Station",
                    )
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") }
                    )
                    TextField(
                        value = lat,
                        onValueChange = { lat = it },
                        label = { Text("Lat") }
                    )
                    TextField(
                        value = lng,
                        onValueChange = { lng = it },
                        label = { Text("Lng") }
                    )
                    TextField(
                        value = chargingPower_kW,
                        onValueChange = { chargingPower_kW = it },
                        label = { Text("Charging Power (kW)") }
                    )
                    TextField(
                        value = nrOfChargingPorts,
                        onValueChange = { nrOfChargingPorts = it },
                        label = { Text("Number Of Charging Ports") }
                    )
                    TextField(
                        value = pricePerHour,
                        onValueChange = { pricePerHour = it },
                        label = { Text("Price Per Hour") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            chargingStationEdit.lat = 0.0
                            chargingStationEdit.lng = 0.0
                            chargingStationEdit.chargingPower_kW = 0
                            chargingStationEdit.nrOfChargingPorts = 0
                            chargingStationEdit.pricePerHour = 0
                            showDialogEdit.value = false
                        }){
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 95.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    chargingStationEdit.lat = lat.toDouble()
                                    chargingStationEdit.lng = lng.toDouble()
                                    chargingStationEdit.chargingPower_kW = chargingPower_kW.toInt()
                                    chargingStationEdit.nrOfChargingPorts = nrOfChargingPorts.toInt()
                                    chargingStationEdit.pricePerHour = pricePerHour.toInt()
                                    chargingStationRepository.updateChargingStation(chargingStationEdit)
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
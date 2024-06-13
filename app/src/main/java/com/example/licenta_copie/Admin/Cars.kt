package com.example.licenta_copie.Admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.ModelView.CarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun CarCard(car: Car){
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
            Text(text = "ID: "+car.id.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //owner id
            Text(text = "Owner ID: "+car.ownerId.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //license plate
            Text(text = "License Plate: "+car.licensePlate)
            Spacer(modifier = Modifier.height(5.dp))
            //battery capacity
            Text(text = "Battery Capacity (kW): "+car.batteryCapacity.toString())
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cars(carViewModel: CarViewModel, goBack:() -> Unit,
         showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean>){
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    val notification = remember{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Cars") },
                actions = {
                    IconButton(onClick = { showDialogEdit.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit user")
                    }
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete car")
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
                //afisezi tabela Car
                LazyColumn(modifier = Modifier.padding(8.dp).fillMaxSize()){
                    items(cars.size){ index ->
                        CarCard(car = cars[index])
                    }
                }
            }
        }
    )
    if(showDialogDelete.value){
        var idDelete by remember { mutableStateOf("") }
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
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
                                carRepository.deleteCarById(idDelete.toInt())
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
        var ownerId by remember { mutableStateOf("") }
        var model by remember { mutableStateOf("") }
        var licensePlate by remember { mutableStateOf("") }
        var batteryCapacity by remember { mutableStateOf("") }
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        val carEdit by remember { mutableStateOf(Car()) }
        LaunchedEffect(id) {
            carEdit.ownerId = 0
            carEdit.model = ""
            carEdit.licensePlate = ""
            carEdit.batteryCapacity = 0
            if(id.isNotEmpty()){
                delay(500)
                val car = carRepository.getCarById(id.toInt()).firstOrNull()
                car?.let {
                    carEdit.id = it.id
                    carEdit.ownerId = it.ownerId
                    carEdit.model = it.model
                    carEdit.licensePlate = it.licensePlate
                    carEdit.batteryCapacity = it.batteryCapacity

                    id = it.id.toString()
                    ownerId = it.ownerId.toString()
                    model = it.model
                    licensePlate = it.licensePlate
                    batteryCapacity = it.batteryCapacity.toString()
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
                        text = "Edit User",
                    )
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("Id") }
                    )
                    TextField(
                        value = ownerId,
                        onValueChange = { ownerId = it },
                        label = { Text("Owner Id") }
                    )
                    TextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model") }
                    )
                    TextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        label = { Text("License Plate") }
                    )
                    TextField(
                        value = batteryCapacity,
                        onValueChange = { batteryCapacity = it },
                        label = { Text("Battery Capacity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            carEdit.ownerId = 0
                            carEdit.model = ""
                            carEdit.licensePlate = ""
                            carEdit.batteryCapacity = 0
                            showDialogEdit.value = false
                        }){
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 95.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val exists = carRepository.existsBylicensePlate(licensePlate)
                                    if (exists){
                                        notification.value = "License plate already exists!"
                                    }
                                    else {
                                        carEdit.ownerId = ownerId.toInt()
                                        carEdit.model = model
                                        carEdit.licensePlate = licensePlate
                                        carEdit.batteryCapacity = batteryCapacity.toInt()
                                        carRepository.updateCar(carEdit)
                                        showDialogEdit.value = false
                                    }
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
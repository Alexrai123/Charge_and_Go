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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.example.licenta_copie.ModelView.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun CarCard(car: Car) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F8F7),
            contentColor = Color(0xFF000000)
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "Car ID: ${car.id}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Model: ${car.model}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "License Plate: ${car.licensePlate}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Battery Capacity: ${car.batteryCapacity}")
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarsScreen(showDialogAddCar: MutableState<Boolean>, sharedViewModel: SharedViewModel, showDialogEditCar: MutableState<Boolean>,
               carViewModel: CarViewModel, showDialogDeleteCar: MutableState<Boolean>){
    var model by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var batteryCapacity by remember { mutableStateOf("") }
    val newCar by remember { mutableStateOf(Car()) }
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    val carRepository = OfflineCarRepository(
        carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
    )
    val notification = remember{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "") },
                actions = {
                    IconButton(onClick = { showDialogEditCar.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Car")
                    }
                    IconButton(onClick = { showDialogDeleteCar.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Car")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 80.dp),
                onClick = { showDialogAddCar.value = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Car") }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp, top = 60.dp)
            ) {
                items(cars.filter { it.ownerId.toString() == sharedViewModel.user_id.value }) { cars ->
                    CarCard(cars)
                }
            }
        }
    )
    if(showDialogAddCar.value) {
        newCar.ownerId = sharedViewModel.user_id.value?.toInt() ?: 0
        Dialog(onDismissRequest = { showDialogAddCar.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ){
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(455.dp)
                .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Add Car",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(//model
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model of Car") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(//licensePlate
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        label = { Text("License plate number") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(//capacitate baterie
                        value = batteryCapacity,
                        onValueChange = { batteryCapacity = it },
                        label = { Text("Battery Capacity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Row (horizontalArrangement = Arrangement.SpaceEvenly){
                        Button(onClick = {
                            model = ""
                            licensePlate = ""
                            batteryCapacity = ""
                            showDialogAddCar.value = false
                        }
                        ) {
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 65.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val exists = carRepository.existsBylicensePlate(licensePlate)
                                    if (exists){
                                        notification.value = "License plate already exists!"
                                    }
                                    else {
                                        newCar.model = model
                                        newCar.licensePlate = licensePlate
                                        newCar.batteryCapacity = batteryCapacity.toInt()
                                        carRepository.insertCar(newCar)
                                        showDialogAddCar.value = false
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
    if(showDialogEditCar.value){
        var id by remember { mutableStateOf("") }
        var ownerId by remember { mutableStateOf("") }
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
        Dialog(onDismissRequest = { showDialogEditCar.value = false },
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
                        text = "Edit Car",
                    )
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("Id") }
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
                            showDialogEditCar.value = false
                        }){
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 85.dp),
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
                                        showDialogEditCar.value = false
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
    if(showDialogDeleteCar.value){
        var idDelete by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showDialogDeleteCar.value = false },
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
                        showDialogDeleteCar.value = false}) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                carRepository.deleteCarById(idDelete.toInt())
                                showDialogDeleteCar.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Color.Red)){
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}
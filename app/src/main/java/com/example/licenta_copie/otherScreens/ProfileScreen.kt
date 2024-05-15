package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.ModelView.SharedViewModel
import com.example.licenta_copie.ui.theme.focusedTextFieldText
import com.example.licenta_copie.ui.theme.textFieldContainer
import com.example.licenta_copie.ui.theme.unfocusedTextFieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun convertStringToStars(input: String): String {
    val convertedString = StringBuilder()
    for (i in input.indices) {
        convertedString.append("*")
    }
    return convertedString.toString()
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Profile(showDialogAddCar: MutableState<Boolean>, sharedViewModel: SharedViewModel, onLogout: () -> Unit,
            showDialogEditCar: MutableState<Boolean>) {
    //user
    var id by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val showDialogEditPassword = remember { mutableStateOf(false) }
    //car
    var carId by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var batteryCapacity by remember { mutableStateOf("") }
    val newCar by remember { mutableStateOf(Car()) }
    val carRepository = OfflineCarRepository(
        carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
    )
    val notification = remember{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    val userRepository = OfflineUserRepository(
        userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
    )
    email = sharedViewModel.user_email.value.toString()
    password = sharedViewModel.user_password.value.toString()
    LaunchedEffect(email, password) {
        id = ""
        username = ""
        phoneNumber = ""
        carId = ""
        model = ""
        licensePlate = ""
        batteryCapacity = ""
        if(email.isNotEmpty() && password.isNotEmpty()){
            delay(500)
            val user = userRepository.getUserByEmailAndPassword(email, password).firstOrNull()
            user?.let {
                id = it.id.toString()
                username = it.email.substringBefore('@')
                phoneNumber = it.phoneNumber
            }
            delay(500)
            val car = carRepository.getCarByOwnerId(id.toInt()).firstOrNull()
            car?.let {
                carId = it.id.toString()
                model = it.model
                licensePlate = it.licensePlate
                batteryCapacity = it.batteryCapacity.toString()
            }
        }
    }
    Scaffold(
        topBar = {
          TopAppBar(title = { Text(text = "Profile") },
              actions = {
                  IconButton(onClick = { showDialogEditCar.value = true }) {
                      Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Car")
                  }
                  IconButton(onClick = { onLogout() }) {
                      Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                  }
              })
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 75.dp),
                onClick = { showDialogAddCar.value = true },
                content = { Icon(Icons.Default.AddCircle, contentDescription = "Add Car") }
            )
        },
        content = {contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                Text(text = "User Information", modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "ID: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = id,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Email: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = email,
                        onValueChange = {  },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 4.dp, end = 4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(text = "Username: ", modifier = Modifier.width(100.dp))
//                    TextField(
//                        value = username,
//                        onValueChange = { },
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
//                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
//                        )
//                    )
//                }
//                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Phone Number: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = phoneNumber,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Password: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = convertStringToStars(password),
                        onValueChange = {  },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        ),
                        trailingIcon = {
                            IconButton(onClick = { showDialogEditPassword.value = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Password")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Car Information", modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Car ID: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = carId,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Model: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = model,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "License Plate: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = licensePlate,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Battery Capacity: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = batteryCapacity,
                        onValueChange = { },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
            }
        }
    )
    if(showDialogAddCar.value) {
        newCar.ownerId = id.toInt()
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
                            CoroutineScope(Dispatchers.IO).launch {
                                if (carRepository.countCarsByOwnerId(newCar.ownerId) == 0){
                                    newCar.model = model
                                    newCar.licensePlate = licensePlate
                                    newCar.batteryCapacity = batteryCapacity.toInt()
                                    carRepository.insertCar(newCar)
                                    showDialogAddCar.value = false
                                }
                                else notification.value = "Each user can own only 1 car!"
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
    sharedViewModel.car_id.value = carId
    sharedViewModel.user_id.value = id
    if(showDialogEditCar.value){
        var idCar by remember { mutableStateOf("") }
        var ownerIdCar by remember { mutableStateOf("") }
        var modelCar by remember { mutableStateOf("") }
        var licensePlateCar by remember { mutableStateOf("") }
        var batteryCapacityCar by remember { mutableStateOf("") }
        val carRepositoryCar = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        val carEdit by remember { mutableStateOf(Car()) }
        idCar = sharedViewModel.car_id.value.toString()
        ownerIdCar = sharedViewModel.user_id.value.toString()
        LaunchedEffect(idCar, ownerIdCar) {
            carEdit.model = ""
            carEdit.licensePlate = ""
            carEdit.batteryCapacity = 0
            if(idCar.isNotEmpty()){
                delay(500)
                val car = carRepositoryCar.getCarById(idCar.toInt()).firstOrNull()
                car?.let {
                    carEdit.id = it.id
                    carEdit.ownerId = it.ownerId
                    carEdit.model = it.model
                    carEdit.licensePlate = it.licensePlate
                    carEdit.batteryCapacity = it.batteryCapacity

                    idCar = it.id.toString()
                    ownerIdCar = it.ownerId.toString()
                    modelCar = it.model
                    licensePlateCar = it.licensePlate
                    batteryCapacityCar = it.batteryCapacity.toString()
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
                        value = idCar,
                        onValueChange = { },
                        label = { Text("Id") }
                    )
                    TextField(
                        value = ownerIdCar,
                        onValueChange = { },
                        label = { Text("Owner Id") }
                    )
                    TextField(
                        value = modelCar,
                        onValueChange = { modelCar = it },
                        label = { Text("Model") }
                    )
                    TextField(
                        value = licensePlateCar,
                        onValueChange = { licensePlateCar = it },
                        label = { Text("License Plate") }
                    )
                    TextField(
                        value = batteryCapacityCar,
                        onValueChange = { batteryCapacityCar = it },
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
                        Button(modifier = Modifier.padding(start = 95.dp),
                            onClick = {
                                val batteryCapacityCheck = batteryCapacityCar.toDoubleOrNull()
                                if (batteryCapacityCheck != null && batteryCapacityCheck == batteryCapacityCheck.toInt().toDouble()) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        carEdit.ownerId = ownerIdCar.toInt()
                                        carEdit.model = modelCar
                                        carEdit.licensePlate = licensePlateCar
                                        carEdit.batteryCapacity = batteryCapacityCheck.toInt()
                                        carRepositoryCar.updateCar(carEdit)
                                        showDialogEditCar.value = false
                                    }
                                } else {
                                    notification.value = "Battery Capacity must be an integer"
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
    if(showDialogEditPassword.value){
        var newPassword by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showDialogEditPassword.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(text = "New Password") }
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(
                        onClick = {
                        newPassword = ""
                        showDialogEditPassword.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                sharedViewModel.user_password.value = newPassword
                                userRepository.updatePassword(email, phoneNumber, newPassword)
                                withContext(Dispatchers.Main) {
                                    notification.value = "Password updated successfully"
                                    showDialogEditPassword.value = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)){
                        Text(text = "Change Password")
                    }
                }
            }
        }
    }
}
package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Username: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = username,
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
                        )
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
                        label = { Text("Battery Capacity") }
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
                                newCar.model = model
                                newCar.licensePlate = licensePlate
                                newCar.batteryCapacity = batteryCapacity.toInt()
                                carRepository.insertCar(newCar)
                                showDialogAddCar.value = false
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
//    if(showDialogEditProfile.value){
//        var idUser by remember { mutableStateOf("") }
//        var emailUser by remember { mutableStateOf("") }
//        var phoneNumberUser by remember { mutableStateOf("") }
//        var passwordUser by remember { mutableStateOf("") }
//        val userRepositoryUser = OfflineUserRepository(
//            userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
//        )
//        val userEdit by remember { mutableStateOf(User()) }
//        idUser = sharedViewModel.user_id.value.toString()
//        LaunchedEffect(idUser) {
//            userEdit.email = ""
//            userEdit.phoneNumber = ""
//            userEdit.password = ""
//            if(idUser.isNotEmpty()){
//                delay(500)
//                val user = userRepositoryUser.getUserById(idUser.toInt()).firstOrNull()
//                user?.let {
//                    user.id = it.id
//                    user.email = it.email
//                    user.phoneNumber = it.phoneNumber
//                    user.password = it.password
//
//                    idUser = it.id.toString()
//                    emailUser = it.email
//                    phoneNumberUser = it.phoneNumber
//                    passwordUser = it.password
//                }
//                delay(500)
//            }
//        }
//        Dialog(onDismissRequest = { showDialogEditCar.value = false },
//            properties = DialogProperties(
//                dismissOnClickOutside = false,
//                dismissOnBackPress = false
//            )) {
//            Card(modifier = Modifier
//                .fillMaxWidth()
//                .height(455.dp)
//                .padding(16.dp),
//                shape = RoundedCornerShape(16.dp),
//            ) {
//                Column(modifier = Modifier.padding(5.dp)) {
//                    Text(
//                        text = "Edit Profile",
//                    )
//                    TextField(
//                        value = idUser,
//                        onValueChange = { /*idUser = it*/ },
//                        label = { Text("Id") }
//                    )
//                    TextField(
//                        value = emailUser,
//                        onValueChange = { emailUser = it },
//                        label = { Text("Email") }
//                    )
//                    TextField(
//                        value = phoneNumberUser,
//                        onValueChange = { phoneNumberUser = it },
//                        label = { Text("Phone Number") }
//                    )
//                    TextField(
//                        value = passwordUser,
//                        onValueChange = { passwordUser = it },
//                        label = { Text("Password") }
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
//                        Button(onClick = {
//                            userEdit.email = ""
//                            userEdit.phoneNumber = ""
//                            userEdit.password = ""
//                            showDialogEditProfile.value = false
//                        }){
//                            Text("Cancel")
//                        }
//                        Button(modifier = Modifier.padding(start = 95.dp),
//                            onClick = {
//                                CoroutineScope(Dispatchers.Main).launch {
//                                    userEdit.email = emailUser
//                                    userEdit.phoneNumber = phoneNumberUser
//                                    userEdit.password = passwordUser
//                                    userRepositoryUser.updateUser(userEdit)
//                                    showDialogEditProfile.value = false
//                                }
//                            }
//                        ) {
//                            Text("Submit")
//                        }
//                    }
//                }
//            }
//        }
//    }
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
            //carEdit.ownerId = 0
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
                        onValueChange = { /*id = it*/ },
                        label = { Text("Id") }
                    )
                    TextField(
                        value = ownerIdCar,
                        onValueChange = { /*ownerId = it*/ },
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
                        label = { Text("Battery Capacity") }
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
                                CoroutineScope(Dispatchers.Main).launch {
                                    carEdit.ownerId = ownerIdCar.toInt()
                                    carEdit.model = modelCar
                                    carEdit.licensePlate = licensePlateCar
                                    carEdit.batteryCapacity = batteryCapacityCar.toInt()
                                    carRepository.updateCar(carEdit)
                                    showDialogEditCar.value = false
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
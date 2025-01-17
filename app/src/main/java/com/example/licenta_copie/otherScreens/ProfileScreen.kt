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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

fun validateEmail(email: String): Boolean {
    val emailRegex = "^[\\w]{1,40}@(gmail\\.com|yahoo\\.com|student\\.usv\\.ro|hotmail\\.com|outlook\\.com)$".toRegex()
    return email.matches(emailRegex)
}
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
fun Profile(sharedViewModel: SharedViewModel, onLogout: () -> Unit) {
    //user
    var id by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val showDialogEditEmail = remember { mutableStateOf(false) }
    val showDialogEditPhoneNumber = remember { mutableStateOf(false) }
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
          TopAppBar(title = { Text(text = "") },
              actions = {
                  IconButton(onClick = { onLogout() }) {
                      Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                  }
              })
        },
        content = {contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                Spacer(modifier = Modifier.height(85.dp))
                Text(text = "User Information", modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                Spacer(modifier = Modifier.height(95.dp))
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
                        ),
                        trailingIcon = {
                            IconButton(onClick = { showDialogEditEmail.value = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Email")
                            }
                        }
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
                        ),
                        trailingIcon = {
                            IconButton(onClick = { showDialogEditPhoneNumber.value = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Phone Number")
                            }
                        }
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
            }
        }
    )
    sharedViewModel.user_id.value = id
    if(showDialogEditEmail.value){
        var newEmail by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showDialogEditEmail.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text(text = "New Email") }
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(
                        onClick = {
                            newEmail = ""
                            showDialogEditEmail.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if(validateEmail(newEmail) == true){
                                CoroutineScope(Dispatchers.Main).launch {
                                    sharedViewModel.user_email.value = newEmail
                                    userRepository.updateEmail(newEmail, phoneNumber, password)
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Email updated successfully"
                                        showDialogEditEmail.value = false
                                    }
                                }
                            } else {
                                CoroutineScope(Dispatchers.IO).launch {
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Invalid email format"
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)){
                        Text(text = "Change Email")
                    }
                }
            }
        }
    }
    if(showDialogEditPhoneNumber.value){
        var newPhoneNumber by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showDialogEditPhoneNumber.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = newPhoneNumber,
                    onValueChange = { newPhoneNumber = it },
                    label = { Text(text = "New Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(
                        onClick = {
                            newPhoneNumber = ""
                            showDialogEditPhoneNumber.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                phoneNumber = newPhoneNumber
                                userRepository.updatePhoneNumber(email, newPhoneNumber, password)
                                withContext(Dispatchers.Main) {
                                    notification.value = "Phone number updated successfully"
                                    showDialogEditPhoneNumber.value = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)){
                        Text(text = "Change Phone Number")
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
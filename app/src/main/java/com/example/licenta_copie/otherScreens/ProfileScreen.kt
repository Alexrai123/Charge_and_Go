package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.R
import com.example.licenta_copie.ui.theme.focusedTextFieldText
import com.example.licenta_copie.ui.theme.textFieldContainer
import com.example.licenta_copie.ui.theme.unfocusedTextFieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

//poza profil,schimba parola, adauga masina
fun convertString(text: String): String {
    val alphabet = ('a'..'z').toList()
    val convertedChars = text.map { char ->
        when {
            char.isLowerCase() -> {
                val index = alphabet.indexOf(char)
                if (index != -1) {
                    (index + 1).toString() // Convert letter to its position (1-based)
                } else {
                    char.toString() // Keep other characters unchanged
                }
            }
            char.isDigit() -> {
                val digit = char.digitToInt() - 1 // Convert digit to 0-based index
                if (digit in 0..25) {
                    alphabet[digit].toString() // Convert position to letter (a-z)
                } else {
                    char.toString() // Keep invalid digits unchanged
                }
            }
            else -> char.toString() // Keep other characters unchanged
        }
    }
    return convertedChars.joinToString("")
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Profile(showDialog: MutableState<Boolean>) {
    var id by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var notification = remember{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    val userRepository = OfflineUserRepository(
        userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
    )
    val carRepository = OfflineCarRepository(
        carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
    )
    LaunchedEffect(email) {
        if(email.isNotEmpty()){
            val user = userRepository.getUserByEmail(email).firstOrNull()
            user?.let {
                id = it.id.toString()
                username = it.email.substringBefore('@')
                phoneNumber = it.phoneNumber
                password = it.password
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 75.dp),
                onClick = { showDialog.value = true },
                content = { Icon(Icons.Default.AddCircle, contentDescription = "Add Car") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
            ProfileImage()
            Column {
                Spacer(modifier = Modifier.height(15.dp))
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
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Email: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                            unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
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
                Spacer(modifier = Modifier.height(25.dp))
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
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Password: ", modifier = Modifier.width(100.dp))
                    TextField(
                        value = convertString(password),
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
    }
    if(showDialog.value) {
        val newCar by remember { mutableStateOf(Car()) }
        var carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        Dialog(onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ){
            Card(modifier = Modifier.fillMaxWidth().height(455.dp).padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Add Car",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(//model
                        value = newCar.model,
                        onValueChange = {
                            newCar.model = it
                        },
                        label = { Text("Model of Car") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(//licensePlate
                        value = newCar.licensePlate,
                        onValueChange = {
                            newCar.licensePlate = it
                        },
                        label = { Text("ID of Car") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(//capacitate baterie
                        value = newCar.batteryCapacity.toString(),
                        onValueChange = { newCar.batteryCapacity = it.toIntOrNull() ?:0 },
                        label = { Text("Battery Capacity") }
                    )
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                //if (/*verifici daca toate tf nu is goale*/) {
                                //seteaza ownerId din profil, afiseaza si id
                                //carRepository.insertCar(newCar)
                                showDialog.value = false
                                //}
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
@Composable
fun ProfileImage(){
    val imageUrl = rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? -> uri?.let { imageUrl.value = it.toString() }
    }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(shape = CircleShape, modifier = Modifier
            .padding(8.dp)
            .size(100.dp)){
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
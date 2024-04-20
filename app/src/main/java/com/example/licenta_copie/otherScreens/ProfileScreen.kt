package com.example.licenta_copie.otherScreens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.licenta_copie.R
import com.example.licenta_copie.ui.theme.focusedTextFieldText
import com.example.licenta_copie.ui.theme.textFieldContainer
import com.example.licenta_copie.ui.theme.unfocusedTextFieldText

//poza profil,ID,email,nume,nr telefon,schimba parola
@Composable
fun Profile() {
    var ID by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val notification = rememberSaveable{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
        }
        ProfileImage()
        Column {
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "ID: ", modifier = Modifier.width(100.dp))
                TextField(
                    value = ID,
                    onValueChange = { /*ID = it*/ },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                        unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
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
            ){
                Text(text = "Name: ", modifier = Modifier.width(100.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
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
            ){
                Text(text = "Phone Number: ", modifier = Modifier.width(100.dp))
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
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
            ){
                Text(text = "Password: ", modifier = Modifier.width(100.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
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
@Composable
@Preview
fun prv(){
    Profile()
}
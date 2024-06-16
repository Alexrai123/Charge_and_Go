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
import com.example.licenta_copie.Database.Entity.User
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.ModelView.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun UserCard(user: User){
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
            Text(text = "ID: "+user.id.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //email
            Text(text = "Email: "+user.email)
            Spacer(modifier = Modifier.height(5.dp))
            //phone number
            Text(text = "Phone Number: "+user.phoneNumber)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Users(userViewModel: UserViewModel, goBack:() -> Unit,
          showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean>){
    val users by userViewModel.users.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Users") },
                actions = {
                    IconButton(onClick = { showDialogEdit.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit user")
                    }
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete user")
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
                //afisezi tabela User
                LazyColumn(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()){
                    items(users.size){ index ->
                        UserCard(user = users[index])
                    }
                }
            }
        }
    )
    if(showDialogDelete.value){
        var idDelete by remember { mutableStateOf("") }
        val userRepository = OfflineUserRepository(
            userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
        )
        Dialog(onDismissRequest = { showDialogDelete.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )){
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
                                userRepository.deleteUserById(idDelete.toInt())
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
        var email by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val userRepository = OfflineUserRepository(
            userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
        )
        val userEdit by remember { mutableStateOf(User()) }
        LaunchedEffect(id) {
            userEdit.email = ""
            userEdit.phoneNumber = ""
            userEdit.password = ""
            if(id.isNotEmpty()){
                delay(500)
                val user = userRepository.getUserById(id.toInt()).firstOrNull()
                user?.let {
                    userEdit.id = it.id
                    userEdit.email = it.email
                    userEdit.phoneNumber = it.phoneNumber
                    userEdit.password = it.password

                    email = it.email
                    phoneNumber = it.phoneNumber
                    password = it.password
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
                .height(305.dp)
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
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") }
                    )
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            userEdit.email = ""
                            userEdit.phoneNumber = ""
                            showDialogEdit.value = false
                        }){
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.padding(start = 75.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    userEdit.email = email
                                    userEdit.phoneNumber = phoneNumber
                                    userEdit.password = password
                                    userRepository.updateUser(userEdit)
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
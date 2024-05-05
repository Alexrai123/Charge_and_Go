package com.example.licenta_copie.Admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

fun adminAdd(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPage(onLogout:() -> Unit,
              onUser:() -> Unit,
              onCar:() -> Unit,
              onChargingStation:() -> Unit,
              onReservation:() -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Admin") },
                actions = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                },
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { onUser() }) {
                    Text("Users")
                }
                Button(onClick = { onCar() }) {
                    Text("Cars")
                }
                Button(onClick = { onChargingStation() }) {
                    Text("Charging Stations")
                }
                Button(onClick = { onReservation() }) {
                    Text("Reservations")
                }
            }
        }
    )
}
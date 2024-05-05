package com.example.licenta_copie.Admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.licenta_copie.Database.Entity.ChargingStation

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
            //id
            Text(text = "ID: "+chargingStation.id.toString())
            Spacer(modifier = Modifier.height(5.dp))
            //lat, lng
            Text(text = "Location: "+chargingStation.lat.toString()+", "+chargingStation.lng.toString())
            Text(text = "Address: "+chargingStation.location)
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
fun ChargingStations(goBack:() -> Unit){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Charging Station") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Edit profile")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit profile")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Edit profile")
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
            }
        }
    )
}
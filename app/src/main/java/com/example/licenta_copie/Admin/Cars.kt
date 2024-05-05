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
import com.example.licenta_copie.Database.Entity.Car

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
fun Cars(goBack:() -> Unit){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Cars") },
                actions = {
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
                }
            )
        },
        content = {contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()) {
                //afisezi tabela Car
            }
        }
    )
}
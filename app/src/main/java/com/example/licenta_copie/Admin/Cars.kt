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
import com.example.licenta_copie.Database.Entity.Car
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.ModelView.CarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
fun Cars(carViewModel: CarViewModel, goBack:() -> Unit,
         showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean>){
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Cars") },
                actions = {
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete car")
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
                LazyColumn(modifier = Modifier.padding(8.dp).fillMaxSize()){
                    items(cars.size){ index ->
                        CarCard(car = cars[index])
                    }
                }
            }
        }
    )
    if(showDialogDelete.value){
        var idDelete by remember { mutableStateOf("") }
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        Dialog(onDismissRequest = { showDialogDelete.value = false },
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
                        showDialogDelete.value = false}) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                carRepository.deleteCarById(idDelete.toInt())
                                showDialogDelete.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Color.Red)){
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}
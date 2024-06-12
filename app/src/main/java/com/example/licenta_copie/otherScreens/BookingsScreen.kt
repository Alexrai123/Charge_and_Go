package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.Reservation
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.ModelView.ReservationViewModel
import com.example.licenta_copie.ModelView.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

//**************************VALIDARI DATE**********************
fun isValidTime(time: String): Boolean {
    return try {
        LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        true
    } catch (e: DateTimeParseException) {
        false
    }
}
fun isValidDate(date: String): Boolean {
    return try {
        val parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val today = LocalDate.now()
        val oneYearLater = today.plusYears(1)
        parsedDate.isAfter(today.minusDays(1)) && parsedDate.isBefore(oneYearLater.plusDays(1))
    } catch (e: DateTimeParseException) {
        false
    }
}
fun isReservationCurrent(reservationDate: String, reservationTime: String): Boolean {
    return try {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()

        val formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val parsedDate = LocalDate.parse(reservationDate, formatterDate)

        if (parsedDate.isBefore(currentDate)) {
            return false // Reservation date is in the past
        } else if (parsedDate.isEqual(currentDate)) {
            // If the reservation date is today
            val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
            val parsedTime = LocalTime.parse(reservationTime, formatterTime)
            return !parsedTime.isBefore(currentTime.plusMinutes(1))
        }
        true
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        false
    }
}
const val MAX_HOURS_ALLOWED = 2
fun isValidTimeInterval(startTime: String, endTime: String, maxHours: Int = MAX_HOURS_ALLOWED): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(startTime, formatter)
        val end = LocalTime.parse(endTime, formatter)

        if (start.isAfter(end) || start == end) {
            return false
        }

        val duration = Duration.between(start, end)
        val hoursBetween = duration.toHours()

        hoursBetween <= maxHours
    } catch (e: DateTimeParseException) {
        false
    }
}
fun submitReservation(date: String, startTime: String, endTime: String): String {
    if (!isValidDate(date)) {
        return "Please use 'dd-MM-yyyy' or choose a valid date."
    }
    if (!isValidTime(startTime) || !isValidTime(endTime)) {
        return "Invalid time format. Please use 'HH:mm'."
    }
    if (!isValidTimeInterval(startTime, endTime)) {
        return "End time must be after start time and within the allowed duration (2 hours)."
    }
    if (!isReservationCurrent(date, startTime)) {
        return "Reservation time must be at least 1 minute in the future."
    }
    return "Valid"
}
fun isFutureReservation(date: String, startTime: String): Boolean {
    return try {
        val formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")

        val reservationDate = LocalDate.parse(date, formatterDate)
        val reservationStartTime = LocalTime.parse(startTime, formatterTime)

        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()

        if (reservationDate.isAfter(currentDate)) {
            true
        } else if (reservationDate.isEqual(currentDate)) {
            reservationStartTime.isAfter(currentTime)
        } else {
            false
        }
    } catch (e: DateTimeParseException) {
        false
    }
}
fun calculateChargingCost(
    startTime: String,
    endTime: String,
    chargingPower: Int, // kW
    pricePerHour: Int // Currency units per hour
): Double {
    return try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(startTime, formatter)
        val end = LocalTime.parse(endTime, formatter)

        val duration = Duration.between(start, end)
        val chargingDurationHours = duration.toMinutes() / 60.0

        // Calculate the total cost based on charging duration
        val totalCost = chargingDurationHours * pricePerHour
        totalCost
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        0.0
    }
}

@Composable
fun ReservationCard(reservation: Reservation) {
    val cardColor = if (isFutureReservation(reservation.date, reservation.StartChargeTime)) {
        Color(0xFFD0F0C0) // Light green
    } else {
        Color(0xFFFFC0C0) // Light red
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color(0xFF000000)
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "Reservation ID: ${reservation.idReservation}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Charging Station: ${reservation.nameOfChargingStation}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "ID Of Car: ${reservation.idOfCar}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Date: ${reservation.date}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Time: ${reservation.StartChargeTime} - ${reservation.EndChargeTime}")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Total price: ${reservation.totalCost} lei")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Bookings(reservationViewModel: ReservationViewModel, showDialog: MutableState<Boolean>,
             sharedViewModel: SharedViewModel, showDialogDelete: MutableState<Boolean>, showDialogEdit: MutableState<Boolean>) {
    var nameChargingStation by remember { mutableStateOf("") }
    var idUser by remember { mutableStateOf("") }
    var idCar by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startChargeTime by remember { mutableStateOf("") }
    var endChargeTime by remember { mutableStateOf("") }
    var currentBatteryLevel by remember { mutableStateOf("") }
    var desiredBatteryLevel by remember { mutableStateOf("") }
    val newReservation by remember { mutableStateOf(Reservation()) }
    val reservations by reservationViewModel.reservations.collectAsState(initial = emptyList())
    val notification = remember{ mutableStateOf("") }
    var pricePerHour by remember { mutableIntStateOf(0) }
    var totalCost by remember { mutableDoubleStateOf(0.0)}
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 80.dp),
                onClick = { showDialog.value = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Reservation") }
            )
        },
        topBar = {
            TopAppBar(title = { Text(text = "") },
                actions = {
                    IconButton(onClick = { showDialogEdit.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit reservation")
                    }
                    IconButton(onClick = { showDialogDelete.value = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete reservation")
                    }
                }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp, top = 60.dp)
            ) {
                items(reservations.filter { it.idOfUser.toString() == sharedViewModel.user_id.value }) { reservation ->
                    val chargingStationRepository = OfflineChargingStationRepository(
                        chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
                    )
                    LaunchedEffect(reservation.nameOfChargingStation) {
                        val chargingStation = chargingStationRepository.getChargingStationByName(reservation.nameOfChargingStation).firstOrNull()
                        pricePerHour = chargingStation?.pricePerHour ?: 0
                    }
                    ReservationCard(reservation = reservation)
                }
            }
        }
    )
    if(showDialog.value){
        idUser = sharedViewModel.user_id.value.toString()
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        Dialog(onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = "Add reservation",
                    )
                    TextField(//id of charging station
                        value = nameChargingStation,
                        onValueChange = {
                            nameChargingStation = it
                        },
                        label = { Text("Name of Charging Station") }
                    )
                    //id car
                    TextField(
                        value = idCar,
                        onValueChange = { idCar = it },
                        label = { Text("ID Of Car") }
                    )
                    //data
                    TextField(
                        value = date,
                        onValueChange = {date = it},
                        label = { Text("Date") }
                    )
                    //startTime si endTime
                    TextField(
                        value = startChargeTime,
                        onValueChange = {startChargeTime = it},
                        label = { Text("Start Time") }
                    )
                    TextField(
                        value = endChargeTime,
                        onValueChange = {endChargeTime = it},
                        label = { Text("End Time") }
                    )
//                    TextField(
//                        value = currentBatteryLevel,
//                        onValueChange = {currentBatteryLevel = it},
//                        label = { Text("Current Battery Level") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                    TextField(
//                        value = desiredBatteryLevel,
//                        onValueChange = {desiredBatteryLevel = it},
//                        label = { Text("Desired Battery Level") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                                nameChargingStation = ""
                                idCar = ""
                                startChargeTime = ""
                                endChargeTime = ""
                                date = ""
//                                currentBatteryLevel = ""
//                                desiredBatteryLevel = ""
                                showDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val exists = chargingStationRepository.existsByName(nameChargingStation)
                                if (exists) {
                                    val chargingStation = chargingStationRepository.getChargingStationByName(nameChargingStation).firstOrNull()
                                    val car = carRepository.getCarById(idCar.toInt()).firstOrNull()
                                    if (chargingStation != null) {
                                        if(car != null) {
                                            val overlapCount =
                                                reservationRepository.checkForOverlappingReservations(
                                                    nameChargingStation,
                                                    startChargeTime,
                                                    endChargeTime,
                                                    date
                                                )
                                            val validationResult = submitReservation(
                                                date,
                                                startChargeTime,
                                                endChargeTime
                                            )
                                            if (validationResult == "Valid") {
                                                if (overlapCount == 0) {
                                                    delay(1000)
                                                    newReservation.nameOfChargingStation =
                                                        nameChargingStation
                                                    newReservation.idOfUser = idUser.toInt()
                                                    newReservation.idOfCar = idCar.toInt()
                                                    newReservation.date = date
                                                    newReservation.StartChargeTime = startChargeTime
                                                    newReservation.EndChargeTime = endChargeTime
                                                    delay(1000)
                                                    pricePerHour =
                                                        chargingStationRepository.getChargingStationByName(
                                                            nameChargingStation
                                                        ).firstOrNull()?.pricePerHour ?: 0
                                                    newReservation.totalCost =
                                                        calculateChargingCost(
                                                            startChargeTime,
                                                            endChargeTime,
                                                            chargingStation.chargingPower_kW,
                                                            pricePerHour
                                                        )
                                                    reservationRepository.insertReservation(
                                                        newReservation
                                                    )
                                                    withContext(Dispatchers.Main) {
                                                        notification.value =
                                                            "Reservation created successfully."
                                                        showDialog.value = false
                                                    }
                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        notification.value =
                                                            "Time slot not available. Please choose another time or another day"
                                                    }
                                                }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    notification.value = validationResult
                                                }
                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                notification.value = "Car ID does not exist. Please enter a valid ID."
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            notification.value = "Charging station does not exist. Please enter a valid station name."
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Charging station does not exist. Please enter a valid station name."
                                    }
                                }
                            }
                        }) {
                            Text("Submit")
                        }
                        if (notification.value.isNotEmpty()) {
                            Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
                            notification.value = ""
                        }
                    }
                }
            }
        }
    }
    if(showDialogDelete.value){
        var idDelete by remember { mutableStateOf("") }
        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
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
                    label = { Text(text = "ID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                                reservationRepository.deleteReservationById(idDelete.toInt())
                                showDialogDelete.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Color.Red)){
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
    if (showDialogEdit.value) {
        var id by remember { mutableStateOf("") }
        var nameOfChargingStation by remember { mutableStateOf("") }
        var idOfUser by remember { mutableStateOf("") }

        val reservationRepository = OfflineReservationRepository(
            reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
        )
        val chargingStationRepository = OfflineChargingStationRepository(
            chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
        )
        val carRepository = OfflineCarRepository(
            carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
        )
        val reservationEdit by remember { mutableStateOf(Reservation()) }

        LaunchedEffect(id) {
            reservationEdit.nameOfChargingStation = ""
            reservationEdit.idOfUser = 0
            reservationEdit.idOfCar = 0
            reservationEdit.date = ""
            reservationEdit.StartChargeTime = ""
            reservationEdit.EndChargeTime = ""
            reservationEdit.totalCost = 0.0

            if (id.isNotEmpty()) {
                delay(500)
                val reservation = reservationRepository.getReservationById(id.toInt()).firstOrNull()
                reservation?.let {
                    reservationEdit.idReservation = it.idReservation
                    reservationEdit.nameOfChargingStation = it.nameOfChargingStation
                    reservationEdit.idOfUser = it.idOfUser
                    reservationEdit.idOfCar = it.idOfCar
                    reservationEdit.date = it.date
                    reservationEdit.StartChargeTime = it.StartChargeTime
                    reservationEdit.EndChargeTime = it.EndChargeTime
                    reservationEdit.totalCost = it.totalCost

                    nameOfChargingStation = it.nameOfChargingStation
                    idOfUser = it.idOfUser.toString()
                    idCar = it.idOfCar.toString()
                    date = it.date
                    startChargeTime = it.StartChargeTime
                    endChargeTime = it.EndChargeTime
                    totalCost = it.totalCost
                }
                delay(500)
            }
        }
        Dialog(onDismissRequest = { showDialogEdit.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(455.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = "Edit Reservation",
                    )
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("Id") }
                    )
                    TextField(
                        value = nameOfChargingStation,
                        onValueChange = { nameOfChargingStation = it },
                        label = { Text("Name of Charging Station") }
                    )
                    TextField(
                        value = idCar,
                        onValueChange = { idCar = it },
                        label = { Text("ID Of Car") }
                    )
                    TextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") }
                    )
                    TextField(
                        value = startChargeTime,
                        onValueChange = { startChargeTime = it },
                        label = { Text("Start Charge Time") }
                    )
                    TextField(
                        value = endChargeTime,
                        onValueChange = { endChargeTime = it },
                        label = { Text("End Charge Time") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            reservationEdit.nameOfChargingStation = ""
                            reservationEdit.idOfUser = 0
                            reservationEdit.idOfCar = 0
                            reservationEdit.date = ""
                            reservationEdit.StartChargeTime = ""
                            reservationEdit.EndChargeTime = ""
                            reservationEdit.totalCost = 0.0
                            showDialogEdit.value = false
                        }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val exists = chargingStationRepository.existsByName(nameOfChargingStation)
                                if (exists) {
                                    val chargingStation = chargingStationRepository.getChargingStationByName(nameOfChargingStation).firstOrNull()
                                    if (chargingStation != null) {
                                        val overlapCount = reservationRepository.checkForOverlappingReservations(
                                            nameOfChargingStation, startChargeTime, endChargeTime, date, reservationEdit.idReservation
                                        )
                                        val validationResult = submitReservation(date, startChargeTime, endChargeTime)
                                        if (validationResult == "Valid") {
                                            if (overlapCount == 0) {
                                                delay(500)
                                                val car = sharedViewModel.user_id.value?.let { id ->
                                                    carRepository.getCarByOwnerId(id.toInt()).firstOrNull()
                                                }
                                                delay(500)
                                                if (car != null) {
                                                    reservationEdit.nameOfChargingStation = nameOfChargingStation
                                                    reservationEdit.idOfUser = idOfUser.toInt()
                                                    reservationEdit.date = date
                                                    reservationEdit.StartChargeTime = startChargeTime
                                                    reservationEdit.EndChargeTime = endChargeTime
                                                    reservationEdit.totalCost =
                                                        calculateChargingCost(
                                                            startChargeTime,
                                                            endChargeTime,
                                                            chargingStation.chargingPower_kW,
                                                            pricePerHour
                                                        )
                                                    reservationRepository.updateReservationDetails(
                                                        reservationEdit.idReservation,
                                                        reservationEdit.date,
                                                        reservationEdit.StartChargeTime,
                                                        reservationEdit.EndChargeTime,
                                                        reservationEdit.totalCost.toString()
                                                    )
                                                    withContext(Dispatchers.Main) {
                                                        notification.value = "Reservation modified successfully."
                                                        showDialogEdit.value = false
                                                    }
                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        notification.value = "Failed to fetch car details."
                                                    }
                                                }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    notification.value = "Time slot not available. Please choose another time or another day"
                                                }
                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                notification.value = validationResult
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            notification.value = "Charging station does not exist. Please enter a valid station name."
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Charging station does not exist. Please enter a valid station name."
                                    }
                                }
                            }
                        }) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }

}


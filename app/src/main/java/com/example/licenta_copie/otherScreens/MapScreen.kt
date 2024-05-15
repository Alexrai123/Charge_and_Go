package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.licenta_copie.ModelView.ChargingStationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(chargingStationViewModel: ChargingStationViewModel){
    val chargingStations by chargingStationViewModel.chargingStations.collectAsState(initial = emptyList())
    Scaffold(
        content = {
            GoogleMap(
                modifier = Modifier.padding(bottom = 70.dp),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(47.65, 26.247), 13f) }
            ) {
                chargingStations.forEach { chargingStation ->
                    val markerPosition = LatLng(chargingStation.lat, chargingStation.lng)
                    val markerState = MarkerState(position = markerPosition)
                    val title = chargingStation.name + ", " +chargingStation.pricePerHour+"lei/h" + ", "+ chargingStation.chargingPower_kW + "kW"
                    Marker(state = markerState, title = title)
                }
            }
        }
    )
}
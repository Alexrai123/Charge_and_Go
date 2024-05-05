package com.example.licenta_copie.otherScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(){
    val suceava = LatLng(47.65, 26.28)
    val suceavaState = MarkerState(position = suceava)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(suceava, 10f)
    }
    Scaffold (
        content = { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 75.dp), cameraPositionState = cameraPositionState
                ) {
                    Marker(state = suceavaState, title = "Marker in Singapore")
                }
            }
        }
    )
}
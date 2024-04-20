package com.example.licenta_copie.otherScreens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "MapScreen",
        title = "HOME",
        icon = Icons.Default.Place
    )
    object Bookings: BottomBarScreen(
        route = "BookingsScreen",
        title = "BOOKINGS",
        icon = Icons.Default.DateRange
    )
    object Profile : BottomBarScreen(
        route = "ProfileScreen",
        title = "PROFILE",
        icon = Icons.Default.Person
    )
}
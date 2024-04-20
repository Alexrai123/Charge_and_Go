package com.example.licenta_copie.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.licenta_copie.otherScreens.Bookings
import com.example.licenta_copie.otherScreens.MapScreen
import com.example.licenta_copie.otherScreens.Profile

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(navController = navController, route = Graph.HOME, startDestination = BottomBarScreen.Map.route){
        composable(route = BottomBarScreen.Map.route){
            MapScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Profile.route){
            Profile()
        }
        composable(route = BottomBarScreen.Bookings.route){
            Bookings()
        }
    }
}
sealed class BottomBarScreen (val route: String, val title: String, val icon: ImageVector){
    object Map: BottomBarScreen(route = "MapScreen", title = "MAP", icon = Icons.Default.Place)
    object Profile : BottomBarScreen(route = "ProfileScreen", title = "PROFILE", icon = Icons.Default.Person)
    object Bookings : BottomBarScreen(route = "BookingsScreen", title = "BOOKINGS", icon = Icons.Default.DateRange)
}
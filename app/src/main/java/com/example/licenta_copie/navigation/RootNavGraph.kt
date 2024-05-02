package com.example.licenta_copie.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.licenta_copie.ModelView.SharedViewModel
import com.example.licenta_copie.otherScreens.HomeScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, sharedViewModel: SharedViewModel){
    NavHost(navController = navController, startDestination = Graph.AUTHENTICATION){
        authNavGraph(navController = navController, sharedViewModel)
        composable(route = Graph.HOME){
            HomeScreen(sharedViewModel = sharedViewModel)
        }
    }
}
object Graph{
    const val AUTHENTICATION = "AuthGraph"
    const val HOME = "HomeScreen"
}
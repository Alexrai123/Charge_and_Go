package com.example.licenta_copie.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.licenta_copie.otherScreens.HomeScreen

@Composable
fun RootNavigationGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Graph.AUTHENTICATION){
        authNavGraph(navController = navController)
        composable(route = Graph.HOME){
            HomeScreen()
        }
    }
}
object Graph{
    const val AUTHENTICATION = "AuthGraph"
    const val HOME = "HomeScreen"
}
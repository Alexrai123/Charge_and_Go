package com.example.licenta_copie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.licenta_copie.Admin.AdminPage
import com.example.licenta_copie.Admin.Cars
import com.example.licenta_copie.Admin.ChargingStations
import com.example.licenta_copie.Admin.Reservations
import com.example.licenta_copie.Admin.Users
import com.example.licenta_copie.Authentication.ForgotPasswordScreen
import com.example.licenta_copie.Authentication.LoginScreen
import com.example.licenta_copie.Authentication.SignupScreen
import com.example.licenta_copie.ModelView.SharedViewModel

fun NavGraphBuilder.authNavGraph(navController: NavHostController, sharedViewModel: SharedViewModel){
    navigation(route = Graph.AUTHENTICATION, startDestination = AuthScreen.Login.route){
        composable(route = AuthScreen.Login.route){
            LoginScreen(
                onLogin = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)},
                onSignup = { navController.navigate(AuthScreen.Signup.route) },
                onForgotPassword = { navController.navigate(AuthScreen.ForgotPassword.route) },
                onAdmin = { navController.navigate(AuthScreen.AdminPage.route) },
                sharedViewModel = sharedViewModel
            )
        }
        composable(route = AuthScreen.Signup.route){
            SignupScreen(
                onSign = {navController.navigate(AuthScreen.Login.route)}
            )
        }
        composable(route = AuthScreen.ForgotPassword.route){
            ForgotPasswordScreen(
                onForgot = {navController.navigate(AuthScreen.Login.route)}
            )
        }
        composable(route = AuthScreen.AdminPage.route){
            AdminPage(
                onLogout = {navController.navigate(AuthScreen.Login.route)},
                onUser = {navController.navigate(AuthScreen.Users.route)},
                onCar = {navController.navigate(AuthScreen.Cars.route)},
                onChargingStation = {navController.navigate(AuthScreen.ChargingStations.route)},
                onReservation = {navController.navigate(AuthScreen.Reservations.route)})
        }
        composable(route = AuthScreen.Users.route){
            Users(goBack = { navController.popBackStack() })
        }
        composable(route = AuthScreen.Cars.route){
            Cars(goBack = { navController.popBackStack() })
        }
        composable(route = AuthScreen.ChargingStations.route){
            ChargingStations(goBack = { navController.popBackStack() })
        }
        composable(route = AuthScreen.Reservations.route){
            Reservations(goBack = { navController.popBackStack() })
        }
    }
}
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LoginScreen")
    object Signup : AuthScreen(route = "SignupScreen")
    object ForgotPassword : AuthScreen(route = "ForgotPasswordScreen")
    object AdminPage : AuthScreen(route = "AdminPage")
    object Users : AuthScreen(route = "Users")
    object Cars : AuthScreen(route = "Cars")
    object ChargingStations : AuthScreen(route = "ChargingStations")
    object Reservations : AuthScreen(route = "Reservations")
}
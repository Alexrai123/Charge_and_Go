package com.example.licenta_copie.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
import com.example.licenta_copie.Authentication.NewPasswordScreen
import com.example.licenta_copie.Authentication.SignupScreen
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.OfflineRepository.OfflineCarRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineChargingStationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineReservationRepository
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.ModelView.CarViewModel
import com.example.licenta_copie.ModelView.ChargingStationViewModel
import com.example.licenta_copie.ModelView.ReservationViewModel
import com.example.licenta_copie.ModelView.SharedViewModel
import com.example.licenta_copie.ModelView.UserViewModel

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
                onForgot = {navController.navigate(AuthScreen.NewPassword.route)},
                sharedViewModel = sharedViewModel
            )
        }
        composable(route = AuthScreen.NewPassword.route){
            NewPasswordScreen(
                onChangePassword = {navController.navigate(AuthScreen.Login.route)},
                sharedViewModel = sharedViewModel
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
            val userRepository = OfflineUserRepository(
                userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
            )
            val userViewModel = UserViewModel(userRepository)
            val showDialogDelete = remember { mutableStateOf(false) }
            val showDialogEdit = remember { mutableStateOf(false) }
            Users(userViewModel, goBack = { navController.popBackStack() }, showDialogDelete, showDialogEdit)
        }
        composable(route = AuthScreen.Cars.route){
            val carRepository = OfflineCarRepository(
                carDao = AppDatabase.getDatabase(LocalContext.current).carDao()
            )
            val carViewModel = CarViewModel(carRepository)
            val showDialogDelete = remember { mutableStateOf(false) }
            val showDialogEdit = remember { mutableStateOf(false) }
            Cars(carViewModel, goBack = { navController.popBackStack() }, showDialogDelete, showDialogEdit)
        }
        composable(route = AuthScreen.ChargingStations.route){
            val chargingStationRepository = OfflineChargingStationRepository(
                chargingStationDao = AppDatabase.getDatabase(LocalContext.current).chargingStationDao()
            )
            val chargingStationViewModel = ChargingStationViewModel(chargingStationRepository)
            val showDialogDelete = remember { mutableStateOf(false) }
            val showDialogEdit = remember { mutableStateOf(false) }
            val showDialogAdd = remember { mutableStateOf(false) }
            ChargingStations(chargingStationViewModel, goBack = { navController.popBackStack() }, showDialogDelete, showDialogEdit, showDialogAdd)
        }
        composable(route = AuthScreen.Reservations.route){
            val reservationRepository = OfflineReservationRepository(
                reservationDao = AppDatabase.getDatabase(LocalContext.current).reservationDao()
            )
            val reservationViewModel = ReservationViewModel(reservationRepository)
            val showDialogDelete = remember { mutableStateOf(false) }
            val showDialogEdit = remember { mutableStateOf(false) }
            Reservations(reservationViewModel, goBack = { navController.popBackStack() }, showDialogDelete, showDialogEdit)
        }
    }
}
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LoginScreen")
    object Signup : AuthScreen(route = "SignupScreen")
    object ForgotPassword : AuthScreen(route = "ForgotPasswordScreen")
    object NewPassword : AuthScreen(route = "NewPasswordScreen")
    object AdminPage : AuthScreen(route = "AdminPage")
    object Users : AuthScreen(route = "Users")
    object Cars : AuthScreen(route = "Cars")
    object ChargingStations : AuthScreen(route = "ChargingStations")
    object Reservations : AuthScreen(route = "Reservations")
}
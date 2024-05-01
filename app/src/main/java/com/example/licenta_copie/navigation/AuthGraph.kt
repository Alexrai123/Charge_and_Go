package com.example.licenta_copie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.licenta_copie.Authentication.ForgotPasswordScreen
import com.example.licenta_copie.Authentication.LoginScreen
import com.example.licenta_copie.Authentication.SignupScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    navigation(route = Graph.AUTHENTICATION, startDestination = AuthScreen.Login.route){
        composable(route = AuthScreen.Login.route){
            LoginScreen(
                onLogin = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)},
                onSignup = { navController.navigate(AuthScreen.Signup.route) },
                onForgotPassword = { navController.navigate(AuthScreen.ForgotPassword.route) },
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
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LoginScreen")
    object Signup : AuthScreen(route = "SignupScreen")
    object ForgotPassword : AuthScreen(route = "ForgotPasswordScreen")
}
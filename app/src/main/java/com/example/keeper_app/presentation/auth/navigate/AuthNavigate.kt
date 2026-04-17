package com.example.keeper_app.presentation.auth.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keeper_app.presentation.auth.ui.screens.ForgotPasswordScreen
import com.example.keeper_app.presentation.auth.ui.screens.LoginScreen
import com.example.keeper_app.presentation.auth.ui.screens.RegisterScreen
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import com.example.keeper_app.presentation.auth.viewmodel.AuthViewModel


sealed class AuthNav(val route: String) {
    object Login : AuthNav("login")
    object Register : AuthNav("register")
    object ForgotPassword : AuthNav("forgot_password")
}



@Composable
fun AuthNavigation(
    onSuccess: () -> Unit,
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
){

    val authState = viewModel.authState.collectAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Success -> {onSuccess()}
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthNav.Login.route
    ){
        composable(AuthNav.Login.route){
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(AuthNav.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack()}
            )
        }

        composable(AuthNav.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack()}
            )
        }

    }
}

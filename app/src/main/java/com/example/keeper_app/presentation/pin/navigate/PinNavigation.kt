package com.example.keeper_app.presentation.pin.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keeper_app.AppState
import com.example.keeper_app.presentation.pin.ui.screen.PinEnterScreen
import com.example.keeper_app.presentation.pin.ui.screen.PinSetupScreen

sealed class Pin(val route: String) {
    object Setup : Pin("pin_setup")
    object Enter : Pin("pin_enter")
}

@Composable
fun PinNavigation(
    navController: NavHostController,
    appState: AppState
) {

    NavHost(
        navController = navController,
        startDestination = Pin.Enter.route
    ) {
        composable(Pin.Enter.route) {
            PinEnterScreen(
                onSuccess = {
                    //успех входа
                },
                onSetupRequired = {
                    navController.navigate(Pin.Setup.route)
                }
            )
        }

        composable(Pin.Setup.route) {
            PinSetupScreen(
                onSuccess = {
                   //успех установки пин-кода
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
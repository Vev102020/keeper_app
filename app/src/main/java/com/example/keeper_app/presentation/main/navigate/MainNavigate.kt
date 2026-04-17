package com.example.keeper_app.presentation.main.navigate

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keeper_app.presentation.about.AboutActivity
import com.example.keeper_app.presentation.main.ui.screen.AddServiceScreen
import com.example.keeper_app.presentation.main.ui.screen.MainScreen
import com.example.keeper_app.presentation.settings.SettingsActivity

sealed class MainNav(val route: String) {
    object Home : MainNav("home")
    object AddService : MainNav("add_service")
}


@Composable
fun MainNavigate(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainNav.Home.route
    ){
        composable (MainNav.Home.route) {
            MainScreen(
                onNavigateToAddService = {
                    navController.navigate(MainNav.AddService.route)
                },
                onSettingsClick = {
                    val intent = Intent(navController.context, SettingsActivity::class.java)
                    navController.context.startActivity(intent)
                },
                onAboutClick = {
                    val intent = Intent(navController.context, AboutActivity::class.java)
                    navController.context.startActivity(intent)
                }
            )
        }
        composable (MainNav.AddService.route) {
            AddServiceScreen(
                onServiceAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
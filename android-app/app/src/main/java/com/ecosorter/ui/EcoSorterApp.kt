package com.ecosorter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecosorter.ui.camera.CameraScreen
import com.ecosorter.ui.history.HistoryScreen
import com.ecosorter.ui.home.HomeScreen
import com.ecosorter.ui.settings.SettingsScreen

/**
 * Main composable function for the EcoSorter application
 */
@Composable
fun EcoSorterApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }
        composable(Screen.Camera.route) {
            CameraScreen(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Sealed class representing the different screens in the app
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object History : Screen("history")
    object Settings : Screen("settings")
}
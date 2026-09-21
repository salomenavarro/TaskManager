package com.example.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.screen.drafts.DraftListScreen
import com.example.taskmanager.ui.screen.login.LoginScreen
import com.example.taskmanager.ui.screen.register.RegisterScreen
import com.example.taskmanager.ui.screen.tasklist.TaskListScreen

// ui/navigation/AppNavHost.kt
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        // Elimina Login del backstack: con "Atrás" no se puede volver
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ui/navigation/AppNavHost.kt (reemplaza el composable de TaskList)
        composable(Screen.TaskList.route) {
            TaskListScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) // limpia todo el backstack, no se puede volver con Atrás
                    }
                }
            )
        }

        // ui/navigation/AppNavHost.kt — agrega dentro del NavHost
        composable(Screen.Drafts.route) {
            DraftListScreen()
        }

    }
}
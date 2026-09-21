package com.example.taskmanager.ui.navigation

// ui/navigation/Screen.kt
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object TaskList : Screen("task_list")
    // ui/navigation/Screen.kt — agrega
    data object Drafts : Screen("drafts")
}
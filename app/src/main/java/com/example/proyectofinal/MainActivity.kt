package com.example.proyectofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.ui.screens.CalendarScreen
import com.example.proyectofinal.ui.screens.DashboardScreen
import com.example.proyectofinal.ui.screens.LoginScreen
import com.example.proyectofinal.ui.screens.SettingsScreen
import com.example.proyectofinal.ui.screens.StatisticsScreen
import com.example.proyectofinal.ui.screens.TaskListScreen
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPrefs = UserPreferencesRepository(this)

        enableEdgeToEdge()
        setContent {
            val isDarkMode by userPrefs.isDarkMode.collectAsState(initial = false)

            ProyectoFinalTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(userPrefs)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(userPrefs: UserPreferencesRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userPrefs) }
        composable("dashboard") { DashboardScreen(navController, userPrefs) }
        composable("calendar") { CalendarScreen(navController, userPrefs) }
        composable("tasks") { TaskListScreen(navController, userPrefs) }
        composable("settings") { SettingsScreen(navController, userPrefs) }
        composable("statistics") { StatisticsScreen(navController, userPrefs) }
    }
}
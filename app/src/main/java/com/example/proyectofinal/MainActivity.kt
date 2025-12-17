package com.example.proyectofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.ui.screens.*
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme
import com.example.proyectofinal.ui.viewmodel.TaskViewModel
import com.example.proyectofinal.ui.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPrefs = UserPreferencesRepository(applicationContext)

        var startDestination = "login"
        runBlocking {
            val email = userPrefs.userEmail.first()
            if (email.isNotEmpty()) {
                startDestination = "dashboard"
            }
        }

        setContent {
            val isDarkMode by userPrefs.isDarkMode.collectAsState(initial = isSystemInDarkTheme())

            ProyectoFinalTheme(darkTheme = isDarkMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(startDestination = startDestination, userPrefs = userPrefs)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    startDestination: String,
    userPrefs: UserPreferencesRepository
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val sharedTaskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(userPrefs, context)
    )

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(navController, userPrefs)
        }

        composable("dashboard") {
            DashboardScreen(navController, userPrefs, sharedTaskViewModel)
        }

        composable("tasks") {
            TaskListScreen(navController, userPrefs, sharedTaskViewModel)
        }

        composable("calendar") {
            CalendarScreen(navController, userPrefs)
        }

        composable("statistics") {
            StatisticsScreen(navController, userPrefs)
        }

        composable("settings") {
            SettingsScreen(navController, userPrefs)
        }
    }
}
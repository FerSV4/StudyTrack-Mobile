package com.example.proyectofinal.model

data class SettingsState(
    val userName: String = "Fernando Sejas",
    val userEmail: String = "f.sejas@universidad.edu.bo",
    val notificationsPush: Boolean = true,
    val notificationsEmail: Boolean = false,
    val alertsExams: Boolean = true,
    val isDarkMode: Boolean = false,

    val selectedThemeIndex: Int = 0
)
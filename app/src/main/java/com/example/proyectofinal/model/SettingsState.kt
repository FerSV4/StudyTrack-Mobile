package com.example.proyectofinal.model

data class SettingsState(
    val userName: String = "",
    val userEmail: String = "",
    val notificationsPush: Boolean = false,
    val notificationsEmail: Boolean = false,
    val alertsExams: Boolean = true,
    val isDarkMode: Boolean = false,
    val selectedThemeIndex: Int = 0
)
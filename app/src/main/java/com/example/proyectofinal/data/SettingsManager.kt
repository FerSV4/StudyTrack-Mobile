package com.example.proyectofinal.data

import com.example.proyectofinal.model.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SettingsManager {

    private val _settingsState = MutableStateFlow(SettingsState())

    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()


    fun toggleDarkMode(isEnabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(isDarkMode = isEnabled)
    }

    fun togglePushNotifications(isEnabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(notificationsPush = isEnabled)
    }

    fun updateSetting(settingUpdater: (SettingsState) -> SettingsState) {
        _settingsState.value = settingUpdater(_settingsState.value)
    }
}
package com.example.proyectofinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.data.UserRepository
import com.example.proyectofinal.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferencesRepository
) : ViewModel() {

    private val repository = UserRepository()

    // Estados de la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMsg.value = "Llena todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            val user = repository.login(email, pass)

            if (user != null) {
                userPrefs.saveUserEmail(user.email)
                userPrefs.saveUserName(user.fullName)
                _loginSuccess.value = true
            } else {
                _errorMsg.value = "Credenciales incorrectas"
            }
            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _errorMsg.value = "Llena todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            val newUser = User(email = email, password = pass, fullName = name)
            val success = repository.register(newUser)

            if (success) {
                userPrefs.saveUserEmail(email)
                userPrefs.saveUserName(name)
                _loginSuccess.value = true
            } else {
                _errorMsg.value = "Error: El email ya existe o falló la conexión"
            }
            _isLoading.value = false
        }
    }
}

class LoginViewModelFactory(private val userPrefs: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
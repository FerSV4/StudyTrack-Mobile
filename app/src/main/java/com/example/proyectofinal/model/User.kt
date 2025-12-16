package com.example.proyectofinal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    @SerialName("full_name") val fullName: String
)
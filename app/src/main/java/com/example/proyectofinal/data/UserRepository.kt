package com.example.proyectofinal.data

import android.util.Log
import com.example.proyectofinal.model.User
import io.github.jan.supabase.postgrest.postgrest

class UserRepository {

    suspend fun login(email: String, pass: String): User? {
        return try {
            SupabaseClient.client.postgrest["users"]
                .select {
                    filter {
                        eq("email", email)
                        eq("password", pass)
                    }
                }.decodeSingleOrNull<User>()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error login", e)
            null
        }
    }

    suspend fun register(user: User): Boolean {
        return try {
            SupabaseClient.client.postgrest["users"]
                .insert(user)
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error registro", e)
            false
        }
    }
}
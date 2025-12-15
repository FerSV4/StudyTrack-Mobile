package com.example.proyectofinal.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {

    private const val SUPABASE_URL = "https://nzrfibuqkqjfefekmhvf.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im56cmZpYnVxa3FqZmVmZWttaHZmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4MTYxMDMsImV4cCI6MjA4MTM5MjEwM30.KdURJCQpXdqFpXioNTWKmtmv7lJMLa1P59iFpr9qiRM"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)

        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
        })
    }
}
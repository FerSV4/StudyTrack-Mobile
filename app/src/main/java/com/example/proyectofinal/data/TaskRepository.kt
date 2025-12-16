package com.example.proyectofinal.data

import android.util.Log
import com.example.proyectofinal.model.Task
import io.github.jan.supabase.postgrest.postgrest

class TaskRepository {

    suspend fun getTasks(userEmail: String): List<Task> {
        return try {
            SupabaseClient.client.postgrest["tasks"]
                .select {
                    filter {
                        eq("user_email", userEmail)
                    }
                }.decodeList<Task>()
        } catch (e: Exception) {
            Log.e("TaskRepository", "Error al cargar la tarea", e)
            emptyList()
        }
    }

    suspend fun createTask(task: Task): Task? {
        return try {
            SupabaseClient.client.postgrest["tasks"].insert(task) {
                select()
            }.decodeSingleOrNull<Task>()
        } catch (e: Exception) {
            Log.e("TaskRepository", "Error al crear la tarea", e)
            null
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            task.id?.let { id ->
                SupabaseClient.client.postgrest["tasks"].update(task) {
                    filter {
                        eq("id", id)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TaskRepository", "Error al actualizar tarea", e)
        }
    }

    suspend fun deleteTask(taskId: Int) {
        try {
            SupabaseClient.client.postgrest["tasks"].delete {
                filter {
                    eq("id", taskId)
                }
            }
        } catch (e: Exception) {
            Log.e("TaskRepository", "Error eliminando la tarea", e)
        }
    }
}
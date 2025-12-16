package com.example.proyectofinal.data

import com.example.proyectofinal.model.Task
import io.github.jan.supabase.postgrest.from

class TaskRepository {

    suspend fun getTasks(email: String): List<Task> {
        return try {
            SupabaseClient.client
                .from("tasks")
                .select {
                    filter {
                        eq("user_email", email)
                    }
                }
                .decodeList<Task>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createTask(task: Task) {
        try {
            SupabaseClient.client.from("tasks").insert(task)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            val id = task.id ?: return

            SupabaseClient.client
                .from("tasks")
                .update(task) {
                    filter { eq("id", id) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    suspend fun deleteTask(taskId: Int) {
        try {
            SupabaseClient.client
                .from("tasks")
                .delete {
                    filter { eq("id", taskId) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
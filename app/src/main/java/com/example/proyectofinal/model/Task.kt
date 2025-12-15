package com.example.proyectofinal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Priority { HIGH, MEDIUM, LOW }

@Serializable
enum class TaskStatus { PENDING, COMPLETED }

@Serializable
enum class TaskType { TASK, EVENT, CLASS }

@Serializable
data class Task(
    val id: Int? = null,

    @SerialName("title")
    val title: String,

    @SerialName("subject")
    val subject: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("due_date")
    val dueDate: String? = null,

    @SerialName("priority")
    val priority: Priority = Priority.MEDIUM,

    @SerialName("status")
    val status: TaskStatus = TaskStatus.PENDING,

    @SerialName("user_email")
    val userEmail: String,

    @SerialName("task_type")
    val taskType: TaskType = TaskType.TASK
)
package com.example.proyectofinal.model

import java.time.LocalDateTime
import java.util.UUID

enum class Priority { HIGH, MEDIUM, LOW }
enum class TaskStatus { PENDING, COMPLETED }

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subject: String,
    val description: String? = null,
    val dueDate: LocalDateTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val status: TaskStatus = TaskStatus.PENDING,
    val tags: List<String> = emptyList()
)
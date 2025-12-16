package com.example.proyectofinal.data

import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import java.time.LocalDateTime // Solo si usas conversiones, pero el tipo base será String

class TaskBuilder {
    private var title: String = "Nueva Tarea"
    private var subject: String = "General"
    private var description: String? = null

    private var dueDate: String? = null

    private var priority: Priority = Priority.MEDIUM
    private var status: TaskStatus = TaskStatus.PENDING

    private var userEmail: String = "anonimo@test.com"

    fun setTitle(title: String): TaskBuilder = apply { this.title = title }

    fun setSubject(subject: String): TaskBuilder = apply { this.subject = subject }

    fun setDescription(description: String?): TaskBuilder = apply { this.description = description }

    fun setDueDate(dueDate: String?): TaskBuilder = apply { this.dueDate = dueDate }

    fun setPriority(priority: Priority): TaskBuilder = apply { this.priority = priority }

    fun setStatus(status: TaskStatus): TaskBuilder = apply { this.status = status }

    fun setUserEmail(email: String): TaskBuilder = apply { this.userEmail = email }

    fun build(): Task {
        if (title.isBlank()) {
            throw IllegalStateException("La tarea debe tener un título")
        }

        return Task(
            title = this.title,
            subject = this.subject,
            description = this.description,
            dueDate = this.dueDate,
            priority = this.priority,
            status = this.status,
            userEmail = this.userEmail
        )
    }
}
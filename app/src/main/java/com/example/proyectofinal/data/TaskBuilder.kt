package com.example.proyectofinal.data

import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.model.TaskType

class TaskBuilder {
    private var title: String = "Nueva Tarea"
    private var subject: String = "General"
    private var description: String? = null
    private var dueDate: String? = null
    private var priority: Priority = Priority.MEDIUM
    private var status: TaskStatus = TaskStatus.PENDING
    private var userEmail: String = "anonimo@test.com"
    private var taskType: TaskType = TaskType.TASK

    fun setTitle(title: String) = apply { this.title = title }
    fun setSubject(subject: String) = apply { this.subject = subject }
    fun setDescription(description: String?) = apply { this.description = description }
    fun setDueDate(dueDate: String?) = apply { this.dueDate = dueDate }
    fun setPriority(priority: Priority) = apply { this.priority = priority }
    fun setStatus(status: TaskStatus) = apply { this.status = status }
    fun setUserEmail(email: String) = apply { this.userEmail = email }
    fun setTaskType(type: TaskType) = apply { this.taskType = type }

    fun build(): Task {
        if (title.isBlank()) throw IllegalStateException("La tarea debe tener un título")

        return Task(
            title = this.title,
            subject = this.subject,
            description = this.description,
            dueDate = this.dueDate,
            priority = this.priority,
            status = this.status,
            userEmail = this.userEmail,
            taskType = this.taskType
        )
    }
}
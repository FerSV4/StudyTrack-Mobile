package com.example.proyectofinal.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.TaskRepository
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.model.TaskType
import com.example.proyectofinal.notification.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskViewModel(
    private val userPrefs: UserPreferencesRepository,
    private val context: Context
) : ViewModel() {

    private val repository = TaskRepository()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            val email = userPrefs.userEmail.first()

            val result = repository.getTasks(email)
            _tasks.value = result
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(title: String, subject: String, description: String, priority: Priority, dueDate: String, taskType: TaskType) {
        viewModelScope.launch {
            val email = userPrefs.userEmail.first()
            val newTask = Task(
                title = title,
                subject = subject,
                description = description,
                priority = priority,
                dueDate = dueDate,
                userEmail = email,
                status = TaskStatus.PENDING,
                taskType = taskType
            )

            val createdTask = repository.createTask(newTask)

            if (createdTask != null) {
                NotificationScheduler.scheduleNotification(context, createdTask)
            }

            loadTasks()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)

            NotificationScheduler.scheduleNotification(context, task)

            loadTasks()
        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            val newStatus = if (task.status == TaskStatus.PENDING) TaskStatus.COMPLETED else TaskStatus.PENDING
            val updatedTask = task.copy(status = newStatus)

            repository.updateTask(updatedTask)

            // Si se completó, podríamos cancelar la notificación
            if (newStatus == TaskStatus.COMPLETED && task.id != null) {
                NotificationScheduler.cancelNotification(context, task.id)
            }

            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            task.id?.let { id ->
                repository.deleteTask(id)
                // 5. Cancelamos la notificación al borrar
                NotificationScheduler.cancelNotification(context, id)
                loadTasks()
            }
        }
    }
}

// 6. Actualizamos la Factory para recibir Contexto
class TaskViewModelFactory(
    private val userPrefs: UserPreferencesRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(userPrefs, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
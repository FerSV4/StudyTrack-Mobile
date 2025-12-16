package com.example.proyectofinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.TaskRepository
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.model.TaskType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskViewModel(
    private val userPrefs: UserPreferencesRepository
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
            if (email.isNotEmpty()) {
                val result = repository.getTasks(email)
                _tasks.value = result
            }
        }
    }

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
            repository.createTask(newTask)
            loadTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks()
        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            val newStatus = if (task.status == TaskStatus.PENDING) TaskStatus.COMPLETED else TaskStatus.PENDING
            val updatedTask = task.copy(status = newStatus)
            repository.updateTask(updatedTask)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            task.id?.let { id ->
                repository.deleteTask(id)
                loadTasks()
            }
        }
    }
}

class TaskViewModelFactory(private val userPrefs: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(userPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
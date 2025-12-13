package com.example.proyectofinal.data

import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import java.time.LocalDateTime


data class Exam(
    val title: String,
    val subject: String,
    val date: String,
    val daysLeft: Int,
    val type: ExamType
)

enum class ExamType { PURPLE, BLUE }

data class DashboardState(
    val userName: String,
    val currentDate: String,
    val pendingTasksCount: Int,
    val todaysTasks: List<Task>,
    val upcomingExams: List<Exam>
)

object DashboardFacade {

    fun getDashboardData(): DashboardState {

        val user = SettingsManager.settingsState.value.userName

        val date = "Jueves, Dic 6, 2025"

        val tasks = listOf(
            TaskBuilder().setTitle("Tarea de App Movil").setSubject("App Movil").setPriority(Priority.HIGH).build(),
            TaskBuilder().setTitle("Notas de QA II").setSubject("QA II").setPriority(Priority.LOW).build()
        )

        val exams = listOf(
            Exam(
                title = "Examen Criptografia",
                subject = "Criptografia",
                date = "Dic 9, 2025",
                daysLeft = 3,
                type = ExamType.PURPLE
            ),
            Exam(
                title = "Examen QA II",
                subject = "Quality Assurance",
                date = "Dic 10, 2025",
                daysLeft = 4,
                type = ExamType.BLUE
            )
        )

        return DashboardState(
            userName = user,
            currentDate = date,
            pendingTasksCount = tasks.size,
            todaysTasks = tasks,
            upcomingExams = exams
        )
    }
}
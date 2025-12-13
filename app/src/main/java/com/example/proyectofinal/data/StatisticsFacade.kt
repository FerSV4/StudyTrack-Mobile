package com.example.proyectofinal.data

import androidx.compose.ui.graphics.Color
import com.example.proyectofinal.ui.theme.*

data class SubjectPerformance(
    val name: String,
    val completed: Int,
    val total: Int,
    val color: Color
)

data class UpcomingDeadline(
    val title: String,
    val date: String,
    val daysLeft: String,
    val bgColor: Color,
    val textColor: Color,
    val badgeColor: Color
)

data class StatisticsState(
    val completionPercentage: Int,
    val completedTasks: Int,
    val totalTasks: Int,
    val subjectPerformance: List<SubjectPerformance>,
    val upcomingDeadlines: List<UpcomingDeadline>
)

object StatisticsFacade {
    fun getStatistics(): StatisticsState {
        return StatisticsState(
            completionPercentage = 73,
            completedTasks = 28,
            totalTasks = 38,
            subjectPerformance = listOf(
                SubjectPerformance("App movil", 12, 15, StatBlue),
                SubjectPerformance("Criptografia", 8, 10, StatPurple),
                SubjectPerformance("Comunicacion Asertiva", 5, 8, StatGreen),
                SubjectPerformance("Quality Assurance II", 3, 5, StatOrange)
            ),
            upcomingDeadlines = listOf(
                UpcomingDeadline("Proyecto App Movil", "Dic 11, 2025", "2 dias", DeadlineRedBg, DeadlineRedText, Color(0xFFEF9A9A)),
                UpcomingDeadline("K6 Quality Assurance", "Dic 15, 2025", "5 dias", DeadlineOrangeBg, DeadlineOrangeText, Color(0xFFFFCC80)),
                UpcomingDeadline("Metasploit Cripto", "Dic 16, 2025", "6 dias", DeadlineYellowBg, DeadlineYellowText, Color(0xFFFFF59D))
            )
        )
    }
}
package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.data.StatisticsFacade
import com.example.proyectofinal.data.SubjectPerformance
import com.example.proyectofinal.data.UpcomingDeadline
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    val stats = remember { StatisticsFacade.getStatistics() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadisticas y progreso", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BackgroundLight)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = false, onClick = {}, label = { Text("Semana") })
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text("Mes") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = StatOrange, selectedLabelColor = Color.White)
                        )
                        FilterChip(selected = false, onClick = {}, label = { Text("Semestre") })
                    }
                }

                item {
                    GeneralProgressCard(
                        percentage = stats.completionPercentage,
                        completed = stats.completedTasks,
                        total = stats.totalTasks
                    )
                }

                item {
                    SubjectPerformanceCard(stats.subjectPerformance)
                }

                item {
                    DeadlinesCard(stats.upcomingDeadlines)
                }
            }
        }
    }
}

@Composable
fun GeneralProgressCard(percentage: Int, completed: Int, total: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFBBDEFB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Finalizacion general", color = PrimaryBlue, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$percentage%", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                Spacer(modifier = Modifier.width(16.dp))
                LinearProgressIndicator(
                    progress = percentage / 100f,
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = StatBlue,
                    trackColor = Color(0xFFE3F2FD)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$completed de $total tareas completadas este mes",
                color = StatBlue,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun SubjectPerformanceCard(subjects: List<SubjectPerformance>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Rendimiento por materia", color = Color.DarkGray, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            subjects.forEach { subject ->
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(subject.name, fontSize = 14.sp, color = Color.DarkGray)
                        Text("${subject.completed}/${subject.total}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = subject.completed.toFloat() / subject.total.toFloat(),
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = subject.color,
                        trackColor = Color(0xFFF5F5F5),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
fun DeadlinesCard(deadlines: List<UpcomingDeadline>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFFFE0B2)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fechas límite cercanas (proximos 7 dias)", color = Color.DarkGray, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            deadlines.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .background(item.bgColor, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.title, color = Color(0xFF5D4037), fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text(item.date, color = item.textColor, fontSize = 12.sp)
                    }
                    Surface(
                        color = item.badgeColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            item.daysLeft,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
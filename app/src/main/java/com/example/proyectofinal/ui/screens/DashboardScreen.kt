package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.data.DashboardFacade
import com.example.proyectofinal.data.Exam
import com.example.proyectofinal.data.ExamType
import com.example.proyectofinal.ui.theme.*
import androidx.navigation.NavController
import com.example.proyectofinal.ui.components.BottomNavBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val dashboardData = remember { DashboardFacade.getDashboardData() }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(PrimaryBlue)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Hola de nuevo, ${dashboardData.userName.split(" ")[0]}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = dashboardData.currentDate, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.White)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = BackgroundLight
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item { SectionHeader("Tareas de hoy") }
                    items(dashboardData.todaysTasks) { task ->
                        TaskItemCard(task)
                    }

                    item {
                        SectionHeader("Proximos examenes")

                        Card(
                            colors = CardDefaults.cardColors(containerColor = WhiteCard),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, CardBorderPurple),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                dashboardData.upcomingExams.forEach { exam ->
                                    ExamItem(exam)
                                }
                            }
                        }
                    }

                    // BOTONES DE ACCESO RÁPIDO CON NAVEGACIÓN
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            QuickAccessButton(
                                text = "Calendario",
                                icon = Icons.Default.DateRange,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFE1BEE7),
                                onClick = { navController.navigate("calendar") }
                            )
                            QuickAccessButton(
                                text = "Tareas",
                                icon = Icons.Default.CheckCircle,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFC8E6C9),
                                onClick = { navController.navigate("tasks") }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            QuickAccessButton(
                                text = "Estadísticas",
                                icon = Icons.Default.BarChart,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFFFE0B2),
                                onClick = { navController.navigate("statistics") } // ¡AQUÍ ESTÁ EL CAMBIO!
                            )
                            QuickAccessButton(
                                text = "Ajustes",
                                icon = Icons.Default.Settings,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFE3F2FD),
                                onClick = { navController.navigate("settings") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExamItem(exam: Exam) {
    val (bgColor, textColor, labelColor) = when (exam.type) {
        ExamType.PURPLE -> Triple(ExamPurpleBg, ExamPurpleText, ExamPurpleLabel)
        ExamType.BLUE -> Triple(ExamBlueBg, ExamBlueText, ExamBlueLabel)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = exam.title,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${exam.date} • ${exam.daysLeft} dias faltantes",
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Gray)
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAccessButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.DarkGray)
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}
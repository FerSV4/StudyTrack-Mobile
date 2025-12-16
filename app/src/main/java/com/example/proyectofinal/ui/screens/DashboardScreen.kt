package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.model.TaskType
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*
import com.example.proyectofinal.ui.viewmodel.TaskViewModel
import com.example.proyectofinal.ui.viewmodel.TaskViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    userPrefs: UserPreferencesRepository
) {
    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(userPrefs)
    )

    val allTasks by viewModel.tasks.collectAsState()
    val userName by userPrefs.userName.collectAsState(initial = "Estudiante")

    val todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val todaysTasks = allTasks.filter { it.dueDate == todayStr }

    val upcomingEvents = allTasks.filter {
        (it.taskType == TaskType.EVENT || it.taskType == TaskType.CLASS) && it.dueDate != todayStr
    }.take(3)

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
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
                        Text(text = "Hola, ${userName.split(" ")[0]}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Hoy es $todayStr", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.White)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { SectionHeader("Para hoy (${todaysTasks.size})") }

                    if (todaysTasks.isEmpty()) {
                        item {
                            Text(
                                "¡Nada para hoy! 🎉",
                                color = Color.Gray,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        items(todaysTasks) { task ->
                            // Reutilizamos la tarjeta en modo "solo lectura"
                            TaskItemCard(task, {}, {}, {})
                        }
                    }

                    item { SectionHeader("Próximos Eventos") }

                    if (upcomingEvents.isEmpty()) {
                        item {
                            Text(
                                "Sin eventos próximos",
                                color = Color.Gray,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        items(upcomingEvents) { event ->
                            TaskItemCard(event, {}, {}, {})
                        }
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            QuickAccessButton("Calendario", Icons.Default.DateRange, Modifier.weight(1f), Color(0xFFE1BEE7)) { navController.navigate("calendar") }
                            QuickAccessButton("Tareas", Icons.Default.CheckCircle, Modifier.weight(1f), Color(0xFFC8E6C9)) { navController.navigate("tasks") }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            QuickAccessButton("Estadísticas", Icons.Default.BarChart, Modifier.weight(1f), Color(0xFFFFE0B2)) { navController.navigate("statistics") }
                            QuickAccessButton("Ajustes", Icons.Default.Settings, Modifier.weight(1f), Color(0xFFE3F2FD)) { navController.navigate("settings") }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Gray
        )
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
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
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.4f)
        ),
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
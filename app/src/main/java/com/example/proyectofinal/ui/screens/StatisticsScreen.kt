package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*
import com.example.proyectofinal.ui.viewmodel.TaskViewModel
import com.example.proyectofinal.ui.viewmodel.TaskViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    userPrefs: UserPreferencesRepository
) {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(userPrefs, context))
    val taskList by viewModel.tasks.collectAsState()

    val totalTasks = taskList.size
    val completedTasks = taskList.count { it.status == TaskStatus.COMPLETED }

    val percentage = if (totalTasks > 0) (completedTasks * 100) / totalTasks else 0

    val tasksBySubject = taskList.groupBy { it.subject }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas", color = Color.White) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GeneralProgressCard(percentage, completedTasks, totalTasks)
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Rendimiento por materia", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (tasksBySubject.isEmpty()) {
                            Text("No hay datos suficientes", fontSize = 14.sp, color = Color.Gray)
                        }

                        tasksBySubject.forEach { (subject, tasks) ->
                            val subjTotal = tasks.size
                            val subjCompleted = tasks.count { it.status == TaskStatus.COMPLETED }
                            val subjProgress = if(subjTotal > 0) subjCompleted.toFloat() / subjTotal.toFloat() else 0f

                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(subject, fontSize = 14.sp)
                                    Text("$subjCompleted/$subjTotal", fontSize = 12.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = subjProgress,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = PrimaryBlue,
                                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GeneralProgressCard(percentage: Int, completed: Int, total: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Finalización general", color = PrimaryBlue, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$percentage%", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                Spacer(modifier = Modifier.width(16.dp))
                LinearProgressIndicator(
                    progress = percentage / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = StatBlue,
                    trackColor = Color(0xFFE3F2FD)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("$completed de $total tareas completadas", color = StatBlue, fontSize = 12.sp)
        }
    }
}
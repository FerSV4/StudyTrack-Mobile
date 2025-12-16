package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*
import com.example.proyectofinal.ui.viewmodel.TaskViewModel
import com.example.proyectofinal.ui.viewmodel.TaskViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    userPrefs: UserPreferencesRepository
) {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(userPrefs, context))
    val allTasks by viewModel.tasks.collectAsState()

    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val tasksForDay = allTasks.filter { it.dueDate == selectedDateStr }

    val monthTitle = currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es", "ES")).replaceFirstChar { it.uppercase() }
    val yearTitle = currentYearMonth.year.toString()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Calendario", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("$monthTitle $yearTitle", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        currentYearMonth = YearMonth.now()
                        selectedDate = LocalDate.now()
                    }) {
                        Icon(Icons.Default.Today, "Hoy", tint = Color.White)
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
                .background(PrimaryBlue)
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { currentYearMonth = currentYearMonth.minusMonths(1) }) {
                            Icon(Icons.Default.ChevronLeft, "Anterior")
                        }

                        Text(
                            text = "$monthTitle $yearTitle",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(onClick = { currentYearMonth = currentYearMonth.plusMonths(1) }) {
                            Icon(Icons.Default.ChevronRight, "Siguiente")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa").forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val daysInMonth = currentYearMonth.lengthOfMonth()
                    val firstDayOfWeek = currentYearMonth.atDay(1).dayOfWeek.value % 7

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.height(240.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(firstDayOfWeek) {
                            Spacer(modifier = Modifier.size(36.dp))
                        }

                        items(daysInMonth) { index ->
                            val dayNum = index + 1
                            val date = currentYearMonth.atDay(dayNum)
                            val isSelected = date == selectedDate
                            val isToday = date == LocalDate.now()

                            val bgColor = when {
                                isSelected -> PrimaryBlue
                                isToday -> PrimaryBlue.copy(alpha = 0.1f)
                                else -> Color.Transparent
                            }

                            val txtColor = when {
                                isSelected -> Color.White
                                isToday -> PrimaryBlue
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(bgColor)
                                    .clickable { selectedDate = date }
                            ) {
                                Text(
                                    text = "$dayNum",
                                    color = txtColor,
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                                )

                                val hasTask = allTasks.any { it.dueDate == date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
                                if (hasTask && !isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 4.dp)
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlue)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tareas para el ${selectedDate.dayOfMonth} de $monthTitle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (tasksForDay.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.EventAvailable, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Sin tareas para este día", color = Color.Gray)
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(tasksForDay) { task ->
                                TaskItemCard(
                                    task = task,
                                    onItemClick = {},
                                    onStatusChange = { viewModel.toggleTaskStatus(it) },
                                    onDelete = { viewModel.deleteTask(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
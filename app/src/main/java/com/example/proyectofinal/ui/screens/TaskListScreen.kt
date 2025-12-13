package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.data.TaskBuilder
import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController) {
    val taskList = remember {
        listOf(
            TaskBuilder()
                .setTitle("Enviar tarea App Movil")
                .setSubject("App Movil")
                .setPriority(Priority.HIGH)
                .setDueDate(LocalDateTime.now().plusDays(2))
                .build(),
            TaskBuilder()
                .setTitle("Presentacion proyecto")
                .setSubject("Diseño")
                .setPriority(Priority.MEDIUM)
                .setDueDate(LocalDateTime.now().plusDays(3))
                .build(),
            TaskBuilder()
                .setTitle("Revisar notas")
                .setSubject("Criptografia")
                .setPriority(Priority.LOW)
                .setDueDate(LocalDateTime.now().plusDays(4))
                .build(),
            TaskBuilder()
                .setTitle("Leer tecnicas de comunicacion")
                .setSubject("Comunicacion A.")
                .setStatus(TaskStatus.COMPLETED)
                .setDueDate(LocalDateTime.now().minusDays(1))
                .build()
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            AddTaskContent(onClose = { showBottomSheet = false })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis tareas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                actions = {
                    Button(
                        onClick = { showBottomSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text("+ Crear tarea", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = true, onClick = {}, label = { Text("Todas (5)") })
                FilterChip(selected = false, onClick = {}, label = { Text("Pendiente (4)") })
                FilterChip(selected = false, onClick = {}, label = { Text("Completado (1)") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(taskList) { task ->
                    TaskItemCard(task)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskContent(onClose: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Nueva Tarea",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Materia") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Prioridad", fontWeight = FontWeight.Medium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Priority.values().forEach { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = { selectedPriority = priority },
                    label = { Text(priority.name.lowercase().capitalize()) },
                    leadingIcon = if (selectedPriority == priority) {
                        { Icon(Icons.Default.Check, null) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fecha")
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Hora")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Guardar Tarea", fontSize = 16.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItemCard(task: Task) {
    val isCompleted = task.status == TaskStatus.COMPLETED
    val textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val contentAlpha = if (isCompleted) 0.5f else 1f

    val priorityColor = when(task.priority) {
        Priority.HIGH -> PriorityHigh
        Priority.MEDIUM -> PriorityMedium
        Priority.LOW -> PriorityLow
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4CAF50),
                    uncheckedColor = Color.Gray
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .alpha(contentAlpha)
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = textDecoration
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = TagPurple.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = task.subject,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val dateStr = task.dueDate?.format(DateTimeFormatter.ofPattern("MMM dd, h:mm a")) ?: "Sin fecha"
                    Text(text = "Para: $dateStr", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                if (!isCompleted) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = priorityColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Prioridad ${task.priority.name.lowercase()}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Column {
                Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
            }
        }
    }
}
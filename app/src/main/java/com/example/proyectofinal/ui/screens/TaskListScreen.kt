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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.data.UserPreferencesRepository
import com.example.proyectofinal.model.Priority
import com.example.proyectofinal.model.Task
import com.example.proyectofinal.model.TaskStatus
import com.example.proyectofinal.model.TaskType
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*
import com.example.proyectofinal.ui.viewmodel.TaskViewModel
import com.example.proyectofinal.ui.viewmodel.TaskViewModelFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class TaskFilter { ALL, PENDING, COMPLETED }

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavController,
    userPrefs: UserPreferencesRepository
) {
    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(userPrefs)
    )

    val taskList by viewModel.tasks.collectAsState()

    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val filteredList = when (selectedFilter) {
        TaskFilter.ALL -> taskList
        TaskFilter.PENDING -> taskList.filter { it.status == TaskStatus.PENDING }
        TaskFilter.COMPLETED -> taskList.filter { it.status == TaskStatus.COMPLETED }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun openCreateModal() {
        taskToEdit = null
        showBottomSheet = true
    }

    fun openEditModal(task: Task) {
        taskToEdit = task
        showBottomSheet = true
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface // Fix Modo Oscuro
        ) {
            TaskFormContent(
                task = taskToEdit,
                onClose = { showBottomSheet = false },
                onSave = { title, subject, desc, priority, date, type ->
                    if (taskToEdit == null) {
                        viewModel.addTask(title, subject, desc, priority, date, type)
                    } else {
                        val updatedTask = taskToEdit!!.copy(
                            title = title,
                            subject = subject,
                            description = desc,
                            priority = priority,
                            dueDate = date,
                            taskType = type
                        )
                        viewModel.updateTask(updatedTask)
                    }
                }
            )
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
                        onClick = { openCreateModal() },
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
                onClick = { openCreateModal() },
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
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedFilter == TaskFilter.ALL,
                    onClick = { selectedFilter = TaskFilter.ALL },
                    label = { Text("Todas (${taskList.size})") }
                )
                FilterChip(
                    selected = selectedFilter == TaskFilter.PENDING,
                    onClick = { selectedFilter = TaskFilter.PENDING },
                    label = { Text("Pendiente") }
                )
                FilterChip(
                    selected = selectedFilter == TaskFilter.COMPLETED,
                    onClick = { selectedFilter = TaskFilter.COMPLETED },
                    label = { Text("Completado") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tareas aquí", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredList) { task ->
                        TaskItemCard(
                            task = task,
                            onItemClick = { openEditModal(task) },
                            onStatusChange = { viewModel.toggleTaskStatus(it) },
                            onDelete = { viewModel.deleteTask(it) }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormContent(
    task: Task?,
    onClose: () -> Unit,
    onSave: (String, String, String, Priority, String, TaskType) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var subject by remember { mutableStateOf(task?.subject ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(task?.priority ?: Priority.MEDIUM) }
    var selectedType by remember { mutableStateOf(task?.taskType ?: TaskType.TASK) }

    var selectedDateStr by remember { mutableStateOf(task?.dueDate ?: "2025-12-20") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val instant = Instant.ofEpochMilli(millis)
                        val localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate()
                        selectedDateStr = localDate.format(formatter)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = if (task == null) "Nueva Tarea" else "Editar Tarea",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = subject, onValueChange = { subject = it },
            label = { Text("Materia") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description, onValueChange = { description = it },
            label = { Text("Descripción (Opcional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            minLines = 3, maxLines = 5
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Tipo de Actividad", fontWeight = FontWeight.Medium, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskType.values().forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    leadingIcon = if (selectedType == type) { { Icon(Icons.Default.Check, null) } } else null
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Prioridad", fontWeight = FontWeight.Medium, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Priority.values().forEach { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = { selectedPriority = priority },
                    label = { Text(priority.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    leadingIcon = if (selectedPriority == priority) { { Icon(Icons.Default.Check, null) } } else null
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // FECHA
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Fecha: $selectedDateStr")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (title.isNotEmpty() && subject.isNotEmpty()) {
                    onSave(title, subject, description, selectedPriority, selectedDateStr, selectedType)
                    onClose()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(if (task == null) "Guardar Tarea" else "Actualizar Tarea", fontSize = 16.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItemCard(
    task: Task,
    onItemClick: () -> Unit,
    onStatusChange: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    val isCompleted = task.status == TaskStatus.COMPLETED
    val textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val contentAlpha = if (isCompleted) 0.5f else 1f

    val priorityColor = when (task.priority) {
        Priority.HIGH -> PriorityHigh
        Priority.MEDIUM -> PriorityMedium
        Priority.LOW -> PriorityLow
    }

    val typeIcon = when(task.taskType) {
        TaskType.TASK -> Icons.Default.CheckCircle
        TaskType.EVENT -> Icons.Default.Event
        TaskType.CLASS -> Icons.Default.School
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onStatusChange(task) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4CAF50),
                    uncheckedColor = Color.Gray
                )
            )

            Column(
                modifier = Modifier.weight(1f).padding(start = 8.dp).alpha(contentAlpha)
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = textDecoration,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = TagPurple.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp)) {
                        Text(text = task.subject, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(typeIcon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = task.taskType.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = task.dueDate ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                if (!isCompleted) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(color = priorityColor, shape = RoundedCornerShape(4.dp)) {
                        Text(text = "Prioridad ${task.priority.name.lowercase()}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
                    }
                }
            }
            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}
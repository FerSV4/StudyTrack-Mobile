package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.data.ScheduleCardFactory
import com.example.proyectofinal.data.ScheduleItem
import com.example.proyectofinal.ui.components.BottomNavBar
import com.example.proyectofinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CalendarScreen(navController: NavController) {

    val scheduleItems = remember {
        listOf(
            ScheduleItem.ClassSession("Clase app Movil", "10:50 AM - 13:00 PM", "C 2-1"),
            ScheduleItem.TaskDeadline("Proyecto QA II", "11:59 PM"),
            ScheduleItem.Event("Conferencia UCB", "3:00 PM - 5:00 PM", "Posgrado Equipetrol")
        )
    }

    var selectedDay by remember { mutableStateOf(9) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Calendario", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Diciembre 2025", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, "Anterior", tint = Color.White) }
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, "Siguiente", tint = Color.White) }
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
                .padding(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, "") }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FakeDropdown("Sep")
                            FakeDropdown("2025")
                        }

                        IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, "") }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Días de la semana
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                            Text(text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grid de días
                    val daysList = (1..30).toList()
                    val offset = 2

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.height(240.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(offset) { Spacer(modifier = Modifier.size(30.dp)) }

                        items(daysList.size) { index ->
                            val dayNum = index + 1
                            val isSelected = dayNum == selectedDay

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CalendarSelectedDay else Color.Transparent)
                                    .clickable { selectedDay = dayNum }
                            ) {
                                Text(
                                    text = "$dayNum",
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(scheduleItems) { item ->
                    ScheduleCardFactory.CreateCard(item)
                }
            }
        }
    }
}

@Composable
fun FakeDropdown(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.ArrowDropDown, "", modifier = Modifier.size(16.dp))
        }
    }
}
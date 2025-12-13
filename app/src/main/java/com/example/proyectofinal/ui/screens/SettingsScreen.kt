package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // IMPORTANTE
import com.example.proyectofinal.data.SettingsManager
import com.example.proyectofinal.ui.components.BottomNavBar // IMPORTANTE
import com.example.proyectofinal.ui.theme.BackgroundLight
import com.example.proyectofinal.ui.theme.PrimaryBlue
import com.example.proyectofinal.ui.theme.WhiteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // PATRÓN OBSERVER
    // SINGLETON
    val settingsState by SettingsManager.settingsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProfileCard(name = settingsState.userName, email = settingsState.userEmail)


            SettingsSectionCard(title = "Notificaciones", icon = Icons.Default.Notifications) {
                SettingsSwitchRow(
                    label = "Notificaciones push",
                    subLabel = "Se notificado de tus tareas",
                    checked = settingsState.notificationsPush,
                    onCheckedChange = { SettingsManager.togglePushNotifications(it) } //SINGLETON
                )
                SettingsSwitchRow(
                    label = "Emails recordatorio",
                    subLabel = "Avisos por email",
                    checked = settingsState.notificationsEmail,
                    onCheckedChange = { SettingsManager.updateSetting { s -> s.copy(notificationsEmail = it) } }
                )
                SettingsSwitchRow(
                    label = "Alerta de examenes",
                    subLabel = "Recordatorios 1 semana antes",
                    checked = settingsState.alertsExams,
                    onCheckedChange = { SettingsManager.updateSetting { s -> s.copy(alertsExams = it) } }
                )
            }

            //Apariencia
            SettingsSectionCard(title = "Apariencia", icon = Icons.Default.DarkMode) {
                SettingsSwitchRow(
                    label = "Modo oscuro",
                    subLabel = "Usar modo oscuro",
                    checked = settingsState.isDarkMode,
                    onCheckedChange = { SettingsManager.toggleDarkMode(it) } //SINGLETON
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Cambios", modifier = Modifier.padding(8.dp))
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Sesión", modifier = Modifier.padding(8.dp))
            }
        }
    }
}


@Composable
fun ProfileCard(name: String, email: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = name.take(1), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = email, color = Color.Gray, fontSize = 14.sp)
                    Text(text = "Ingeniería de Software", color = PrimaryBlue, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = {}, shape = RoundedCornerShape(50)) {
                Text("Editar perfil")
            }
        }
    }
}

@Composable
fun SettingsSectionCard(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun SettingsSwitchRow(label: String, subLabel: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.Medium)
            Text(subLabel, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
        )
    }
}
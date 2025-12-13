package com.example.proyectofinal.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ui.theme.*

sealed class ScheduleItem {
    data class ClassSession(val title: String, val time: String, val room: String) : ScheduleItem()
    data class TaskDeadline(val title: String, val time: String) : ScheduleItem()
    data class Event(val title: String, val timeRange: String, val location: String) : ScheduleItem()
}

object ScheduleCardFactory {

    @Composable
    fun CreateCard(item: ScheduleItem) {
        when (item) {
            is ScheduleItem.ClassSession -> RenderClassCard(item)
            is ScheduleItem.TaskDeadline -> RenderTaskCard(item)
            is ScheduleItem.Event -> RenderEventCard(item)
        }
    }


    @Composable
    private fun RenderClassCard(item: ScheduleItem.ClassSession) {
        BaseScheduleCard(
            bgColor = ClassBlueBg,
            tagColor = ClassTagBg,
            tagText = "Clase",
            textColor = ClassBlueText
        ) {
            Text(text = item.title, fontWeight = FontWeight.Bold, color = ClassBlueText)
            Text(text = item.time, fontSize = 12.sp, color = ClassBlueText)
            Text(text = item.room, fontSize = 12.sp, color = Color.Gray)
        }
    }

    @Composable
    private fun RenderTaskCard(item: ScheduleItem.TaskDeadline) {
        BaseScheduleCard(
            bgColor = TaskOrangeBg,
            tagColor = TaskTagBg,
            tagText = "Tarea",
            textColor = TaskOrangeText
        ) {
            Text(text = item.title, fontWeight = FontWeight.Bold, color = TaskOrangeText)
            Text(text = "Entrega: ${item.time}", fontSize = 12.sp, color = TaskOrangeText, fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    private fun RenderEventCard(item: ScheduleItem.Event) {
        BaseScheduleCard(
            bgColor = EventGreenBg,
            tagColor = EventTagBg,
            tagText = "Evento",
            textColor = EventGreenText
        ) {
            Text(text = item.title, fontWeight = FontWeight.Bold, color = EventGreenText)
            Text(text = item.timeRange, fontSize = 12.sp, color = EventGreenText)
            Text(text = item.location, fontSize = 12.sp, color = Color.Gray)
        }
    }

    @Composable
    private fun BaseScheduleCard(
        bgColor: Color,
        tagColor: Color,
        tagText: String,
        textColor: Color,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bgColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    content()
                }
                Box(
                    modifier = Modifier
                        .background(tagColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = tagText, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
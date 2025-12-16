package com.example.proyectofinal.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.proyectofinal.model.Task
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object NotificationScheduler {

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification(context: Context, task: Task) {
        if (task.dueDate == null || task.id == null) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TASK_ID", task.id)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_SUBJECT", task.subject)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PRUEBA 10 SEGUNDOS
        val triggerTime = System.currentTimeMillis() + 10_000 // 10,000 milisegundos = 10 seg

        Log.d("NotificationScheduler", "PRUEBA: Alarma programada para dentro de 10 segundos")

        /* ORIGINAL
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(task.dueDate, formatter)
        val localDateTime = LocalDateTime.of(localDate, LocalTime.of(9, 0))
        val triggerTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        */


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
            Log.w("NotificationScheduler", "No permission for exact alarm. Using inexact.")
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
            Log.d("NotificationScheduler", "Scheduled exact alarm for task ${task.id}")
        }
    }

    fun cancelNotification(context: Context, taskId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
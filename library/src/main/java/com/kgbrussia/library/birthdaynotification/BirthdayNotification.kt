package com.kgbrussia.library.birthdaynotification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kgbrussia.java.notification.NotificationRepository
import java.util.Calendar
import javax.inject.Inject

class BirthdayNotification @Inject constructor(private val context: Context) : NotificationRepository {

    private val intent = Intent(context, ContactBroadcastReceiver::class.java)

    override fun createNotification(id: Int, name: String, date: Calendar) {
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.timeInMillis, pendingIntent)
    }

    override fun deleteNotification(id: Int) {
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.cancel()
    }

    override fun checkNotificationState(id: Int): Boolean {
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE)
        return pendingIntent == null
    }
}
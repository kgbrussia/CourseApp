package com.kgbrussia.library

import android.app.AlarmManager
import android.app.PendingIntent
import com.kgbrussia.java.notification.NotificationRepository
import java.util.*

class StubBirthdayNotification(
    private val intent: PendingIntent,
    private val alarmManager: AlarmManager)
    : NotificationRepository {

    private val dummyNotifications: HashMap<Int, Long> = HashMap()

    override fun createNotification(id: Int, name: String, date: Calendar) {
        val time = date.timeInMillis
        dummyNotifications[id] = time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, intent)
    }

    override fun deleteNotification(id: Int) {
        dummyNotifications.remove(id)
        alarmManager.cancel(intent)
    }

    override fun checkNotificationState(id: Int): Boolean {
        return !dummyNotifications.containsKey(id)
    }
}
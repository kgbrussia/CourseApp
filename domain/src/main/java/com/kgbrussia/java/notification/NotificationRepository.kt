package com.kgbrussia.java.notification

import java.util.*

interface NotificationRepository {
    fun createNotification(id: Int, name: String, date: Calendar)
    fun deleteNotification(id: Int)
    fun checkNotificationState(id: Int): Boolean
}
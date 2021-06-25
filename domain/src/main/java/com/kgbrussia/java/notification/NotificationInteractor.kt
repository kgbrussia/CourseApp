package com.kgbrussia.java.notification

interface NotificationInteractor {
    fun newNotification(
        id: Int,
        contactName: String,
        dayOfBirthday: Int,
        monthOfBirthday: Int
    )

    fun checkNotificationState(id: Int): Boolean
}
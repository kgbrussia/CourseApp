package com.kgbrussia.library.birthdaynotification

interface NotificationContainer {
    fun inject(notificationReceiver: ContactBroadcastReceiver)
}
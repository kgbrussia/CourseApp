package com.kgbrussia.library.di

import com.kgbrussia.library.birthdaynotification.NotificationContainer
import com.kgbrussia.library.contactdetails.ContactDetailsContainer
import com.kgbrussia.library.contactlist.ContactListContainer

interface AppContainer {
    fun contactListContainer(): ContactListContainer
    fun contactDetailsContainer(): ContactDetailsContainer
    fun notificationContainer(): NotificationContainer
}
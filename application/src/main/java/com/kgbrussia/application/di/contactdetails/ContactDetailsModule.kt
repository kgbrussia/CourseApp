package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactDetailsScope
import com.kgbrussia.java.calendar.CalendarRepository
import com.kgbrussia.java.contactdetails.ContactDetailsInteractor
import com.kgbrussia.java.contactdetails.ContactDetailsModel
import com.kgbrussia.java.contactdetails.ContactDetailsRepository
import com.kgbrussia.java.notification.NotificationInteractor
import com.kgbrussia.java.notification.NotificationModel
import com.kgbrussia.java.notification.NotificationRepository
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @ContactDetailsScope
    @Provides
    fun providesContactDetailsInteractor(repository: ContactDetailsRepository): ContactDetailsInteractor =
        ContactDetailsModel(repository)

    @ContactDetailsScope
    @Provides
    fun providesBirthdayNotificationInteractor(
        notificationRepository: NotificationRepository,
        calendarRepository: CalendarRepository
    ): NotificationInteractor =
        NotificationModel(
            calendarRepository, notificationRepository
        )
}
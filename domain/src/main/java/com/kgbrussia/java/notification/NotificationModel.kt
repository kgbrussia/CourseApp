package com.kgbrussia.java.notification

import com.kgbrussia.java.calendar.CalendarRepository
import java.util.Calendar

class NotificationModel(
    private val calendarRepository: CalendarRepository,
    private val notificationRepository: NotificationRepository
) : NotificationInteractor {

    override fun newNotification(
        id: Int,
        contactName: String,
        dayOfBirthday: Int,
        monthOfBirthday: Int
    ) {
        if (checkNotificationState(id)) {
            createNotification(id, contactName, dayOfBirthday, monthOfBirthday)
        } else {
            notificationRepository.deleteNotification(id)
        }
    }

    override fun checkNotificationState(id: Int) =
        notificationRepository.checkNotificationState(id)

    private fun createNotification(
        id: Int,
        contactName: String,
        dayOfBirthday: Int,
        monthOfBirthday: Int
    ) {
        val birthday = changeYearIfNecessary(dayOfBirthday, monthOfBirthday)
        notificationRepository.createNotification(id, contactName, birthday)
    }

    private fun changeYearIfNecessary(dayOfBirthday: Int, monthOfBirthday: Int): Calendar {
        val calendar = calendarRepository.getCalendar()
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var newYear = calendar.get(Calendar.YEAR)
        if (month > monthOfBirthday || (
            month == monthOfBirthday &&
                day >= dayOfBirthday
            )
        ) {
            newYear++
        }
        if (monthOfBirthday == Calendar.FEBRUARY &&
            dayOfBirthday == 29
        ) {
            while (!isLeap(newYear)) {
                newYear++
            }
        }
        calendar.apply {
            set(Calendar.YEAR, newYear)
            set(Calendar.MONTH, monthOfBirthday)
            set(Calendar.DAY_OF_MONTH, dayOfBirthday)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

    private fun isLeap(year: Int) =
        ((year % 4) == 0 && (year % 100) != 0) || (year % 400) == 0
}
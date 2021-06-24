package com.kgbrussia.java.notification

import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.calendar.CalendarModel
import com.kgbrussia.java.calendar.CalendarRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Test
import org.mockito.Mockito
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationModelTest {

    @Mock
    private lateinit var notificationInteractor: NotificationInteractor
    private lateinit var calendarRepository: CalendarRepository
    private lateinit var notificationRepository: NotificationRepository
    private val contact = ContactEntity(
        id = 1,
        name = "Иван Иванович",
        phone ="",
        dayOfBirthday = 8,
        monthOfBirthday = 9,
        photo = ""
    )

    @Before
    fun before() {
        notificationRepository =
            Mockito.mock(NotificationRepository::class.java)
        calendarRepository = CalendarModel()
        notificationInteractor =
            NotificationModel(calendarRepository, notificationRepository)
    }

    @Test
    fun `successfully adding a reminder`() {
        Mockito.`when`(notificationRepository.checkNotificationState(contact.id))
            .thenReturn(true)
        calendarRepository.setCalendarDate(9, Calendar.SEPTEMBER, 1999)
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            contact.dayOfBirthday!!,
            contact.monthOfBirthday!!-1
        )
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DATE, 8)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Mockito.verify(notificationRepository)
            .createNotification(contact.id, contact.name, nextBirthday)
    }

    @Test
    fun `successful addition of a reminder bd was not yet in the current year`() {
        Mockito.`when`(notificationRepository.checkNotificationState(contact.id))
            .thenReturn(true)
        calendarRepository.setCalendarDate(7, Calendar.SEPTEMBER, 1999)
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            contact.dayOfBirthday!!,
            contact.monthOfBirthday!!-1
        )
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1999)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DATE, 8)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Mockito.verify(notificationRepository)
            .createNotification(contact.id, contact.name, nextBirthday)
    }

    @Test
    fun `successfully deleting a reminder`() {
        Mockito.`when`(notificationRepository.checkNotificationState(contact.id))
            .thenReturn(false)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DATE, 8)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendarRepository.setCalendarDate(9, Calendar.SEPTEMBER, 1999)
        notificationRepository.createNotification(
            contact.id,
            contact.name,
            nextBirthday)
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            contact.dayOfBirthday!!,
            contact.monthOfBirthday!!-1
        )
        Mockito.verify(notificationRepository).deleteNotification(contact.id)
    }

    @Test
    fun `adding a reminder for a contact born on February 29`() {
        val contact = contact.copy(
            name = "Павел Павлович",
            dayOfBirthday = 29,
            monthOfBirthday = 2,
        )
        Mockito.`when`(notificationRepository.checkNotificationState(contact.id))
            .thenReturn(true)
        calendarRepository.setCalendarDate(2, Calendar.MARCH, 1999)
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            contact.dayOfBirthday!!,
            contact.monthOfBirthday!!-1
        )
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH,  Calendar.FEBRUARY)
            set(Calendar.DATE, 29)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Mockito.verify(notificationRepository)
            .createNotification(contact.id, contact.name, nextBirthday)
    }

    @Test
    fun `adding a reminder for a contact born on February 29 in a leap year`() {
        val contact = contact.copy(
            name = "Павел Павлович",
            dayOfBirthday = 29,
            monthOfBirthday = 2,
        )
        Mockito.`when`(notificationRepository.checkNotificationState(contact.id))
            .thenReturn(true)
        calendarRepository.setCalendarDate(1, Calendar.MARCH, 2000)
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            contact.dayOfBirthday!!,
            contact.monthOfBirthday!!-1
        )
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2004)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DATE, 29)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Mockito.verify(notificationRepository)
            .createNotification(contact.id, contact.name, nextBirthday)
    }

    @After
    fun tearDown() {
    }
}
package com.kgbrussia.library.contactdetails

import android.app.AlarmManager
import android.app.PendingIntent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.calendar.CalendarModel
import com.kgbrussia.java.calendar.CalendarRepository
import com.kgbrussia.java.contactdetails.ContactDetailsInteractor
import com.kgbrussia.java.notification.NotificationInteractor
import com.kgbrussia.java.notification.NotificationModel
import com.kgbrussia.java.notification.NotificationRepository
import com.kgbrussia.library.StubBirthdayNotification
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ContactDetailsViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var intent: PendingIntent

    @Mock
    private lateinit var alarmManager: AlarmManager

    @Mock
    private lateinit var contactDetailsInteractor: ContactDetailsInteractor

    private lateinit var calendarRepository: CalendarRepository
    private lateinit var notificationInteractor: NotificationInteractor
    private lateinit var viewModel: ContactDetailsViewModel
    private lateinit var birthdayNotification: NotificationRepository

    private val contact = ContactEntity(
        id = 1,
        name = "Иван Иванович",
        phone = "",
        dayOfBirthday = 8,
        monthOfBirthday = 9,
        photo = ""
    )

    @Before
    fun before() {
        birthdayNotification =
            StubBirthdayNotification(intent, alarmManager)
        calendarRepository = CalendarModel()
        notificationInteractor =
            NotificationModel(calendarRepository, birthdayNotification)
        viewModel =
            ContactDetailsViewModel(
                contactDetailsInteractor,
                notificationInteractor
            )
    }

    @Test
    fun `successfully adding a reminder`() {
        calendarRepository.setCalendarDate(9, Calendar.SEPTEMBER, 1999)
        viewModel.newNotification(contact)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DATE, 8)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        verify(alarmManager)
            .set(AlarmManager.RTC_WAKEUP, nextBirthday.timeInMillis, intent)
    }

    @Test
    fun `successful addition of a reminder bd was not yet in the current year`() {
        calendarRepository.setCalendarDate(7, Calendar.SEPTEMBER, 1999)
        viewModel.newNotification(contact)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1999)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DATE, 8)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        verify(alarmManager)
            .set(AlarmManager.RTC_WAKEUP, nextBirthday.timeInMillis, intent)
    }

    @Test
    fun `successfully deleting a reminder`() {
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
        birthdayNotification.createNotification(contact.id, contact.name, nextBirthday)
        viewModel.newNotification(contact)
        verify(alarmManager).cancel(intent)
    }

    @Test
    fun `adding a reminder for a contact born on February 29`() {
        calendarRepository.setCalendarDate(2, Calendar.MARCH, 1999)
        viewModel.newNotification(
            contact.copy(
                name = "Павел Павлович",
                dayOfBirthday = 29,
                monthOfBirthday = 2,
            )
        )
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DATE, 29)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        verify(alarmManager)
            .set(AlarmManager.RTC_WAKEUP, nextBirthday.timeInMillis, intent)
    }

    @Test
    fun `adding a reminder for a contact born on February 29 in a leap year`() {
        calendarRepository.setCalendarDate(1, Calendar.MARCH, 2000)
        viewModel.newNotification(
            contact.copy(
                name = "Павел Павлович",
                dayOfBirthday = 29,
                monthOfBirthday = 2,
            )
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
        verify(alarmManager)
            .set(AlarmManager.RTC_WAKEUP, nextBirthday.timeInMillis, intent)
    }

    @After
    fun tearDown() {
    }
}
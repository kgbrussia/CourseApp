package com.kgbrussia.library.birthdaynotification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kgbrussia.courseapp.library.R
import com.kgbrussia.java.calendar.CalendarRepository
import com.kgbrussia.java.notification.NotificationInteractor
import com.kgbrussia.java.notification.NotificationRepository
import com.kgbrussia.library.MainActivity
import com.kgbrussia.library.contactdetails.CONTACT_NAME
import com.kgbrussia.library.contactdetails.ID_ARG
import com.kgbrussia.library.di.HasComponent
import java.util.*
import javax.inject.Inject

const val CHANNEL_ID = "CHANNEL_ID"

class ContactBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var calendarRepositiry: CalendarRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var notificationInteractor: NotificationInteractor

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as HasComponent)
            .getAppComponent()
            .notificationContainer()
            .inject(this)
        val contactId = intent.extras?.getInt(ID_ARG) ?: 123
        val contactName = intent.extras?.getString(CONTACT_NAME) ?: "batman"
        createNotificationChannel(context)
        createNotification(context, contactId, contactName)
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, context.getString(
                R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        context: Context,
        contactId: Int,
        contactName: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(ID_ARG, contactId)
        val pendingIntent = PendingIntent.getActivity(
            context,
            contactId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(context.getString(R.string.notification_text, contactName))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val motificationManager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as? NotificationManager)
        motificationManager?.notify(contactId, notification)
        nextBirthday(contactId, contactName)
    }

    private fun nextBirthday(id: Int, name: String) {
        val calendar = calendarRepositiry.getCalendar()
        notificationRepository.deleteNotification(id)
        notificationInteractor.newNotification(
            id,
            name,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH)
        )
    }
}
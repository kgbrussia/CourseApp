package com.kgbrussia.courseapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.*

const val CHANNEL_ID = "CHANNEL_ID"

class ContactBroadcastReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null
    private var contactId: Int? = 123
    private var contactName: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        contactId = intent.extras?.getInt(ID_ARG)
        contactName = intent.extras?.getString(CONTACT_NAME)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        createNotificationChannel(context)
        createNotification(context, contactId!!)
        val pendingIntent = PendingIntent.getBroadcast(context, contactId!!, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextCalendarBirthday().timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(context: Context, id: Int) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(ID_ARG, id)
        val pendingIntent = PendingIntent.getActivity(context, contactId!!, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.batman)
            .setContentText(context.getString(R.string.notification_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager?.notify(contactId!!, notification)
    }

    private fun nextCalendarBirthday(): Calendar {
        val calendar = GregorianCalendar.getInstance()
        if(calendar.get(Calendar.MONTH) == Calendar.FEBRUARY && calendar.get(Calendar.DAY_OF_MONTH) == 29) {
            calendar.add(Calendar.YEAR, 4)
        } else {
            calendar.add(Calendar.YEAR, 1)
        }
        return calendar
    }
}
package com.kgbrussia.courseapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import java.util.*


const val ID_ARG = "CONTACT_ID"
const val CONTACT_NAME = "CONTACT_NAME"

class ContactDetailsFragment : Fragment() {

    private var serviceInterface: ContactService.ServiceInterface? = null
    private var currentContact: Contact? = null
    private var isNotificationsEnabled = false
    private var buttonReminder: Button? = null
    private var contactId: Int = 123

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ContactService.ServiceInterface){
            serviceInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonReminder = view.findViewById(R.id.button_birthday_reminder)
        loadContactInfoById(arguments?.getString(ID_ARG)!!.toInt())
        updateButtonState()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceInterface = null
    }

    private fun updateButtonState() {
        isNotificationsEnabled = PendingIntent.getBroadcast(context, contactId, Intent(activity, ContactBroadcastReceiver::class.java), PendingIntent.FLAG_NO_CREATE) != null
        if(isNotificationsEnabled) {
            buttonReminder!!.text = getString(R.string.turn_off_notifications)
        } else {
            buttonReminder!!.text = getString(R.string.turn_on_notifications)
        }
    }

    private fun clickOnNotificationButton() {
        val pendingIntent = createPendingIntent()
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if(isNotificationsEnabled) {
            buttonReminder!!.text = getString(R.string.turn_on_notifications)
            isNotificationsEnabled = false
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
        } else {
            buttonReminder!!.text = getString(R.string.turn_off_notifications)
            isNotificationsEnabled = true
            alarmManager?.set(AlarmManager.RTC_WAKEUP, nextCalendarBirthday().timeInMillis, pendingIntent)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(activity, ContactBroadcastReceiver::class.java)
        intent.putExtra(ID_ARG, contactId)
        intent.putExtra(CONTACT_NAME, currentContact!!.name)
        return PendingIntent.getBroadcast(context, contactId, intent, 0)
    }

    private fun nextCalendarBirthday(): Calendar {
        val currentCalendar = GregorianCalendar.getInstance()
        val birthdayCalendar = GregorianCalendar.getInstance()
        birthdayCalendar.set(Calendar.DAY_OF_MONTH, currentContact!!.dayOfBirthday!!)
        birthdayCalendar.set(Calendar.MONTH, currentContact!!.monthOfBirthday!!)
        birthdayCalendar.set(Calendar.HOUR_OF_DAY, 14)
        birthdayCalendar.set(Calendar.MINUTE, 40)
        birthdayCalendar.set(Calendar.SECOND, 20)
        if(birthdayCalendar.before(currentCalendar)) {
            birthdayCalendar.add(Calendar.YEAR, 1)
        }
        return birthdayCalendar
    }

    fun loadContactInfoById(id : Int) = serviceInterface?.getService()?.getInfoById(object:
        GetDetailsByIdListener {
        override fun onSuccess(list: List<Contact>) {
            list.forEach{
                if (it.id == id){
                    requireActivity().runOnUiThread({
                        currentContact = it
                        view?.findViewById<TextView>(R.id.textViewDescription)?.text =
                            "${it.id} ${it.name} ${it.phone}\nbirthday: ${it.dayOfBirthday}.${it.monthOfBirthday}"
                    })
                }
            }
            buttonReminder!!.setOnClickListener { clickOnNotificationButton() }
        }
    })

    interface GetDetailsByIdListener {
        fun onSuccess(list: List<Contact>)
    }

    companion object{
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }
}
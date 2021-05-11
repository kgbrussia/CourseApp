package com.kgbrussia.courseapp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

const val ID_ARG = "CONTACT_ID"
const val CONTACT_NAME = "CONTACT_NAME"
const val CONTACT_PERMISSION = "CONTACT_PERMISSION"

class ContactDetailsFragment : Fragment() {

    private var displayer: ContactPermissionDialog.PermissionDialogDisplayer? = null
    private var currentContact: Contact? = null
    private var isNotificationsEnabled = false
    private var buttonReminder: Button? = null
    private var progressBar: ProgressBar? = null
    private var contactId: Int = arguments?.getInt(ID_ARG) ?: 0
    private var viewModel: ContactDetailsViewModel? = null
    private var contactObserver = Observer<Contact> {
        currentContact = it
        displayScreenData()
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                loadContactInfoById()
            } else {
                when {
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                        displayer?.displayPermissionDialog(R.string.contactPermissionDialogDetails)
                    }
                    else -> {
                        Toast.makeText(
                            context,
                            getString(R.string.noAccessGranted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactPermissionDialog.PermissionDialogDisplayer) {
            displayer = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReminderButton()
        initProgressBar()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onDestroyView() {
        buttonReminder = null
        progressBar = null
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        displayer = null
        requestPermissionLauncher.unregister()
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadContactInfoById()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                displayer?.displayPermissionDialog(R.string.contactPermissionDialogDetails)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
    private fun initProgressBar() {
        progressBar = requireView().findViewById(R.id.progress_bar_details)
        viewModel?.isLoadingIndicatorVisible?.observe(viewLifecycleOwner, Observer { isLoadingIndicatorVisible ->
            progressBar?.isVisible = isLoadingIndicatorVisible
        })
    }

    private fun initViewModel(){
        viewModel?.contact?.observe(viewLifecycleOwner, contactObserver)
    }

    private fun initReminderButton(){
        buttonReminder = requireView().findViewById(R.id.button_birthday_reminder)
        updateButtonState()
        buttonReminder?.setOnClickListener { clickOnNotificationButton() }
    }

    private fun updateButtonState() {
        isNotificationsEnabled = PendingIntent.getBroadcast(
            context, contactId,
            Intent(activity, ContactBroadcastReceiver::class.java), PendingIntent.FLAG_NO_CREATE
        ) != null
        if (isNotificationsEnabled) {
            buttonReminder?.text = getString(R.string.turn_off_notifications)
        } else {
            buttonReminder?.text = getString(R.string.turn_on_notifications)
        }
    }

    private fun clickOnNotificationButton() {
        val pendingIntent = createPendingIntent()
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (isNotificationsEnabled) {
            buttonReminder?.text = getString(R.string.turn_on_notifications)
            isNotificationsEnabled = false
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
        } else {
            buttonReminder?.text = getString(R.string.turn_off_notifications)
            isNotificationsEnabled = true
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                nextCalendarBirthday().timeInMillis,
                pendingIntent
            )
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(activity, ContactBroadcastReceiver::class.java)
        intent.putExtra(ID_ARG, contactId)
        intent.putExtra(CONTACT_NAME, currentContact?.name)
        return PendingIntent.getBroadcast(context, contactId, intent, 0)
    }

    private fun nextCalendarBirthday(): Calendar {
        val currentCalendar = GregorianCalendar.getInstance()
        val birthdayCalendar = GregorianCalendar.getInstance()
        birthdayCalendar.set(Calendar.DAY_OF_MONTH, currentContact?.dayOfBirthday ?: 1)
        birthdayCalendar.set(Calendar.MONTH, currentContact?.monthOfBirthday ?: 1)
        birthdayCalendar.set(Calendar.HOUR_OF_DAY, 14)
        birthdayCalendar.set(Calendar.MINUTE, 40)
        birthdayCalendar.set(Calendar.SECOND, 20)
        if (birthdayCalendar.before(currentCalendar)) {
            birthdayCalendar.add(Calendar.YEAR, 1)
        }
        return birthdayCalendar
    }

    private fun loadContactInfoById() =
        viewModel?.contactByIdLoaded(requireContext(), contactId.toString())

    private fun displayScreenData() {
        currentContact?.let { contact ->
            requireView().findViewById<TextView>(R.id.textViewName).text = contact.name
            requireView().findViewById<TextView>(R.id.textViewPhoneNumber).text = contact.phone

            val imageViewPhoto = requireView().findViewById<ImageView>(R.id.imageViewPhoto)
            val photoUri: Uri? = contact.photo
            if (photoUri != null) {
                imageViewPhoto?.setImageURI(photoUri)
            } else {
                imageViewPhoto?.setImageResource(R.drawable.batman)
            }

            if (contact.dayOfBirthday != null && contact.monthOfBirthday != null) {
                buttonReminder?.isEnabled = true
            }
        }
    }

    companion object {
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }
}
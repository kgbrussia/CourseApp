package com.kgbrussia.library.contactdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kgbrussia.courseapp.library.R
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.library.ContactPermissionDialog
import com.kgbrussia.library.di.HasComponent
import javax.inject.Inject

const val ID_ARG = "CONTACT_ID"
const val CONTACT_NAME = "CONTACT_NAME"
const val CONTACT_PERMISSION = "CONTACT_PERMISSION"

class ContactDetailsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: ContactDetailsViewModel
    private var displayer: ContactPermissionDialog.PermissionDialogDisplayer? = null
    private var currentContact: ContactEntity? = null
    private var buttonReminder: Button? = null
    private var progressBar: ProgressBar? = null
    private var contactId: Int = arguments?.getInt(ID_ARG) ?: 11
    private var contactObserver = Observer<ContactEntity> {
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
        injectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactId = arguments?.getString(ID_ARG)?.toInt() ?: 25
        initViewModel()
        initReminder()
        initProgressBar()
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

    private fun injectFragment() {
        (requireActivity().application as HasComponent)
            .getAppComponent()
            .contactDetailsContainer()
            .inject(this)
        viewModel = ViewModelProvider(this, factory).get(ContactDetailsViewModel::class.java)
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
        viewModel.isLoadingIndicatorVisible.observe(
            viewLifecycleOwner,
            { isLoadingIndicatorVisible ->
                progressBar?.isVisible = isLoadingIndicatorVisible
            }
        )
    }

    private fun initViewModel() {
        viewModel.contact.observe(viewLifecycleOwner, contactObserver)
    }

    private fun initReminder() {
        viewModel.checkNotificationState(contactId)
        buttonReminder = requireView().findViewById(R.id.button_birthday_reminder)
        viewModel.isNotificationEnabled.observe(viewLifecycleOwner, this::updateButtonState)
        buttonReminder?.setOnClickListener {
            currentContact?.let { contact -> viewModel.newNotification(contact) }
        }
    }

    private fun updateButtonState(isNotificationsEnabled: Boolean) {
        if (isNotificationsEnabled) {
            buttonReminder?.text = getString(R.string.turn_off_notifications)
        } else {
            buttonReminder?.text = getString(R.string.turn_on_notifications)
        }
    }

    private fun loadContactInfoById() {
        return viewModel.contactByIdLoaded(contactId.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun displayScreenData() {
        currentContact?.let { contact ->
            requireView().findViewById<TextView>(R.id.textViewName).text = contact.name
            requireView().findViewById<TextView>(R.id.textViewPhoneNumber).text = contact.phone
            val imageViewPhoto = requireView().findViewById<ImageView>(R.id.imageViewPhoto)
            var photoUri: Uri? = null
            if (contact.photo != null) {
                photoUri = Uri.parse(contact.photo)
            }
            if (photoUri != null) {
                imageViewPhoto?.setImageURI(photoUri)
            } else {
                imageViewPhoto?.setImageResource(R.drawable.batman)
            }
            val textViewDescription = requireView().findViewById<TextView>(R.id.textViewDescription)
            textViewDescription.text = "${contact.dayOfBirthday} ${contact.monthOfBirthday}"
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
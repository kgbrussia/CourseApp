package com.kgbrussia.courseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ContactListFragment : Fragment() {

    private var onContactClicked: OnContactClickedListener? = null
    private var isContactPermissionGranted = true
    private var displayer: ContactPermissionDialog.PermissionDialogDisplayer? = null
    private var contactListObserver = Observer<List<Contact>> {
        view?.findViewById<TextView>(R.id.textViewName)?.text =
            it[0].name
        view?.findViewById<TextView>(R.id.textViewPhoneNumber)?.text =
            it[0].phone
    }
    private var viewModel: ContactListViewModel? = null
    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
        if (isGranted) {
            loadContacts()
        } else {
            when {
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    displayer?.displayPermissionDialog(R.string.contactPermissionDialogList)
                }
                else -> {
                    Toast.makeText(context, getString(R.string.noAccessGranted), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnContactClickedListener){
            onContactClicked = context
        }
        if(context is ContactPermissionDialog.PermissionDialogDisplayer) {
            displayer = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.contactCard).setOnClickListener {onContactClicked?.onContactClicked("123")}
    }

    override fun onStart() {
        super.onStart()
        isContactPermissionGranted = arguments?.getBoolean(CONTACT_PERMISSION) ?: true
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED -> {
                loadContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                displayer?.displayPermissionDialog(R.string.contactPermissionDialogDetails)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        onContactClicked = null
        displayer = null
        requestPermissionLauncher.unregister()
    }

    private fun loadContacts() = viewModel?.getContactList(requireContext())
        ?.observe(viewLifecycleOwner, contactListObserver)

    interface OnContactClickedListener {
        fun onContactClicked(id : String)
    }

    companion object{
        fun newInstance(): ContactListFragment = ContactListFragment()

        fun newInstance(isContactPermissionGranted: Boolean): ContactListFragment {
            val args = Bundle()
            args.putBoolean(CONTACT_PERMISSION, isContactPermissionGranted)
            val fragment = ContactListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
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

class ContactListFragment : Fragment() {

    private var onContactClicked: OnContactClickedListener? = null
    private var serviceInterface: ContactService.ServiceInterface? = null
    private var isContactPermissionGranted = true
    private var displayer: ContactPermissionDialog.PermissionDialogDisplayer? = null
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
        if(context is ContactService.ServiceInterface){
            serviceInterface = context
        }
        if(context is ContactPermissionDialog.PermissionDialogDisplayer) {
            displayer = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onContactClicked = null
        serviceInterface = null
        displayer = null
        requestPermissionLauncher.unregister()
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
                displayer?.displayPermissionDialog(R.string.contactPermissionDialogList)
            }
            else -> {
                if(isContactPermissionGranted) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }
    }

    fun loadContacts() = serviceInterface?.getService()?.getContactList(object: GetContactListListener{
        override fun onSuccess(list: List<Contact>) {
            requireActivity().runOnUiThread({
                view?.findViewById<TextView>(R.id.textViewName)?.text =
                    list[1].name
                view?.findViewById<TextView>(R.id.textViewPhoneNumber)?.text =
                    list[1].phone
            })
        }
    })

    interface OnContactClickedListener {
        fun onContactClicked(id : String)
    }

    interface GetContactListListener {
        fun onSuccess(list: List<Contact>)
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
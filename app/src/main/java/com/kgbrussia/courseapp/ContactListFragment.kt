package com.kgbrussia.courseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ContactListFragment : Fragment() {

    private var onContactClicked: OnContactClickedListener? = null
    private var serviceInterface: ContactService.ServiceInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnContactClickedListener){
            onContactClicked = context
        }
        if(context is ContactService.ServiceInterface){
            serviceInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContacts()
        view.findViewById<View>(R.id.contactCard).setOnClickListener {onContactClicked?.onContactClicked("123")}
    }

    override fun onDestroy() {
        super.onDestroy()
        onContactClicked = null
        serviceInterface = null
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

    interface GetContactListListener {
        fun getContactList(list: String)
    }

    companion object{
        fun newInstance() = ContactListFragment()
    }
}
package com.kgbrussia.courseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ContactListFragment : Fragment() {

    private var onContactClicked: OnContactClickedListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnContactClickedListener){
            onContactClicked = context
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
        view.findViewById<View>(R.id.contactCard).setOnClickListener {onContactClicked?.onContactClicked(123)}
    }

    override fun onDestroy() {
        super.onDestroy()
        onContactClicked = null
    }

    interface OnContactClickedListener {
        fun onContactClicked(id : Int)
    }

    companion object{
        fun newInstance() = ContactListFragment()
    }
}
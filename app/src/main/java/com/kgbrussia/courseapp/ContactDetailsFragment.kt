package com.kgbrussia.courseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf

private const val ID_ARG = "ID_ARG"

class ContactDetailsFragment : Fragment() {

    private var serviceInterface: ContactService.ServiceInterface? = null

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
        loadContactInfoById(arguments?.getString(ID_ARG)!!.toInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceInterface = null
    }

    fun loadContactInfoById(id : Int) = serviceInterface?.getService()?.getInfoById(object:
        GetDetailsByIdListener {
        override fun onSuccess(list: List<Contact>) {
            list.forEach{
                if (it.id == id){
                    requireActivity().runOnUiThread({
                        view?.findViewById<TextView>(R.id.textViewDescription)?.text =
                            "${it.id} ${it.name} ${it.phone}"
                    })
                }
            }
        }
    })

    interface GetDetailsByIdListener {
        fun onSuccess(list: List<Contact>)
    }

    interface GetDetailsByIdListener {
        fun getDetailsById(list: String)
    }

    companion object{
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }
}
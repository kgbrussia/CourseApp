package com.kgbrussia.courseapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactListViewModel : ViewModel() {
    fun getContactList(context: Context) = ContactLoaderRepository.loadContactList(context)
}

class ContactDetailsViewModel : ViewModel() {
    fun getContactById(context: Context, id: String) = ContactLoaderRepository.loadContact(context, id)
}

object ContactLoaderRepository: ContactRepository {
    private lateinit var contactList: MutableLiveData<List<Contact>>
    private lateinit var contact: MutableLiveData<Contact>

    override fun loadContactList(context: Context) : LiveData<List<Contact>> {
        if(!::contactList.isInitialized) {
            contactList = MutableLiveData()
        }
        Thread {
            contactList.postValue(ContactResolver.getContactsList(context))
        }.start()
        return contactList
    }

    override fun loadContact(context: Context, id: String) : LiveData<Contact> {
        if(!::contact.isInitialized) {
            contact = MutableLiveData()
        }
        Thread {
            contact.postValue(ContactResolver.findContactById(context, id))
        }.start()
        return contact
    }
}

interface ContactRepository {
    fun loadContactList(context: Context): LiveData<List<Contact>>
    fun loadContact(context: Context, id: String): LiveData<Contact>
}
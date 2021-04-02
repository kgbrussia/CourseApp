package com.kgbrussia.courseapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

private const val SERVICE_THREAD_DELAY = 3000L

class ContactService : Service() {
    private val binder = ContactBinder()

    fun getContactList(getContactListListener: ContactListFragment.GetContactListListener?) {
        val weakReferenceListener = WeakReference(getContactListListener)
        thread {
            Thread.sleep(SERVICE_THREAD_DELAY)
            weakReferenceListener.get()?.onSuccess(createContacts())
        }
    }

    fun getInfoById(getDetailsByIdListener: ContactDetailsFragment.GetDetailsByIdListener?) {
        val weakReferenceListener = WeakReference(getDetailsByIdListener)
        thread {
            Thread.sleep(SERVICE_THREAD_DELAY)
            weakReferenceListener.get()?.onSuccess(createContacts())
        }
    }

    private fun createContacts(): List<Contact> =
        listOf(
            Contact(123,getString(R.string.contact_list_c1_name),getString(R.string.contact_list_c1_phone1)),
            Contact(234,getString(R.string.contact_list_c2_name),getString(R.string.contact_list_c2_phone1)),
            Contact(356,getString(R.string.contact_list_c3_name),getString(R.string.contact_list_c3_phone1))
        )

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    interface ServiceInterface {
        fun getService(): ContactService?
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }
}
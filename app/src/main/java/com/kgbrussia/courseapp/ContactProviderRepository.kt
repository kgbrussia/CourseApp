package com.kgbrussia.courseapp

import android.content.Context
import io.reactivex.rxjava3.core.Single

class ContactProviderRepository(private val context: Context) : ContactRepository {

    override fun loadContactList(name: String): Single<List<Contact>> =
        Single.fromCallable<List<Contact>> { ContactResolver.getContactsList(context, name) }

    override fun loadContact(id: String): Single<Contact> =
        Single.fromCallable<Contact> { ContactResolver.findContactById(context, id) }

}
package com.kgbrussia.library.data

import android.content.Context
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.contactlist.ContactListRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ContactProviderListRepository @Inject constructor(private val context: Context):
    ContactListRepository {

    override fun readContacts(name: String): Single<List<ContactEntity>> =
        Single.fromCallable { ContactResolver.getContactsList(context, name) }
}
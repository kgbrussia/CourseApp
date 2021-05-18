package com.kgbrussia.library.data

import android.content.Context
import com.kgbrussia.java.ContactDetailsRepository
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.ContactListRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ContactProviderDetailsRepository @Inject constructor(private val context: Context) :
    ContactDetailsRepository {

    override fun readContactById(id: String): Single<ContactEntity> =
        Single.fromCallable { ContactResolver.findContactById(context, id) }
}

class ContactProviderListRepository @Inject constructor(private val context: Context):
    ContactListRepository {

    override fun readContacts(name: String): Single<List<ContactEntity>> =
        Single.fromCallable { ContactResolver.getContactsList(context, name) }
}
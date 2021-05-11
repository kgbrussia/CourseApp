package com.kgbrussia.courseapp

import android.content.Context
import io.reactivex.rxjava3.core.Single

object ContactLoaderRepository : ContactRepository {

    override fun loadContactList(context: Context, name: String): Single<List<Contact>> =
        Single.fromCallable<List<Contact>> { ContactResolver.getContactsList(context, name) }

    override fun loadContact(context: Context, id: String): Single<Contact> =
        Single.fromCallable<Contact> { ContactResolver.findContactById(context, id) }

}
package com.kgbrussia.library.data

import android.content.Context
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.contactdetails.ContactDetailsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ContactProviderDetailsRepository @Inject constructor(private val context: Context) :
    ContactDetailsRepository {

    override fun readContactById(id: String): Single<ContactEntity> =
        Single.fromCallable { ContactResolver.findContactById(context, id) }
}
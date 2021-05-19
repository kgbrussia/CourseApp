package com.kgbrussia.java.contactlist

import com.kgbrussia.java.ContactEntity
import io.reactivex.rxjava3.core.Single

interface ContactListInteractor {
    fun loadContacts(name: String): Single<List<ContactEntity>>
}
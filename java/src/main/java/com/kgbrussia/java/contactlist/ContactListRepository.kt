package com.kgbrussia.java.contactlist

import com.kgbrussia.java.ContactEntity
import io.reactivex.rxjava3.core.Single

interface ContactListRepository {
    fun readContacts(name: String): Single<List<ContactEntity>>
}
package com.kgbrussia.java

import io.reactivex.rxjava3.core.Single

interface ContactDetailsRepository {
    fun readContactById(id: String): Single<ContactEntity>
}

interface ContactListRepository {
    fun readContacts(name: String): Single<List<ContactEntity>>
}
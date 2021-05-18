package com.kgbrussia.java

import io.reactivex.rxjava3.core.Single
import java.util.*

interface ContactListInteractor {
    fun loadContacts(name: String): Single<List<ContactEntity>>
}

interface ContactDetailsInteractor {
    fun loadContactById(id: String): Single<ContactEntity>
}
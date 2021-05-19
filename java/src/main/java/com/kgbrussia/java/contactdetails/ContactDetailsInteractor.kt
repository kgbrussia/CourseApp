package com.kgbrussia.java.contactdetails

import com.kgbrussia.java.ContactEntity
import io.reactivex.rxjava3.core.Single

interface ContactDetailsInteractor {
    fun loadContactById(id: String): Single<ContactEntity>
}
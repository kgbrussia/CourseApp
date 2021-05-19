package com.kgbrussia.java.contactdetails

import com.kgbrussia.java.ContactEntity
import io.reactivex.rxjava3.core.Single

interface ContactDetailsRepository {
    fun readContactById(id: String): Single<ContactEntity>
}
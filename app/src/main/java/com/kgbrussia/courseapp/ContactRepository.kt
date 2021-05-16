package com.kgbrussia.courseapp

import io.reactivex.rxjava3.core.Single

interface ContactRepository {
    fun loadContactList(name: String): Single<List<Contact>>
    fun loadContact(id: String): Single<Contact>
}
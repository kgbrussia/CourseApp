package com.kgbrussia.java.contactlist

class ContactListModel(private val repository: ContactListRepository)
    : ContactListInteractor {
    override fun loadContacts(name: String) = repository.readContacts(name)
}
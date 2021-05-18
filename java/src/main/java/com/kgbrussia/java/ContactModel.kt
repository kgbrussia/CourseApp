package com.kgbrussia.java

class ContactListModel(private val repository: ContactListRepository)
    : ContactListInteractor {
    override fun loadContacts(name: String) = repository.readContacts(name)
}

class ContactDetailsModel(private val repository: ContactDetailsRepository)
    : ContactDetailsInteractor {
    override fun loadContactById(id: String) = repository.readContactById(id)
}
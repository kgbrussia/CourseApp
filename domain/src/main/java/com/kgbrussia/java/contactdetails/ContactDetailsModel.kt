package com.kgbrussia.java.contactdetails

class ContactDetailsModel(private val repository: ContactDetailsRepository)
    : ContactDetailsInteractor {
    override fun loadContactById(id: String) = repository.readContactById(id)
}
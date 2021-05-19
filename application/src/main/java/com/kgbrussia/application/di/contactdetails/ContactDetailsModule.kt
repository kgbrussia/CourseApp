package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.java.contactdetails.ContactDetailsInteractor
import com.kgbrussia.java.contactdetails.ContactDetailsModel
import com.kgbrussia.java.contactdetails.ContactDetailsRepository
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @ContactsDetailsScope
    @Provides
    fun providesContactDetailsInteractor(repository: ContactDetailsRepository): ContactDetailsInteractor
            = ContactDetailsModel(repository)
}
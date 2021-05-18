package com.kgbrussia.application.di.modules

import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.java.ContactDetailsInteractor
import com.kgbrussia.java.ContactDetailsModel
import com.kgbrussia.java.ContactDetailsRepository
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @ContactsDetailsScope
    @Provides
    fun providesContactDetailsInteractor(repository: ContactDetailsRepository): ContactDetailsInteractor
            = ContactDetailsModel(repository)
}
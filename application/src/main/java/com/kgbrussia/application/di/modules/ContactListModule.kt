package com.kgbrussia.application.di.modules

import com.kgbrussia.application.di.scopes.ContactsListScope
import com.kgbrussia.java.ContactListInteractor
import com.kgbrussia.java.ContactListModel
import com.kgbrussia.java.ContactListRepository
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {
    @ContactsListScope
    @Provides
    fun providesContactListInteractor(repository: ContactListRepository): ContactListInteractor
            = ContactListModel(repository)
}
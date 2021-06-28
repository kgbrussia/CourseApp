package com.kgbrussia.application.di.contactlist

import com.kgbrussia.application.di.scopes.ContactListScope
import com.kgbrussia.java.contactlist.ContactListInteractor
import com.kgbrussia.java.contactlist.ContactListModel
import com.kgbrussia.java.contactlist.ContactListRepository
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {
    @ContactListScope
    @Provides
    fun providesContactListInteractor(repository: ContactListRepository): ContactListInteractor =
        ContactListModel(repository)
}
package com.kgbrussia.application.di.contactlist

import com.kgbrussia.application.di.scopes.ContactsListScope
import com.kgbrussia.library.di.ContactListContainer
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListViewModelModule::class, ContactListModule::class])
interface ContactListComponent : ContactListContainer
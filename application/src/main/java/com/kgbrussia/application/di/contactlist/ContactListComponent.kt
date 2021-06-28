package com.kgbrussia.application.di.contactlist

import com.kgbrussia.application.di.scopes.ContactListScope
import com.kgbrussia.library.contactlist.ContactListContainer
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListViewModelModule::class, ContactListModule::class])
interface ContactListComponent : ContactListContainer
package com.kgbrussia.application.di.components

import com.kgbrussia.application.di.modules.ContactListModule
import com.kgbrussia.application.di.modules.ContactListViewModelModule
import com.kgbrussia.application.di.scopes.ContactsListScope
import com.kgbrussia.library.di.ContactListContainer
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListViewModelModule::class, ContactListModule::class])
interface ContactListComponent : ContactListContainer
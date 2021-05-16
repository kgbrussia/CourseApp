package com.kgbrussia.courseapp.di.components

import com.kgbrussia.courseapp.ContactListFragment
import com.kgbrussia.courseapp.di.modules.ContactListViewModelModule
import com.kgbrussia.courseapp.di.scopes.ContactsListScope
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListViewModelModule::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}
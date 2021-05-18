package com.kgbrussia.courseapp.di.components

import com.kgbrussia.courseapp.ContactDetailsFragment
import com.kgbrussia.courseapp.di.modules.ContactDetailsViewModelModule
import com.kgbrussia.courseapp.di.scopes.ContactsDetailsScope
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsViewModelModule::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}
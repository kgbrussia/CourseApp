package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.library.di.ContactDetailsContainer
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsViewModelModule::class, ContactDetailsModule::class])
interface ContactDetailsComponent : ContactDetailsContainer
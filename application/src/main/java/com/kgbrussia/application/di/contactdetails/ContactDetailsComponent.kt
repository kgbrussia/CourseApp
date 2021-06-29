package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactDetailsScope
import com.kgbrussia.library.contactdetails.ContactDetailsContainer
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsViewModelModule::class, ContactDetailsModule::class])
interface ContactDetailsComponent : ContactDetailsContainer
package com.kgbrussia.application.di.components

import com.kgbrussia.application.di.modules.ContactDetailsModule
import com.kgbrussia.application.di.modules.ContactDetailsViewModelModule
import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.library.di.ContactDetailsContainer
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsViewModelModule::class, ContactDetailsModule::class])
interface ContactDetailsComponent : ContactDetailsContainer
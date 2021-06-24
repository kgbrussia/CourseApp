package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.library.birthdaynotification.NotificationContainer
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface NotificationComponent : NotificationContainer
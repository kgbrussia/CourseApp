package com.kgbrussia.application.di.contactdetails

import com.kgbrussia.application.di.scopes.ContactDetailsScope
import com.kgbrussia.library.birthdaynotification.NotificationContainer
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface NotificationComponent : NotificationContainer
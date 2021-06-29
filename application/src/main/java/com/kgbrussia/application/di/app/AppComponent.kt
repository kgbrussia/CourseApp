package com.kgbrussia.application.di.app

import com.kgbrussia.application.di.contactdetails.ContactDetailsComponent
import com.kgbrussia.application.di.contactdetails.NotificationComponent
import com.kgbrussia.application.di.contactlist.ContactListComponent
import com.kgbrussia.application.di.modules.ViewModelFactoryModule
import com.kgbrussia.library.di.AppContainer
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules =
    [
        AppModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AppContainer {
    override fun contactListContainer(): ContactListComponent
    override fun contactDetailsContainer(): ContactDetailsComponent
    override fun notificationContainer(): NotificationComponent
}
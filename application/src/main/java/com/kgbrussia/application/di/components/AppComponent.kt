package com.kgbrussia.application.di.components


import com.kgbrussia.application.di.modules.AppModule
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
}
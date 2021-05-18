package com.kgbrussia.courseapp.di.components

import com.kgbrussia.courseapp.di.modules.AppModule
import com.kgbrussia.courseapp.di.modules.ViewModelFactoryModule
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
interface AppComponent {
    fun contactListComponent(): ContactListComponent
    fun contactDetailsComponent(): ContactDetailsComponent
}
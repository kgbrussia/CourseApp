package com.kgbrussia.application.di.modules

import android.app.Application
import android.content.Context
import com.kgbrussia.java.ContactDetailsRepository
import com.kgbrussia.java.ContactListRepository
import com.kgbrussia.library.data.ContactProviderDetailsRepository
import com.kgbrussia.library.data.ContactProviderListRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesContext(): Context = application

    @Singleton
    @Provides
    fun providesListRepository(): ContactListRepository = ContactProviderListRepository(application)

    @Singleton
    @Provides
    fun providesDetailsRepository(): ContactDetailsRepository = ContactProviderDetailsRepository(application)
}
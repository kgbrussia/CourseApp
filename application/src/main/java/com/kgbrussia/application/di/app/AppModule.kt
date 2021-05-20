package com.kgbrussia.application.di.app

import android.app.Application
import android.content.Context
import com.kgbrussia.java.contactdetails.ContactDetailsRepository
import com.kgbrussia.java.contactlist.ContactListRepository
import com.kgbrussia.library.contactdetails.ContactProviderDetailsRepository
import com.kgbrussia.library.contactlist.ContactProviderListRepository
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
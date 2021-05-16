package com.kgbrussia.courseapp.di.modules

import android.content.Context
import com.kgbrussia.courseapp.ContactProviderRepository
import com.kgbrussia.courseapp.ContactRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun providesContext() = context

    @Singleton
    @Provides
    fun providesRepository(): ContactRepository = ContactProviderRepository(context)
}
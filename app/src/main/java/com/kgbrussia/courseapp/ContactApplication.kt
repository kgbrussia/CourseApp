package com.kgbrussia.courseapp

import android.app.Application
import com.kgbrussia.courseapp.di.components.AppComponent
import com.kgbrussia.courseapp.di.components.DaggerAppComponent
import com.kgbrussia.courseapp.di.modules.AppModule
import timber.log.Timber

class ContactApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
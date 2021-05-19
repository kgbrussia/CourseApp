package com.kgbrussia.application

import android.app.Application
import com.kgbrussia.application.di.app.AppComponent
import com.kgbrussia.application.di.app.AppModule
import com.kgbrussia.application.di.app.DaggerAppComponent
import com.kgbrussia.library.di.AppContainer
import com.kgbrussia.library.di.HasComponent
import timber.log.Timber

class ContactApplication: Application(), HasComponent {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun getAppComponent(): AppContainer = appComponent
}
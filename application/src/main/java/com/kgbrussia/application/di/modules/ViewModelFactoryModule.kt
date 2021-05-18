package com.kgbrussia.application.di.modules

import androidx.lifecycle.ViewModelProvider
import com.kgbrussia.library.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
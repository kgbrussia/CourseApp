package com.kgbrussia.courseapp.di.modules

import androidx.lifecycle.ViewModelProvider
import com.kgbrussia.courseapp.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
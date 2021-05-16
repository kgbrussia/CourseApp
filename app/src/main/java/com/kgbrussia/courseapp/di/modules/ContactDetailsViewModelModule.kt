package com.kgbrussia.courseapp.di.modules

import androidx.lifecycle.ViewModel
import com.kgbrussia.courseapp.di.key.ViewModelKey
import com.kgbrussia.courseapp.di.scopes.ContactsDetailsScope
import com.kgbrussia.courseapp.viewmodel.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactDetailsViewModelModule {

    @ContactsDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(viewModel: ContactDetailsViewModel): ViewModel
}
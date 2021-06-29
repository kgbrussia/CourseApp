package com.kgbrussia.application.di.contactdetails

import androidx.lifecycle.ViewModel
import com.kgbrussia.application.di.key.ViewModelKey
import com.kgbrussia.application.di.scopes.ContactDetailsScope
import com.kgbrussia.library.contactdetails.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactDetailsViewModelModule {

    @ContactDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(viewModel: ContactDetailsViewModel): ViewModel
}
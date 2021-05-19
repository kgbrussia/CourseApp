package com.kgbrussia.application.di.contactdetails

import androidx.lifecycle.ViewModel
import com.kgbrussia.application.di.key.ViewModelKey
import com.kgbrussia.application.di.scopes.ContactsDetailsScope
import com.kgbrussia.library.viewmodel.ContactDetailsViewModel
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
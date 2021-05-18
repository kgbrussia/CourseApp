package com.kgbrussia.application.di.modules

import androidx.lifecycle.ViewModel
import com.kgbrussia.application.di.key.ViewModelKey
import com.kgbrussia.application.di.scopes.ContactsListScope
import com.kgbrussia.library.viewmodel.ContactListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactListViewModelModule {

    @ContactsListScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindContactListViewModel(viewModel: ContactListViewModel): ViewModel
}
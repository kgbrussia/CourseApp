package com.kgbrussia.courseapp.di.modules

import androidx.lifecycle.ViewModel
import com.kgbrussia.courseapp.di.key.ViewModelKey
import com.kgbrussia.courseapp.di.scopes.ContactsListScope
import com.kgbrussia.courseapp.viewmodel.ContactListViewModel
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
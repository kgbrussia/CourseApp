package com.kgbrussia.library.di

import com.kgbrussia.library.fragments.ContactListFragment

interface ContactListContainer {
    fun inject(contactListFragment: ContactListFragment)
}
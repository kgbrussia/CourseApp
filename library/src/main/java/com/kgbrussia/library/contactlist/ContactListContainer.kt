package com.kgbrussia.library.contactlist

import com.kgbrussia.library.fragments.ContactListFragment

interface ContactListContainer {
    fun inject(contactListFragment: ContactListFragment)
}
package com.kgbrussia.library.di

import com.kgbrussia.library.fragments.ContactDetailsFragment

interface ContactDetailsContainer {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}
package com.kgbrussia.library.di

interface AppContainer {
    fun contactListContainer(): ContactListContainer
    fun contactDetailsContainer(): ContactDetailsContainer
}
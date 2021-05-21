package com.kgbrussia.java

data class ContactEntity(
    val id: Int,
    val name: String,
    val phone: String,
    val dayOfBirthday: Int?,
    val monthOfBirthday: Int?,
    val photo: String?
)
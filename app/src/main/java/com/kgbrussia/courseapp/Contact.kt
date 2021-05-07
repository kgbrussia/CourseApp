package com.kgbrussia.courseapp

import android.net.Uri

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val dayOfBirthday: Int?,
    val monthOfBirthday: Int?,
    val photo: Uri?
)

package com.kgbrussia.library.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.Data
import com.kgbrussia.java.ContactEntity

object ContactResolver {
    private const val DATA_CONTACT_ID = "${Data.CONTACT_ID}="
    private const val FIND_CONTACT_BY_ID_SELECTION = "${Contacts._ID} = ?"
    private const val GET_DISPLAY_NAME_SELECTION = "${Contacts.DISPLAY_NAME} LIKE ?"
    private const val GET_LIST_PHONES_SELECTION = "${CommonDataKinds.Phone.CONTACT_ID} = ?"
    private const val GET_BIRTHDAY_DATE_SELECTION = " AND ${Data.MIMETYPE}= '${CommonDataKinds.Event.CONTENT_ITEM_TYPE}'" +
            " AND ${CommonDataKinds.Event.TYPE}=${CommonDataKinds.Event.TYPE_BIRTHDAY}"
    private const val GET_PHOTO_URI_SELECTION = " AND ${Data.MIMETYPE}=" +
            "'${CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'"

    fun getContactsList(context: Context, name: String): List<ContactEntity> {
        val contactsList = mutableListOf<ContactEntity>()
        context.contentResolver.query(
            Contacts.CONTENT_URI,
            null,
            GET_DISPLAY_NAME_SELECTION,
            arrayOf("$name%"),
            null
        )?.use {
            while (it.moveToNext()) {
                val contact = it.toContactForList(context.contentResolver)
                if(contact != null) {
                    contactsList.add(contact)
                }
            }
        }
        return contactsList
    }

    fun findContactById(context: Context, id: String): ContactEntity? {
        var contact: ContactEntity? = null
        context.contentResolver.query(
            Contacts.CONTENT_URI,
            arrayOf(Contacts.DISPLAY_NAME),
            FIND_CONTACT_BY_ID_SELECTION,
            arrayOf(id),
            null
        )?.use {
            while (it.moveToNext()) {
                contact = it.toContact(context.contentResolver, id)
            }
        }
        return contact
    }

    private fun getPhoneNumber(contentResolver: ContentResolver, id: String): String? {
        var phoneNumber: String? = null
        contentResolver.query(
            CommonDataKinds.Phone.CONTENT_URI,
            null,
            GET_LIST_PHONES_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                phoneNumber = it.getString(it.getColumnIndex(CommonDataKinds.Phone.NUMBER))
            }
        }
        return phoneNumber
    }

    private fun getPhotoUri(contentResolver: ContentResolver, id: String): String? {
        var uriString: String? = null
        contentResolver.query(
            Data.CONTENT_URI,
            null,
            DATA_CONTACT_ID + id + GET_PHOTO_URI_SELECTION,
            null,
            null
        )?.use {
            if (it.moveToFirst()) {
                uriString = it.getString(it.getColumnIndex(Contacts.Photo.PHOTO_URI))
            }
        }
        return uriString
    }

    private fun getBirthdayDate(contentResolver: ContentResolver, id: String): String? {
        var birthday: String? = null
        contentResolver.query(
            Data.CONTENT_URI,
            arrayOf(CommonDataKinds.Event.DATA),
            DATA_CONTACT_ID + id + GET_BIRTHDAY_DATE_SELECTION,
            null,
            null
        )?.use {
            while (it.moveToNext()) {
                birthday =
                    it.getString(it.getColumnIndex(CommonDataKinds.Event.START_DATE))
            }
        }
        return birthday
    }

    private fun Cursor.toContactForList(contentResolver: ContentResolver): ContactEntity? {
        val id = getString(getColumnIndex(Contacts._ID)) ?: return null
        return ContactEntity(
            id = id.toInt(),
            name = getString(getColumnIndex(Contacts.DISPLAY_NAME)) ?: "",
            phone = getPhoneNumber(contentResolver, id) ?: "",
            dayOfBirthday = 1,
            monthOfBirthday = 1,
            photo = getPhotoUri(contentResolver, id)
        )
    }

    private fun Cursor.toContact(contentResolver: ContentResolver, id: String): ContactEntity {
        var dayOfBirthday: Int? = null
        var monthOfBirthday: Int? = null
        val birthdayList = getBirthdayDate(contentResolver, id)?.split(Regex("-+"))
        if(birthdayList?.size == 3) {
            monthOfBirthday = birthdayList[1].toIntOrNull()
            dayOfBirthday = birthdayList[2].toIntOrNull()
        }
        return ContactEntity(
            id = id.toInt(),
            name = getString(getColumnIndex(Contacts.DISPLAY_NAME)) ?: "",
            phone = getPhoneNumber(contentResolver, id) ?: "",
            dayOfBirthday = dayOfBirthday,
            monthOfBirthday = monthOfBirthday,
            photo = getPhotoUri(contentResolver, id)
        )
    }
}
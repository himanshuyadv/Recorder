package com.ansh.recorder.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.ansh.recorder.core.models.Call
import com.ansh.recorder.core.models.ContactData
import com.ansh.recorder.core.models.ContactHeader

@SuppressLint("Range")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
@JvmOverloads
fun Context.retrieveAllContactsCllRcc(
    searchPatternCllRcc: String = "",
    retrieveAvatarCllRcc: Boolean = true,
    limitCllRcc: Int = -1,
    offsetCllRcc: Int = -1
): List<Any> {

    val contactsListCllRcc = mutableListOf<Any>()
    contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPatternCllRcc.isNotBlank()) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE '%?%'" else null,
        if (searchPatternCllRcc.isNotBlank()) arrayOf(searchPatternCllRcc) else null,
        if (limitCllRcc > 0 && offsetCllRcc > -1) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC LIMIT $limitCllRcc OFFSET $offsetCllRcc"
        else ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactIdCllRcc = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val nameCllRcc = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val hasPhoneNumberCllRcc =
                    it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])).toInt()
                val phoneNumberCllRcc: List<String> = if (hasPhoneNumberCllRcc > 0) {
                    retrievePhoneNumberCllRcc(contactIdCllRcc)
                } else mutableListOf()

                val avatarCllRcc =
                    if (retrieveAvatarCllRcc) retrieveAvatarCllRcc(contactIdCllRcc) else null
                if (phoneNumberCllRcc.isNotEmpty()) {
//                    Log.d("phoneNumber", phoneNumber.toString())
                    contactsListCllRcc.add(
                        ContactData(
                            contactIdCllRcc,
                            nameCllRcc,
                            phoneNumberCllRcc,
                            avatarCllRcc
                        )
                    )
                }
            } while (it.moveToNext())
        }
        it.close()
    }
    return contactsListCllRcc
}

@SuppressLint("Range")
private fun Context.retrievePhoneNumberCllRcc(contactIdCllRcc: Long): List<String> {

    val resultCllRcc: MutableList<String> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} =?",
        arrayOf(contactIdCllRcc.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            do {
                resultCllRcc.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            } while (it.moveToNext())
        }
        it.close()
    }
    return resultCllRcc
}

fun Context.retrieveAvatarCllRcc(contactIdCllRcc: Long): Uri? {

    return contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} =? AND ${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'",
        arrayOf(contactIdCllRcc.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactIdCllRcc
            )
            Uri.withAppendedPath(
                contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
            )
        } else null
    }
}

private val CONTACT_PROJECTION = arrayOf(
    ContactsContract.Contacts._ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
    ContactsContract.Contacts.HAS_PHONE_NUMBER
)

fun splitContactsListToCategoriesCllRcc(contactsListCllRcc: MutableList<Any>) {

    var idxCllRcc = contactsListCllRcc.size - 1
    for (i in 0..idxCllRcc) {
        if (contactsListCllRcc.find { headerCllRcc -> headerCllRcc == ContactHeader((contactsListCllRcc[i] as ContactData).nameCllRcc[0].toString()) } == null) {
            contactsListCllRcc.add(
                i,
                ContactHeader((contactsListCllRcc[i] as ContactData).nameCllRcc[0].toString())
            )
            idxCllRcc++
        }
    }
}

@JvmName("splitContactsListToCategories1")
fun splitContactsListToCategoriesCllRcc(contactsListCllRcc: MutableList<Call>) {

    var idxCllRcc = contactsListCllRcc.size - 1
    for (i in 0..idxCllRcc) {
        if (contactsListCllRcc.find { headerCllRcc ->
                headerCllRcc == Call(contactNameCllRcc = (contactsListCllRcc[i] as Call).contactNameCllRcc[0].toString())
            } == null) {
            contactsListCllRcc.add(
                i,
                Call(contactNameCllRcc = (contactsListCllRcc[i] as Call).contactNameCllRcc[0].toString())
            )
            idxCllRcc++
        }
    }
}

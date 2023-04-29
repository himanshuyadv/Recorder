package com.ansh.recorder.core.models

import android.net.Uri

data class ContactData(
    val contactIdCllRcc: Long,
    val nameCllRcc: String,
    val phoneNumberCllRcc: List<String>,
    val avatarCllRcc: Uri?
)
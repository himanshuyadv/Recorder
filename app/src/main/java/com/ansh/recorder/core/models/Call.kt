package com.ansh.recorder.core.models

import android.net.Uri
import java.util.*

data class Call(
    val id: Int = 0,
    val phoneNumberCllRcc: String = "",
    val contactNameCllRcc: String = "",
    val typeCllRcc: CallTypes = CallTypes.NULLCllRcc,
    val dateCllRcc: Date? = null,
    val avatarCllRcc: Uri? = null,
    val durationCllRcc: String = ""
)
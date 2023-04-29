package com.ansh.recorder.core.data.database

import android.net.Uri
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun toUriCllRcc(value: String): Uri? {
        return Uri.parse(value)
    }

    @TypeConverter
    fun fromUriCllRcc(value: Uri): String {
        return value.toString()
    }
}
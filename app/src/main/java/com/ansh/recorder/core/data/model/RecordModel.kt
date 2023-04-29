package com.ansh.recorder.core.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "record_cr")
@Parcelize
data class RecordModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var timestampCllRcc: Long = System.currentTimeMillis(),
    var fileUriCllRcc: Uri = Uri.EMPTY,
    var nameCllRcc: String = "record_$timestampCllRcc",
    var durationCllRcc: Long = 0L,
    var numberCllRcc: String = ""
) : Parcelable {

    var expandCllRcc: Boolean = false
}

package com.ansh.recorder.core.utils

import android.content.Context
import kotlinx.coroutines.flow.flow
import java.io.File


fun Context.getFreeSpaceInGbCllRcc() = flow {


    val freeBytesExternalCllRcc: Long = File(getExternalFilesDir(null).toString()).freeSpace
    val spaceInGbCllRcc = (freeBytesExternalCllRcc.toDouble() / (1024 * 1024 * 1024)).roundCllRcc(2)
    emit(spaceInGbCllRcc)
}
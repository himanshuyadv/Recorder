package com.ansh.recorder.core.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import java.io.File


object FilesManager {

    fun getInternalDestFileCllRcc(context: Context, fileNameCllRcc: String): File? {

        return context.getExternalFilesDir(getDefaultRecordsPathCllRcc())?.let { destFileCllRcc ->
            if (!destFileCllRcc.exists()) destFileCllRcc.mkdirs()
            File(destFileCllRcc, fileNameCllRcc)
        }
    }

    fun getDefaultRecordsPathCllRcc() =
        Environment.getExternalStorageDirectory().toString() + "/Call/"


    fun getTempFileNameCllRcc(extensionCllRcc: String = ""): String {

        return "record_${System.currentTimeMillis()}$extensionCllRcc"
    }

    fun File.renameFileCllRcc(fileName: String): File {

        var renamedFileCllRcc: File? = null
        parentFile?.let { parentCllRcc ->
            val newFileCllRcc = File(parentCllRcc, fileName)
            if (renameTo(newFileCllRcc)) {
                renamedFileCllRcc = newFileCllRcc
            }
        }
        return renamedFileCllRcc ?: this
    }

    fun deleteFileCllRcc(context: Context, uriCllRcc: Uri) {

        context.contentResolver.delete(uriCllRcc, null, null)
    }

    fun updateFileCllRcc(
        context: Context,
        fileUriCllRcc: Uri,
        dirCllRcc: String,
        fileNameCllRcc: String,
        addedAtCllRcc: Long
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val valuesCllRcc = getContentValuesCllRcc(fileNameCllRcc, dirCllRcc, addedAtCllRcc)
            context.contentResolver.update(fileUriCllRcc, valuesCllRcc, null, null)
        } else {
            fileUriCllRcc.toFile().renameFileCllRcc(fileNameCllRcc)
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getContentValuesCllRcc(
        fileNameCllRcc: String,
        dirCllRcc: String = getDefaultRecordsPathCllRcc(),
        addedAtCllRcc: Long = System.currentTimeMillis()
    ): ContentValues {

        val encodinCllRcc = fileNameCllRcc.split(".").last()
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileNameCllRcc)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/$encodinCllRcc")
            put(MediaStore.MediaColumns.DATE_ADDED, addedAtCllRcc)
            put(MediaStore.MediaColumns.RELATIVE_PATH, dirCllRcc)
        }
    }

}
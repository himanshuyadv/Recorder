package com.ansh.recorder.core.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.ansh.recorder.R
import java.net.URISyntaxException

object PathUtil {
    /*
     * Gets the file path of the given Uri.
     */



    @Throws(URISyntaxException::class)
    fun getPathCllRcc(context: Context, uriCllRcc: Uri): String? {
        var uriCllRcc = uriCllRcc
        var selectionCllRcc: String? = null
        var selectionArgsCllRcc: Array<String>? = null
        if (DocumentsContract.isDocumentUri(context.applicationContext, uriCllRcc)) {
            when {
                isExternalStorageDocumentCllRcc(uriCllRcc) -> {
                    val docIdCllRcc = DocumentsContract.getDocumentId(uriCllRcc)
                    val splitCllRcc = docIdCllRcc.split(
                        context.getString(R.string.two_dots_txt_voice_rec_cll_rcc).toRegex()
                    ).toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + context.getString(
                        R.string.splash_voice_rec_cll_rcc
                    ) + splitCllRcc[1]
                }
                isDownloadsDocumentCllRcc(uriCllRcc) -> {
                    val idCllRcc = DocumentsContract.getDocumentId(uriCllRcc)
                    uriCllRcc = ContentUris.withAppendedId(
                        Uri.parse(context.getString(R.string.link_content_public_download_cll_rcc)),
                        idCllRcc.toLong()
                    )
                }
                isMediaDocumentCllRcc(uriCllRcc) -> {
                    val docIdCllRcc = DocumentsContract.getDocumentId(uriCllRcc)
                    val splitCllRcc = docIdCllRcc.split(
                        context.getString(R.string.two_dots_txt_voice_rec_cll_rcc).toRegex()
                    ).toTypedArray()
                    when (splitCllRcc[0]) {
                        context.getString(R.string.image_txt_voice_rec_cll_rcc) -> {
                            uriCllRcc = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        context.getString(R.string.video_voice_rec_txt_cll_rcc) -> {
                            uriCllRcc = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        context.getString(R.string.audio_voice_rec_txt_cll_rcc) -> {
                            uriCllRcc = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    selectionCllRcc = context.getString(R.string.id_qwesion_voice_rec_cll_rcc)
                    selectionArgsCllRcc = arrayOf(splitCllRcc[1])
                }
            }
        }
        if (context.getString(R.string.content_voice_rec_txt_cll_rcc)
                .equals(uriCllRcc.scheme, ignoreCase = true)
        ) {
            val projectionCllRcc = arrayOf(MediaStore.Images.Media.DATA)
            var cursorCllRcc: Cursor? = null
            try {
                cursorCllRcc =
                    context.contentResolver.query(
                        uriCllRcc,
                        projectionCllRcc,
                        selectionCllRcc,
                        selectionArgsCllRcc,
                        null
                    )
                val column_index_CllRcc =
                    cursorCllRcc?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursorCllRcc?.moveToFirst() == true) {
                    return cursorCllRcc.getString(column_index_CllRcc!!)
                }
            } catch (eCllRcc: Exception) {
                eCllRcc.printStackTrace()
            }
        } else if (context.getString(R.string.file_voice_rec_cll_rcc)
                .equals(uriCllRcc.scheme, ignoreCase = true)
        ) {
            return uriCllRcc.path
        }


        return null
    }

    /**
     * @param uriCllRcc The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocumentCllRcc(uriCllRcc: Uri): Boolean {


        return "com.android.externalstorage.documents" == uriCllRcc.authority
    }

    /**
     * @param uriCllRcc The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocumentCllRcc(uriCllRcc: Uri): Boolean {


        return "com.android.providers.downloads.documents" == uriCllRcc.authority
    }

    /**
     * @param uriCllRcc The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocumentCllRcc(uriCllRcc: Uri): Boolean {


        return "com.android.providers.media.documents" == uriCllRcc.authority
    }
}
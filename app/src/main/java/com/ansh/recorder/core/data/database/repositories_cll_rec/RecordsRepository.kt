package com.ansh.recorder.core.data.database.repositories_cll_rec

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.ansh.recorder.core.data.database.AppDatabase
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.utils.FilesManager


object RecordsRepository {
    private val daoCllRcc = AppDatabase.instanceCllRcc.recordDaoCllRcc()

    fun getRecordsCllRcc(): LiveData<List<RecordModel>> {

        return daoCllRcc.getAllCllRcc()
    }


    suspend fun saveRecordCllRcc(context: Context, recordModel: RecordModel) =
        withContext(Dispatchers.IO) {
            if (recordModel.durationCllRcc > 1000) {
                recordModel.fileUriCllRcc =
                    saveToPublicDirCllRcc(context, recordModel) ?: recordModel.fileUriCllRcc
                daoCllRcc.insertCllRcc(recordModel)
            }
        }

    suspend fun updateRecordCllRcc(context: Context, recordModel: RecordModel) {

        updateInStorageCllRcc(context, recordModel)
        daoCllRcc.updateCllRcc(recordModel)
    }

    suspend fun deleteRecordCllRcc(context: Context, recordModel: RecordModel) {
        deleteFromStorageCllRcc(context, recordModel)
        daoCllRcc.deleteCllRcc(recordModel)
    }

    private suspend fun updateInStorageCllRcc(context: Context, recordModel: RecordModel) {
        try {
            val saveDirPathCllRcc = getSaveStoragePathCllRcc()
            FilesManager.updateFileCllRcc(
                context,
                recordModel.fileUriCllRcc,
                recordModel.nameCllRcc,
                saveDirPathCllRcc,
                recordModel.timestampCllRcc
            )
        } catch (eCllRcc: Exception) {

            eCllRcc.printStackTrace()
        }
    }

    private fun deleteFromStorageCllRcc(context: Context, recordModel: RecordModel) {
        try {
            FilesManager.deleteFileCllRcc(context, recordModel.fileUriCllRcc)
        } catch (eCllRcc: Exception) {

            eCllRcc.printStackTrace()
        }
    }

    private suspend fun getSaveStoragePathCllRcc() =
        SettingsRepository.getCurrentSettingsCllRcc()?.saveStoragePathCllRcc
            ?: FilesManager.getDefaultRecordsPathCllRcc()


    private suspend fun saveToPublicDirCllRcc(context: Context, recordModel: RecordModel): Uri? {
        val contentResolverCllRcc = context.contentResolver
        val saveDirPathCllRcc = getSaveStoragePathCllRcc()


        val resultCllRcc: Uri? = null
        return resultCllRcc
    }
}

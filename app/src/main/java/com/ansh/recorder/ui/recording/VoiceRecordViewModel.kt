package com.ansh.recorder.ui.recording

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ansh.recorder.core.checkLastmodCllRcc
import com.ansh.recorder.core.data.database.repositories_cll_rec.RecordsRepository
import com.ansh.recorder.core.data.database.repositories_cll_rec.SettingsRepository
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.data.model.Settings
import com.ansh.recorder.core.listfCllRcc
import com.ansh.recorder.core.media.AudioRecorder
import com.ansh.recorder.core.utils.*
import com.ansh.recorder.core.utils.FilesManager.renameFileCllRcc
import com.ansh.recorder.ui.recording.receiver_cll_rec.MediaNotificationReceiver
import kotlinx.coroutines.*
import com.ansh.recorder.R
import java.io.File

class VoiceRecordViewModel : ViewModel() {

    private val _recordingEventCllRcc = MutableLiveData<Boolean>()

    private val _freeSpaceCllRcc = MutableLiveData<String>()

    private val _finishRecordingEventCllRcc = LiveEvent<Unit>()

    var recorderCllRcc: AudioRecorder? = null

    var recordModel: RecordModel? = null

    var recordingCllRcc = false
    fun startRecordingCllRcc(context: Context) {
//        Log.e("logs", "start1")

        recordingCllRcc = true
        viewModelScope.launch(Dispatchers.IO) {
            SettingsRepository.getCurrentSettingsCllRcc()?.let { settingsCllRcc ->
                withContext(Dispatchers.Main) {
//                    if(Build.MANUFACTURER.lowercase() != "samsung"){
                    prepareSettingCllRcc(context, settingsCllRcc)
                    recorderCllRcc = AudioRecorder(context, settingsCllRcc)
                    recorderCllRcc?.startRecordingCllRcc(true)
                    observeTimeCllRcc(context)
                    observeFreeSpaceCllRcc(context)
                    _recordingEventCllRcc.value = true
//                    }else{
//                        val serviceIntent = Intent(
//                            context, CleanService::class.java
//                        ).apply {
//                            putExtra(CleanReceiver.RECEIVE_TYPE, CleanType.CACHE)
//                        }
//
//                        context.startService(serviceIntent)
//                    }

                }
            }
        }
    }


    private fun prepareSettingCllRcc(context: Context, settings: Settings) {

        settings.apply {
            silentModeCllRcc.ifTrueCllRcc {
                SystemSettingsManager.turnOnSilentModeCllRcc(context)
            }
            lockScreenControlsCllRcc.ifTrueCllRcc {
//                SystemSettingsManager.turnOnLockScreenControlsVoiceRec(context)
            }
        }
    }

    private fun observeFreeSpaceCllRcc(context: Context) {

        viewModelScope.launch {
            try {
                while (isNotStoppedCllRcc()) {
                    context.getFreeSpaceInGbCllRcc().collect { freeSpacCllRcc ->
                        val formatCllRcc =
                            freeSpacCllRcc.toString() + context.getString(R.string.space_cll_rcc) + context.getString(
                                R.string.gb_cll_rcc
                            )
                        _freeSpaceCllRcc.postValue(formatCllRcc)
                    }
                    delay(10000)
                }
            } catch (eVoiceRec: Exception) {
                eVoiceRec.printStackTrace()
            }

        }
    }

    private fun observeTimeCllRcc(context: Context) {

        viewModelScope.launch {
            try {
                while (isNotStoppedCllRcc()) {
                    recorderCllRcc?.getRecordingTimeCllRcc()?.minutesAndSecondsFormatCllRcc()
                        .ifNotNullOrEmptyCllRcc { timeCllRcc ->
                            sendBroadcast(context, timeCllRcc)
                        }
                    delay(1000)
                }
            } catch (eVoiceRec: Exception) {
                eVoiceRec.printStackTrace()
            }

        }
    }


    private fun isNotStoppedCllRcc() = recorderCllRcc?.stoppedCllRcc != true

    private fun sendBroadcast(context: Context, timeCllRcc: String?) {

//        log(timeVoiceRec)
        timeCllRcc.ifNotNullOrEmptyCllRcc {
            val intentCllRcc = Intent().apply {
                action = MediaNotificationReceiver.ACTION_TIMECllRcc
                putExtra(MediaNotificationReceiver.EXTRA_TIMECllRcc, it)
            }
            context.sendBroadcast(intentCllRcc)
        }
    }


    fun finishRecordingCllRcc(context: Context, declineCllRcc: () -> Unit) {

        recordingCllRcc = false
        _recordingEventCllRcc.value = false

        val recordFileCllRcc = recorderCllRcc?.stopRecordingCllRcc() ?: return
        renameFile(context, recordFileCllRcc) { file ->

            GlobalScope.launch(Dispatchers.IO) {
                val files = ArrayList<File>()
                val dCllRcc = Environment.getExternalStorageDirectory().toString() + "/Call"
                val allRecordsCllRcc = listfCllRcc(dCllRcc, files)
                val recordCllRcc = createRecord(context, checkLastmodCllRcc(allRecordsCllRcc!!)!!)

                GlobalScope.launch(Dispatchers.Main) {
                    saveRecord(context, recordCllRcc)
                    this@VoiceRecordViewModel.recordModel = recordCllRcc
                }
            }
            declineCllRcc()
            _finishRecordingEventCllRcc.callCllRcc()
        }
    }

    private fun renameFile(context: Context, file: File, callbackCllRcc: (File) -> Unit) {

        showEnterTheNameDialog(context, file.name) { newFileNameCllRcc ->
            val renamedCllRcc = if (newFileNameCllRcc != file.name) {
                file.renameFileCllRcc(newFileNameCllRcc)
            } else file
            callbackCllRcc(renamedCllRcc)
        }
    }

    private fun saveRecord(context: Context, recordModel: RecordModel) {

//        context.showToast("Saved")
        viewModelScope.launch(Dispatchers.IO) {
            RecordsRepository.saveRecordCllRcc(context, recordModel)
        }
    }

    private fun showEnterTheNameDialog(
        context: Context,
        currentFileNameVoiceRecCllRcc: String,
        callbackCllRcc: (String) -> Unit
    ) {

//        EnterNameDialogVoiceRec().showVoiceRec(context, currentFileNameVoiceRec) { resultVoiceRec ->
//            callback(resultVoiceRec)
        callbackCllRcc(currentFileNameVoiceRecCllRcc)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pauseResumeRecordingCllRcc() {

        recorderCllRcc?.pauseOrResumeCllRcc()
        _recordingEventCllRcc.value = recorderCllRcc?.recordingCllRcc
    }

    private fun createRecord(context: Context, file: File): RecordModel {

        return RecordModel(
            nameCllRcc = file.name,
            durationCllRcc = recorderCllRcc?.getRecordDurationCllRcc() ?: 0L,
            numberCllRcc = "",
            fileUriCllRcc = file.toUriCllRcc(context),
            timestampCllRcc = file.lastModified()
        )
    }


}
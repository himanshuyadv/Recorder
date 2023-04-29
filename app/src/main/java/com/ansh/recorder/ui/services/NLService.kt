package com.ansh.recorder.ui.services

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import com.ansh.recorder.R
import com.ansh.recorder.core.checkLastmodCllRcc
import com.ansh.recorder.core.data.database.repositories_cll_rec.RecordsRepository
import com.ansh.recorder.core.data.database.repositories_cll_rec.SettingsRepository
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.data.model.Settings
import com.ansh.recorder.core.listfCllRcc
import com.ansh.recorder.core.media.AudioRecorder
import com.ansh.recorder.core.utils.*
import com.ansh.recorder.core.utils.FilesManager.renameFileCllRcc
import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.recording.receiver_cll_rec.MediaNotificationReceiver
import java.io.File

class NLService : NotificationListenerService() {
    private val TAGCllRcc = this.javaClass.simpleName
    private var nlservicereciverCllRcc: NLServiceReceiverCllRcc? = null

    private var flagCllRcc = false
    override fun onCreate() {
        super.onCreate()
        nlservicereciverCllRcc = NLServiceReceiverCllRcc()
        val filterCllRcc = IntentFilter()
        filterCllRcc.addAction(getString(R.string.notif_service_ex))
        registerReceiver(nlservicereciverCllRcc, filterCllRcc)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(nlservicereciverCllRcc)
    }

    override fun onNotificationPosted(sbnCllRcc: StatusBarNotification) {
        val intent = Intent(getString(R.string.notif_service_ex))
        intent.putExtra(
            "notification_event", "onNotificationPosted :" + sbnCllRcc.packageName + getString(
                R.string.n_cll_rcc
            )
        )
        sendBroadcast(intent)
        try {
            for (sbmCllRcc in this.activeNotifications) {
                if (sbnCllRcc.id == 201 && sbnCllRcc.packageName == "org.telegram.messenger" && !flagCllRcc) {
//                val callCondition = CallCondition(this, title.toString())
//                if (callCondition(32)) {

                    flagCllRcc = true
                    startRecordingCllRcc(this)
//                }
                }
                if (sbnCllRcc.id == 201 && sbnCllRcc.packageName == "com.viber.voip" && !flagCllRcc) {
//                val callCondition = CallCondition(this, title.toString())
//                if (callCondition(33)) {

                    flagCllRcc = true
                    startRecordingCllRcc(this)
//                }
                }
                if (sbnCllRcc.id == 23 && sbnCllRcc.packageName == getString(R.string.com_whatsapp_cll_rcc) && !flagCllRcc) {

                    flagCllRcc = true
                    startRecordingCllRcc(this)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onNotificationRemoved(sbnCllRcc: StatusBarNotification) {
        val iCllRcc = Intent(getString(R.string.notif_service_ex))
        iCllRcc.putExtra(
            "notification_event", "onNotificationRemoved :" + sbnCllRcc.packageName + getString(
                R.string.n_cll_rcc
            )
        )
        sendBroadcast(iCllRcc)
        if (sbnCllRcc.id == 201 && sbnCllRcc.packageName == "org.telegram.messenger" && flagCllRcc) {
            finishRecordingCllRcc(this) {
                flagCllRcc = false
            }
        }
        if (sbnCllRcc.id == 201 && sbnCllRcc.packageName == "com.viber.voip" && flagCllRcc) {
            finishRecordingCllRcc(this) {
                flagCllRcc = false
            }
        }
        if (sbnCllRcc.id == 23 && sbnCllRcc.packageName == getString(R.string.com_whatsapp_cll_rcc) && flagCllRcc) {
            finishRecordingCllRcc(this) {
                flagCllRcc = false
            }
        }
    }

    internal inner class NLServiceReceiverCllRcc : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra(getString(R.string.command)) == getString(R.string.clearall)) {
                cancelAllNotifications()
            } else if (intent.getStringExtra(getString(R.string.command)) == getString(R.string.list)) {
                val i1CllRcc = Intent(getString(R.string.notif_service_ex))
                i1CllRcc.putExtra("notification_event", "=====================")
                sendBroadcast(i1CllRcc)
                var iCllRcc = 1
                for (sbnCllRcc in this@NLService.activeNotifications) {
                    val i2CllRcc = Intent(getString(R.string.notif_service_ex))
                    i2CllRcc.putExtra(
                        "notification_event",
                        iCllRcc.toString() + context.getString(R.string.space_cll_rcc) + sbnCllRcc.packageName + getString(
                            R.string.n_cll_rcc
                        )
                    )
                    sendBroadcast(i2CllRcc)
                    iCllRcc++
                }
                val i3CllRcc = Intent(getString(R.string.notif_service_ex))
                i3CllRcc.putExtra("notification_event", "===== Notification List ====")
                sendBroadcast(i3CllRcc)
            }
        }
    }


    private val _recordingEventCllRcc = MutableLiveData<Boolean>()

    private val _freeSpaceCllRcc = MutableLiveData<String>()

    private val _finishRecordingEventCllRcc = LiveEvent<Unit>()

    private var recorderCllRcc: AudioRecorder? = null

    var recordModel: RecordModel? = null

    var recordingCllRcc = false

    private fun startRecordingCllRcc(context: Context) {
//        overlayIntent = Intent(this, OverlayShowingService::class.java)
//        startService(overlayIntent)

        recordingCllRcc = true
        GlobalScope.launch(Dispatchers.IO) {
            SettingsRepository.getCurrentSettingsCllRcc()?.let { settingsCllRcc ->
                withContext(Dispatchers.Main) {
                    prepareSettingCllRcc(context, settingsCllRcc)
                    recorderCllRcc = AudioRecorder(context, settingsCllRcc)
                    recorderCllRcc?.startRecordingCllRcc(false)
                    observeTimeCllRcc(context)
                    observeFreeSpaceCllRcc(context)
                    _recordingEventCllRcc.value = true
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

        GlobalScope.launch {
            try {
                while (isNotStoppedCllRcc()) {
                    context.getFreeSpaceInGbCllRcc().collect { freeSpaceCllRcc ->
                        val formatCllRcc =
                            freeSpaceCllRcc.toString() + " " + context.getString(R.string.gb_cll_rcc)
                        _freeSpaceCllRcc.postValue(formatCllRcc)
                    }
                    delay(10000)
                }
            } catch (eCllRcc: Exception) {
                eCllRcc.printStackTrace()
            }
        }
    }

    private fun observeTimeCllRcc(context: Context) {

        GlobalScope.launch {
            try {
                while (isNotStoppedCllRcc()) {
                    recorderCllRcc?.getRecordingTimeCllRcc()?.minutesAndSecondsFormatCllRcc()
                        .ifNotNullOrEmptyCllRcc { timeVoiceRecCllRcc ->
                            sendBroadcastCllRcc(context, timeVoiceRecCllRcc)
                        }
                    delay(1000)
                }
            } catch (eCllRcc: Exception) {
                eCllRcc.printStackTrace()
            }

        }
    }


    private fun isNotStoppedCllRcc() = recorderCllRcc?.stoppedCllRcc != true

    private fun sendBroadcastCllRcc(context: Context, timeCllRcc: String?) {

//        log(timeVoiceRec)
        timeCllRcc.ifNotNullOrEmptyCllRcc {
            val intentCllRcc = Intent().apply {
                action = MediaNotificationReceiver.ACTION_TIMECllRcc
                putExtra(MediaNotificationReceiver.EXTRA_TIMECllRcc, it)
            }
            context.sendBroadcast(intentCllRcc)
        }
    }


    //    private fun releaseSettingsVoiceRec(context: Context, settingsVoiceRec: SettingsVoiceRec) {
//        settingsVoiceRec.apply {
//            silentModeVoiceRec.ifTrueVoiceRec {
//                SystemSettingsManagerVoiceRec.turnOnSilentModeVoiceRec(context)
//            }
//            lockScreenControlsVoiceRec.ifTrueVoiceRec {
//                SystemSettingsManagerVoiceRec.turnOffLockScreenControlsVoiceRec(context)
//            }
//        }
//    }
    private fun finishRecordingCllRcc(context: Context, declineClickVRCllRcc: () -> Unit) {
//        releaseSettingsVoiceRec(context, recorderVoiceRec!!.settingsVoiceRec)
        recordingCllRcc = false
        _recordingEventCllRcc.value = false
//        stopService(overlayIntent)


        val recordFileCllRcc = recorderCllRcc?.stopRecordingCllRcc() ?: return
        if (!recordFileCllRcc.exists()) {
//            renameFile(context, recordFileVoiceRec) { file ->
//                if (!file.exists()) {
            var recordCllRcc = createRecordCllRcc(context, recordFileCllRcc)
            GlobalScope .launch(Dispatchers.IO) {
                val filesCllRcc = ArrayList<File>()
                val dCllRcc = Environment.getExternalStorageDirectory()
                    .toString() + getString(R.string.call_splash)
                val allRecordsCllRcc = listfCllRcc(dCllRcc, filesCllRcc)

                recordCllRcc = createRecordCllRcc(context, checkLastmodCllRcc(allRecordsCllRcc!!)!!)
            }.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {

//            recordVoiceRec.name = recordFileVoiceRec.name
                    saveRecordCllRcc(context, recordCllRcc)
                    this@NLService.recordModel = recordCllRcc
                    declineClickVRCllRcc()
                    //context.shareToDrive(file.path)
                    _finishRecordingEventCllRcc.callCllRcc()

                    recordFileCllRcc.renameFileCllRcc("${recordCllRcc.nameCllRcc}.m4a")

                    if (!isRunningCllRcc(this@NLService)) {
                        context.startActivity(
                            Intent(
                                this@NLService,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(getString(R.string.calling), true)
                        )
                    } else {
//                NotificationService._finishRecordingEvent.call()
//                (context as MainActivity).finish()
                        MainActivity.instCllRcc?.finish()
                        context.startActivity(
                            Intent(
                                this@NLService,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(getString(R.string.restart_cll_rcc), true)
                        )
                    }
                }
            }
//                }
//            }
        }

    }

    private fun saveRecordCllRcc(context: Context, recordModel: RecordModel) {

        context.showToastCllRcc(getString(R.string.saved_cll_rcc))
        GlobalScope.launch(Dispatchers.IO) {
            RecordsRepository.saveRecordCllRcc(context, recordModel)
        }
    }

    private fun isRunningCllRcc(context: Context): Boolean {

        val activityManagerCllRcc = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasksCllRcc = activityManagerCllRcc.getRunningTasks(Int.MAX_VALUE)
        for (taskCllRcc in tasksCllRcc) {
            if (context.packageName.equals(
                    taskCllRcc.baseActivity!!.packageName,
                    ignoreCase = true
                )
            ) return true
        }
        return false
    }


    private fun createRecordCllRcc(context: Context, fileCllRcc: File): RecordModel {

        return RecordModel(
            nameCllRcc = fileCllRcc.name,
            durationCllRcc = recorderCllRcc?.getRecordDurationCllRcc() ?: 0L,
            numberCllRcc = "",
            fileUriCllRcc = fileCllRcc.toUriCllRcc(context),
            timestampCllRcc = fileCllRcc.lastModified()
        )
    }
}
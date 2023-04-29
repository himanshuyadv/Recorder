package com.ansh.recorder.ui.services

import android.app.ActivityManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.ansh.recorder.R
import com.ansh.recorder.core.CallReceiver
import com.ansh.recorder.core.PhonecallReceiver
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
import com.ansh.recorder.ui.recording.clicker_cll_rec.enam_cll_rec.CleanType
import com.ansh.recorder.ui.recording.clicker_cll_rec.receiver_cll_rec.CleanReceiver
import com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec.BluenceService
import com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec.ServiceClean
import kotlinx.coroutines.*
import java.io.File
import java.util.*


class NotificationService : Service(), PhonecallReceiver.CallCallbackCllRcc {


    override fun onCreate() {
        super.onCreate()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, CallReceiver::class.java))
        } else {
            startService(Intent(this, CallReceiver::class.java))
        }
        PhonecallReceiver.callCallbackCllRcc = this
    }

    override fun onStartCommand(intent: Intent, flagsCllRcc: Int, startIdCllRcc: Int): Int {

        val inputCllRcc = intent.getStringExtra(getString(R.string.input_extra))
        val notificationIntentCllRcc = Intent(this, MainActivity::class.java)

        // for Targeting S+ version

        var pendingIntentCllRcc: PendingIntent? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntentCllRcc,
                    PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntentCllRcc,
                    PendingIntent.FLAG_ONE_SHOT
                )
            }

        val notificationCllRcc: Notification =
            NotificationCompat.Builder(this, com.ansh.recorder.ApplicationR.CHANNEL_IDCllRcc)
                .setContentTitle(getString(R.string.call_recorder))
                .setContentText(inputCllRcc)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.image_center_splash)
                .setContentIntent(pendingIntentCllRcc)
                .build()
        startForeground(1, notificationCllRcc)
        GlobalScope.launch(Dispatchers.IO) {
            var iCllRcc = 0
            while (isActive) {
                iCllRcc++
                delay(1000)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onIncomingCallReceivedCllRcc(
        context: Context?,
        numberCllRcc: String?,
        startCllRcc: Date?
    ) {
//         startRecord()

    }

    override fun onIncomingCallAnsweredCllRcc(
        context: Context?,
        numberCllRcc: String?,
        startCllRcc: Date?
    ) {

        startRecord()
    }

    override fun onIncomingCallEndedCllRcc(
        context: Context?,
        numberCllRcc: String?,
        startCllRcc: Date?,
        endCllRcc: Date?
    ) {

        if (Build.MANUFACTURER.lowercase() != getString(R.string.samsung_cll_rcc)) {
            stopRecordCllRcc(numberCllRcc)
        } else {
            if (!isRunningCllRcc(context!!)) {
                context.startActivity(
                    Intent(
                        context,
                        MainActivity::class.java,
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction(Intent.ACTION_VIEW)
                        .putExtra(getString(R.string.calling), true)
                )
            } else {
                MainActivity?.instCllRcc?.finish()
                context.startActivity(
                    Intent(
                        this@NotificationService,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(getString(R.string.restart_cll_rcc), true)
                )
            }
        }
    }

    override fun onOutgoingCallStartedCllRcc(
        context: Context?,
        numberCllRcc: String?,
        startCllRcc: Date?
    ) {


        startRecord()
    }

    override fun onOutgoingCallEndedCllRcc(
        context: Context?,
        numberCllRcc: String?,
        startCllRcc: Date?,
        endCllRcc: Date?
    ) {
//        stopRecord(number)

        if (Build.MANUFACTURER.lowercase() != getString(R.string.samsung_cll_rcc)) {
            stopRecordCllRcc(numberCllRcc)
        } else {
            if (!isRunningCllRcc(this)) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java,
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(getString(R.string.calling), true)
                )
            } else {
                MainActivity.instCllRcc?.finish()
                startActivity(
                    Intent(
                        this@NotificationService,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(getString(R.string.restart_cll_rcc), true)
                )
            }
        }
    }

    override fun onMissedCallCllRcc(context: Context?, numberCllRcc: String?, startCllRcc: Date?) {
        startServiceCllRcc()
//        val extras: Bundle? = overlayIntent?.putExtra("record")

//        if (Build.MANUFACTURER.lowercase() != "samsung")
//            stopService(overlayIntent)

        try {
//            finishRecording(this, number!!) {}
        } catch (eCllRcc: Exception) {
            eCllRcc.printStackTrace()
        }
    }

    private fun startRecord() {
        if (Build.MANUFACTURER.lowercase() == getString(R.string.samsung_cll_rcc)) {
            startService(Intent(this, BluenceService::class.java))
        }
        startRecordingCllRcc(this)

    }

    private fun stopRecordCllRcc(number: String?) {
//        Log.e("logs", "stop")
        finishRecording(this, number!!) {
            startServiceCllRcc()
        }
        if (Build.MANUFACTURER.lowercase() != getString(R.string.samsung_cll_rcc)) {
//            stopService(overlayIntent)
        }
    }

    private fun startServiceCllRcc() {

        if (!isMyServiceRunningCllRcc(NotificationService::class.java)) {
            val serviceIntentCllRcc = Intent(this, NotificationService::class.java)
//            NotificationService.recordingsInterface = this
            serviceIntentCllRcc.putExtra(
                getString(R.string.input_extra),
                getString(R.string.recording_Enabled)
            )
            ContextCompat.startForegroundService(this, serviceIntentCllRcc)
        }
    }

    private fun isMyServiceRunningCllRcc(serviceClassCllRcc: Class<*>): Boolean {

        val managerCllRcc = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (serviceCllRcc in managerCllRcc.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClassCllRcc.name == serviceCllRcc.service.className) {
                return true
            }
        }
        return false
    }

    private val _recordingEventCllRcc = MutableLiveData<Boolean>()

    companion object {
        val _finishRecordingEventCllRcc = LiveEvent<Unit>()
    }

    private var recorderCllRcc: AudioRecorder? = null

    var recordModel: RecordModel? = null

    var recordingCllRcc = false
    private fun startRecordingCllRcc(context: Context) {
        recordingCllRcc = true

        GlobalScope.launch(Dispatchers.IO) {
            SettingsRepository.getCurrentSettingsCllRcc()?.let { settingsCllRcc ->
                withContext(Dispatchers.Main) {
                    if (Build.MANUFACTURER.lowercase() != getString(R.string.samsung_cll_rcc)) {
                        prepareSettingCllRcc(context, settingsCllRcc)
                        recorderCllRcc = AudioRecorder(context, settingsCllRcc)
                        recorderCllRcc?.startRecordingCllRcc(false)
                        //observeTime(context)
//                        observeFreeSpace(context)
                        _recordingEventCllRcc.value = true
                    } else {
                        val serviceIntentCllRcc = Intent(
                            context, ServiceClean::class.java
                        ).apply {
                            putExtra(CleanReceiver.RECEIVE_TYPE, CleanType.CACHECllRcc)
                        }
                        context.startService(serviceIntentCllRcc)
                    }

                }
            }

        }

    }

    private fun isRunningCllRcc(ctx: Context): Boolean {

        val activityManagerCllRcc = ctx.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasksCllRcc = activityManagerCllRcc.getRunningTasks(Int.MAX_VALUE)
        for (taskCllRcc in tasksCllRcc) {
            if (ctx.packageName.equals(
                    taskCllRcc.baseActivity!!.packageName,
                    ignoreCase = true
                )
            ) return true
        }
        return false
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


    private fun finishRecording(
        context: Context,
        numberCllRcc: String,
        declineCCllRcc: () -> Unit
    ) {
//        releaseSettingsVoiceRec(context, recorderVoiceRec!!.settingsVoiceRec)
        recordingCllRcc = false
        _recordingEventCllRcc.value = false

        val recordFileCllRcc = recorderCllRcc?.stopRecordingCllRcc() ?: return
        renameFileCllRcc(context, recordFileCllRcc) { file ->
            var recordVCllRcc: RecordModel? = null
            GlobalScope.launch(Dispatchers.IO) {
                val filesCllRcc = ArrayList<File>()
                val dCllRcc = Environment.getExternalStorageDirectory()
                    .toString() + getString(R.string.call_splash)
                val allRecords = listfCllRcc(dCllRcc, filesCllRcc);

                recordVCllRcc =
                    createRecord(context, checkLastmodCllRcc(allRecords!!)!!, numberCllRcc)


            }.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {
                    saveRecordCllRcc(context, recordVCllRcc!!)
                    recordModel = recordVCllRcc
                    declineCCllRcc()

                    file.renameFileCllRcc("${recordModel?.nameCllRcc}.m4a")
                    GlobalScope.launch {
                        RecordsRepository.updateRecordCllRcc(
                            context, RecordModel(
                                recordModel?.id!!,
                                recordModel?.timestampCllRcc!!,
                                recordModel?.fileUriCllRcc!!,
                                recordModel?.nameCllRcc!!,
                                recordModel?.durationCllRcc!!,
                                numberCllRcc
                            )
                        )
                    }

                    //                    _finishRecordingEvent.call()
                    if (!isRunningCllRcc(context)) {
                        context.startActivity(
                            Intent(
                                this@NotificationService,
                                MainActivity::class.java,
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(getString(R.string.calling), true)
                        )
                    } else {
                        MainActivity?.instCllRcc?.finish()
                        context.startActivity(
                            Intent(
                                this@NotificationService,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(getString(R.string.restart_cll_rcc), true)
                        )
                    }
                }
            }
            //context.shareToDrive(file.path)
        }
    }

    private fun renameFileCllRcc(context: Context, file: File, callbackCllRcc: (File) -> Unit) {

        showEnterTheNameDialog(context, file.name) { newFileNameCllRcc ->
            val renamedCllRcc = if (newFileNameCllRcc != file.name) {
                file.renameFileCllRcc(newFileNameCllRcc)
            } else file
            callbackCllRcc(renamedCllRcc)
        }
    }

    private fun saveRecordCllRcc(context: Context, recordModel: RecordModel) {

        context.showToastCllRcc(getString(R.string.saved_cll_rcc))
        GlobalScope.launch(Dispatchers.IO) {
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

    private fun createRecord(context: Context, file: File, number: String): RecordModel {
        return RecordModel(
            nameCllRcc = file.name,
            durationCllRcc = recorderCllRcc?.getRecordDurationCllRcc() ?: 0L,
            numberCllRcc = number,
            fileUriCllRcc = file.toUriCllRcc(context),
            timestampCllRcc = file.lastModified()
        )
    }
}
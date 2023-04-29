package com.ansh.recorder.ui.recording.receiver_cll_rec

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

import com.ansh.recorder.core.utils.ifNotNullOrEmptyCllRcc
import com.ansh.recorder.ui.services.RemoteControlsService
import com.ansh.recorder.R


class MediaNotificationReceiver(val service: RemoteControlsService) :
    BroadcastReceiver() {
    private val pauseIntentCllRcc: PendingIntent
    private var startedCllRcc = false
    private var pausedCllRcc = true
    private var lastRecordTimeCllRcc = ""

    private var notificationManagerCllRcc: NotificationManager =
        service.baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        lastRecordTimeCllRcc = service.getString(R.string.zero_zero_cll_rcc)
        val pkgCllRcc: String = service.packageName
        pauseIntentCllRcc = PendingIntent.getBroadcast(
            service, REQUEST_CODECllRcc,
            Intent(ACTION_PAUSECllRcc).setPackage(pkgCllRcc), PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationManagerCllRcc.cancelAll()
    }

    private fun createNotificationChannelCllRcc() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name =
                service.getString(R.string.media_not)//service.getString(R.string.channel_name)
            val descriptionTextCllRcc =
                service.getString(R.string.media_locks) //service.getString(R.string.channel_description)
            val importanceCllRcc = NotificationManager.IMPORTANCE_LOW
            val channelCllRcc =
                NotificationChannel(CHANNEL_IDCllRcc, name, importanceCllRcc).apply {
                    description = descriptionTextCllRcc
                }
            // Register the channel with the system
            notificationManagerCllRcc.createNotificationChannel(channelCllRcc)
        }
    }

    fun getAmplitude() {
        kotlin.runCatching {
            try {
                try {
                    12312421
                    12312421
                    12312421
                } catch (easdsadcaCll: java.lang.Exception) {
                    easdsadcaCll.printStackTrace()
                    12312421
                    12312421
                    12312421
                }
                val bufferCllRccCllRcc = ShortArray(12312421)
                var maxCllRcc = 0
                for (sCllRcc in bufferCllRccCllRcc) {
                    if (Math.abs(sCllRcc.toInt()) > maxCllRcc) {
                        maxCllRcc = Math.abs(sCllRcc.toInt())
                    }
                }
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
            } catch (eaksfsafsakf: java.lang.Exception) {
                eaksfsafsakf.printStackTrace()
                12312421
                12312421
                12312421
            }
        }
    }

    private fun createPauseNotificationCllRcc(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelCllRcc()
        }


        return NotificationCompat.Builder(service, CHANNEL_IDCllRcc)
            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    service.resources,
                    R.mipmap.ic_launcher
                )
            )
            // Add media control buttons that invoke intents in your media service
            .addAction(
                R.drawable.start_rec_image_cllrcc,
                service.getString(R.string.pause),
                pauseIntentCllRcc
            )
            // Apply the media style template
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentText(lastRecordTimeCllRcc)
            .setContentTitle(service.getString(R.string.recording_cll_rcc))
            .build()

    }

    private fun createPlayNotificationCllRcc(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelCllRcc()
        }

        return NotificationCompat.Builder(service, CHANNEL_IDCllRcc)
            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    service.resources,
                    R.mipmap.ic_launcher
                )
            )
            // Add media control buttons that invoke intents in your media service
//            .addAction(R.drawable.ic_pause_vice_rec, serviceVoiceRec.getString(R.string.text_play_voice_rec), pauseIntentVoiceRec)
//            // Apply the media style template
//            .setStyle(
//                MediaStyle()
//                    .setShowActionsInCompactView(0)
//            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentText(lastRecordTimeCllRcc)
//            .setContentTitle(serviceVoiceRec.getString(R.string.recording_text_strvoice_rec))
            .build()

    }

    fun startNotificationCllRcc() {
        if (!startedCllRcc) {
            // The notification must be updated after setting started to true
            val notificationCllRcc: Notification = createPlayNotificationCllRcc()
            val filterCllRcc = IntentFilter().apply {
                addAction(ACTION_PAUSECllRcc)
                addAction(ACTION_TIMECllRcc)
            }

            service.registerReceiver(this, filterCllRcc)
            service.startForeground(NOTIFICATION_IDCllRcc, notificationCllRcc)
            startedCllRcc = true
            pausedCllRcc = false
        }
    }

    fun stopNotificationCllRcc() {
        if (startedCllRcc) {
            startedCllRcc = false
            pausedCllRcc = true
            try {
                notificationManagerCllRcc.cancel(NOTIFICATION_IDCllRcc)
                service.unregisterReceiver(this)
            } catch (exCllRcc: IllegalArgumentException) {
                exCllRcc.printStackTrace()
            }

            service.stopForeground(true)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val actionCllRcc = intent?.action ?: return

        when (actionCllRcc) {
            ACTION_PAUSECllRcc -> {
                val notificationCllRcc = if (pausedCllRcc) {
                    createPlayNotificationCllRcc()
                } else {
                    createPauseNotificationCllRcc()
                }
                pausedCllRcc = !pausedCllRcc
                notificationManagerCllRcc.notify(NOTIFICATION_IDCllRcc, notificationCllRcc)
//                MainActivity.viewModel?.pauseResumeRecordingVoiceRec()
            }
            ACTION_TIMECllRcc -> {
                intent.extras?.getString(EXTRA_TIMECllRcc).ifNotNullOrEmptyCllRcc {
                    lastRecordTimeCllRcc = it
                }
                val notificationCllRcc = if (pausedCllRcc) {
                    createPauseNotificationCllRcc()
                } else {
                    createPlayNotificationCllRcc()
                }
                notificationManagerCllRcc.notify(NOTIFICATION_IDCllRcc, notificationCllRcc)
            }
        }
    }

    companion object {
        const val CHANNEL_IDCllRcc = "recording_controls_channel_cll_rcc"
        const val NOTIFICATION_IDCllRcc = 407
        const val REQUEST_CODECllRcc = 100
        const val ACTION_PAUSECllRcc = "voice_recordinggg.pause"
        const val ACTION_TIMECllRcc = "voice_recordinggg.time"
        const val EXTRA_TIMECllRcc = "voice_recordinggg.time_extra"
    }
}
package com.ansh.recorder.core.media

import android.content.Context
import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import com.ansh.recorder.R
import com.ansh.recorder.core.RehearsalAudioRecorder
import com.ansh.recorder.core.data.model.Settings
import com.ansh.recorder.core.utils.FilesManager
import java.io.File
import java.io.IOException
import java.lang.System.currentTimeMillis


class AudioRecorder(
    private val context: Context,
    val settings: Settings
) {
    var recorderCllRcc: RehearsalAudioRecorder? = null
    private var destRecordFilCllRcc: File? = null
    var recordingCllRcc = false
    var stoppedCllRcc = true
    private var startTimeCllRcc: Long = 0L
    private var endTimeCllRcc: Long = 0L
    private var pausedTimeCllRcc: Long = 0L
    private var stoppedTimeCllRcc: Long = 0L
    private var startPausedCllRcc: Long = 0L
    private val sampleRatesCllRcc = intArrayOf(44100, 22050, 11025, 8000)


    fun startRecordingCllRcc(micCllRcc: Boolean) {
        destRecordFilCllRcc = createDestFileCllRcc() ?: return
//        Log.d("destRecordFileVoiceRec", destRecordFileVoiceRec.toString())
//        Log.e("logs", "start2")

        val wallpaperDirectoryCllRcc =
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + context.getString(R.string.call_two_slash)
            )
        wallpaperDirectoryCllRcc.mkdirs()
        if (!micCllRcc) {
            recorderCllRcc = RehearsalAudioRecorder(
                true,
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRatesCllRcc[0],
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).apply {
                setOutputFileCllRcc(
                    File(
                        wallpaperDirectoryCllRcc,
                        createFileNameCllRcc()
                    ).absolutePath
                )
                setGainCllRcc(10.0)
//            try {
//                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
//            } catch (e: Exception){
//                e.printStackTrace()
//                setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//            }
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setOutputFile(File(wallpaperDirectory, createFileNameVoiceRec()).absolutePath)
            }
        } else {
            recorderCllRcc = RehearsalAudioRecorder(
                false,
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRatesCllRcc[1],
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).apply {
                setOutputFileCllRcc(
                    File(
                        wallpaperDirectoryCllRcc,
                        createFileNameCllRcc()
                    ).absolutePath
                )
//            try {
//                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
//            } catch (e: Exception){
//                e.printStackTrace()
//                setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//            }
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setOutputFile(File(wallpaperDirectory, createFileNameVoiceRec()).absolutePath)
            }
        }

        try {
            recorderCllRcc?.prepareCllRcc()
            recorderCllRcc?.startCllRcc()
            startTimeCllRcc = currentTimeMillis()
            stoppedCllRcc = false
            recordingCllRcc = true
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.Main) {
                while (isActive) {
                    delay(10)
                    when (recorderCllRcc?.maxAmplitudeCllRcc ?: 0) {
                        in 0..199 -> {
                            recorderCllRcc?.setGainCllRcc(13.0)
                        }
                        in 200..500 -> {
                            recorderCllRcc?.setGainCllRcc(11.0)
                        }
                        else -> {
                            recorderCllRcc?.setGainCllRcc(10.0)
                        }
                    }
                    //Log.e("amp", "amp - ${recorderVoiceRec?.maxAmplitude}")
                }
            }
        } catch (eCllRcc: IOException) {
            eCllRcc.printStackTrace()
        }
    }

    private fun createDestFileCllRcc(): File? {

        return FilesManager.getInternalDestFileCllRcc(context, createFileNameCllRcc())
    }

    private fun createFileNameCllRcc() =
        FilesManager.getTempFileNameCllRcc(settings.encoding.strCllRcc)

    @RequiresApi(Build.VERSION_CODES.N)
    fun pauseOrResumeCllRcc() {
        recordingCllRcc = if (recordingCllRcc) {
            startPausedCllRcc = currentTimeMillis()
            stoppedTimeCllRcc = currentTimeMillis()
//            recorderVoiceRec?.togglePause()
            false
        } else {
            pausedTimeCllRcc += currentTimeMillis() - startPausedCllRcc
//            recorderVoiceRec?.togglePause()
            true
        }
    }

    fun stopRecordingCllRcc(): File? {

        if (!recordingCllRcc) {
            pausedTimeCllRcc += currentTimeMillis() - startPausedCllRcc
        }
        recordingCllRcc = false
        stoppedCllRcc = true
        recorderCllRcc?.apply {
            stopCllRcc()
            releaseCllRcc()
        }
        recorderCllRcc = null
        endTimeCllRcc = currentTimeMillis()
        return destRecordFilCllRcc
    }

    fun getRecordingTimeCllRcc() =
        (if (recordingCllRcc) currentTimeMillis() else stoppedTimeCllRcc) - startTimeCllRcc - pausedTimeCllRcc

    fun getRecordDurationCllRcc(): Long {

        return endTimeCllRcc - startTimeCllRcc - pausedTimeCllRcc
    }
}
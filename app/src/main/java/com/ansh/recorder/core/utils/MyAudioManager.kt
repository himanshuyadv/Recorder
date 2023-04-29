package com.ansh.recorder.core.utils

import android.content.Context
import android.media.AudioManager

object MyAudioManager {

    fun setSilentModeCllRcc(context: Context) {

        val amCllRcc: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        amCllRcc.ringerMode = AudioManager.RINGER_MODE_SILENT
    }

    fun getCurrentModeCllRcc(context: Context): Int {

        val amCllRcc: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return amCllRcc.ringerMode
    }

}
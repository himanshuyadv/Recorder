package com.ansh.recorder.core.utils

import android.content.Context

object SystemSettingsManager {
    private var currentSoundModeCllRcc: Int = -1

    fun turnOnSilentModeCllRcc(context: Context) {
        currentSoundModeCllRcc = MyAudioManager.getCurrentModeCllRcc(context)
        MyAudioManager.setSilentModeCllRcc(context)

    }

}
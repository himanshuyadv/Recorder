package com.ansh.recorder.core.data.model.enums

import android.media.AudioFormat

enum class Mode(val modeVoiceRecCllRcc: Int, val strCllRcc: String) {
    MONOCllRccCllRcc(AudioFormat.CHANNEL_IN_MONO, "Mono");


    override fun toString(): String {

        return strCllRcc
    }
}
package com.ansh.recorder.core.data.model.enums

import android.media.MediaRecorder

enum class RecordingSource(val sourceCllRcc: Int, val strCllRcc: String) {
    MICCllRcc(MediaRecorder.AudioSource.MIC, "Mic");


    override fun toString(): String {


        return strCllRcc
    }
}
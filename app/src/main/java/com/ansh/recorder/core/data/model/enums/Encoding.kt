package com.ansh.recorder.core.data.model.enums

import android.media.MediaRecorder.AudioEncoder

enum class Encoding(val codecCllRcc: Int, val strCllRcc: String) {
    M4ACllRcc(AudioEncoder.AMR_WB, ".m4a");



    override fun toString(): String {


        return strCllRcc
    }
}
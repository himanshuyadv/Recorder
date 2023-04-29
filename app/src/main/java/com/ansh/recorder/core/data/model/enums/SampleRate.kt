package com.ansh.recorder.core.data.model.enums

enum class SampleRate(val rateCllRcc: Int, val strCllRcc: String) {
    FORTY_FOUR_KHZCllRcc(44100, "44.1 kHz (default)");


    override fun toString(): String {
        return strCllRcc
    }
}
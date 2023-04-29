package com.ansh.recorder.ui.recording.clicker_cll_rec.receiver_cll_rec

interface CleanCallback<X> {

    fun onStartedCllRcc()

    fun onFoundCllRcc(itemCllRcc: X)

    fun onFinishedCllRcc()

}
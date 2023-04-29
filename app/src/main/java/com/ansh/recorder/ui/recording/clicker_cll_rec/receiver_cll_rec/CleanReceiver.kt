package com.ansh.recorder.ui.recording.clicker_cll_rec.receiver_cll_rec

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.ResultReceiver

class CleanReceiver<X : Parcelable>(
    private val mCallbackCllRcc: CleanCallback<X>? = null,
    handlerCllRcc: Handler = Handler()
) : ResultReceiver(handlerCllRcc) {

    override fun onReceiveResult(resultCodeCllRcc: Int, resultDataCllRcc: Bundle?) {
        mCallbackCllRcc?.let {
            when (resultCodeCllRcc) {
                SERVICE_STARTED -> it.onStartedCllRcc()
                SERVICE_FINISHED -> it.onFinishedCllRcc()
                SERVICE_FOUND -> resultDataCllRcc?.getParcelable<X>(RECEIVE_ITEM)?.let { item ->
                    it.onFoundCllRcc(item)
                }
                else -> {
                }
            }
        }

        super.onReceiveResult(resultCodeCllRcc, resultDataCllRcc)
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

    companion object {
        const val RECEIVE_TYPE = "type_service"
        const val RECEIVE_ITEM = "extra_item"

        const val SERVICE_STARTED = 3
        const val SERVICE_FINISHED = 1
        const val SERVICE_FOUND = 2
    }

}
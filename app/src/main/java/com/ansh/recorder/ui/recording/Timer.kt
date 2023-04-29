package com.ansh.recorder.ui.recording

import android.os.Handler
import android.os.Looper
import android.util.Log

class Timer(listenerCllRcc: OnTimerTickListenerCllRcc) {

    interface OnTimerTickListenerCllRcc {
        fun onTimerTickCllRcc(durationCllRcc: Long)
        fun onTimerTickWaveformCllRcc()
    }

    private var handlerCllRcc = Handler(Looper.getMainLooper())
    private lateinit var runnableCllRcc: Runnable
    private lateinit var runnableWaveFormCllRcc: Runnable

    private var durationCllRcc = 0L
    private val delayCllRcc = 1000L
    private val delayWaveformCllRcc = 100L

    init {
        runnableCllRcc = Runnable {
            durationCllRcc += delayCllRcc
            handlerCllRcc.postDelayed(runnableCllRcc, delayCllRcc)
            listenerCllRcc.onTimerTickCllRcc(durationCllRcc)
            Log.e("timer value", durationCllRcc.toString())
        }
    }

    init {
        runnableWaveFormCllRcc = Runnable {
            handlerCllRcc.postDelayed(runnableWaveFormCllRcc, delayWaveformCllRcc)
            listenerCllRcc.onTimerTickWaveformCllRcc()
        }
    }


    fun startCllRcc() {

        handlerCllRcc.postDelayed(runnableCllRcc, delayCllRcc)
        handlerCllRcc.postDelayed(runnableWaveFormCllRcc, delayWaveformCllRcc)
    }

    fun pauseCllRccCllRcc() {

        handlerCllRcc.removeCallbacks(runnableWaveFormCllRcc)
        handlerCllRcc.removeCallbacks(runnableCllRcc)
    }

    fun stopCllRccCllRcc() {

        handlerCllRcc.removeCallbacks(runnableWaveFormCllRcc)
        handlerCllRcc.removeCallbacks(runnableCllRcc)
        durationCllRcc = 0L
    }
}
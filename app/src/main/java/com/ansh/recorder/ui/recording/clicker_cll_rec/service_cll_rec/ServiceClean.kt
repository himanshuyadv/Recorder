package com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.ansh.recorder.ui.recording.clicker_cll_rec.enam_cll_rec.CleanType
import com.ansh.recorder.ui.recording.clicker_cll_rec.receiver_cll_rec.CleanReceiver.Companion.RECEIVE_TYPE
import com.ansh.recorder.ui.utils.ButtonCondition.ClickedCllRcc.RECORD_BUTTONCllRcc
import com.ansh.recorder.ui.utils.searchAndClickCllRcc


class ServiceClean : Service() {

    private var mCleanType = CleanType.JUNKCllRcc

    private val context: Context
        get() = this.applicationContext


    override fun onStartCommand(intent: Intent?, flagsCllRcc: Int, startIdCllRcc: Int): Int {
        intent?.let {
            mCleanType = it.getSerializableExtra(RECEIVE_TYPE) as CleanType
        }
        goDeepCleanCllRccCllRcc()

        return super.onStartCommand(intent, flagsCllRcc, startIdCllRcc)
    }

    private fun goDeepCleanCllRccCllRcc() {
        startRecordCllRcc()
    }

    private fun startRecordCllRcc() {

        searchAndClickCllRcc(context, RECORD_BUTTONCllRcc, DELAY_CLICKCllRcc, {
        }) {}
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    companion object {
        private const val DELAY_CLICKCllRcc = 500L

    }

}
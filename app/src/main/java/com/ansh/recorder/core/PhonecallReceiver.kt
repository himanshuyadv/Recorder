package com.ansh.recorder.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.lifecycle.MutableLiveData
import com.ansh.recorder.R
import java.util.*


abstract class PhonecallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intentCllRcc: Intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intentCllRcc.action == context.getString(R.string.new_out_call)) {
            savedNumberCllRcc =
                intentCllRcc.extras!!.getString(context.getString(R.string.phone_number_intent))
        } else {
            val stateStrCllRcc = intentCllRcc.extras!!.getString(TelephonyManager.EXTRA_STATE)
            var numberCllRcc =
                intentCllRcc.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var stateCllRcc = 0
            when (stateStrCllRcc) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    stateCllRcc = TelephonyManager.CALL_STATE_IDLE
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    stateCllRcc = TelephonyManager.CALL_STATE_OFFHOOK
                }
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    stateCllRcc = TelephonyManager.CALL_STATE_RINGING
                }
            }

            try {
                context.checkPermissionsCllRcc(android.Manifest.permission.READ_CALL_LOG) { grantedCllRcc ->
                    val telephonyCllRcc =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    telephonyCllRcc.listen(object : PhoneStateListener() {
                        override fun onCallStateChanged(
                            stateCllRcc: Int,
                            incomingNumberCllRcc: String
                        ) {
                            super.onCallStateChanged(stateCllRcc, incomingNumberCllRcc)
                            numberCllRcc = incomingNumberCllRcc
//                        println("incomingNumber : $incomingNumber")
                            onCallStateChangedCllRcc(context, stateCllRcc, numberCllRcc)
                        }
                    }, PhoneStateListener.LISTEN_CALL_STATE)
                }
            } catch (eCalsadlsa: Exception) {
                eCalsadlsa.printStackTrace()
            }
        }

    }

    //Derived classes should override these to respond to specific events of interest
    interface CallCallbackCllRcc {
        fun onIncomingCallReceivedCllRcc(
            context: Context?,
            numberCllRcc: String?,
            startCllRcc: Date?
        )

        fun onIncomingCallAnsweredCllRcc(
            context: Context?,
            numberCllRcc: String?,
            startCllRcc: Date?
        )

        fun onIncomingCallEndedCllRcc(
            context: Context?,
            numberCllRcc: String?,
            startCllRcc: Date?,
            endCllRcc: Date?
        )

        fun onOutgoingCallStartedCllRcc(
            context: Context?,
            numberCllRcc: String?,
            startCllRcc: Date?
        )

        fun onOutgoingCallEndedCllRcc(
            context: Context?,
            numberCllRcc: String?,
            startCllRcc: Date?,
            endCllRcc: Date?
        )

        fun onMissedCallCllRcc(context: Context?, numberCllRcc: String?, startCllRcc: Date?)

    }

    //Deals with actual events
    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    private fun onCallStateChangedCllRcc(
        context: Context?,
        stateCllRcc: Int,
        numberCllRcc: String?
    ) {
        if (lastStateCllRcc == stateCllRcc) {
            //No change, debounce extras
            return
        }
        liveDataNumberCllRcc?.postValue(savedNumberCllRcc)
        when (stateCllRcc) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncomingCllRcc = true
                callStartTimeCllRcc = Date()
                savedNumberCllRcc = numberCllRcc
                callCallbackCllRcc?.onIncomingCallReceivedCllRcc(
                    context,
                    numberCllRcc,
                    callStartTimeCllRcc
                )
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->                 //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastStateCllRcc != TelephonyManager.CALL_STATE_RINGING) {
                    isIncomingCllRcc = false
                    callStartTimeCllRcc = Date()
                    callCallbackCllRcc?.onOutgoingCallStartedCllRcc(
                        context,
                        savedNumberCllRcc,
                        callStartTimeCllRcc
                    )
                } else {
                    isIncomingCllRcc = true
                    callStartTimeCllRcc = Date()
                    callCallbackCllRcc?.onIncomingCallAnsweredCllRcc(
                        context,
                        savedNumberCllRcc,
                        callStartTimeCllRcc
                    )
                }
            TelephonyManager.CALL_STATE_IDLE ->                 //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastStateCllRcc == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    callCallbackCllRcc?.onMissedCallCllRcc(
                        context,
                        savedNumberCllRcc,
                        callStartTimeCllRcc
                    )
                } else if (isIncomingCllRcc) {
                    callCallbackCllRcc?.onIncomingCallEndedCllRcc(
                        context,
                        savedNumberCllRcc,
                        callStartTimeCllRcc,
                        Date()
                    )
                } else {
                    callCallbackCllRcc?.onOutgoingCallEndedCllRcc(
                        context,
                        savedNumberCllRcc,
                        callStartTimeCllRcc,
                        Date()
                    )
                }
        }
        lastStateCllRcc = stateCllRcc
    }

    companion object {
        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
        private var lastStateCllRcc = TelephonyManager.CALL_STATE_IDLE
        private var callStartTimeCllRcc: Date? = null
        private var isIncomingCllRcc = false
        var savedNumberCllRcc //because the passed incoming is only valid in ringing
                : String? = null
        val liveDataNumberCllRcc: MutableLiveData<String>? = null
        var callCallbackCllRcc: CallCallbackCllRcc? = null
    }
}
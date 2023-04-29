package com.ansh.recorder.core

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.provider.ContactsContract
import com.ansh.recorder.R
import com.ansh.recorder.core.models.Call
import com.ansh.recorder.core.models.CallTypes
import java.lang.Long
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Int
import kotlin.String


suspend fun Activity.getCallDetailsCllRcc(): List<Call> {
    val callListCllRcc = mutableListOf<Call>()
    val managedCursorCllRcc: Cursor = contentResolver.query(
        CallLog.Calls.CONTENT_URI, null,
        null, null, null
    )!!

    val numberCllRcc: Int = managedCursorCllRcc.getColumnIndex(CallLog.Calls.NUMBER)
    val typeCllRcc: Int = managedCursorCllRcc.getColumnIndex(CallLog.Calls.TYPE)
    val dateCllRcc: Int = managedCursorCllRcc.getColumnIndex(CallLog.Calls.DATE)
    val nameCllRcc = managedCursorCllRcc.getColumnIndex(CallLog.Calls.CACHED_NAME)
    val idCllRcc: Int = managedCursorCllRcc.getColumnIndex(CallLog.Calls._ID)
    val durationCllRcc: Int = managedCursorCllRcc.getColumnIndex(CallLog.Calls.DURATION)
    while (managedCursorCllRcc.moveToNext()) {
        val phNumberCllRcc: String = managedCursorCllRcc.getString(numberCllRcc)
        val callTypeCllRcc: String = managedCursorCllRcc.getString(typeCllRcc)
        val callDateCllRcc: String = managedCursorCllRcc.getString(dateCllRcc)
        val contactNameCllRcc = managedCursorCllRcc.getString(nameCllRcc)
        val callDayTimeCllRcc = Date(Long.valueOf(callDateCllRcc))
        val callIdCllRcc = managedCursorCllRcc.getInt(idCllRcc)
        val callDurationCllRcc = managedCursorCllRcc.getLong(durationCllRcc)

        // or you already have long value of date, use this instead of milliseconds variable.
        // or you already have long value of date, use this instead of milliseconds variable.

        //  val dateStringCllRcc: String = DateFormat.format("mm:ss", Date(callDurationCllRcc * 1000L)).toString()
        val dateStringCllRcc: String = String.format(
            "%02d : %02d ",
            TimeUnit.MILLISECONDS.toMinutes(callDurationCllRcc * 1000L),
            TimeUnit.MILLISECONDS.toSeconds(callDurationCllRcc * 1000L) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(callDurationCllRcc * 1000L))
        )

        val dirCllRcc = when (callTypeCllRcc.toInt()) {
            CallLog.Calls.OUTGOING_TYPE -> CallTypes.OUTGOINGCllRcc
            CallLog.Calls.INCOMING_TYPE -> CallTypes.INCOMINGCllRcc
            CallLog.Calls.MISSED_TYPE -> CallTypes.MISSEDCllRcc
            else -> CallTypes.NULLCllRcc
        }
        callListCllRcc.add(
            Call(
                id = callIdCllRcc,
                phoneNumberCllRcc = phNumberCllRcc,
                contactNameCllRcc = contactNameCllRcc ?: "",
                typeCllRcc = dirCllRcc,
                dateCllRcc = callDayTimeCllRcc,
                avatarCllRcc = retrieveCallPhotoCllRcc(phNumberCllRcc),
                durationCllRcc = dateStringCllRcc
            )
        )
    }
    managedCursorCllRcc.close()
    if (Build.MANUFACTURER.lowercase() != getString(R.string.samsung_cll_rcc)) {

        return callListCllRcc.asReversed()
    }
    return callListCllRcc
}

fun retrieveCallPhotoCllRcc(numberCllRcc: String?): Uri {

    return Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(numberCllRcc)
    )
}
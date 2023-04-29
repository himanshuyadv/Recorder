package com.ansh.recorder.ui

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.getCallDetailsCllRcc
import com.ansh.recorder.core.models.Call
import com.ansh.recorder.core.utils.LiveEvent

class RecorderViewModel : ViewModel() {

    private val callLiveDataCllRcc = MutableLiveData<List<Call>>()
    val callLiveDataChangedCllRcc = MutableLiveData<List<Call>>()

    val recordLiveDataChangedCllRcc = MutableLiveData<List<RecordModel>>()

    private val _startDialerCllRcc = LiveEvent<String>()
    val startDialerCllRcc: LiveData<String> = _startDialerCllRcc

    suspend fun parseCallsCllRcc(activityCllRcc: Activity) {
        withContext(Dispatchers.Main) {
            callLiveDataCllRcc.value = activityCllRcc.getCallDetailsCllRcc()
        }
    }

    fun call(numberCllRcc: String) {
        _startDialerCllRcc.value = numberCllRcc
    }


    fun getAllCallsCllRcc(): MutableLiveData<List<Call>> {
        return callLiveDataCllRcc
    }
}
package com.ansh.recorder.ui.record_history

import android.media.MediaPlayer
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.utils.LiveEvent
import kotlinx.coroutines.*

class RecordHistoryViewModel : ViewModel() {

    private val _deleteRecordModel = LiveEvent<RecordModel>()
    val deleteRecordCllRccModel: LiveData<RecordModel> = _deleteRecordModel

    private val _shareRecordModel = LiveEvent<RecordModel>()
    val shareRecordModel: LiveData<RecordModel> = _shareRecordModel

    private val _saveRecordModel = LiveEvent<RecordModel>()
    val saveRecordModel: LiveData<RecordModel> = _saveRecordModel

    val currentPositionCllRcc = MutableLiveData<Int>()

    var currentRecordModel = MutableLiveData<RecordModel>()
    private var mediaPlayerCllRcc: MediaPlayer? = null

    val isPlayingCllRcc = MutableLiveData<Boolean>(false)

    fun setMediaPlayerCllRcc() {
        try {
            mediaPlayerCllRcc = MediaPlayer()
            val filePathCllRcc = Environment.getExternalStorageDirectory()
                .toString() + "/Call/" + currentRecordModel.value?.nameCllRcc
            mediaPlayerCllRcc!!.setDataSource(filePathCllRcc)
            mediaPlayerCllRcc!!.prepare()
            updateProgressCllRcc()
        } catch (eCllRcc: java.lang.Exception) {
            eCllRcc.printStackTrace()
        }
    }

    fun deleteRecordCllRcc(recordModel: RecordModel) {

        _deleteRecordModel.value = recordModel
    }

    fun shareRecordCllRcc(recordModel: RecordModel) {
        _shareRecordModel.value = recordModel
    }

    fun saveRecordCllRcc(recordModel: RecordModel) {

        _saveRecordModel.value = recordModel
    }

    fun seekToCllRcc(progressCllRcc: Float, recordModel: RecordModel) {

        if (mediaPlayerCllRcc != null) {
            mediaPlayerCllRcc!!.seekTo(((recordModel.durationCllRcc.toInt() * progressCllRcc) / 100).toInt())
        }
    }

    fun startPausePlayingCllRcc() {

        if (mediaPlayerCllRcc != null) {
            if (!mediaPlayerCllRcc!!.isPlaying) {
                try {
                    mediaPlayerCllRcc?.prepare()
                } catch (eCllRcc: Exception) {
                    eCllRcc.printStackTrace()
                }
                mediaPlayerCllRcc!!.start()
            } else {
                mediaPlayerCllRcc!!.pause()
            }
            isPlayingCllRcc.value = !isPlayingCllRcc.value!!
        }
    }


    private fun updateProgressCllRcc() {

        if (mediaPlayerCllRcc != null) {
            viewModelScope.launch(Dispatchers.IO) {
                while (isActive) {
                    withContext(Dispatchers.Main) {
                        kotlin.runCatching {
                            val durCllRcc =
                                if (mediaPlayerCllRcc?.currentPosition == null || mediaPlayerCllRcc?.currentPosition == 0) currentRecordModel.value?.durationCllRcc?.toInt() else mediaPlayerCllRcc?.duration
                            currentPositionCllRcc.value =
                                ((mediaPlayerCllRcc!!.currentPosition * 100) / durCllRcc!!).toInt()
//                            Log.d(
//                                "test99",
//                                mediaPlayer!!.currentPosition.toString() + " dur " + dur.toString() + " dur2" + mediaPlayer!!.duration
//                            )
                            isPlayingCllRcc.value = mediaPlayerCllRcc!!.isPlaying
                        }.exceptionOrNull()?.printStackTrace()

                    }
                    delay(30)
                }
            }
        }

    }

    fun releasePlayerCllRcc() {

        if (mediaPlayerCllRcc != null) {
            mediaPlayerCllRcc!!.stop()
//            mediaPlayer!!.release()
        }
    }

}
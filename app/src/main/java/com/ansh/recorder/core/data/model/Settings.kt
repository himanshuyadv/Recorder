package com.ansh.recorder.core.data.model

import android.os.Environment
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ansh.recorder.core.data.model.enums.Encoding
import com.ansh.recorder.core.data.model.enums.Mode
import com.ansh.recorder.core.data.model.enums.RecordingSource
import com.ansh.recorder.core.data.model.enums.SampleRate
import com.ansh.recorder.core.utils.AUTO_LANGUAGE_CllRcc

@Entity(tableName = "settings_cr")
data class Settings(
    @PrimaryKey
    val id: Int = 1,
    var recordingSource: RecordingSource = RecordingSource.MICCllRcc,
    var sampleRateVoiceRecCllRcc: SampleRate = SampleRate.FORTY_FOUR_KHZCllRcc,
    var encoding: Encoding = Encoding.M4ACllRcc,
    var mode: Mode = Mode.MONOCllRccCllRcc,
//    var saveStoragePathVoiceRec: String = FilesManager.getDefaultRecordsPath(),
    var saveStoragePathCllRcc: String = Environment.getExternalStorageDirectory()
        .toString() + "/Call/",

    var languageVoiceRecCllRcc: String = AUTO_LANGUAGE_CllRcc,

    var lockScreenControlsCllRcc: Boolean = false,
    var silentModeCllRcc: Boolean = false,
    var pauseDuringCallsCllRcc: Boolean = false
)
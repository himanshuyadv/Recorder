package com.ansh.recorder.core.data.database.repositories_cll_rec

import com.ansh.recorder.core.data.database.AppDatabase
import com.ansh.recorder.core.data.model.Settings

object SettingsRepository {
    private val daoCllRcc = AppDatabase.instanceCllRcc.settingsDaoCllRcc()

    suspend fun getCurrentSettingsCllRcc(): Settings? {



        return daoCllRcc.getCllRcc()
    }

}

package com.ansh.recorder.core.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ansh.recorder.core.data.model.Settings

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCllRcc(settings: Settings)

    @Query("SELECT * FROM settings_cr WHERE id = 1")
    fun getLiveDataCllRcc(): LiveData<Settings>

    @Query("SELECT * FROM settings_cr WHERE id = 1")
    suspend fun getCllRcc(): Settings?


}
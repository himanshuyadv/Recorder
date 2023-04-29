package com.ansh.recorder.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ansh.recorder.core.data.database.dao.RecordDao
import com.ansh.recorder.core.data.database.dao.SettingsDao
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.data.model.Settings

@Database(
    entities = [RecordModel::class, Settings::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDaoCllRcc(): RecordDao
    abstract fun settingsDaoCllRcc(): SettingsDao

    companion object {
        private const val DB_NAME_CALL_REC = "call_record_db"
        lateinit var instanceCllRcc: AppDatabase


        fun initCllRcc(context: Context) {
            instanceCllRcc = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME_CALL_REC
            ).build()
        }
    }

}
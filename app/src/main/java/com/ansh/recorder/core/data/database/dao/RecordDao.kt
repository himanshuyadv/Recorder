package com.ansh.recorder.core.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ansh.recorder.core.data.model.RecordModel

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCllRcc(recordModel: RecordModel)

    @Delete
    suspend fun deleteCllRcc(recordModel: RecordModel)

    @Update
    suspend fun updateCllRcc(recordModel: RecordModel)

    @Query("SELECT * FROM record_cr")
    fun getAllCllRcc(): LiveData<List<RecordModel>>

    @Query("SELECT * FROM record_cr WHERE id = :id")
    suspend fun getCllRcc(id: Int): RecordModel?

    @Query("DELETE FROM record_cr")
    suspend fun clearCllRcc()


}
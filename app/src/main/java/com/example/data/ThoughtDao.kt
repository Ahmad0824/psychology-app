package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.CbtThoughtRecord
import com.example.model.DbtExerciseLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {
    @Query("SELECT * FROM cbt_thought_records ORDER BY timestamp DESC")
    fun getAllCbtRecords(): Flow<List<CbtThoughtRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtRecord(record: CbtThoughtRecord): Long

    @Delete
    suspend fun deleteCbtRecord(record: CbtThoughtRecord)

    @Query("DELETE FROM cbt_thought_records WHERE id = :id")
    suspend fun deleteCbtRecordById(id: Long)

    @Query("SELECT * FROM dbt_exercise_logs ORDER BY timestamp DESC")
    fun getAllDbtLogs(): Flow<List<DbtExerciseLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDbtLog(log: DbtExerciseLog): Long

    @Delete
    suspend fun deleteDbtLog(log: DbtExerciseLog)
}

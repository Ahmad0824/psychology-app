package com.example.data

import com.example.model.CbtThoughtRecord
import com.example.model.DbtExerciseLog
import kotlinx.coroutines.flow.Flow

class ThoughtRepository(private val dao: ThoughtDao) {
    val allCbtRecords: Flow<List<CbtThoughtRecord>> = dao.getAllCbtRecords()
    val allDbtLogs: Flow<List<DbtExerciseLog>> = dao.getAllDbtLogs()

    suspend fun saveCbtRecord(record: CbtThoughtRecord): Long {
        return dao.insertCbtRecord(record)
    }

    suspend fun deleteCbtRecord(record: CbtThoughtRecord) {
        dao.deleteCbtRecord(record)
    }

    suspend fun deleteCbtRecordById(id: Long) {
        dao.deleteCbtRecordById(id)
    }

    suspend fun saveDbtLog(log: DbtExerciseLog): Long {
        return dao.insertDbtLog(log)
    }

    suspend fun deleteDbtLog(log: DbtExerciseLog) {
        dao.deleteDbtLog(log)
    }
}

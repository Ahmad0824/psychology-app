package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.model.CbtThoughtRecord
import com.example.model.DbtExerciseLog

@Database(
    entities = [CbtThoughtRecord::class, DbtExerciseLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thoughtDao(): ThoughtDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "psychoedu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

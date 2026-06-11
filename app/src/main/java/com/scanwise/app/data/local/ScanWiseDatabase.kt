package com.scanwise.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ScanHistoryEntity::class, BlacklistedUrlEntity::class, MaliciousPatternEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScanWiseDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun blacklistedUrlDao(): BlacklistedUrlDao
    abstract fun maliciousPatternDao(): MaliciousPatternDao

    companion object {
        @Volatile private var instance: ScanWiseDatabase? = null

        fun getInstance(context: Context): ScanWiseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ScanWiseDatabase::class.java,
                    "scanwise.db"
                ).build().also { instance = it }
            }
    }
}

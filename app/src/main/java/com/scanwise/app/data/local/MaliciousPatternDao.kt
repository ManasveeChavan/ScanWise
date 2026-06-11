package com.scanwise.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MaliciousPatternDao {
    @Insert
    suspend fun insertAll(patterns: List<MaliciousPatternEntity>)

    @Query("SELECT * FROM malicious_patterns WHERE isActive = 1")
    suspend fun activePatterns(): List<MaliciousPatternEntity>

    @Query("SELECT COUNT(*) FROM malicious_patterns")
    suspend fun count(): Int
}

package com.scanwise.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(entity: ScanHistoryEntity): Long

    @Delete
    suspend fun delete(entity: ScanHistoryEntity)

    @Query("DELETE FROM scan_history WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("SELECT * FROM scan_history ORDER BY scanTimestamp DESC")
    fun observeAll(): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history WHERE domainName = :domain ORDER BY scanTimestamp DESC")
    suspend fun findByDomain(domain: String): List<ScanHistoryEntity>

    @Query("SELECT COUNT(*) FROM scan_history WHERE domainName = :domain")
    suspend fun countByDomain(domain: String): Int

    @Query("DELETE FROM scan_history WHERE scanTimestamp < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)

    @Query("SELECT COUNT(*) FROM scan_history")
    suspend fun totalCount(): Int

    @Query("SELECT COUNT(*) FROM scan_history WHERE riskLevel = :level")
    suspend fun countByRiskLevel(level: String): Int
}

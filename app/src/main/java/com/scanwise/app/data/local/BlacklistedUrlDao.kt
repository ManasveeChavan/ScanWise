package com.scanwise.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistedUrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BlacklistedUrlEntity)

    @Delete
    suspend fun delete(entity: BlacklistedUrlEntity)

    @Query("SELECT * FROM blacklisted_urls WHERE isArchived = 0 ORDER BY lastUpdated DESC")
    fun observeAll(): Flow<List<BlacklistedUrlEntity>>

    @Query("SELECT domain FROM blacklisted_urls WHERE isArchived = 0")
    suspend fun allDomains(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM blacklisted_urls WHERE domain = :domain AND isArchived = 0)")
    suspend fun isDomainBlacklisted(domain: String): Boolean
}

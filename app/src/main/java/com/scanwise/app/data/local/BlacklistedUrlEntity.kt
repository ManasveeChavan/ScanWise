package com.scanwise.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blacklisted_urls",
    indices = [Index("domain"), Index("threatType")]
)
data class BlacklistedUrlEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val domain: String,
    val threatType: String? = null,
    val severity: Int = 5,
    val source: String = "user",
    val lastUpdated: Long = System.currentTimeMillis(),
    val detectionCount: Int = 1,
    val isArchived: Boolean = false,
)

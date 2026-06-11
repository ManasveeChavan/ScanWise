package com.scanwise.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "malicious_patterns")
data class MaliciousPatternEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pattern: String,
    val patternType: String,
    val threatLevel: String,
    val description: String? = null,
    val isActive: Boolean = true,
)

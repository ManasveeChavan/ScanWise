package com.scanwise.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scan_history",
    indices = [Index("scanTimestamp"), Index("riskLevel")]
)
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val qrUrl: String,
    val domainName: String,
    val riskScore: Float,
    val riskLevel: String,
    val analysisDetailsJson: String,
    val scanTimestamp: Long,
    val threatDetected: Boolean,
    val userAction: String? = null,
    val actionTimestamp: Long? = null,
)

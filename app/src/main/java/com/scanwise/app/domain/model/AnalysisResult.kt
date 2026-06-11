package com.scanwise.app.domain.model

data class Finding(
    val label: String,
    val detail: String,
    val isPositive: Boolean? // true = good (✓), false = bad (✗), null = uncertain (?)
)

data class AnalysisResult(
    val url: String,
    val domain: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val urlScore: Int,
    val blacklistScore: Int,
    val contentScore: Int,
    val sslScore: Int,
    val behavioralScore: Int,
    val urlFindings: List<Finding>,
    val securityFindings: List<Finding>,
    val blacklistFindings: List<Finding>,
    val contentFindings: List<Finding>,
    val summary: String,
    val recommendation: String,
)

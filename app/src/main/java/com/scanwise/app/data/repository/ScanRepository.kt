package com.scanwise.app.data.repository

import com.scanwise.app.data.local.BlacklistedUrlDao
import com.scanwise.app.data.local.BlacklistedUrlEntity
import com.scanwise.app.data.local.ScanHistoryDao
import com.scanwise.app.data.local.ScanHistoryEntity
import com.scanwise.app.domain.analysis.UrlAnalyzer
import com.scanwise.app.domain.model.AnalysisResult
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.net.URI
import java.util.concurrent.TimeUnit

class ScanRepository(
    private val scanHistoryDao: ScanHistoryDao,
    private val blacklistedUrlDao: BlacklistedUrlDao,
) {
    val historyFlow: Flow<List<ScanHistoryEntity>> = scanHistoryDao.observeAll()
    val blacklistFlow: Flow<List<BlacklistedUrlEntity>> = blacklistedUrlDao.observeAll()

    suspend fun analyze(url: String): AnalysisResult {
        val blacklist = blacklistedUrlDao.allDomains().toSet()
        val domain = hostOf(url)
        val priorCount = scanHistoryDao.countByDomain(domain)
        val analyzer = UrlAnalyzer(
            blacklistedDomains = blacklist,
            previousScanCount = { priorCount }
        )
        val result = analyzer.analyze(url)
        scanHistoryDao.insert(
            ScanHistoryEntity(
                qrUrl = result.url,
                domainName = result.domain,
                riskScore = result.riskScore.toFloat(),
                riskLevel = result.riskLevel.name,
                analysisDetailsJson = toJson(result),
                scanTimestamp = System.currentTimeMillis(),
                threatDetected = result.riskLevel.name != "SAFE",
            )
        )
        return result
    }

    suspend fun blockDomain(domain: String, url: String) {
        blacklistedUrlDao.insert(
            BlacklistedUrlEntity(
                url = url,
                domain = domain,
                threatType = "user_blocked",
                source = "user",
            )
        )
    }

    suspend fun deleteScans(ids: List<Long>) = scanHistoryDao.deleteByIds(ids)

    suspend fun purgeOlderThanDays(days: Int) {
        val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days.toLong())
        scanHistoryDao.deleteOlderThan(cutoff)
    }

    suspend fun stats(): ScanStats {
        val total = scanHistoryDao.totalCount()
        val safe = scanHistoryDao.countByRiskLevel("SAFE")
        val medium = scanHistoryDao.countByRiskLevel("MEDIUM")
        val dangerous = scanHistoryDao.countByRiskLevel("DANGEROUS")
        return ScanStats(total, safe, medium, dangerous)
    }

    private fun hostOf(url: String): String {
        val uri = runCatching { URI(if (url.contains("://")) url else "http://$url") }.getOrNull()
        return uri?.host ?: url.substringAfter("://", url).substringBefore("/")
    }

    private fun toJson(result: AnalysisResult): String = JSONObject().apply {
        put("urlScore", result.urlScore)
        put("blacklistScore", result.blacklistScore)
        put("contentScore", result.contentScore)
        put("sslScore", result.sslScore)
        put("behavioralScore", result.behavioralScore)
        put("summary", result.summary)
        put("recommendation", result.recommendation)
    }.toString()
}

data class ScanStats(
    val total: Int,
    val safe: Int,
    val medium: Int,
    val dangerous: Int,
) {
    val safePercent: Int get() = if (total == 0) 0 else (safe * 100 / total)
    val mediumPercent: Int get() = if (total == 0) 0 else (medium * 100 / total)
    val dangerousPercent: Int get() = if (total == 0) 0 else (dangerous * 100 / total)
}

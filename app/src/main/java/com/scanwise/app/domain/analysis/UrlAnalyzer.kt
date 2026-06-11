package com.scanwise.app.domain.analysis

import com.scanwise.app.domain.model.AnalysisResult
import com.scanwise.app.domain.model.Finding
import com.scanwise.app.domain.model.RiskLevel
import java.net.URI
import kotlin.math.roundToInt

/**
 * On-device heuristic risk-scoring engine. Combines structural URL analysis,
 * a local blacklist/pattern lookup, lightweight content-keyword heuristics,
 * SSL/scheme checks and behavioral history into a single 0-100 risk score
 * using the weighting specified by the product spec:
 *   final = url*0.30 + blacklist*0.30 + content*0.20 + ssl*0.10 + behavioral*0.10
 */
class UrlAnalyzer(
    private val blacklistedDomains: Set<String> = emptySet(),
    private val maliciousKeywords: List<String> = DEFAULT_PHISHING_KEYWORDS,
    private val previousScanCount: (String) -> Int = { 0 },
) {

    fun analyze(rawUrl: String): AnalysisResult {
        val url = rawUrl.trim()
        val uri = runCatching { URI(if (url.contains("://")) url else "http://$url") }.getOrNull()
        val host = uri?.host ?: extractHostFallback(url)
        val domain = host ?: url

        val (urlScore, urlFindings) = scoreUrlStructure(url, uri, domain)
        val (blacklistScore, blacklistFindings) = scoreBlacklist(domain, url)
        val (contentScore, contentFindings) = scoreContentHeuristics(url)
        val (sslScore, securityFindings) = scoreSsl(uri)
        val behavioralScore = scoreBehavioral(domain)

        val finalScore = (urlScore * 0.30 +
            blacklistScore * 0.30 +
            contentScore * 0.20 +
            sslScore * 0.10 +
            behavioralScore * 0.10).roundToInt().coerceIn(0, 100)

        val level = RiskLevel.fromScore(finalScore)

        val (summary, recommendation) = summaryFor(level, domain)

        return AnalysisResult(
            url = url,
            domain = domain,
            riskScore = finalScore,
            riskLevel = level,
            urlScore = urlScore,
            blacklistScore = blacklistScore,
            contentScore = contentScore,
            sslScore = sslScore,
            behavioralScore = behavioralScore,
            urlFindings = urlFindings,
            securityFindings = securityFindings,
            blacklistFindings = blacklistFindings,
            contentFindings = contentFindings,
            summary = summary,
            recommendation = recommendation,
        )
    }

    private fun scoreUrlStructure(url: String, uri: URI?, domain: String): Pair<Int, List<Finding>> {
        var risk = 0
        val findings = mutableListOf<Finding>()

        val isIpHost = domain.matches(Regex("^\\d{1,3}(\\.\\d{1,3}){3}$"))
        if (isIpHost) {
            risk += 35
            findings += Finding("IP address as host", "URL uses a raw IP address instead of a domain name", false)
        } else {
            findings += Finding("Domain format", "URL uses a standard domain name", true)
        }

        val subdomainCount = domain.count { it == '.' }
        if (subdomainCount >= 3) {
            risk += 15
            findings += Finding("Excessive subdomains", "Domain has $subdomainCount levels, often used to obscure the real domain", false)
        }

        val suspiciousChars = Regex("[@%]").containsMatchIn(url) || url.count { it == '-' } > 3
        if (suspiciousChars) {
            risk += 15
            findings += Finding("Suspicious characters", "URL contains characters often used to mask the real destination", false)
        } else {
            findings += Finding("Character check", "No suspicious characters detected in the URL", true)
        }

        val port = uri?.port ?: -1
        if (port != -1 && port !in listOf(80, 443)) {
            risk += 10
            findings += Finding("Unusual port", "URL points to non-standard port $port", false)
        }

        val tld = domain.substringAfterLast('.', "")
        val suspiciousTlds = setOf("zip", "review", "country", "kim", "cricket", "science", "work", "party", "gq", "tk", "ml")
        if (tld.lowercase() in suspiciousTlds) {
            risk += 15
            findings += Finding("TLD reputation", ".$tld is a top-level domain frequently abused for scams", false)
        } else if (tld.isNotEmpty()) {
            findings += Finding("TLD reputation", ".$tld is a commonly trusted top-level domain", true)
        }

        val knownBrands = listOf("paypal", "google", "apple", "amazon", "microsoft", "bank", "netflix")
        val brandLookalike = knownBrands.any { brand ->
            domain.contains(brand, ignoreCase = true) && !domain.endsWith("$brand.com", ignoreCase = true)
        }
        if (brandLookalike) {
            risk += 25
            findings += Finding("Brand impersonation", "Domain references a known brand but doesn't match its official domain", false)
        }

        return risk.coerceIn(0, 100) to findings
    }

    private fun scoreBlacklist(domain: String, url: String): Pair<Int, List<Finding>> {
        val findings = mutableListOf<Finding>()
        val matched = blacklistedDomains.any { domain.equals(it, true) || domain.endsWith(".$it", true) || url.contains(it, true) }
        return if (matched) {
            findings += Finding("Blacklist match", "Domain matches an entry in the local threat database", false)
            100 to findings
        } else {
            findings += Finding("Blacklist check", "No match found in the local threat database", true)
            0 to findings
        }
    }

    private fun scoreContentHeuristics(url: String): Pair<Int, List<Finding>> {
        val findings = mutableListOf<Finding>()
        val lower = url.lowercase()
        val hits = maliciousKeywords.filter { lower.contains(it) }
        return if (hits.isNotEmpty()) {
            findings += Finding("Phishing keywords", "Found suspicious terms in URL: ${hits.joinToString(", ")}", false)
            (hits.size * 25).coerceAtMost(100) to findings
        } else {
            findings += Finding("Keyword scan", "No known phishing keywords detected in the URL", true)
            0 to findings
        }
    }

    private fun scoreSsl(uri: URI?): Pair<Int, List<Finding>> {
        val findings = mutableListOf<Finding>()
        val scheme = uri?.scheme?.lowercase()
        return if (scheme == "https") {
            findings += Finding("HTTPS", "Connection is encrypted via HTTPS", true)
            0 to findings
        } else {
            findings += Finding("HTTPS", "URL does not use a secure HTTPS connection", false)
            70 to findings
        }
    }

    private fun scoreBehavioral(domain: String): Int {
        val seenBefore = previousScanCount(domain)
        return if (seenBefore == 0) 30 else 0
    }

    private fun summaryFor(level: RiskLevel, domain: String): Pair<String, String> = when (level) {
        RiskLevel.SAFE -> "This QR code points to $domain and shows no significant signs of risk." to
            "It looks safe to proceed, but always stay alert when entering personal information."
        RiskLevel.MEDIUM -> "This QR code points to $domain and shows some characteristics worth a closer look." to
            "Proceed with caution. Avoid entering sensitive information until you verify the source."
        RiskLevel.DANGEROUS -> "This QR code points to $domain and matches strong indicators of a scam or phishing attempt." to
            "Do not open this link. Consider blocking the domain and reporting it as phishing."
    }

    private fun extractHostFallback(url: String): String? {
        val withoutScheme = url.substringAfter("://", url)
        return withoutScheme.substringBefore("/").substringBefore("?").takeIf { it.isNotBlank() }
    }

    companion object {
        val DEFAULT_PHISHING_KEYWORDS = listOf(
            "verify-account", "verify_account", "confirm-identity", "urgent-action",
            "limited-time", "claim-reward", "update-payment", "secure-login",
            "account-locked", "reset-password", "signin-verify"
        )
    }
}

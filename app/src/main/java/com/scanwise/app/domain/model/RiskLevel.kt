package com.scanwise.app.domain.model

import androidx.compose.ui.graphics.Color
import com.scanwise.app.ui.theme.ScanWiseGreen
import com.scanwise.app.ui.theme.ScanWiseOrange
import com.scanwise.app.ui.theme.ScanWiseRed

enum class RiskLevel(val label: String, val color: Color, val range: IntRange) {
    SAFE("SAFE", ScanWiseGreen, 0..30),
    MEDIUM("MEDIUM RISK", ScanWiseOrange, 31..70),
    DANGEROUS("DANGEROUS", ScanWiseRed, 71..100);

    companion object {
        fun fromScore(score: Int): RiskLevel = when {
            score <= 30 -> SAFE
            score <= 70 -> MEDIUM
            else -> DANGEROUS
        }
    }
}

package com.scanwise.app.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scanwise.app.data.local.ScanHistoryEntity
import com.scanwise.app.data.repository.ScanStats
import com.scanwise.app.domain.model.RiskLevel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    history: List<ScanHistoryEntity>,
    stats: ScanStats,
    onDelete: (Long) -> Unit,
) {
    if (history.isEmpty()) {
        EmptyHistory()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { StatsCard(stats) }
        items(history, key = { it.id }) { entry ->
            HistoryRow(entry, onDelete = { onDelete(entry.id) })
        }
    }
}

@Composable
private fun EmptyHistory() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("No scans yet", style = MaterialTheme.typography.titleMedium)
        Text(
            "Scan a QR code to see its security analysis appear here.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun StatsCard(stats: ScanStats) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Statistics", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StatItem("Total", stats.total.toString(), MaterialTheme.colorScheme.primary)
                StatItem("Safe", "${stats.safePercent}%", Color(0xFF4CAF50))
                StatItem("Medium", "${stats.mediumPercent}%", Color(0xFFFF9800))
                StatItem("Danger", "${stats.dangerousPercent}%", Color(0xFFFF5252))
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun HistoryRow(entry: ScanHistoryEntity, onDelete: () -> Unit) {
    val level = runCatching { RiskLevel.valueOf(entry.riskLevel) }.getOrDefault(RiskLevel.MEDIUM)
    val dateText = remember(entry.scanTimestamp) {
        SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(entry.scanTimestamp))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.domainName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(dateText, style = MaterialTheme.typography.labelSmall)
            }
            Surface(color = level.color.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
                Text(
                    "${level.label} · ${entry.riskScore.toInt()}",
                    color = level.color,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                )
            }
        }
    }
}

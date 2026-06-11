package com.scanwise.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var dangerAlerts by remember { mutableStateOf(true) }
    var contentAnalysis by remember { mutableStateOf(true) }
    var autoFetchContent by remember { mutableStateOf(false) }
    var encryptData by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            SettingsSection("Notifications") {
                SettingsToggle("Danger alerts", "Full-screen warning for high-risk QR codes", dangerAlerts) { dangerAlerts = it }
            }
        }
        item {
            SettingsSection("Analysis") {
                SettingsToggle("Content analysis", "Scan page content for phishing keywords", contentAnalysis) { contentAnalysis = it }
                SettingsToggle("Auto-fetch webpage content", "Fetch pages automatically when scanning", autoFetchContent) { autoFetchContent = it }
            }
        }
        item {
            SettingsSection("Privacy & Security") {
                SettingsToggle("Encrypt scan data", "Store scan history with encryption at rest", encryptData) { encryptData = it }
            }
        }
        item {
            SettingsSection("About") {
                Text("ScanWise v1.0", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "QR code scam detection powered by on-device heuristic analysis.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(modifier = Modifier.padding(top = 8.dp)) { content() }
        }
    }
}

@Composable
private fun SettingsToggle(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(description, style = MaterialTheme.typography.labelSmall)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

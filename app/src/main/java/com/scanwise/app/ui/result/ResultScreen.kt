package com.scanwise.app.ui.result

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scanwise.app.domain.model.AnalysisResult
import com.scanwise.app.domain.model.Finding
import com.scanwise.app.domain.model.RiskLevel
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    result: AnalysisResult,
    onOpenUrl: (String) -> Unit,
    onBlockDomain: (String) -> Unit,
    onReport: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { RiskScoreCircle(result) }
        item { SummaryCard(result, onOpenUrl, onBlockDomain, onReport) }
        item { FindingsCard("URL Analysis", result.urlFindings) }
        item { FindingsCard("Security Check", result.securityFindings) }
        item { FindingsCard("Blacklist Status", result.blacklistFindings) }
        item { FindingsCard("Content Analysis", result.contentFindings) }
        item { ScoreBreakdownCard(result) }
    }
}

@Composable
private fun RiskScoreCircle(result: AnalysisResult) {
    var animatedTarget by remember { mutableIntStateOf(0) }
    LaunchedEffect(result.riskScore) {
        animatedTarget = 0
        delay(80)
        animatedTarget = result.riskScore
    }
    val animatedScore by animateFloatAsState(
        targetValue = animatedTarget.toFloat(),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "score",
    )
    val color = result.riskLevel.color

    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulseScale",
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val stroke = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                drawArc(
                    color = color.copy(alpha = 0.2f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = stroke,
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * (animatedScore / 100f),
                    useCenter = false,
                    style = stroke,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = animatedScore.toInt().toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
                Text(text = "/ 100", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = result.riskLevel.label,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            modifier = Modifier
                .padding(top = 12.dp)
                .scale(if (result.riskLevel == RiskLevel.DANGEROUS) pulseScale else 1f),
        )
    }
}

@Composable
private fun SummaryCard(
    result: AnalysisResult,
    onOpenUrl: (String) -> Unit,
    onBlockDomain: (String) -> Unit,
    onReport: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(result.summary, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                result.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
            Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onOpenUrl(result.url) },
                    enabled = result.riskLevel != RiskLevel.DANGEROUS,
                ) {
                    Icon(Icons.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Open Safely", modifier = Modifier.padding(start = 4.dp))
                }
            }
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onBlockDomain(result.domain) }) {
                    Icon(Icons.Filled.Block, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Block Domain", modifier = Modifier.padding(start = 4.dp))
                }
                OutlinedButton(onClick = { onReport(result.url) }) {
                    Icon(Icons.Filled.Flag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Report", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun FindingsCard(title: String, findings: List<Finding>) {
    var expanded by remember { mutableStateOf(true) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            if (expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    findings.forEach { finding ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            val (icon, tint) = when (finding.isPositive) {
                                true -> Icons.Filled.Check to Color(0xFF4CAF50)
                                false -> Icons.Filled.Close to Color(0xFFFF5252)
                                null -> Icons.Filled.HelpOutline to Color(0xFFFF9800)
                            }
                            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.padding(top = 2.dp))
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(finding.label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                Text(finding.detail, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBreakdownCard(result: AnalysisResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Score Breakdown", style = MaterialTheme.typography.titleMedium)
            ScoreRow("URL structure (30%)", result.urlScore)
            ScoreRow("Blacklist match (30%)", result.blacklistScore)
            ScoreRow("Content analysis (20%)", result.contentScore)
            ScoreRow("SSL / HTTPS (10%)", result.sslScore)
            ScoreRow("Behavioral history (10%)", result.behavioralScore)
        }
    }
}

@Composable
private fun ScoreRow(label: String, score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text("$score / 100", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

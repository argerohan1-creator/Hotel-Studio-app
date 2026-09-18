package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun FssaiComplianceTrendsChart() {
    // Simulated FSSAI violation trends over the last 30 days
    // Showing a trend of decreasing violations as compliance improves
    val chartEntryModel = remember {
        entryModelOf(
            12f, 14f, 10f, 15f, 11f, 8f, 9f, 6f, 
            7f, 5f, 8f, 4f, 6f, 3f, 5f, 2f, 
            3f, 2f, 4f, 1f, 2f, 1f, 0f, 1f, 
            0f, 0f, 1f, 0f, 0f, 0f
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "FSSAI COMPLIANCE AUDITING TRENDS",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Food safety violations detected over the last 30 days.",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    title = "Violations",
                    titleComponent = null, 
                    tickLength = 0.dp,
                    valueFormatter = { value, _ -> value.toInt().toString() }
                ),
                bottomAxis = rememberBottomAxis(
                    title = "Days (Last 30)",
                    titleComponent = null,
                    tickLength = 0.dp,
                    valueFormatter = { value, _ -> "${value.toInt() + 1}" }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Trend Analysis: 92% reduction in recurring FSSAI temperature and storage violations in the past month.",
                fontSize = 10.sp,
                color = Color(0xFF15803D), // Success Green
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

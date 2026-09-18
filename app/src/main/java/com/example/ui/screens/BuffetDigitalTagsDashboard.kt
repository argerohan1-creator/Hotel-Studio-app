package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuffetCounterGroup
import com.example.data.model.ConnectedDigitalTag
import com.example.data.remote.DigitalTagSize
import com.example.viewmodel.HotelStudioViewModel

/**
 * Executive Real-Time Dashboard View for Cloud-Synced Digital Buffet Tags.
 * Displays real-time telemetry, battery health metrics, wireless signal quality,
 * and last-updated timestamps grouped by buffet counter station.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuffetDigitalTagsDashboard(
    viewModel: HotelStudioViewModel,
    onBackToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val connectedTags by viewModel.connectedDigitalTags.collectAsState()
    val cloudConfig by viewModel.cloudConfig.collectAsState()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsState()
    val activeFilter by viewModel.tagDashboardFilter.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTagForBatteryDialog by remember { mutableStateOf<ConnectedDigitalTag?>(null) }

    // Map connected tags into canonical buffet counter stations
    val counterGroups = remember(connectedTags) {
        val baseCounters = listOf(
            BuffetCounterGroup("CTR-01", "Counter 1: Hot Chafing & Carvery Mains", "Zone A - Central Island", "Hot Mains", "🍲", emptyList()),
            BuffetCounterGroup("CTR-02", "Counter 2: Live Tandoor & Grill Station", "Zone B - Open Kitchen Promenade", "Live Grill", "🔥", emptyList()),
            BuffetCounterGroup("CTR-03", "Counter 3: Soup Kettle & Artisanal Bread Bar", "Zone C - Soup & Bread Island", "Soups & Bakery", "🥣", emptyList()),
            BuffetCounterGroup("CTR-04", "Counter 4: Chilled Salad Bar & Deli Charcuterie", "Zone D - Cold Island", "Salad & Cold Cuts", "🥗", emptyList()),
            BuffetCounterGroup("CTR-05", "Counter 5: Live Wok & Asian Noodle Counter", "Zone E - Live Action Station", "Asian Wok", "🥢", emptyList()),
            BuffetCounterGroup("CTR-06", "Counter 6: Dessert & Patisserie Station", "Zone F - Sweet Galleria", "Desserts", "🍰", emptyList()),
            BuffetCounterGroup("CTR-07", "Counter 7: Beverage & Mocktail Dispensary", "Zone G - Beverage Lounge", "Beverages", "🍹", emptyList())
        )
        baseCounters.map { group ->
            group.copy(tags = connectedTags.filter { it.counterCode == group.counterCode })
        }
    }

    // High-level fleet metrics
    val totalTagsCount = connectedTags.size
    val onlineTagsCount = connectedTags.count { it.syncStatus != "OFFLINE" }
    val avgFleetBattery = if (connectedTags.isNotEmpty()) connectedTags.map { it.batteryPct }.average().toInt() else 100
    val lowBatteryCount = connectedTags.count { it.batteryPct < 25 }
    val soldOutCount = connectedTags.count { it.isSoldOut }
    val lastGlobalSyncTimestamp = connectedTags.maxOfOrNull { it.lastUpdatedTimestamp } ?: System.currentTimeMillis()

    // Filtered list of counters to show
    val filteredCounters = remember(counterGroups, activeFilter, searchQuery) {
        val base = when (activeFilter) {
            "ALL" -> counterGroups
            "LOW_BATTERY" -> counterGroups.map { g -> g.copy(tags = g.tags.filter { it.batteryPct < 25 }) }.filter { it.tags.isNotEmpty() }
            "SOLD_OUT" -> counterGroups.map { g -> g.copy(tags = g.tags.filter { it.isSoldOut }) }.filter { it.tags.isNotEmpty() }
            else -> counterGroups.filter { it.counterCode == activeFilter }
        }

        if (searchQuery.isBlank()) {
            base
        } else {
            val q = searchQuery.trim().lowercase()
            base.map { group ->
                group.copy(tags = group.tags.filter {
                    it.dishName.lowercase().contains(q) ||
                    it.tagId.lowercase().contains(q) ||
                    it.macAddress.lowercase().contains(q) ||
                    group.counterName.lowercase().contains(q)
                })
            }.filter { it.tags.isNotEmpty() }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // =========================================================================
        // TOP NAVIGATION & EXECUTIVE HEADER
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        IconButton(
                            onClick = onBackToStudio,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Tag Studio",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Digital Tag Fleet Dashboard",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF2E7D32).copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, Color(0xFF2E7D32))
                                ) {
                                    Text(
                                        text = "LIVE TELEMETRY",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Real-time battery levels, last-updated sync times & counter fleet status",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onBackToStudio,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tag Studio", style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(12.dp))

                // Cloud Gateway Telemetry Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PulsingIndicatorDot(color = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cloud ESL Gateway: ${cloudConfig.endpointUrl.substringAfter("://").substringBefore("/")}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "2.4GHz IEEE 802.15.4 Mesh",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // =========================================================================
        // FLEET SUMMARY METRICS (4 EXECUTIVE KPIS)
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Metric 1: Connected Counters
            MetricKpiCard(
                modifier = Modifier.weight(1f),
                title = "ACTIVE COUNTERS",
                value = "${counterGroups.size}",
                subtitle = "All 7 Stations Online",
                icon = Icons.Default.Sensors,
                tint = MaterialTheme.colorScheme.primary
            )

            // Metric 2: Digital Tags Fleet
            MetricKpiCard(
                modifier = Modifier.weight(1f),
                title = "CONNECTED TAGS",
                value = "$onlineTagsCount / $totalTagsCount",
                subtitle = "100% Synced ESLs",
                icon = Icons.Default.CloudDone,
                tint = Color(0xFF2E7D32)
            )

            // Metric 3: Fleet Battery Health
            MetricKpiCard(
                modifier = Modifier.weight(1f),
                title = "FLEET BATTERY",
                value = "$avgFleetBattery%",
                subtitle = if (lowBatteryCount > 0) "$lowBatteryCount Low Alert (<25%)" else "Optimal Health",
                icon = if (lowBatteryCount > 0) Icons.Default.BatteryAlert else Icons.Default.BatteryFull,
                tint = if (lowBatteryCount > 0) Color(0xFFD32F2F) else Color(0xFF00897B)
            )

            // Metric 4: Refill / Sold Out Tags
            MetricKpiCard(
                modifier = Modifier.weight(1f),
                title = "SOLD OUT / REFILL",
                value = "$soldOutCount",
                subtitle = if (soldOutCount > 0) "Needs Chef Refill" else "All In Stock",
                icon = Icons.Default.Warning,
                tint = if (soldOutCount > 0) Color(0xFFF57C00) else MaterialTheme.colorScheme.outline
            )
        }

        // =========================================================================
        // FLEET ACTIONS TOOLBAR & GLOBAL BROADCAST
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Fleet Wireless Orchestration",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Last fleet-wide sync: ${com.example.data.model.formatRelativeTimestamp(lastGlobalSyncTimestamp)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { viewModel.broadcastRefreshAllCounters() },
                        enabled = !isCloudSyncing,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isCloudSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Broadcasting...", style = MaterialTheme.typography.labelMedium)
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sync All Counters", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by dish, tag ID (e.g. ESL-BF01), or counter...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Counter Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        label = "All Counters (${connectedTags.size})",
                        selected = activeFilter == "ALL",
                        onClick = { viewModel.setTagDashboardFilter("ALL") }
                    )
                    FilterChip(
                        label = "⚠️ Low Battery (${connectedTags.count { it.batteryPct < 25 }})",
                        selected = activeFilter == "LOW_BATTERY",
                        onClick = { viewModel.setTagDashboardFilter("LOW_BATTERY") },
                        isAlert = lowBatteryCount > 0
                    )
                    FilterChip(
                        label = "⛔ Sold Out (${soldOutCount})",
                        selected = activeFilter == "SOLD_OUT",
                        onClick = { viewModel.setTagDashboardFilter("SOLD_OUT") }
                    )
                    counterGroups.forEach { group ->
                        FilterChip(
                            label = "${group.iconEmoji} ${group.counterCode}",
                            selected = activeFilter == group.counterCode,
                            onClick = { viewModel.setTagDashboardFilter(group.counterCode) }
                        )
                    }
                }
            }
        }

        // =========================================================================
        // COUNTER-BY-COUNTER FLEET LIST
        // =========================================================================
        if (filteredCounters.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No digital tags match the current filter or search criteria.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = {
                        viewModel.setTagDashboardFilter("ALL")
                        searchQuery = ""
                    }) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            filteredCounters.forEach { counterGroup ->
                BuffetCounterStationCard(
                    counterGroup = counterGroup,
                    viewModel = viewModel,
                    onInspectBattery = { selectedTagForBatteryDialog = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // =========================================================================
    // BATTERY HEALTH & CELL SPECIFICATIONS DIALOG
    // =========================================================================
    selectedTagForBatteryDialog?.let { tag ->
        BatteryDetailDialog(
            tag = tag,
            onDismiss = { selectedTagForBatteryDialog = null },
            onSimulateReplacement = { newPct ->
                viewModel.updateTagBattery(tag.tagId, newPct)
                selectedTagForBatteryDialog = null
            }
        )
    }
}

/**
 * Visual container for an individual buffet counter station with its connected digital tags.
 */
@Composable
fun BuffetCounterStationCard(
    counterGroup: BuffetCounterGroup,
    viewModel: HotelStudioViewModel,
    onInspectBattery: (ConnectedDigitalTag) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Counter Station Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = counterGroup.iconEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = counterGroup.counterName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = counterGroup.counterZone,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${counterGroup.totalTags} Digital Tags Assigned",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Station Actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { viewModel.syncCounterTags(counterGroup.counterCode) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync Counter",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync Counter", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Counter Telemetry Bar (Avg Battery, Low Battery warnings, Last Updated)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (counterGroup.hasLowBattery) Icons.Default.BatteryAlert else Icons.Default.BatteryChargingFull,
                            contentDescription = null,
                            tint = if (counterGroup.hasLowBattery) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Counter Avg Battery: ${counterGroup.avgBattery}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (counterGroup.hasLowBattery) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
                        )
                        if (counterGroup.hasLowBattery) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFD32F2F).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "LOW BATTERY ALERT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Updated: ${counterGroup.formattedLastUpdated}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // List of Connected Digital Tags for this Counter
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                counterGroup.tags.forEach { tag ->
                    ConnectedDigitalTagCard(
                        tag = tag,
                        onFlashLed = { viewModel.remoteFlashFleetTagLed(tag.tagId) },
                        onForceRefresh = { viewModel.forceRefreshFleetTag(tag.tagId) },
                        onToggleSoldOut = { viewModel.toggleFleetTagSoldOut(tag.tagId) },
                        onInspectInPreview = { viewModel.loadDishIntoPreviewFromTag(tag) },
                        onInspectBattery = { onInspectBattery(tag) }
                    )
                }
            }
        }
    }
}

/**
 * Individual Digital Tag Telemetry Card with Battery Level & Last-Updated Timestamps.
 */
@Composable
fun ConnectedDigitalTagCard(
    tag: ConnectedDigitalTag,
    onFlashLed: () -> Unit,
    onForceRefresh: () -> Unit,
    onToggleSoldOut: () -> Unit,
    onInspectInPreview: () -> Unit,
    onInspectBattery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFlashing = tag.isFlashingLed
    val infiniteTransition = rememberInfiniteTransition(label = "beacon")
    val beaconAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beaconAlpha"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tag.isSoldOut) Color(0xFFFFF1F1) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isFlashing) 2.dp else 1.dp,
            color = if (isFlashing) Color(0xFF00E5FF) else if (tag.batteryPct < 25) Color(0xFFD32F2F) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Hardware Header Row (Tag ID, MAC, Screen Size, Signal, Beacon)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = tag.tagId,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MAC: ${tag.macAddress}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isFlashing) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF00E5FF).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .alpha(beaconAlpha),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00E5FF))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "FLASHING LED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF00838F)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Display Mode pill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (tag.screenSize == DigitalTagSize.DELUXE_7_5) "7.5\" Deluxe" else "4.2\" E-Paper",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    // Signal RSSI
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Signal Strength",
                            tint = if (tag.signalDbm > -60) Color(0xFF2E7D32) else Color(0xFFF57C00),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${tag.signalDbm} dBm",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Content: Dish Name & Nutritional Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Veg / Non-Veg Indicator Square
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .border(
                            1.5.dp,
                            if (tag.isVeg) Color(0xFF2E7D32) else Color(0xFFC62828),
                            RoundedCornerShape(3.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (tag.isVeg) Color(0xFF2E7D32) else Color(0xFFC62828))
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tag.dishName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (tag.isSoldOut) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFD32F2F)
                            ) {
                                Text(
                                    text = "SOLD OUT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (tag.dishDesc.isNotBlank()) {
                        Text(
                            text = tag.dishDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (tag.calories.isNotBlank() || tag.allergens.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (tag.calories.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = tag.calories,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            if (tag.allergens.isNotEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFFF3E0),
                                    border = BorderStroke(0.5.dp, Color(0xFFFFB74D))
                                ) {
                                    Text(
                                        text = "Allergens: ${tag.allergens.joinToString(", ")}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFE65100),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =========================================================================
            // BATTERY STATUS & LAST-UPDATED TIMESTAMP SECTION (CRITICAL USER REQUEST)
            // =========================================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onInspectBattery() },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column: Visual Battery Gauge & Level
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BatteryVisualMeter(
                            batteryPct = tag.batteryPct,
                            modifier = Modifier.size(width = 38.dp, height = 20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${tag.batteryPct}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = getBatteryColor(tag.batteryPct)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${tag.batteryHealth})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = getBatteryColor(tag.batteryPct),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = tag.estimatedRemainingLife,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tag.batteryPct < 25) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Right Column: Last-Updated Timestamp & Status Pill
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E7D32))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "CLOUD SYNCED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Last Updated",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tag.formattedLastUpdated,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hardware Action Buttons (Flash LED, Force Refresh, Sold Out, Preview)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onFlashLed,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isFlashing) Color(0xFF00E5FF) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Flash LED",
                        modifier = Modifier.size(14.dp),
                        tint = if (isFlashing) Color(0xFF00ACC1) else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isFlashing) "Flashing..." else "Locate",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                OutlinedButton(
                    onClick = onForceRefresh,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Refresh", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = onToggleSoldOut,
                    modifier = Modifier
                        .weight(1.1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (tag.isSoldOut) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (tag.isSoldOut) Color(0xFF2E7D32) else Color(0xFFD32F2F).copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = if (tag.isSoldOut) "Clear Sold Out" else "Mark Sold Out",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onInspectInPreview,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Inspect in Preview",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Custom High-Fidelity Battery Meter Component.
 * Draws an authentic lithium-cell battery outline with proportional fill.
 */
@Composable
fun BatteryVisualMeter(
    batteryPct: Int,
    modifier: Modifier = Modifier
) {
    val color = getBatteryColor(batteryPct)
    val clampedPct = batteryPct.coerceIn(0, 100)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Battery Body
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .border(1.5.dp, color, RoundedCornerShape(4.dp))
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (clampedPct / 100f))
                        .fillMaxSize()
                        .background(color)
                )
            }
        }

        // Battery Positive Terminal Nub
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(8.dp)
                .background(color, RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp))
        )
    }
}

fun getBatteryColor(batteryPct: Int): Color {
    return when {
        batteryPct >= 80 -> Color(0xFF2E7D32) // Emerald Green
        batteryPct >= 50 -> Color(0xFF00897B) // Teal Green
        batteryPct >= 25 -> Color(0xFFF57C00) // Amber
        else -> Color(0xFFD32F2F)             // Crimson Red Alert
    }
}

/**
 * Pulsing Green / Cyan Connection Indicator Dot.
 */
@Composable
fun PulsingIndicatorDot(
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}

/**
 * Filter Chip with Active Pill State.
 */
@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    isAlert: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = when {
            selected && isAlert -> Color(0xFFD32F2F)
            selected -> MaterialTheme.colorScheme.primary
            isAlert -> Color(0xFFD32F2F).copy(alpha = 0.15f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        border = BorderStroke(
            1.dp,
            if (isAlert) Color(0xFFD32F2F) else if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = when {
                selected -> Color.White
                isAlert -> Color(0xFFD32F2F)
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

/**
 * Metric KPI Box for top executive strip.
 */
@Composable
fun MetricKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = tint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Detailed Battery Diagnostics & Cell Simulation Modal Dialog.
 */
@Composable
fun BatteryDetailDialog(
    tag: ConnectedDigitalTag,
    onDismiss: () -> Unit,
    onSimulateReplacement: (Int) -> Unit
) {
    var testLevel by remember { mutableStateOf(tag.batteryPct.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = null,
                    tint = getBatteryColor(tag.batteryPct)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tag Battery Telemetry")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Hardware ID: ${tag.tagId} (${tag.macAddress})",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Assigned Dish: ${tag.dishName} • ${tag.counterName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider()

                // Key battery specs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cell Type:", style = MaterialTheme.typography.bodySmall)
                    Text("2x CR2450 Lithium 3V Coin Cells", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Current Voltage:", style = MaterialTheme.typography.bodySmall)
                    Text(tag.batteryVoltage, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Battery Percentage:", style = MaterialTheme.typography.bodySmall)
                    Text("${tag.batteryPct}% (${tag.batteryHealth})", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = getBatteryColor(tag.batteryPct))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Estimated Lifespan:", style = MaterialTheme.typography.bodySmall)
                    Text(tag.estimatedRemainingLife, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Last Cloud Sync:", style = MaterialTheme.typography.bodySmall)
                    Text(tag.formattedLastUpdated, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }

                HorizontalDivider()

                Text(
                    text = "Hardware Maintenance Simulator:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set Battery Level: ${testLevel.toInt()}%", style = MaterialTheme.typography.bodySmall)
                    Button(
                        onClick = { onSimulateReplacement(100) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("Replace Cell (100%)", style = MaterialTheme.typography.labelSmall)
                    }
                }
                Slider(
                    value = testLevel,
                    onValueChange = { testLevel = it },
                    valueRange = 5f..100f,
                    steps = 19,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSimulateReplacement(testLevel.toInt()) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DishEntity
import com.example.data.remote.CloudGatewayConfig
import com.example.data.remote.CloudSyncResult
import com.example.data.remote.DigitalTagSize
import com.example.data.remote.DigitalTagTheme
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FssaiRegulatoryMark
import com.example.ui.theme.MontserratSans
import com.example.ui.theme.PlayfairSerif
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Executive-Grade Digital Buffet Tag Preview & Cloud Systems Controller.
 * Renders an Electronic Shelf Label (ESL) hardware simulator with real-time cloud sync.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DigitalBuffetTagPreview(
    dish: DishEntity,
    stationName: String,
    tagId: String,
    screenSize: DigitalTagSize,
    theme: DigitalTagTheme,
    isSoldOut: Boolean,
    isCloudSyncing: Boolean,
    cloudConfig: CloudGatewayConfig,
    lastSyncResult: CloudSyncResult?,
    onPushToCloud: () -> Unit,
    onFlashLed: () -> Unit,
    onToggleSoldOut: () -> Unit,
    onForceRefresh: () -> Unit,
    onSizeChange: (DigitalTagSize) -> Unit,
    onThemeChange: (DigitalTagTheme) -> Unit,
    onStationChange: (String) -> Unit,
    onTagIdChange: (String) -> Unit,
    onUpdateCloudConfig: (CloudGatewayConfig) -> Unit,
    onTestCloudConnection: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfigDialog by remember { mutableStateOf(false) }
    var showLogsDialog by remember { mutableStateOf(false) }
    var showStationEditDialog by remember { mutableStateOf(false) }
    var isLedFlashingSimulation by remember { mutableStateOf(false) }

    // Color palette according to Digital Tag Theme
    val tagBgColor = when (theme) {
        DigitalTagTheme.EPAPER_TRICOLOR -> Color(0xFFF9F9F8)
        DigitalTagTheme.OLED_DARK -> Color(0xFF090D16)
        DigitalTagTheme.IVORY_LUXURY -> Color(0xFFFBF8F2)
    }

    val tagTextColor = when (theme) {
        DigitalTagTheme.EPAPER_TRICOLOR -> Color(0xFF111827)
        DigitalTagTheme.OLED_DARK -> Color(0xFFF8FAFC)
        DigitalTagTheme.IVORY_LUXURY -> Color(0xFF1E293B)
    }

    val tagSecondaryTextColor = when (theme) {
        DigitalTagTheme.EPAPER_TRICOLOR -> Color(0xFF4B5563)
        DigitalTagTheme.OLED_DARK -> Color(0xFF94A3B8)
        DigitalTagTheme.IVORY_LUXURY -> Color(0xFF64748B)
    }

    val tagAccentColor = when (theme) {
        DigitalTagTheme.EPAPER_TRICOLOR -> Color(0xFFDC2626) // E-paper red accent
        DigitalTagTheme.OLED_DARK -> Color(0xFFF59E0B) // Glowing Amber
        DigitalTagTheme.IVORY_LUXURY -> Color(0xFFB45309) // Heritage Bronze
    }

    val tagBorderColor = when (theme) {
        DigitalTagTheme.EPAPER_TRICOLOR -> Color(0xFFCBD5E1)
        DigitalTagTheme.OLED_DARK -> Color(0xFF1E293B)
        DigitalTagTheme.IVORY_LUXURY -> Color(0xFFE2D9CC)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ============================================================
        // 1. DIGITAL TAG HARDWARE CASING & SCREEN SIMULATOR
        // ============================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E2430)) // Outer industrial matte casing (Bezel)
                .border(2.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {
            Column {
                // Top Bezel: Screws, Brand, and Hardware Status LED
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left screw + Hardware Brand
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF64748B))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HOTEL STUDIO ESL-PRO",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF94A3B8),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF334155))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = screenSize.screenInches,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }

                    // Right Hardware Status: Wireless Signal, Battery, & Flashing Cloud LED
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Wi-Fi signal
                        Icon(
                            Icons.Default.Wifi,
                            contentDescription = "Wireless Signal",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "-42dBm",
                            fontSize = 7.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Battery
                        Icon(
                            Icons.Default.BatteryChargingFull,
                            contentDescription = "Battery 96%",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "96%",
                            fontSize = 7.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Hardware status LED (flashes if remote action triggered)
                        val infiniteTransition = rememberInfiniteTransition(label = "led_blink")
                        val ledAlpha by infiniteTransition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 600),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "led_alpha"
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCloudSyncing) Color(0xFF3B82F6).copy(alpha = ledAlpha)
                                    else if (isSoldOut) Color(0xFFEF4444).copy(alpha = ledAlpha)
                                    else Color(0xFF10B981)
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF64748B))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // ==========================================
                // THE ELECTRONIC DISPLAY CANVAS (Screen itself)
                // ==========================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(tagBgColor)
                        .border(1.5.dp, tagBorderColor, RoundedCornerShape(8.dp))
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Display Header: Station Name & Tag ID Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FssaiRegulatoryMark(
                                    isVeg = dish.type.lowercase() == "veg",
                                    size = 14.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = stationName.uppercase(),
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp,
                                        color = tagSecondaryTextColor
                                    )
                                    Text(
                                        text = "TAG: $tagId",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tagSecondaryTextColor.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Cloud Sync status badge on screen
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (theme == DigitalTagTheme.OLED_DARK) Color(0xFF1E293B)
                                        else Color(0xFFE2E8F0)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        if (isCloudSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = if (isCloudSyncing) Color(0xFF3B82F6) else Color(0xFF10B981),
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isCloudSyncing) "SYNCING..." else "CLOUD ACTIVE",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isCloudSyncing) Color(0xFF3B82F6) else Color(0xFF10B981)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Middle: Food Title & Details + QR Code side-by-side
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dish.name.uppercase(),
                                    fontFamily = if (theme == DigitalTagTheme.IVORY_LUXURY) PlayfairSerif else FontFamily.SansSerif,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = when (screenSize) {
                                        DigitalTagSize.COMPACT_4_2 -> 16.sp
                                        DigitalTagSize.DELUXE_7_5 -> 19.sp
                                        DigitalTagSize.STATION_10_1 -> 22.sp
                                    },
                                    lineHeight = 21.sp,
                                    color = tagTextColor,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                if (dish.desc.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = dish.desc,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 10.sp,
                                        lineHeight = 13.sp,
                                        color = tagSecondaryTextColor,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (dish.cals.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(tagAccentColor.copy(alpha = 0.12f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${dish.cals} • FSSAI Energy Declaration",
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tagAccentColor
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Right: High-Contrast Dynamic QR Code for Guest Smartphone
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(if (screenSize == DigitalTagSize.COMPACT_4_2) 58.dp else 68.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White)
                                        .border(1.5.dp, Color.Black, RoundedCornerShape(4.dp))
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.QrCode2,
                                        contentDescription = "Guest QR Code",
                                        tint = Color.Black,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "SCAN FOR MENU",
                                    fontSize = 6.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = tagSecondaryTextColor,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom: Dietary Badges & Statutory Allergen Icons
                        if (dish.allergens.isNotEmpty()) {
                            HorizontalDivider(
                                color = tagBorderColor.copy(alpha = 0.6f),
                                thickness = 0.8.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "ALLERGENS:",
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = tagAccentColor,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        dish.allergens.take(6).forEach { allergen ->
                                            AllergenIcon(allergen = allergen, size = 16.dp)
                                        }
                                    }
                                }

                                Text(
                                    text = "FSSAI Sch. IV",
                                    fontSize = 6.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tagSecondaryTextColor.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Sold Out Overlay Banner (Remote Cloud Action)
                        if (isSoldOut) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFDC2626))
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "⚠️ SOLD OUT — KITCHEN IS REFILLING",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ============================================================
        // 2. DIGITAL TAG CONTROLS (FORM FACTOR & THEME TOGGLE)
        // ============================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "DIGITAL TAG DISPLAY PROFILE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Form Factor / Size Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DigitalTagSize.values().forEach { size ->
                        FilterChip(
                            selected = screenSize == size,
                            onClick = { onSizeChange(size) },
                            label = { Text(size.label, fontSize = 10.5.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display Theme Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DigitalTagTheme.values().forEach { thm ->
                        FilterChip(
                            selected = theme == thm,
                            onClick = { onThemeChange(thm) },
                            label = { Text(thm.label, fontSize = 10.5.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Station and Tag Binding Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Assigned: $stationName • Tag ID: $tagId",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Hardware MAC: 7A:9B:4C:12:34:F1 • Frequency: 2.4GHz BLE",
                            fontSize = 8.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    TextButton(
                        onClick = { showStationEditDialog = true },
                        modifier = Modifier.testTag("btn_edit_tag_station")
                    ) {
                        Text("Change Station", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ============================================================
        // 3. CLOUD SYSTEMS INTEGRATION TOOLBAR & ACTIONS
        // ============================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Cloud Status Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Cloud,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CLOUD ESL SYSTEMS INTEGRATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "${cloudConfig.cloudProvider} (${if (cloudConfig.isConnected) "Online" else "Offline"})",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = { showConfigDialog = true },
                        modifier = Modifier.testTag("btn_cloud_config")
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Cloud Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Last sync result alert banner if available
                if (lastSyncResult != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (lastSyncResult.success) Color(0xFFDCFCE7)
                                else Color(0xFFFEE2E2)
                            )
                            .border(
                                1.dp,
                                if (lastSyncResult.success) Color(0xFF16A34A)
                                else Color(0xFFDC2626),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    if (lastSyncResult.success) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (lastSyncResult.success) Color(0xFF16A34A) else Color(0xFFDC2626),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = lastSyncResult.message,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (lastSyncResult.success) Color(0xFF166534) else Color(0xFF991B1B)
                                )
                            }
                            Text(
                                text = "${lastSyncResult.latencyMs}ms",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (lastSyncResult.success) Color(0xFF166534) else Color(0xFF991B1B)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Cloud Actions Row 1: Push to Tag & Remote Sold Out
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Push to Cloud Button
                    Button(
                        onClick = onPushToCloud,
                        enabled = !isCloudSyncing,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_push_tag_to_cloud")
                    ) {
                        if (isCloudSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pushing...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Push To Cloud ESL", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Toggle Sold Out
                    OutlinedButton(
                        onClick = onToggleSoldOut,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isSoldOut) Color(0xFF16A34A) else Color(0xFFDC2626)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSoldOut) Color(0xFF16A34A) else Color(0xFFDC2626)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_toggle_sold_out")
                    ) {
                        Icon(
                            if (isSoldOut) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSoldOut) "Mark In Stock" else "Mark Sold Out",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cloud Actions Row 2: Flash LED & Refresh Screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isLedFlashingSimulation = true
                            onFlashLed()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_flash_tag_led")
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Flash Tag LED", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onForceRefresh,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_refresh_epaper")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Force E-Paper Refresh", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Test Connection and View Logs Links
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onTestCloudConnection,
                        modifier = Modifier.testTag("btn_test_cloud_ping")
                    ) {
                        Icon(Icons.Default.CloudQueue, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ping Cloud Gateway", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = { showLogsDialog = true },
                        modifier = Modifier.testTag("btn_view_sync_logs")
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View Cloud Sync Logs", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // ============================================================
    // DIALOG 1: CLOUD GATEWAY CONFIGURATION MODAL
    // ============================================================
    if (showConfigDialog) {
        var endpointInput by remember { mutableStateOf(cloudConfig.endpointUrl) }
        var apiKeyInput by remember { mutableStateOf(cloudConfig.apiKey) }
        var hotelIdInput by remember { mutableStateOf(cloudConfig.hotelId) }
        var autoSyncInput by remember { mutableStateOf(cloudConfig.autoSyncEnabled) }

        AlertDialog(
            onDismissRequest = { showConfigDialog = false },
            title = {
                Text(
                    text = "Cloud ESL Gateway Settings",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Configure the enterprise cloud system endpoint for wireless digital tags (SES-imagotag, Pricer, SoluM, or Hotel Studio Cloud).",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = endpointInput,
                        onValueChange = { endpointInput = it },
                        label = { Text("Cloud Gateway Webhook URL", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        label = { Text("Cloud API Key / Auth Token", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hotelIdInput,
                        onValueChange = { hotelIdInput = it },
                        label = { Text("Hotel / Outlet Identification Code", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Auto-Sync on Dish Update", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Broadcast changes to digital tags immediately", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = autoSyncInput,
                            onCheckedChange = { autoSyncInput = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateCloudConfig(
                            cloudConfig.copy(
                                endpointUrl = endpointInput.trim(),
                                apiKey = apiKeyInput.trim(),
                                hotelId = hotelIdInput.trim(),
                                autoSyncEnabled = autoSyncInput
                            )
                        )
                        showConfigDialog = false
                    }
                ) {
                    Text("Save & Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfigDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ============================================================
    // DIALOG 2: STATION & HARDWARE TAG ID BINDING MODAL
    // ============================================================
    if (showStationEditDialog) {
        var stationInput by remember { mutableStateOf(stationName) }
        var tagIdInput by remember { mutableStateOf(tagId) }

        AlertDialog(
            onDismissRequest = { showStationEditDialog = false },
            title = {
                Text(
                    text = "Bind Digital Tag & Station",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Assign this recipe dish to a physical digital tag and counter location.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = stationInput,
                        onValueChange = { stationInput = it },
                        label = { Text("Buffet Station Name", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = tagIdInput,
                        onValueChange = { tagIdInput = it },
                        label = { Text("Physical Hardware Tag ID", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onStationChange(stationInput.trim())
                        onTagIdChange(tagIdInput.trim())
                        showStationEditDialog = false
                    }
                ) {
                    Text("Save Binding")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStationEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ============================================================
    // DIALOG 3: CLOUD SYNC LOGS MODAL
    // ============================================================
    if (showLogsDialog) {
        AlertDialog(
            onDismissRequest = { showLogsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cloud ESL Sync History",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (lastSyncResult == null) {
                        Text(
                            text = "No sync transactions recorded yet. Tap 'Push To Cloud ESL' or 'Ping Cloud Gateway' to initiate communication.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        // Display active transaction detail
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = lastSyncResult.transactionId,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${lastSyncResult.latencyMs} ms",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (lastSyncResult.success) Color(0xFF16A34A) else Color(0xFFDC2626)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lastSyncResult.message,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = lastSyncResult.details,
                                    fontSize = 8.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Timestamp: ${SimpleDateFormat("HH:mm:ss dd-MMM-yyyy", Locale.getDefault()).format(Date(lastSyncResult.timestamp))}",
                                    fontSize = 7.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLogsDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

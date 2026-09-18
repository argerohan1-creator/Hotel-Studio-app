package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalPrintshop
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BuffetMenuItemEntity
import com.example.ui.screens.BUFFET_COURSES
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FssaiRegulatoryMark
import com.example.util.BuffetMenuExporter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExportBuffetMenuDialog(
    day: String,
    session: String,
    paxCount: Int,
    establishmentName: String,
    items: List<BuffetMenuItemEntity>,
    initialPaperSize: String = "A4",
    initialPageLayout: String = "1 Page",
    customLogoBase64: String? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val fssaiLicense = "10021011000892" // Standard registered FBO License

    var selectedTheme by remember { mutableStateOf("ink_saver") } // "ink_saver", "luxury_gold", "slate_modern"
    var selectedPaperSize by remember { mutableStateOf(initialPaperSize) } // "A4", "A5", "A3"
    var selectedPageLayout by remember { mutableStateOf(initialPageLayout) } // "1 Page", "Multiple Pages"
    var showCalories by remember { mutableStateOf(true) }
    var showAllergens by remember { mutableStateOf(true) }
    var showStations by remember { mutableStateOf(true) }
    var showDisclaimers by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .testTag("dialog_export_buffet_menu"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Title Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_back_export_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalPrintshop,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Print Preview",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$day $session Buffet",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // We can keep the close icon or remove it. Let's keep it for symmetry, but Back is often preferred on the left.
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_export_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Scrollable Content Area: Options Toolbar + Printable Card Preview
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Export Layout Customizer Bar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "PRINT & EXPORT PREFERENCES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Theme Selector: Ink-saver vs Gold vs Slate
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                PrintThemeOption(
                                    label = "Ink-Saver (B&W)",
                                    subLabel = "Crisp Monochrome",
                                    isSelected = selectedTheme == "ink_saver",
                                    onClick = { selectedTheme = "ink_saver" },
                                    modifier = Modifier.weight(1f)
                                )
                                PrintThemeOption(
                                    label = "Banquet Gold",
                                    subLabel = "Fine Dining Luxury",
                                    isSelected = selectedTheme == "luxury_gold",
                                    onClick = { selectedTheme = "luxury_gold" },
                                    modifier = Modifier.weight(1f)
                                )
                                PrintThemeOption(
                                    label = "Executive Slate",
                                    subLabel = "Corporate Modern",
                                    isSelected = selectedTheme == "slate_modern",
                                    onClick = { selectedTheme = "slate_modern" },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Paper Size Options
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Paper Format:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.width(90.dp)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("A5", "A4", "A3").forEach { format ->
                                        val isSelected = selectedPaperSize == format
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isSelected) MaterialTheme.colorScheme.primary
                                                    else Color.Transparent,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { selectedPaperSize = format }
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (format == "A5") "A5 (Standee)" else if (format == "A4") "A4 (Banquet)" else "A3 (Poster)",
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Page Layout Options
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Page Layout:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.width(90.dp)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("1 Page", "Multiple Pages").forEach { layout ->
                                        val isSelected = selectedPageLayout == layout
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isSelected) MaterialTheme.colorScheme.primary
                                                    else Color.Transparent,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { selectedPageLayout = layout }
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = layout,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Inclusion Toggles: Calories, Allergens, Station, Disclaimers
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ToggleItem(
                                    label = "Calorie Values",
                                    checked = showCalories,
                                    onCheckedChange = { showCalories = it }
                                )
                                ToggleItem(
                                    label = "Allergen Matrix",
                                    checked = showAllergens,
                                    onCheckedChange = { showAllergens = it }
                                )
                                ToggleItem(
                                    label = "Kitchen Stations",
                                    checked = showStations,
                                    onCheckedChange = { showStations = it }
                                )
                                ToggleItem(
                                    label = "Regulatory Disclaimers",
                                    checked = showDisclaimers,
                                    onCheckedChange = { showDisclaimers = it }
                                )
                            }
                        }
                    }

                    // Live Clean Printer-Friendly Card Layout Preview
                    PrinterFriendlyMenuPaper(
                        day = day,
                        session = session,
                        paxCount = paxCount,
                        establishmentName = establishmentName,
                        fssaiLicense = fssaiLicense,
                        items = items,
                        theme = selectedTheme,
                        paperSize = selectedPaperSize,
                        showCalories = showCalories,
                        showAllergens = showAllergens,
                        showStations = showStations,
                        showDisclaimers = showDisclaimers
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Bottom Actions Bar (Print, Share, Copy)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            val text = BuffetMenuExporter.generatePrintablePlainText(
                                day = day,
                                session = session,
                                paxCount = paxCount,
                                establishmentName = establishmentName,
                                fssaiLicense = fssaiLicense,
                                items = items
                            )
                            BuffetMenuExporter.copyToClipboard(context, text)
                        },
                        modifier = Modifier
                            .weight(0.9f)
                            .testTag("btn_copy_printable_text"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = {
                            val text = BuffetMenuExporter.generatePrintablePlainText(
                                day = day,
                                session = session,
                                paxCount = paxCount,
                                establishmentName = establishmentName,
                                fssaiLicense = fssaiLicense,
                                items = items
                            )
                            BuffetMenuExporter.shareMenu(
                                context = context,
                                text = text,
                                subject = "$establishmentName - $day $session Buffet Menu"
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_printable_menu"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            BuffetMenuExporter.printBuffetMenu(
                                context = context,
                                day = day,
                                session = session,
                                paxCount = paxCount,
                                establishmentName = establishmentName,
                                fssaiLicense = fssaiLicense,
                                items = items,
                                includeCalories = showCalories,
                                includeAllergens = showAllergens,
                                includeStations = showStations,
                                theme = selectedTheme,
                                paperSize = selectedPaperSize,
                                pageLayout = selectedPageLayout,
                                customLogoUri = customLogoBase64
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("btn_trigger_print_manager"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUBCOMPONENTS: PRINTER-FRIENDLY CLEAN PAPER CARD
// =========================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrinterFriendlyMenuPaper(
    day: String,
    session: String,
    paxCount: Int,
    establishmentName: String,
    fssaiLicense: String,
    items: List<BuffetMenuItemEntity>,
    theme: String,
    paperSize: String,
    showCalories: Boolean,
    showAllergens: Boolean,
    showStations: Boolean,
    showDisclaimers: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = when (theme) {
        "luxury_gold" -> Color(0xFFB45309)
        "slate_modern" -> Color(0xFF0284C7)
        else -> Color(0xFF0F172A) // Ink saver crisp dark slate
    }

    val paperBorder = when (theme) {
        "luxury_gold" -> Color(0xFFD97706)
        "slate_modern" -> Color(0xFF38BDF8)
        else -> Color(0xFFCBD5E1)
    }

    // Clean white paper sheet styling
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.5.dp, paperBorder, RoundedCornerShape(8.dp))
            .padding(18.dp)
            .testTag("printer_friendly_paper_preview")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Establishment and Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = establishmentName.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.5.sp,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "EXECUTIVE DAILY BUFFET MENU • $day $session".uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Service Meta Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👥 Service Covers: $paxCount Pax",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "🍽️ Offerings: ${items.size} Dishes",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "📜 Format: $paperSize Standard",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = accentColor, thickness = 1.5.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Course-by-Course Organized Menu Presentation
            val groupedByCourse = BUFFET_COURSES.associateWith { courseName ->
                items.filter { it.courseSection.equals(courseName, ignoreCase = true) }
            }.filterValues { it.isNotEmpty() }

            if (groupedByCourse.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items curated in the $day $session buffet yet.\nAdd recipes or use F&B Assistant Menu Builder to populate.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    groupedByCourse.forEach { (courseName, courseItems) ->
                        PrintableCourseSection(
                            courseName = courseName,
                            items = courseItems,
                            accentColor = accentColor,
                            showCalories = showCalories,
                            showAllergens = showAllergens,
                            showStations = showStations
                        )
                    }
                }
            }

            // Regulatory Compliance Disclaimers Section
            if (showDisclaimers) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                PrintableRegulatoryComplianceBox(
                    establishmentName = establishmentName,
                    fssaiLicense = fssaiLicense,
                    accentColor = accentColor
                )
            }
        }
    }
}

@Composable
fun PrintableCourseSection(
    courseName: String,
    items: List<BuffetMenuItemEntity>,
    accentColor: Color,
    showCalories: Boolean,
    showAllergens: Boolean,
    showStations: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = courseName.uppercase(),
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            letterSpacing = 0.5.sp,
            color = accentColor
        )
        HorizontalDivider(
            color = Color(0xFFE2E8F0),
            thickness = 0.8.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // 2-column or list flow for dishes
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items.forEach { item ->
                PrintableDishRow(
                    item = item,
                    showCalories = showCalories,
                    showAllergens = showAllergens,
                    showStations = showStations
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrintableDishRow(
    item: BuffetMenuItemEntity,
    showCalories: Boolean,
    showAllergens: Boolean,
    showStations: Boolean
) {
    val isVeg = item.type.equals("veg", ignoreCase = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
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
                // Official FSSAI Veg / Non-Veg Mark
                FssaiRegulatoryMark(
                    isVeg = isVeg,
                    size = 11.dp,
                    modifier = Modifier.padding(end = 6.dp)
                )

                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF0F172A)
                )
            }

            if (showCalories && item.cals.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.cals,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1)
                    )
                }
            }
        }

        if (item.desc.isNotBlank()) {
            Text(
                text = item.desc,
                fontSize = 9.5.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF475569),
                modifier = Modifier.padding(start = 17.dp, top = 1.dp)
            )
        }

        // Station & Allergens metadata
        if ((showStations && item.station.isNotBlank()) || (showAllergens && item.allergens.isNotEmpty())) {
            FlowRow(
                modifier = Modifier.padding(start = 17.dp, top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (showStations && item.station.isNotBlank()) {
                    Text(
                        text = "📍 ${item.station}",
                        fontSize = 8.5.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }

                if (showAllergens && item.allergens.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFFFE4E6), RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Allergens: ${item.allergens.joinToString(", ")}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFBE123C)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUBCOMPONENTS: REGULATORY COMPLIANCE DISCLAIMERS SECTION
// =========================================================================

@Composable
fun PrintableRegulatoryComplianceBox(
    establishmentName: String,
    fssaiLicense: String,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
            .padding(10.dp)
            .testTag("regulatory_compliance_box")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MANDATORY STATUTORY & REGULATORY COMPLIANCE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF0F172A)
                )
            }
            Text(
                text = "FSSAI Food Safety Act 2006",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4 Grid Blocks of Disclaimers
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            DisclaimerItem(
                symbol = "⚖️",
                title = "Statutory Energy & Calorie Declaration",
                description = "An average active adult requires 2,000 kcal energy per day, however, individual calorie needs may vary. Calorie estimations shown are approximate per standard banquet serving size in compliance with FSSAI Food Safety & Standards (Labelling and Display) Regulations, 2020."
            )
            DisclaimerItem(
                symbol = "⚠️",
                title = "Food Allergens & Intolerance Warning",
                description = "Kindly inform our executive chef or service team of any food allergies, food intolerances, or dietary restrictions prior to dining. Major allergens (Milk, Gluten, Tree Nuts, Peanuts, Soya, Fish, Crustaceans, Eggs) are handled in our kitchen and potential cross-contact cannot be completely eliminated."
            )
            DisclaimerItem(
                symbol = "🟢",
                title = "Regulatory Dietary Classification",
                description = "FSSAI standard symbols: Green circle in square represents 100% Vegetarian cuisine; Brown triangle in square represents Non-Vegetarian preparation. Vegetarian and Non-Vegetarian dishes are prepared and handled using segregated cookware, knives, and kitchen zones."
            )
            DisclaimerItem(
                symbol = "🌡️",
                title = "HACCP Safe Temperature & Hygiene Standards",
                description = "Hot buffet dishes are held at a minimum temperature of 63°C and chilled salads/desserts are maintained below 5°C. Prepared fresh daily for immediate consumption during the designated meal service session. Government taxes (GST) applicable as per statute."
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(6.dp))

        // Bottom Licensing & Endorsement Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FBO FSSAI License No: $fssaiLicense",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Executive Chef & QA Officer Verified",
                fontSize = 8.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun DisclaimerItem(
    symbol: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = symbol, fontSize = 9.sp)
        Column {
            Text(
                text = title,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = description,
                fontSize = 7.5.sp,
                lineHeight = 10.sp,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun PrintThemeOption(
    label: String,
    subLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
            .border(
                1.5.dp,
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subLabel,
                fontSize = 8.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ToggleItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

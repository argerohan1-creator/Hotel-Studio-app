package com.example.ui.screens

import com.example.ui.theme.PlayfairSerif
import com.example.ui.theme.MontserratSans
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.DishEntity
import com.example.ui.components.ExportBuffetMenuDialog
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FSSAI_ALLERGENS_LIST
import com.example.ui.theme.FssaiRegulatoryMark
import com.example.ui.theme.TemplateSkin
import com.example.ui.theme.TemplateSkins
import com.example.ui.theme.getTemplateSkinById
import com.example.viewmodel.HotelStudioViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuMakerScreen(
    viewModel: HotelStudioViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val dishes by viewModel.dishes.collectAsState()
    val activeTemplateId by viewModel.activeTemplateId.collectAsState()
    val menuPrintSize by viewModel.menuPrintSize.collectAsState()
    val menuOrientation by viewModel.menuOrientation.collectAsState()
    val menuPageLayout by viewModel.menuPageLayout.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val currentBuffetItems by viewModel.currentBuffetItems.collectAsState()
    val selectedBuffetDay by viewModel.selectedBuffetDay.collectAsState()
    val selectedBuffetMealSession by viewModel.selectedBuffetMealSession.collectAsState()
    val buffetPaxCount by viewModel.buffetPaxCount.collectAsState()

    val currentSkin = getTemplateSkinById(activeTemplateId)
    var templateDropdownExpanded by remember { mutableStateOf(false) }
    var langDropdownExpanded by remember { mutableStateOf(false) }
    var selectedLangName by remember { mutableStateOf("English (Default)") }
    var showExportBuffetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ==========================================
        // SECTION 1: TARGET PRINT SIZE & ORIENTATION
        // ==========================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. MENU EXTRACTOR & FORMAT SELECTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "FSSAI QA Agent Active",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Size Selector (A5, A4, A3)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TARGET PRINT SIZE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("A5", "A4", "A3").forEach { size ->
                                val isSelected = menuPrintSize == size
                                Button(
                                    onClick = { viewModel.setMenuSize(size) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                                ) {
                                    Text(
                                        text = size,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Orientation Selector (Portrait, Landscape)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PAGE ORIENTATION",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("portrait" to "Portrait", "landscape" to "Landscape").forEach { (orientKey, orientLabel) ->
                                val isSelected = menuOrientation == orientKey
                                Button(
                                    onClick = { viewModel.setMenuOrientation(orientKey) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                                ) {
                                    Text(
                                        text = orientLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                // Page Layout Selector (1 Page, Multiple Pages)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "PAGE LAYOUT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("1 Page", "Multiple Pages").forEach { layout ->
                            val isSelected = menuPageLayout == layout
                            Button(
                                onClick = { viewModel.setMenuPageLayout(layout) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                            ) {
                                Text(
                                    text = layout,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Upload menu PDF or scan image. Our Quality Assurance Agent will extract event title, date, and timing into structured headers with 99.99% accuracy.",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { viewModel.runAiEnhancer() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Extracting Menu Scan...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Menu PDF / Image Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ==========================================
        // SECTION 2: TEMPLATE SKIN SELECTOR
        // ==========================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "2. TEMPLATE SKIN SELECTOR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text("10 THEMES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { templateDropdownExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = currentSkin.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }

                        DropdownMenu(
                            expanded = templateDropdownExpanded,
                            onDismissRequest = { templateDropdownExpanded = false }
                        ) {
                            TemplateSkins.forEach { skin ->
                                DropdownMenuItem(
                                    text = { Text(skin.name, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.setTemplate(skin.id)
                                        templateDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.cycleTemplate(-1) },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text("‹", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.cycleTemplate(1) },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text("›", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ==========================================
        // SECTION 3: MASTER MENU PREVIEW (2-PARTITION)
        // ==========================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "3. MASTER RESTAURANT MENU PREVIEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Full 2-Partition Printable ($menuPrintSize Size, ${menuOrientation.replaceFirstChar { it.uppercase() }}, $menuPageLayout)",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            showExportBuffetDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export / Print Menu", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // The 2-Partition Menu Preview Card
                MenuTwoColumnPreviewCard(
                    dishes = dishes,
                    skin = currentSkin,
                    establishmentName = userProfile?.establishment ?: "Grand Horizon Culinary Suite",
                    customLogoBase64 = userProfile?.customLogoBase64
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    // Export Curated Daily Buffet Menu Dialog with Regulatory Compliance Disclaimers
    if (showExportBuffetDialog) {
        val exportItems = if (currentBuffetItems.isNotEmpty()) {
            currentBuffetItems
        } else {
            dishes.map { dish ->
                BuffetMenuItemEntity(
                    dayOfWeek = selectedBuffetDay,
                    mealSession = selectedBuffetMealSession,
                    courseSection = dish.category,
                    name = dish.name,
                    desc = dish.desc,
                    cals = dish.cals,
                    type = dish.type,
                    allergens = dish.allergens
                )
            }
        }
        ExportBuffetMenuDialog(
            day = selectedBuffetDay,
            session = selectedBuffetMealSession,
            paxCount = buffetPaxCount,
            establishmentName = userProfile?.establishment ?: "Grand Horizon Culinary Suite",
            items = exportItems,
            initialPaperSize = menuPrintSize,
            initialPageLayout = menuPageLayout,
            customLogoBase64 = userProfile?.customLogoBase64,
            onDismiss = { showExportBuffetDialog = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuTwoColumnPreviewCard(
    dishes: List<DishEntity>,
    skin: TemplateSkin,
    establishmentName: String,
    customLogoBase64: String?,
    modifier: Modifier = Modifier
) {
    val strictSequence = listOf(
        "Mocktails / Non-Alcoholic Beverages",
        "Starters / Pass Arounds",
        "Salads & Accompaniments",
        "Soup",
        "Main Course",
        "Desserts"
    )

    // Group dishes by category
    val grouped = strictSequence.mapNotNull { cat ->
        val itemsInCat = dishes.filter { it.category == cat }
        if (itemsInCat.isNotEmpty()) cat to itemsInCat else null
    }

    val halfIndex = (grouped.size + 1) / 2
    val col1Categories = grouped.take(halfIndex)
    val col2Categories = grouped.drop(halfIndex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(skin.backgroundColor)
            .border(skin.borderWidth, skin.borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Wordmark & Establishment
            if (!customLogoBase64.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (skin.isDark) Color.White else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    AsyncImage(
                        model = customLogoBase64,
                        contentDescription = "Establishment Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .heightIn(max = 40.dp)
                            .fillMaxWidth(0.5f)
                    )
                }
            } else {
                Text(
                    text = "HOTEL STUDIO",
                    fontFamily = PlayfairSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp,
                    color = if (skin.isDark) skin.borderColor else MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = establishmentName.uppercase(),
                fontFamily = PlayfairSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                letterSpacing = 1.sp,
                color = skin.textColor
            )
            Text(
                text = "EXECUTIVE CULINARY BANQUET • CHEF'S SELECTION",
                fontFamily = MontserratSans,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                color = skin.secondaryTextColor
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = skin.borderColor.copy(alpha = 0.35f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Two-partition columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Left Column
                Column(modifier = Modifier.weight(1f)) {
                    col1Categories.forEach { (catName, items) ->
                        Text(
                            text = catName.uppercase(),
                            fontFamily = PlayfairSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp,
                            color = skin.borderColor,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        items.forEach { dish ->
                            MenuItemSnippet(dish = dish, skin = skin)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Vertical Divider Line
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .background(skin.borderColor.copy(alpha = 0.35f))
                )

                // Right Column
                Column(modifier = Modifier.weight(1f)) {
                    col2Categories.forEach { (catName, items) ->
                        Text(
                            text = catName.uppercase(),
                            fontFamily = PlayfairSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp,
                            color = skin.borderColor,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        items.forEach { dish ->
                            MenuItemSnippet(dish = dish, skin = skin)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = skin.borderColor.copy(alpha = 0.35f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer Allergen and FSSAI mark legend
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FssaiRegulatoryMark(isVeg = true, size = 10.dp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Vegetarian", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = skin.textColor)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FssaiRegulatoryMark(isVeg = false, size = 10.dp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Non Vegetarian", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = skin.textColor)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "LIST OF ALLERGENS:",
                fontSize = 7.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = skin.textColor
            )
            Spacer(modifier = Modifier.height(3.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                FSSAI_ALLERGENS_LIST.forEach { a ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    ) {
                        AllergenIcon(allergen = a, size = 10.dp)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(a, fontSize = 6.sp, color = skin.secondaryTextColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kindly inform us if you are allergic to any food ingredients.",
                fontStyle = FontStyle.Italic,
                fontSize = 6.5.sp,
                color = skin.secondaryTextColor
            )
        }
    }
}

@Composable
private fun MenuItemSnippet(
    dish: DishEntity,
    skin: TemplateSkin
) {
    Column(modifier = Modifier.padding(bottom = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FssaiRegulatoryMark(isVeg = dish.type == "veg", size = 8.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = dish.name.uppercase(),
                fontFamily = PlayfairSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.3.sp,
                color = skin.textColor
            )
        }
        if (dish.desc.isNotBlank()) {
            Text(
                text = dish.desc,
                fontFamily = MontserratSans,
                fontWeight = FontWeight.Medium,
                fontSize = 8.sp,
                lineHeight = 11.sp,
                color = skin.secondaryTextColor,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        if (dish.cals.isNotBlank()) {
            Text(
                text = dish.cals,
                fontFamily = MontserratSans,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                color = skin.secondaryTextColor.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

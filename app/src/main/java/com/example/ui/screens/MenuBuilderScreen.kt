/* RECONSTRUCTED FILE AFTER ACCIDENTAL DELETION */
package com.example.ui.screens

import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.DietaryBadgeRow
import com.example.ui.components.DietaryTagAssignerDialog
import com.example.util.BuffetMenuExporter
import com.example.util.CulinaryAgent
import com.example.data.model.DishEntity
import com.example.viewmodel.ManualDishDraft
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.BuffetClosingEntity
import com.example.ui.components.ExportBuffetMenuDialog
import com.example.ui.components.ExportQRDisplayTagsDialog
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FssaiRegulatoryMark
import com.example.viewmodel.HotelStudioViewModel

val BUFFET_COURSES = listOf(
    "Welcome Beverages",
    "Starters & Live Counter",
    "Soups & Breads",
    "Salad Bar",
    "Hi Tea Snacks",
    "Main Course (Veg)",
    "Main Course (Non-Veg)",
    "Accompaniments & Rices",
    "Desserts & Sweets"
)

val DAYS_OF_WEEK = listOf(
    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
)

val MEAL_SESSIONS = listOf(
    "Breakfast", "Brunch", "Lunch", "Hi Tea", "Dinner", "Supper"
)

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MenuBuilderScreen(
    viewModel: HotelStudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentItems by viewModel.currentBuffetItems.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val dishes by viewModel.dishes.collectAsState()
    val selectedDay by viewModel.selectedBuffetDay.collectAsState()
    val selectedSession by viewModel.selectedBuffetMealSession.collectAsState()
    val paxCount by viewModel.buffetPaxCount.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val isRecipePickerOpen by viewModel.isRecipePickerOpen.collectAsState()
    val allClosingRecords by viewModel.allBuffetClosingRecords.collectAsState()
    
    val activeClosing = allClosingRecords.find { it.day == selectedDay && it.session == selectedSession }
    val activeTab by viewModel.menuBuilderTab.collectAsState()
    val totalEstimatedCost = remember(currentItems, paxCount) { viewModel.calculateTotalBuffetCost() }
    val userProfile by viewModel.userProfile.collectAsState()
    val menuPrintSize by viewModel.menuPrintSize.collectAsState()
    val menuPageLayout by viewModel.menuPageLayout.collectAsState()

    var showCustomItemDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<BuffetMenuItemEntity?>(null) }
    var itemToAudit by remember { mutableStateOf<BuffetMenuItemEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<BuffetMenuItemEntity?>(null) }
    var showDuplicateDialog by remember { mutableStateOf(false) }
    var showPrepSheetDialog by remember { mutableStateOf(false) }
    var showPaxEditDialog by remember { mutableStateOf(false) }
    var showExportPrintDialog by remember { mutableStateOf(false) }
    var showQRDisplayTagsDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = when(activeTab) {
                "builder" -> 0
                "grid" -> 1
                "recipes" -> 2
                "operations" -> 3
                else -> 4
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = activeTab == "builder",
                onClick = { viewModel.setMenuBuilderTab("builder") },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Builder", fontWeight = if (activeTab == "builder") FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == "grid",
                onClick = { viewModel.setMenuBuilderTab("grid") },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.GridView, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Weekly Grid", fontWeight = if (activeTab == "grid") FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == "recipes",
                onClick = { viewModel.setMenuBuilderTab("recipes") },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Kitchen, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Recipe Bank", fontWeight = if (activeTab == "recipes") FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == "operations",
                onClick = { viewModel.setMenuBuilderTab("operations") },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI & Operations", fontWeight = if (activeTab == "operations") FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == "preview",
                onClick = { viewModel.setMenuBuilderTab("preview") },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Menu Maker", fontWeight = if (activeTab == "preview") FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp)
                    }
                }
            )
        }

        when (activeTab) {
            "preview" -> {
                MenuMakerScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            }
            "grid" -> {
                WeeklyGridView(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            }
            "recipes" -> {
                RecipeBankScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            }
            "operations" -> {
                OperationsAndPlanningTab(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        DayOfWeekSelector(selectedDay = selectedDay, onSelectDay = { viewModel.setBuffetDay(it) })
                    }
                    item {
                        MealSessionSelector(selectedSession = selectedSession, onSelectSession = { viewModel.setBuffetMealSession(it) })
                    }
                    item {
                        BuffetMetricsCard(
                            items = currentItems,
                            paxCount = paxCount,
                            estimatedCost = totalEstimatedCost,
                            selectedDay = selectedDay,
                            selectedSession = selectedSession,
                            onEditPax = { showPaxEditDialog = true },
                            onOpenRecipePicker = { viewModel.openRecipePicker() },
                            onGenerateAi = { viewModel.setMenuBuilderTab("operations") },
                            onDuplicate = { showDuplicateDialog = true },
                            onClear = { viewModel.clearCurrentBuffetMenu() },
                            onViewPrepSheet = { showPrepSheetDialog = true },
                            onExportDisplayTags = { showQRDisplayTagsDialog = false },
                            onExportPrintableMenu = { showExportPrintDialog = true },
                            isAiLoading = isAiLoading
                        )
                    }
                    if (activeClosing != null) {
                        item { BuffetAnalyticsSummaryCard(record = activeClosing) }
                    }
                    items(BUFFET_COURSES) { courseName ->
                        val courseItems = currentItems.filter { it.courseSection.equals(courseName, ignoreCase = true) }
                        CourseSectionCard(
                            courseName = courseName,
                            items = courseItems,
                            onAddRecipe = { viewModel.openRecipePicker(targetCourse = courseName) },
                            onAddCustom = { 
                                viewModel.setRecipePickerTargetCourse(courseName)
                                showCustomItemDialog = true 
                            },
                            onToggleReady = { viewModel.toggleBuffetItemReady(it) },
                            onMoveUp = { viewModel.reorderBuffetItem(it, moveUp = true) },
                            onMoveDown = { viewModel.reorderBuffetItem(it, moveUp = false) },
                            onEdit = { itemToEdit = it },
                            onAuditClick = { itemToAudit = it },
                            onDelete = { itemToDelete = it }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
        }
    }

    if (isRecipePickerOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeRecipePicker() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            RecipeDatabasePickerSheet(viewModel = viewModel)
        }
    }

    if (showCustomItemDialog) {
        AddCustomBuffetItemDialog(
            day = selectedDay,
            session = selectedSession,
            course = viewModel.recipePickerTargetCourse.collectAsState().value ?: "Main Course (Veg)",
            viewModel = viewModel,
            onDismiss = { showCustomItemDialog = false },
            onConfirm = { name, desc, station, cals, allergens ->
                viewModel.addCustomBuffetItem(name, desc, station, cals, allergens)
                showCustomItemDialog = false
            }
        )
    }

    if (showPaxEditDialog) {
        SetPaxCountDialog(currentPax = paxCount, onDismiss = { showPaxEditDialog = false }, onConfirm = { viewModel.setBuffetPaxCount(it) })
    }

    if (showDuplicateDialog) {
        DuplicateBuffetDialog(onDismiss = { showDuplicateDialog = false }, onConfirm = { d, s -> viewModel.duplicateBuffetMenu(d, s) })
    }

    if (showPrepSheetDialog) {
        KitchenPrepSheetDialog(
            day = selectedDay,
            session = selectedSession,
            paxCount = paxCount,
            items = currentItems,
            recipes = recipes,
            onDismiss = { showPrepSheetDialog = false },
            onPrint = {
                BuffetMenuExporter.printKitchenPrepSheet(context, selectedDay, selectedSession, paxCount, currentItems, recipes)
                showPrepSheetDialog = false
            }
        )
    }

    if (showExportPrintDialog) {
        ExportBuffetMenuDialog(
            day = selectedDay,
            session = selectedSession,
            paxCount = paxCount,
            establishmentName = userProfile?.establishment ?: "Global Culinary Suite",
            items = currentItems,
            initialPaperSize = menuPrintSize,
            initialPageLayout = menuPageLayout,
            customLogoBase64 = userProfile?.customLogoBase64,
            onDismiss = { showExportPrintDialog = false }
        )
    }

    itemToEdit?.let { item ->
        EditBuffetItemDialog(
            item = item,
            viewModel = viewModel,
            onDismiss = { itemToEdit = null },
            onConfirm = { updated ->
                viewModel.updateBuffetItem(updated)
                itemToEdit = null
            }
        )
    }
}

@Composable
fun DayOfWeekSelector(selectedDay: String, onSelectDay: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("SELECT WORK DAY", fontSize = 10.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DAYS_OF_WEEK.forEach { day ->
                val isSelected = day.equals(selectedDay, ignoreCase = true)
                Surface(
                    onClick = { onSelectDay(day) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.height(48.dp).width(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = day.take(3).uppercase(), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealSessionSelector(selectedSession: String, onSelectSession: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("CULINARY SESSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MEAL_SESSIONS.forEach { session ->
                val isSelected = session.equals(selectedSession, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectSession(session) },
                    label = { Text(session, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OperationsAndPlanningTab(viewModel: HotelStudioViewModel, modifier: Modifier = Modifier) {
    var showClosingDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    val paxCount by viewModel.buffetPaxCount.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val selectedDay by viewModel.selectedBuffetDay.collectAsState()
    val selectedSession by viewModel.selectedBuffetMealSession.collectAsState()
    
    var paxText by remember { mutableStateOf(paxCount.toString()) }
    var soupCountText by remember { mutableStateOf("1") }
    var saladCountText by remember { mutableStateOf("1") }
    var vegCountText by remember { mutableStateOf("4") }
    var nonVegCountText by remember { mutableStateOf("2") }
    var dessertCountText by remember { mutableStateOf("2") }
    var breadCountText by remember { mutableStateOf("2") }
    var riceCountText by remember { mutableStateOf("1") }
    var starterCountText by remember { mutableStateOf("2") }
    var actionStationCountText by remember { mutableStateOf("1") }
    var beverageCountText by remember { mutableStateOf("1") }

    val selectedActionStations = listOf("Live Action Counter")
    val selectedBeverages = listOf("Signature Beverage")

    Column(modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Establishment Data", fontWeight = FontWeight.Bold)
                Button(onClick = { showImportDialog = true }) { Text("Upload Recipes & Costing") }
            }
        }
        
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AI Menu Generation", fontWeight = FontWeight.Bold)
                OutlinedTextField(value = paxText, onValueChange = { paxText = it }, label = { Text("Pax") })
                Row {
                    OperationalCounter("Soups", soupCountText, Icons.Default.SoupKitchen, MaterialTheme.colorScheme.primary, { soupCountText = it }, 5, Modifier.weight(1f))
                    OperationalCounter("Salads", saladCountText, Icons.Default.Eco, MaterialTheme.colorScheme.secondary, { saladCountText = it }, 5, Modifier.weight(1f))
                }
                Button(
                    onClick = {
                        viewModel.generateAiBuffetMenuWithOptions(
                            targetPax = paxText.toIntOrNull() ?: paxCount,
                            vegCount = vegCountText.toIntOrNull() ?: 4,
                            nonVegCount = nonVegCountText.toIntOrNull() ?: 2,
                            dessertCount = dessertCountText.toIntOrNull() ?: 2,
                            breadCount = breadCountText.toIntOrNull() ?: 2,
                            riceCount = riceCountText.toIntOrNull() ?: 1,
                            starterCount = starterCountText.toIntOrNull() ?: 2,
                            actionStations = selectedActionStations,
                            beverageChoices = selectedBeverages
                        )
                        viewModel.setMenuBuilderTab("builder")
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.AutoAwesome, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate AI Buffet Plan")
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Operational Counters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OperationalCounter("Veg Dishes", vegCountText, Icons.Default.Eco, Color(0xFF4CAF50), { vegCountText = it }, 10, Modifier.weight(1f))
                    OperationalCounter("Non-Veg", nonVegCountText, Icons.Default.Restaurant, Color(0xFFF44336), { nonVegCountText = it }, 5, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OperationalCounter("Starters", starterCountText, Icons.Default.Fingerprint, Color(0xFF9C27B0), { starterCountText = it }, 8, Modifier.weight(1f))
                    OperationalCounter("Desserts", dessertCountText, Icons.Default.Cake, Color(0xFFE91E63), { dessertCountText = it }, 8, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OperationalCounter("Breads", breadCountText, Icons.Default.BakeryDining, Color(0xFF795548), { breadCountText = it }, 6, Modifier.weight(1f))
                    OperationalCounter("Rice/Pulao", riceCountText, Icons.Default.RiceBowl, Color(0xFF607D8B), { riceCountText = it }, 4, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OperationalCounter("Live Stations", actionStationCountText, Icons.Default.OutdoorGrill, Color(0xFFFF9800), { actionStationCountText = it }, 3, Modifier.weight(1f))
                    OperationalCounter("Beverages", beverageCountText, Icons.Default.LocalBar, Color(0xFF2196F3), { beverageCountText = it }, 3, Modifier.weight(1f))
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Operations Control", fontWeight = FontWeight.Bold)
                Button(onClick = { showClosingDialog = true }) { Text("Close Buffet & Track Waste") }
            }
        }
    }

    if (showImportDialog) {
        EstablishmentImportDialog(
            onDismiss = { showImportDialog = false }, 
            onImport = { viewModel.importEstablishmentCosting(it) },
            onImportFile = { uri, mimeType -> 
                viewModel.importRecipesFromFile(uri, mimeType)
                showImportDialog = false
            }
        )
    }

    if (showClosingDialog) {
        BuffetClosingDialog(
            day = selectedDay,
            session = selectedSession,
            onDismiss = { showClosingDialog = false },
            onConfirm = { sold, price, wastage -> 
                viewModel.closeBuffetService(sold, price, wastage)
                showClosingDialog = false
            }
        )
    }
}

@Composable
fun OperationalCounter(label: String, countText: String, icon: ImageVector, color: Color, onValueChange: (String) -> Unit, max: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { val c = (countText.toIntOrNull() ?: 0) - 1; if (c >= 0) onValueChange(c.toString()) }) { Icon(Icons.Default.Remove, null) }
                Text(countText, fontWeight = FontWeight.Bold)
                IconButton(onClick = { val c = (countText.toIntOrNull() ?: 0) + 1; if (c <= max) onValueChange(c.toString()) }) { Icon(Icons.Default.Add, null) }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddCustomBuffetItemDialog(
    day: String,
    session: String,
    course: String,
    viewModel: HotelStudioViewModel,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var station by remember { mutableStateOf("") }
    var cals by remember { mutableStateOf("") }
    var allergens by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var wasAiUsed by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(false) }
    var showTagAssignerDialog by remember { mutableStateOf(false) }

    val currentTagList = remember(allergens) {
        allergens.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PostAdd, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add Custom Menu Item", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Adding to: $day $session - $course", style = MaterialTheme.typography.bodySmall)
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (isAnalyzing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            IconButton(
                                onClick = {
                                    if (name.isNotBlank()) {
                                        isAnalyzing = true
                                        wasAiUsed = true
                                        isVerified = false
                                        viewModel.analyzeDishForCustomItem(name) { analysis ->
                                            desc = analysis.description
                                            cals = analysis.caloriesPer100
                                            allergens = analysis.allergens.joinToString(", ")
                                            isAnalyzing = false
                                        }
                                    }
                                },
                                enabled = name.isNotBlank()
                            ) {
                                Icon(Icons.Default.AutoAwesome, "Analyze with AI", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                )
                
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it; if (it.isNotEmpty()) isVerified = false },
                    label = { Text("Short Description") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { if (isAnalyzing) Text("AI is writing...") else null }
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cals,
                        onValueChange = { cals = it; if (it.isNotEmpty()) isVerified = false },
                        label = { Text("Calories") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("e.g. 180 kcal") }
                    )
                    OutlinedTextField(
                        value = station,
                        onValueChange = { station = it },
                        label = { Text("Station") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("e.g. Tandoor") }
                    )
                }

                // Dietary & Allergen Tags Section
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dietary & Allergen Safeguards",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (currentTagList.isNotEmpty()) {
                                Text(
                                    text = "${currentTagList.size} Tags",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (currentTagList.isNotEmpty()) {
                            DietaryBadgeRow(tags = currentTagList, isCompact = true)
                        }

                        Button(
                            onClick = { showTagAssignerDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Assign Dietary Tags (Allergen-free, Vegan, etc.)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = allergens,
                    onValueChange = { allergens = it; if (it.isNotEmpty()) isVerified = false },
                    label = { Text("Allergens / Tags (Comma Separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Allergen-Free, Vegan, Gluten-Free, Milk") }
                )

                if (wasAiUsed) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Safety Disclaimer", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            }
                            Text(
                                "AI suggestions are estimates and can be incorrect. You MUST manually verify all allergens and calorie data for food safety compliance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isVerified,
                                    onCheckedChange = { isVerified = it },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.error)
                                )
                                Text("I have verified the data", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                } else if (name.isNotBlank() && !isAnalyzing && desc.isEmpty()) {
                    Text(
                        "Tip: Tap the magic icon to auto-fill description, calories, and allergens.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (name.isNotBlank()) {
                        val allergenList = allergens.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onConfirm(name, desc, station, cals, allergenList)
                    }
                },
                enabled = name.isNotBlank() && !isAnalyzing && (!wasAiUsed || isVerified),
                colors = if (wasAiUsed && !isVerified) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)) else ButtonDefaults.buttonColors()
            ) {
                Text("Add to Menu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    if (showTagAssignerDialog) {
        DietaryTagAssignerDialog(
            initialTags = currentTagList,
            dishName = name.ifBlank { "Custom Buffet Item" },
            course = course,
            isVeg = true,
            onDismiss = { showTagAssignerDialog = false },
            onConfirm = { updatedTags ->
                allergens = updatedTags.joinToString(", ")
                isVerified = false
                showTagAssignerDialog = false
            }
        )
    }
}

@Composable
fun EditBuffetItemDialog(
    item: BuffetMenuItemEntity,
    viewModel: HotelStudioViewModel,
    onDismiss: () -> Unit,
    onConfirm: (BuffetMenuItemEntity) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var desc by remember { mutableStateOf(item.desc) }
    var station by remember { mutableStateOf(item.station) }
    var cals by remember { mutableStateOf(item.cals) }
    var allergens by remember { mutableStateOf(item.allergens.joinToString(", ")) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var wasAiUsed by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(true) } // Assume true initially for existing data
    var showTagAssignerDialog by remember { mutableStateOf(false) }

    val currentTagList = remember(allergens) {
        allergens.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Edit Buffet Item", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (isAnalyzing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            IconButton(
                                onClick = {
                                    if (name.isNotBlank()) {
                                        isAnalyzing = true
                                        wasAiUsed = true
                                        isVerified = false
                                        viewModel.analyzeDishForCustomItem(name) { analysis ->
                                            desc = analysis.description
                                            cals = analysis.caloriesPer100
                                            allergens = analysis.allergens.joinToString(", ")
                                            isAnalyzing = false
                                        }
                                    }
                                },
                                enabled = name.isNotBlank()
                            ) {
                                Icon(Icons.Default.AutoAwesome, "AI Analysis", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it; if (wasAiUsed) isVerified = false },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    placeholder = { if (isAnalyzing) Text("AI is writing...") else null }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cals,
                        onValueChange = { cals = it; if (wasAiUsed) isVerified = false },
                        label = { Text("Calories") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("e.g. 180 kcal") }
                    )
                    OutlinedTextField(
                        value = station,
                        onValueChange = { station = it },
                        label = { Text("Station") },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Dietary & Allergen Tags Section
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dietary & Allergen Safeguards",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (currentTagList.isNotEmpty()) {
                                Text(
                                    text = "${currentTagList.size} Tags",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (currentTagList.isNotEmpty()) {
                            DietaryBadgeRow(tags = currentTagList, isCompact = true)
                        }

                        Button(
                            onClick = { showTagAssignerDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Assign Dietary Tags (Allergen-free, Vegan, etc.)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = allergens,
                    onValueChange = { allergens = it; if (wasAiUsed) isVerified = false },
                    label = { Text("Allergens / Tags (Comma Separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Allergen-Free, Vegan, Gluten-Free, Milk") }
                )

                if (wasAiUsed) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Safety Disclaimer", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            }
                            Text(
                                "AI suggestions are estimates and can be incorrect. You MUST manually verify all allergens and calorie data for food safety compliance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isVerified,
                                    onCheckedChange = { isVerified = it },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.error)
                                )
                                Text("I have verified the data", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
                Text(
                    "Current Category: ${item.courseSection}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(item.copy(
                        name = name,
                        desc = desc,
                        station = station,
                        cals = cals,
                        allergens = allergens.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    ))
                },
                enabled = name.isNotBlank() && !isAnalyzing && (!wasAiUsed || isVerified),
                colors = if (wasAiUsed && !isVerified) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)) else ButtonDefaults.buttonColors()
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    if (showTagAssignerDialog) {
        DietaryTagAssignerDialog(
            initialTags = currentTagList,
            dishName = name.ifBlank { item.name },
            course = item.courseSection,
            isVeg = item.type == "veg",
            onDismiss = { showTagAssignerDialog = false },
            onConfirm = { updatedTags ->
                allergens = updatedTags.joinToString(", ")
                if (wasAiUsed) isVerified = false
                showTagAssignerDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuffetMetricsCard(
    items: List<BuffetMenuItemEntity>,
    paxCount: Int,
    estimatedCost: Double,
    selectedDay: String,
    selectedSession: String,
    onEditPax: () -> Unit,
    onOpenRecipePicker: () -> Unit,
    onGenerateAi: () -> Unit,
    onDuplicate: () -> Unit,
    onClear: () -> Unit,
    onViewPrepSheet: () -> Unit,
    onExportDisplayTags: () -> Unit,
    onExportPrintableMenu: () -> Unit,
    isAiLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$selectedDay $selectedSession",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${items.size} Items | Est. Cost: ₹${String.format("%.0f", estimatedCost)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = onEditPax,
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(Icons.Default.People, contentDescription = "Edit Pax", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Add Recipe",
                    icon = Icons.Default.Add,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = onOpenRecipePicker
                )
                ActionButton(
                    text = "AI Smart Menu",
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFF673AB7),
                    onClick = onGenerateAi,
                    isLoading = isAiLoading
                )
                ActionButton(
                    text = "Duplicate",
                    icon = Icons.Default.ContentCopy,
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = onDuplicate
                )
                ActionButton(
                    text = "Prep Sheet",
                    icon = Icons.Default.Assignment,
                    color = MaterialTheme.colorScheme.tertiary,
                    onClick = onViewPrepSheet
                )
                ActionButton(
                    text = "Clear All",
                    icon = Icons.Default.DeleteSweep,
                    color = MaterialTheme.colorScheme.error,
                    onClick = onClear
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = color)
            } else {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = color)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun CourseSectionCard(
    courseName: String,
    items: List<BuffetMenuItemEntity>,
    onAddRecipe: () -> Unit,
    onAddCustom: () -> Unit,
    onToggleReady: (BuffetMenuItemEntity) -> Unit,
    onMoveUp: (BuffetMenuItemEntity) -> Unit,
    onMoveDown: (BuffetMenuItemEntity) -> Unit,
    onEdit: (BuffetMenuItemEntity) -> Unit,
    onAuditClick: (BuffetMenuItemEntity) -> Unit,
    onDelete: (BuffetMenuItemEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = courseName.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onAddRecipe) {
                Icon(Icons.Default.PostAdd, contentDescription = "Add from Bank", modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onAddCustom) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Add Custom", modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }

        AnimatedVisibility(visible = expanded) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items added to this section.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items.sortedBy { it.sortOrder }.forEach { item ->
                        BuffetItemRow(
                            item = item,
                            onToggleReady = { onToggleReady(item) },
                            onMoveUp = { onMoveUp(item) },
                            onMoveDown = { onMoveDown(item) },
                            onEdit = { onEdit(item) },
                            onAuditClick = { onAuditClick(item) },
                            onDelete = { onDelete(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BuffetItemRow(
    item: BuffetMenuItemEntity,
    onToggleReady: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onEdit: () -> Unit,
    onAuditClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isReady) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            1.dp,
            if (item.isReady) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggleReady,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (item.isReady) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Ready",
                    tint = if (item.isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (item.isReady) TextDecoration.None else TextDecoration.None,
                    color = if (item.isReady) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (item.type == "veg") Color(0xFF4CAF50) else Color(0xFFF44336))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.station,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (item.allergens.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    DietaryBadgeRow(
                        tags = item.allergens,
                        isCompact = true,
                        maxTags = 3
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun EstablishmentImportDialog(onDismiss: () -> Unit, onImport: (String) -> Unit, onImportFile: (android.net.Uri, String) -> Unit) {
    var text by remember { mutableStateOf("") }

    val excelLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { onImportFile(it, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Import Recipes & Costing") },
        text = {
            Column {
                Text("Paste Excel data or upload a file directly.", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { excelLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/csv", "application/pdf")) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(androidx.compose.material.icons.Icons.Default.FileUpload, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Upload Excel / PDF / CSV")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Or paste raw data (Item, Category, Ingredients, Prep, Cost, Portion, Steps)", style = MaterialTheme.typography.labelSmall)
                
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    placeholder = { Text("e.g. Paneer Tikka, Starter, Paneer;Curd, 20m, 45.0, 1, Grill...") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onImport(text); onDismiss() }) { Text("Import") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun BuffetClosingDialog(day: String, session: String, onDismiss: () -> Unit, onConfirm: (Int, Double, Double) -> Unit) {
    var soldText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var wasteText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Close Buffet: $day $session") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = soldText,
                    onValueChange = { soldText = it },
                    label = { Text("Actual Covers Sold (Pax)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Selling Price per Buffet (INR)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = wasteText,
                    onValueChange = { wasteText = it },
                    label = { Text("Estimated Wastage Value (INR)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sold = soldText.toIntOrNull() ?: 0
                    val price = priceText.toDoubleOrNull() ?: 0.0
                    val waste = wasteText.toDoubleOrNull() ?: 0.0
                    onConfirm(sold, price, waste)
                }
            ) { Text("Confirm & Calculate") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun BuffetAnalyticsSummaryCard(record: BuffetClosingEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PieChart, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Operational Analytics", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Food Cost: ${String.format("%.1f", record.foodCostPercentage)}% | Waste: ${String.format("%.1f", record.wastagePercentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    "Revenue: ₹${String.format("%.0f", record.totalRevenue)} | Profit: ₹${String.format("%.0f", record.totalRevenue - record.totalFoodCost)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SetPaxCountDialog(currentPax: Int, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var text by remember { mutableStateOf(currentPax.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Pax Count") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Expected Pax") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(text.toIntOrNull() ?: currentPax); onDismiss() }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DuplicateBuffetDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var selectedDay by remember { mutableStateOf(DAYS_OF_WEEK[0]) }
    var selectedSession by remember { mutableStateOf(MEAL_SESSIONS[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Duplicate Current Menu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select target day and session:", style = MaterialTheme.typography.bodySmall)
                Text("Target Day: $selectedDay")
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    DAYS_OF_WEEK.forEach { day ->
                        FilterChip(selected = selectedDay == day, onClick = { selectedDay = day }, label = { Text(day.take(3)) })
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                Text("Target Session: $selectedSession")
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    MEAL_SESSIONS.forEach { session ->
                        FilterChip(selected = selectedSession == session, onClick = { selectedSession = session }, label = { Text(session) })
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedDay, selectedSession); onDismiss() }) { Text("Duplicate") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun KitchenPrepSheetDialog(
    day: String,
    session: String,
    paxCount: Int,
    items: List<BuffetMenuItemEntity>,
    recipes: List<RecipeEntity>,
    onDismiss: () -> Unit,
    onPrint: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kitchen Prep Sheet Preview") },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                item {
                    Text("$day $session - $paxCount Pax", fontWeight = FontWeight.Bold)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
                items(items) { item ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text("Station: ${item.station}", style = MaterialTheme.typography.bodySmall)
                        val recipe = recipes.find { it.id == item.recipeId }
                        if (recipe != null) {
                            Text("Portions: $paxCount", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onPrint) { Text("Export PDF / Print") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable fun RecipeDatabasePickerSheet(viewModel: HotelStudioViewModel) { 
    Box(modifier = Modifier.fillMaxWidth().height(400.dp).padding(16.dp), contentAlignment = Alignment.Center) {
        Text("Recipe Bank Picker (Restoring...)")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeeklyGridView(viewModel: HotelStudioViewModel, modifier: Modifier = Modifier) {
    val allItems by viewModel.allBuffetMenuItems.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Weekly Buffet Schedule",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Overview of planned menus across all sessions. Click a cell to customize.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            // Left Column (Empty for alignment)
            Column(modifier = Modifier.width(100.dp)) {
                Box(modifier = Modifier.height(50.dp))
                MEAL_SESSIONS.forEach { session ->
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            session,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Day Columns
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").forEach { day ->
                Column(modifier = Modifier.width(140.dp)) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    MEAL_SESSIONS.forEach { session ->
                        val itemsForSlot = allItems.filter { it.dayOfWeek == day && it.mealSession == session }
                        WeeklyGridCell(
                            day = day,
                            session = session,
                            itemCount = itemsForSlot.size,
                            onClick = {
                                viewModel.setBuffetDay(day)
                                viewModel.setBuffetMealSession(session)
                                viewModel.setMenuBuilderTab("builder")
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun WeeklyGridCell(
    day: String,
    session: String,
    itemCount: Int,
    onClick: () -> Unit
) {
    val isEmpty = itemCount == 0
    val containerColor = if (isEmpty) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .height(92.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (isEmpty) null else BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEmpty) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    "Empty",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            } else {
                Text(
                    "$itemCount Items",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Planned",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "EDIT",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

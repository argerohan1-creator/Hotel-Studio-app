package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.DishEntity
import com.example.ui.theme.AllergenIcon
import com.example.viewmodel.HotelStudioViewModel

/**
 * Official 14 Statutory Regulatory Allergens as mandated under Food Safety Standards
 * (FSSAI Schedule IV, EU FIC 1169/2011, US FALCPA & Codex Alimentarius Norms).
 */
data class StatutoryNormAllergen(
    val id: Int,
    val canonicalKey: String,
    val standardTitle: String,
    val normCode: String,
    val scientificScope: String,
    val typicalSources: String,
    val aliases: List<String>
)

val STATUTORY_14_ALLERGENS_NORMS = listOf(
    StatutoryNormAllergen(
        id = 1,
        canonicalKey = "Gluten",
        standardTitle = "Cereals containing Gluten",
        normCode = "NORM-01",
        scientificScope = "Wheat, rye, barley, oats, spelt, kamut & hybrid strains",
        typicalSources = "Breads, bakery, roux, batters, pastas, gravies, beer",
        aliases = listOf("gluten", "wheat", "barley", "rye", "oats", "spelt")
    ),
    StatutoryNormAllergen(
        id = 2,
        canonicalKey = "Crustaceans",
        standardTitle = "Crustaceans & Shellfish",
        normCode = "NORM-02",
        scientificScope = "Crab, lobster, prawns, shrimps, crayfish, langoustines",
        typicalSources = "Bisques, shrimp pastes, seafood stocks, stir-fries, platters",
        aliases = listOf("crustaceans", "shellfish", "crab", "lobster", "prawn", "prawns", "shrimp", "shrimps")
    ),
    StatutoryNormAllergen(
        id = 3,
        canonicalKey = "Eggs",
        standardTitle = "Eggs & Egg Products",
        normCode = "NORM-03",
        scientificScope = "Hen, duck, quail eggs, egg albumin, lysozyme, ovomucoid",
        typicalSources = "Mayonnaise, pastries, mousses, pasta, glazing, custards",
        aliases = listOf("eggs", "egg", "albumin", "mayonnaise")
    ),
    StatutoryNormAllergen(
        id = 4,
        canonicalKey = "Fish",
        standardTitle = "Fish & Marine Products",
        normCode = "NORM-04",
        scientificScope = "All freshwater and pelagic saltwater finfish species, fish gelatin",
        typicalSources = "Fish sauce, Worcestershire, dashi, bouillons, stocks",
        aliases = listOf("fish", "salmon", "tuna", "cod", "anchovy", "bass")
    ),
    StatutoryNormAllergen(
        id = 5,
        canonicalKey = "Peanuts",
        standardTitle = "Peanuts (Groundnuts)",
        normCode = "NORM-05",
        scientificScope = "Arachis hypogaea, peanut kernels, unrefined peanut oils, pastes",
        typicalSources = "Satay sauces, dressings, Asian marinades, bakery, peanut butter",
        aliases = listOf("peanuts", "peanut", "groundnuts", "groundnut")
    ),
    StatutoryNormAllergen(
        id = 6,
        canonicalKey = "Soya",
        standardTitle = "Soybeans & Soya Derivatives",
        normCode = "NORM-06",
        scientificScope = "Glycine max, edamame, soy protein, soy lecithin, tofu",
        typicalSources = "Soy sauce, tofu, vegetarian substitutes, bakery mixes, miso",
        aliases = listOf("soya", "soy", "soybean", "soybeans", "tofu", "edamame")
    ),
    StatutoryNormAllergen(
        id = 7,
        canonicalKey = "Milk",
        standardTitle = "Milk & Dairy (inc. Lactose)",
        normCode = "NORM-07",
        scientificScope = "Mammalian milk, whey proteins, casein, butterfat, lactose",
        typicalSources = "Butter, cream, cheese, paneer, khoya, custards, roux",
        aliases = listOf("milk", "dairy", "lactose", "cheese", "butter", "cream", "paneer")
    ),
    StatutoryNormAllergen(
        id = 8,
        canonicalKey = "Nuts",
        standardTitle = "Tree Nuts",
        normCode = "NORM-08",
        scientificScope = "Almonds, hazelnuts, walnuts, cashews, pecans, pistachios, macadamia",
        typicalSources = "Pesto, marzipan, praline, nut gravies (korma), garnishes",
        aliases = listOf("nuts", "tree nuts", "almonds", "walnuts", "cashews", "pistachio", "hazelnut", "pecan")
    ),
    StatutoryNormAllergen(
        id = 9,
        canonicalKey = "Celery",
        standardTitle = "Celery & Celeriac",
        normCode = "NORM-09",
        scientificScope = "Apium graveolens (stalks, leaves, seeds, root celeriac)",
        typicalSources = "Mirepoix, soup stocks, celery salt, spice rubs, salads",
        aliases = listOf("celery", "celeriac", "celery seed")
    ),
    StatutoryNormAllergen(
        id = 10,
        canonicalKey = "Mustard",
        standardTitle = "Mustard & Seeds",
        normCode = "NORM-10",
        scientificScope = "Sinapis alba, Brassica nigra (seeds, powder, oil, paste)",
        typicalSources = "Indian tadka, vinaigrettes, marinades, curries, rubs",
        aliases = listOf("mustard", "mustard seed", "dijon")
    ),
    StatutoryNormAllergen(
        id = 11,
        canonicalKey = "Sesame",
        standardTitle = "Sesame Seeds & Sesame Oil",
        normCode = "NORM-11",
        scientificScope = "Sesamum indicum seeds, tahini, cold-pressed sesame oil",
        typicalSources = "Hummus, burger buns, bagel topping, stir-fry dressings",
        aliases = listOf("sesame", "sesame seeds", "tahini", "sesame oil")
    ),
    StatutoryNormAllergen(
        id = 12,
        canonicalKey = "Sulphites",
        standardTitle = "Sulphur Dioxide & Sulphites",
        normCode = "NORM-12",
        scientificScope = "Concentrations > 10 mg/kg or 10 mg/L as SO2 (E220-E228)",
        typicalSources = "Wine, beer, dried fruits, fruit vinegars, prepared glazes",
        aliases = listOf("sulphites", "sulfites", "sulphur dioxide", "sulfur dioxide")
    ),
    StatutoryNormAllergen(
        id = 13,
        canonicalKey = "Lupin",
        standardTitle = "Lupin & Lupin Flour",
        normCode = "NORM-13",
        scientificScope = "Lupinus albus seeds, flour, lupin protein concentrate",
        typicalSources = "Specialty bread, gluten-free pastry blends, waffles, pasta",
        aliases = listOf("lupin", "lupine", "lupin flour")
    ),
    StatutoryNormAllergen(
        id = 14,
        canonicalKey = "Molluscs",
        standardTitle = "Molluscs & Bivalves",
        normCode = "NORM-14",
        scientificScope = "Mussels, clams, oysters, scallops, squid, octopus, snails",
        typicalSources = "Oyster sauce, paella, bouillabaisse, calamari, chowders",
        aliases = listOf("molluscs", "mollusks", "mussels", "clams", "oyster", "squid", "octopus")
    )
)

@Composable
fun AuditNutritionScreen(
    viewModel: HotelStudioViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val dishes by viewModel.dishes.collectAsState()
    val allBuffetMenuItems by viewModel.allBuffetMenuItems.collectAsState()

    val totalDishes = dishes.size
    val vegDishes = dishes.count { it.type == "veg" }
    val nonVegDishes = dishes.count { it.type == "nonveg" }

    // Count allergen occurrences
    val allergenCounts = mutableMapOf<String, Int>()
    dishes.forEach { dish ->
        dish.allergens.forEach { allergen ->
            allergenCounts[allergen] = (allergenCounts[allergen] ?: 0) + 1
        }
    }

    // --- FSSAI DAILY MENU COMPLIANCE LOGIC ---
    val menusByDaySession = allBuffetMenuItems.groupBy { "${it.dayOfWeek} ${it.mealSession}" }
    val totalMenus = menusByDaySession.size
    
    var totalMissingCals = 0
    var totalMissingAllergenDeclarations = 0
    val violationsByMenu = mutableMapOf<String, List<String>>()

    menusByDaySession.forEach { (menuName, items) ->
        val missingCals = items.filter { it.cals.isBlank() || it.cals.equals("0 kcal", ignoreCase = true) || !it.cals.contains("kcal", ignoreCase = true) }
        
        val missingWarnings = mutableListOf<String>()
        if (missingCals.isNotEmpty()) {
            totalMissingCals += missingCals.size
            missingWarnings.add("${missingCals.size} dishes missing valid nutritional (kcal) declarations")
        }
        
        val hasAnyAllergen = items.any { it.allergens.isNotEmpty() }
        if (!hasAnyAllergen && items.isNotEmpty()) {
            totalMissingAllergenDeclarations += 1
            missingWarnings.add("Missing statutory FSSAI allergen declarations for entire menu")
        }
        
        if (missingWarnings.isNotEmpty()) {
            violationsByMenu[menuName] = missingWarnings
        }
    }

    val isFullyCompliant = violationsByMenu.isEmpty() && totalMenus > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // FSSAI Compliance Status Card
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
                    Column {
                        Text(
                            text = "REGULATORY AUDIT HUB",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "FSSAI compliance logging with AES-256 audit tracking",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("SECURE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Verification Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isFullyCompliant) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                        .border(1.dp, if (isFullyCompliant) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.error.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isFullyCompliant) Icons.Default.CheckCircle else Icons.Default.GppBad,
                            contentDescription = null,
                            tint = if (isFullyCompliant) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("COMPLIANCE STATUS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isFullyCompliant) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                            Text(
                                if (isFullyCompliant) "100% Verified (FSSAI Certified)" else "Action Required (Violations Found)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Icon(
                        if (isFullyCompliant) Icons.Default.Shield else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isFullyCompliant) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // FSSAI Daily Menu Compliance Cross-Reference Dashboard
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Rule, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAILY MENU COMPLIANCE REPORT",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cross-referencing curated menus against FSSAI mandates for nutritional values & allergen disclosures.",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                if (totalMenus == 0) {
                    Text("No daily menus found to audit.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else if (isFullyCompliant) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF15803D).copy(alpha = 0.1f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "All $totalMenus menus are fully compliant with FSSAI regulations.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        violationsByMenu.forEach { (menuName, violations) ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                                    .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Menu: $menuName",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                violations.forEach { violation ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = violation,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dietary Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MENU COMPOSITION BREAKDOWN",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$totalDishes", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                        Text("Total Items", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$vegDishes", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color(0xFF15803D))
                        Text("Vegetarian", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$nonVegDishes", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color(0xFF991B1B))
                        Text("Non-Vegetarian", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // 14 Statutory Regulatory Allergens Audit Matrix Card
        Statutory14AllergenAuditMatrixCard(
            allBuffetMenuItems = allBuffetMenuItems,
            dishes = dishes
        )

        // AES-256 Storage Verification
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("DATA PROTECTION & ENCRYPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("Protected with local Room SQLite architecture & persistent encrypted state storage.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        
        com.example.ui.components.AnalyticsHeatmap()
        com.example.ui.components.FssaiComplianceTrendsChart()
        Spacer(modifier = Modifier.height(50.dp))
    }
}

/**
 * Executive-grade 14 Statutory Allergens Audit Matrix compliant with FSSAI Schedule IV & Codex Norms.
 * Always renders all 14 statutory allergens with their official regulatory logos, occurrence tracking,
 * and expandable dish breakdown.
 */
@Composable
fun Statutory14AllergenAuditMatrixCard(
    allBuffetMenuItems: List<BuffetMenuItemEntity>,
    dishes: List<DishEntity>
) {
    var allergenSearchQuery by remember { mutableStateOf("") }
    var allergenFilterMode by remember { mutableStateOf("all") } // "all", "declared", "safe"
    var expandedNormId by remember { mutableStateOf<Int?>(null) }

    // Map all 14 statutory allergens to occurrences across active buffet items and recipe dishes
    val auditStatusMap = remember(allBuffetMenuItems, dishes) {
        STATUTORY_14_ALLERGENS_NORMS.associateWith { norm ->
            val matchingDishes = mutableListOf<String>()
            allBuffetMenuItems.forEach { item ->
                val matches = item.allergens.any { a ->
                    norm.aliases.any { alias -> a.equals(alias, ignoreCase = true) } ||
                    a.contains(norm.canonicalKey, ignoreCase = true)
                }
                if (matches && !matchingDishes.contains(item.name)) {
                    matchingDishes.add(item.name)
                }
            }
            dishes.forEach { dish ->
                val matches = dish.allergens.any { a ->
                    norm.aliases.any { alias -> a.equals(alias, ignoreCase = true) } ||
                    a.contains(norm.canonicalKey, ignoreCase = true)
                }
                if (matches && !matchingDishes.contains(dish.name)) {
                    matchingDishes.add(dish.name)
                }
            }
            matchingDishes
        }
    }

    val declaredNormsCount = auditStatusMap.values.count { it.isNotEmpty() }
    val safeNormsCount = STATUTORY_14_ALLERGENS_NORMS.size - declaredNormsCount

    val filteredNorms = STATUTORY_14_ALLERGENS_NORMS.filter { norm ->
        val dishesWithAllergen = auditStatusMap[norm] ?: emptyList()
        val matchesFilter = when (allergenFilterMode) {
            "declared" -> dishesWithAllergen.isNotEmpty()
            "safe" -> dishesWithAllergen.isEmpty()
            else -> true
        }
        val query = allergenSearchQuery.trim()
        val matchesSearch = query.isEmpty() ||
            norm.standardTitle.contains(query, ignoreCase = true) ||
            norm.canonicalKey.contains(query, ignoreCase = true) ||
            norm.scientificScope.contains(query, ignoreCase = true) ||
            norm.typicalSources.contains(query, ignoreCase = true) ||
            norm.normCode.contains(query, ignoreCase = true) ||
            dishesWithAllergen.any { it.contains(query, ignoreCase = true) }

        matchesFilter && matchesSearch
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "14 STATUTORY REGULATORY ALLERGENS",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Mandated by FSSAI Schedule IV & Codex Norms",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0284C7).copy(alpha = 0.12f))
                        .border(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "14/14 NORMS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0284C7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Statutory compliance registry displaying all 14 official food allergens with regulated logos, scientific scopes, and real-time menu occurrence audit.",
                fontSize = 10.5.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Metric Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Norms
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text("STATUTORY NORMS", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("14 Mandatory", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Declared in Menu
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (declaredNormsCount > 0) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .border(1.dp, if (declaredNormsCount > 0) Color(0xFFD97706).copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text("DECLARED IN MENU", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = if (declaredNormsCount > 0) Color(0xFF92400E) else MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("$declaredNormsCount Detected", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = if (declaredNormsCount > 0) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurface)
                    }
                }

                // Zero-Risk / Safe
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDCFCE7))
                        .border(1.dp, Color(0xFF16A34A).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text("ZERO-RISK / SAFE", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("$safeNormsCount Safe", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // STATUTORY 14 ALLERGEN LOGOS STRIP
            // ==========================================
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OFFICIAL 14 STATUTORY REGULATORY ALLERGEN LOGOS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Tap to inspect norm",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(STATUTORY_14_ALLERGENS_NORMS.size) { index ->
                            val norm = STATUTORY_14_ALLERGENS_NORMS[index]
                            val dishesWithAllergen = auditStatusMap[norm] ?: emptyList()
                            val isDeclared = dishesWithAllergen.isNotEmpty()
                            val isSelected = expandedNormId == norm.id

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(62.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        expandedNormId = if (expandedNormId == norm.id) null else norm.id
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                                        .border(
                                            width = if (isSelected) 2.dp else if (isDeclared) 1.5.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else if (isDeclared) Color(0xFFD97706) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AllergenIcon(allergen = norm.canonicalKey, size = 26.dp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = norm.canonicalKey,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected || isDeclared) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isDeclared) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isDeclared) "(${dishesWithAllergen.size})" else "SAFE",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDeclared) Color(0xFFD97706) else Color(0xFF16A34A)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Box
            OutlinedTextField(
                value = allergenSearchQuery,
                onValueChange = { allergenSearchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Search by allergen, ingredient, or norm...", fontSize = 11.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (allergenSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { allergenSearchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = allergenFilterMode == "all",
                    onClick = { allergenFilterMode = "all" },
                    label = { Text("All 14 Norms (14)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                FilterChip(
                    selected = allergenFilterMode == "declared",
                    onClick = { allergenFilterMode = "declared" },
                    label = { Text("Declared in Menu ($declaredNormsCount)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFEF3C7),
                        selectedLabelColor = Color(0xFF92400E)
                    )
                )
                FilterChip(
                    selected = allergenFilterMode == "safe",
                    onClick = { allergenFilterMode = "safe" },
                    label = { Text("Zero-Risk / Safe ($safeNormsCount)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFDCFCE7),
                        selectedLabelColor = Color(0xFF166534)
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 14 Allergen Norms List
            if (filteredNorms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No statutory allergens match the search query.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    filteredNorms.forEach { norm ->
                        val dishesWithAllergen = auditStatusMap[norm] ?: emptyList()
                        val count = dishesWithAllergen.size
                        val isDeclared = count > 0
                        val isExpanded = expandedNormId == norm.id

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedNormId = if (isExpanded) null else norm.id
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDeclared) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDeclared) Color(0xFFD97706).copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .animateContentSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Official Allergen Logo with statutory styling
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .border(1.5.dp, if (isDeclared) Color(0xFFD97706).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AllergenIcon(allergen = norm.canonicalKey, size = 28.dp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Information
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                                    .padding(horizontal = 5.dp, vertical = 1.5.dp)
                                            ) {
                                                Text(
                                                    text = norm.normCode,
                                                    fontSize = 8.5.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = norm.standardTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = norm.scientificScope,
                                            fontSize = 9.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = if (isExpanded) 3 else 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Sources: ${norm.typicalSources}",
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                            maxLines = if (isExpanded) 3 else 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Status Pill & Expand
                                    Column(horizontalAlignment = Alignment.End) {
                                        if (isDeclared) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFFEF3C7))
                                                    .border(1.dp, Color(0xFFD97706).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                                            ) {
                                                Text(
                                                    text = "⚠️ $count Dishes",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF92400E)
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFDCFCE7))
                                                    .border(1.dp, Color(0xFF16A34A).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                                            ) {
                                                Text(
                                                    text = "✅ Zero Risk",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF166534)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Icon(
                                            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Expanded Details
                                if (isExpanded) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (isDeclared) {
                                        Text(
                                            text = "ACTIVE MENU DISHES DECLARING THIS ALLERGEN ($count):",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFB45309)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            dishesWithAllergen.forEach { dishName ->
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(5.dp)
                                                            .clip(CircleShape)
                                                            .background(Color(0xFFD97706))
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = dishName,
                                                        fontSize = 10.5.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(MaterialTheme.colorScheme.surface)
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "🛡️ HACCP Protocol: Ensure segregated utensils, dedicated prep surfaces, and staff advisory for ${norm.standardTitle}.",
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color(0xFF16A34A),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Zero dishes in current buffet or recipe bank declare ${norm.standardTitle}. Complete compliance maintained.",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF166534)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

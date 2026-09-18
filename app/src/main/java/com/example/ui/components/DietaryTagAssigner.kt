package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FssaiRegulatoryMark

/**
 * Categorization for dietary tags and allergen indications.
 */
enum class DietaryTagCategory(val label: String, val iconDescription: String) {
    ALL("All", "All available tags"),
    FREE_FROM("Free-From", "Allergen-free and safeguard claims"),
    LIFESTYLE("Lifestyles", "Dietary lifestyle preferences"),
    ALLERGENS("Contains Allergens", "FSSAI and regulatory allergen warnings"),
    WELLNESS("Health & Wellness", "Nutritional and health claims")
}

/**
 * Definition of a dietary tag.
 */
data class DietaryTagInfo(
    val name: String,
    val category: DietaryTagCategory,
    val description: String,
    val isPositiveClaim: Boolean = true,
    val badgeBg: Color,
    val badgeText: Color,
    val badgeBorder: Color
)

/**
 * Canonical registry of dietary tags.
 */
val STANDARD_DIETARY_TAGS: List<DietaryTagInfo> = listOf(
    // Free-From / Hypoallergenic
    DietaryTagInfo(
        name = "Allergen-Free",
        category = DietaryTagCategory.FREE_FROM,
        description = "Safeguarded against top 14 common food allergens",
        isPositiveClaim = true,
        badgeBg = Color(0xFFD1FAE5),
        badgeText = Color(0xFF065F46),
        badgeBorder = Color(0xFF10B981)
    ),
    DietaryTagInfo(
        name = "Gluten-Free",
        category = DietaryTagCategory.FREE_FROM,
        description = "Contains zero gluten, wheat, barley, or rye",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFEF3C7),
        badgeText = Color(0xFF92400E),
        badgeBorder = Color(0xFFF59E0B)
    ),
    DietaryTagInfo(
        name = "Dairy-Free",
        category = DietaryTagCategory.FREE_FROM,
        description = "Contains no dairy, lactose, or milk derivatives",
        isPositiveClaim = true,
        badgeBg = Color(0xFFE0F2FE),
        badgeText = Color(0xFF075985),
        badgeBorder = Color(0xFF38BDF8)
    ),
    DietaryTagInfo(
        name = "Nut-Free",
        category = DietaryTagCategory.FREE_FROM,
        description = "Free from peanuts and tree nuts",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFFEDD5),
        badgeText = Color(0xFF9A3412),
        badgeBorder = Color(0xFFFB923C)
    ),
    DietaryTagInfo(
        name = "Eggless",
        category = DietaryTagCategory.FREE_FROM,
        description = "Prepared without eggs or egg derivatives",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFEF9C3),
        badgeText = Color(0xFF854D0E),
        badgeBorder = Color(0xFFEAB308)
    ),

    // Lifestyles
    DietaryTagInfo(
        name = "Vegan",
        category = DietaryTagCategory.LIFESTYLE,
        description = "100% plant-based with zero animal products",
        isPositiveClaim = true,
        badgeBg = Color(0xFFDCFCE7),
        badgeText = Color(0xFF166534),
        badgeBorder = Color(0xFF22C55E)
    ),
    DietaryTagInfo(
        name = "Vegetarian",
        category = DietaryTagCategory.LIFESTYLE,
        description = "Pure vegetarian (Lacto-vegetarian)",
        isPositiveClaim = true,
        badgeBg = Color(0xFFE8F5E9),
        badgeText = Color(0xFF1B5E20),
        badgeBorder = Color(0xFF4CAF50)
    ),
    DietaryTagInfo(
        name = "Jain",
        category = DietaryTagCategory.LIFESTYLE,
        description = "No onion, garlic, or underground root vegetables",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFFF3E0),
        badgeText = Color(0xFFE65100),
        badgeBorder = Color(0xFFFF9800)
    ),
    DietaryTagInfo(
        name = "Halal",
        category = DietaryTagCategory.LIFESTYLE,
        description = "Prepared according to Islamic dietary requirements",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFEF08A),
        badgeText = Color(0xFF713F12),
        badgeBorder = Color(0xFFCA8A04)
    ),
    DietaryTagInfo(
        name = "Kosher",
        category = DietaryTagCategory.LIFESTYLE,
        description = "Prepared in accordance with Jewish dietary law",
        isPositiveClaim = true,
        badgeBg = Color(0xFFDBEAFE),
        badgeText = Color(0xFF1E40AF),
        badgeBorder = Color(0xFF3B82F6)
    ),

    // Health & Wellness
    DietaryTagInfo(
        name = "Sugar-Free",
        category = DietaryTagCategory.WELLNESS,
        description = "No added sugars; diabetic friendly",
        isPositiveClaim = true,
        badgeBg = Color(0xFFFFE4E6),
        badgeText = Color(0xFF9F1239),
        badgeBorder = Color(0xFFFB7185)
    ),
    DietaryTagInfo(
        name = "Keto / Low-Carb",
        category = DietaryTagCategory.WELLNESS,
        description = "Low carbohydrate formulation",
        isPositiveClaim = true,
        badgeBg = Color(0xFFEDE9FE),
        badgeText = Color(0xFF5B21B6),
        badgeBorder = Color(0xFF8B5CF6)
    ),
    DietaryTagInfo(
        name = "Organic",
        category = DietaryTagCategory.WELLNESS,
        description = "Certified organic and natural ingredients",
        isPositiveClaim = true,
        badgeBg = Color(0xFFCCFBF1),
        badgeText = Color(0xFF115E59),
        badgeBorder = Color(0xFF14B8A6)
    ),
    DietaryTagInfo(
        name = "High-Protein",
        category = DietaryTagCategory.WELLNESS,
        description = "Rich in dietary protein for athletic and active guests",
        isPositiveClaim = true,
        badgeBg = Color(0xFFE0E7FF),
        badgeText = Color(0xFF3730A3),
        badgeBorder = Color(0xFF6366F1)
    ),
    DietaryTagInfo(
        name = "Low-Sodium",
        category = DietaryTagCategory.WELLNESS,
        description = "Heart-friendly recipe with reduced salt",
        isPositiveClaim = true,
        badgeBg = Color(0xFFF1F5F9),
        badgeText = Color(0xFF334155),
        badgeBorder = Color(0xFF94A3B8)
    ),

    // Regulatory Allergens (FSSAI 14)
    DietaryTagInfo(
        name = "Gluten",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Wheat, Barley, Oats or Rye",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFFFBEB),
        badgeText = Color(0xFFB45309),
        badgeBorder = Color(0xFFFCD34D)
    ),
    DietaryTagInfo(
        name = "Milk",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Milk, Cream, Butter, Cheese or Lactose",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF0F9FF),
        badgeText = Color(0xFF0369A1),
        badgeBorder = Color(0xFFBAE6FD)
    ),
    DietaryTagInfo(
        name = "Peanuts",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Peanuts or Peanut Derivatives",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFFF7ED),
        badgeText = Color(0xFFC2410C),
        badgeBorder = Color(0xFFFDBA74)
    ),
    DietaryTagInfo(
        name = "Nuts",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Tree Nuts (Almonds, Cashews, Walnuts, Pistachios)",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFFF7ED),
        badgeText = Color(0xFF9A3412),
        badgeBorder = Color(0xFFFDBA74)
    ),
    DietaryTagInfo(
        name = "Eggs",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Eggs or Egg Derivatives",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFEFCE8),
        badgeText = Color(0xFFA16207),
        badgeBorder = Color(0xFFFDE047)
    ),
    DietaryTagInfo(
        name = "Soya",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Soybeans, Tofu or Soya Sauce",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF7FEE7),
        badgeText = Color(0xFF4D7C0F),
        badgeBorder = Color(0xFFBEF264)
    ),
    DietaryTagInfo(
        name = "Fish",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Fish or Fish Extracts",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFFF7ED),
        badgeText = Color(0xFFEA580C),
        badgeBorder = Color(0xFFFDBA74)
    ),
    DietaryTagInfo(
        name = "Crustaceans",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Prawns, Shrimp, Crab, Lobster",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFFF1F2),
        badgeText = Color(0xFFBE123C),
        badgeBorder = Color(0xFFFDA4AF)
    ),
    DietaryTagInfo(
        name = "Molluscs",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Oysters, Clams, Squid, Mussels",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF0FDF4),
        badgeText = Color(0xFF0F766E),
        badgeBorder = Color(0xFF99F6E4)
    ),
    DietaryTagInfo(
        name = "Mustard",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Mustard seeds, paste or oil",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFEFCE8),
        badgeText = Color(0xFFA16207),
        badgeBorder = Color(0xFFFDE047)
    ),
    DietaryTagInfo(
        name = "Sesame",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Sesame seeds or Sesame oil (Til)",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF8FAFC),
        badgeText = Color(0xFF334155),
        badgeBorder = Color(0xFFCBD5E1)
    ),
    DietaryTagInfo(
        name = "Celery",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Celery stalks, leaves or seeds",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF0FDF4),
        badgeText = Color(0xFF15803D),
        badgeBorder = Color(0xFF86EFAC)
    ),
    DietaryTagInfo(
        name = "Sulphites",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Sulphur Dioxide or Sulphites",
        isPositiveClaim = false,
        badgeBg = Color(0xFFF0FDFA),
        badgeText = Color(0xFF0F766E),
        badgeBorder = Color(0xFF5EEAD4)
    ),
    DietaryTagInfo(
        name = "Lupin",
        category = DietaryTagCategory.ALLERGENS,
        description = "Contains Lupin flour or seeds",
        isPositiveClaim = false,
        badgeBg = Color(0xFFFAF5FF),
        badgeText = Color(0xFF7E22CE),
        badgeBorder = Color(0xFFD8B4FE)
    )
)

/**
 * Registry helper for dietary tag resolution and categorization.
 */
object DietaryTagRegistry {
    fun findTag(name: String): DietaryTagInfo? {
        val normalized = name.trim().lowercase().replace("-", "").replace(" ", "")
        return STANDARD_DIETARY_TAGS.firstOrNull {
            val tagNorm = it.name.trim().lowercase().replace("-", "").replace(" ", "")
            tagNorm == normalized || it.name.equals(name.trim(), ignoreCase = true)
        }
    }

    fun getAllTagsByCategory(): Map<DietaryTagCategory, List<DietaryTagInfo>> {
        return STANDARD_DIETARY_TAGS.groupBy { it.category }
    }
}

/**
 * Returns metadata for any dietary tag name, creating a clean fallback if not pre-registered.
 */
fun getDietaryTagInfo(tagName: String): DietaryTagInfo {
    val clean = tagName.trim()
    val match = STANDARD_DIETARY_TAGS.firstOrNull { it.name.equals(clean, ignoreCase = true) }
    if (match != null) return match

    val isFreeFrom = clean.contains("free", ignoreCase = true) || clean.contains("safe", ignoreCase = true)
    return DietaryTagInfo(
        name = clean,
        category = DietaryTagCategory.LIFESTYLE,
        description = "Kitchen custom dietary tag: $clean",
        isPositiveClaim = isFreeFrom,
        badgeBg = if (isFreeFrom) Color(0xFFD1FAE5) else Color(0xFFF1F5F9),
        badgeText = if (isFreeFrom) Color(0xFF065F46) else Color(0xFF1E293B),
        badgeBorder = if (isFreeFrom) Color(0xFF10B981) else Color(0xFF94A3B8)
    )
}

/**
 * Renders a single dietary tag badge with icon and styled container.
 */
@Composable
fun DietaryBadge(
    tagName: String,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    iconSize: Dp = if (isCompact) 13.dp else 16.dp,
    fontSize: Float = if (isCompact) 8.5f else 11.5f
) {
    val info = remember(tagName) { getDietaryTagInfo(tagName) }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = info.badgeBg,
        border = BorderStroke(1.dp, info.badgeBorder.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (isCompact) 5.dp else 8.dp,
                vertical = if (isCompact) 2.dp else 4.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AllergenIcon(
                allergen = info.name,
                size = iconSize
            )
            Spacer(modifier = Modifier.width(if (isCompact) 3.dp else 5.dp))
            Text(
                text = info.name.uppercase(),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = info.badgeText,
                letterSpacing = 0.4.sp,
                maxLines = 1
            )
        }
    }
}

/**
 * Displays a responsive flow row of dietary tags on digital buffet tags.
 * Prominently presents positive dietary claims (Allergen-Free, Vegan, Gluten-Free, etc.)
 * first, followed by specific allergen warnings.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietaryBadgeRow(
    tags: List<String>,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    maxTags: Int = Int.MAX_VALUE
) {
    if (tags.isEmpty()) return

    val sortedTags = remember(tags) {
        tags.distinct().sortedWith(compareByDescending<String> {
            val info = getDietaryTagInfo(it)
            // Prioritize Allergen-Free, Vegan, Gluten-Free, etc.
            if (info.name.equals("Allergen-Free", ignoreCase = true)) 100
            else if (info.isPositiveClaim) 50
            else 10
        })
    }

    val visibleTags = sortedTags.take(maxTags)
    val remainingCount = sortedTags.size - visibleTags.size

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(if (isCompact) 4.dp else 6.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 4.dp else 6.dp)
    ) {
        visibleTags.forEach { tag ->
            DietaryBadge(tagName = tag, isCompact = isCompact)
        }

        if (remainingCount > 0) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "+$remainingCount MORE",
                    fontSize = if (isCompact) 8.sp else 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

/**
 * Interactive UI component for assigning dietary tags (e.g. Allergen-free, Vegan, Gluten-free)
 * to menu items for display on digital buffet tags.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DietaryTagAssigner(
    selectedTags: List<String>,
    onTagsChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    dishName: String = "Paneer Tikka Angara",
    dishCourse: String = "Starters & Live Counter",
    isVeg: Boolean = true,
    showBuffetTagPreview: Boolean = true
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(DietaryTagCategory.ALL) }
    var customTagInput by remember { mutableStateOf("") }

    val currentSelected = remember(selectedTags) { selectedTags.toSet() }

    // Filter tags based on selected category and search
    val filteredTags = remember(searchQuery, selectedCategory) {
        STANDARD_DIETARY_TAGS.filter { tag ->
            val matchesCategory = (selectedCategory == DietaryTagCategory.ALL) || (tag.category == selectedCategory)
            val matchesSearch = searchQuery.isBlank() ||
                    tag.name.contains(searchQuery, ignoreCase = true) ||
                    tag.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    // Check if custom tag can be added
    val canAddCustomTag = customTagInput.trim().isNotEmpty() &&
            !currentSelected.contains(customTagInput.trim())

    fun toggleTag(tagName: String) {
        val clean = tagName.trim()
        val newSet = if (currentSelected.contains(clean)) {
            currentSelected - clean
        } else {
            currentSelected + clean
        }
        onTagsChanged(newSet.toList())
    }

    fun applyPreset(presetTags: List<String>, removeConflictingAllergens: Boolean = false) {
        var updated = currentSelected + presetTags
        if (removeConflictingAllergens) {
            // Remove contains allergens flags
            val allergenNames = STANDARD_DIETARY_TAGS
                .filter { it.category == DietaryTagCategory.ALLERGENS }
                .map { it.name }
                .toSet()
            updated = updated - allergenNames
        }
        onTagsChanged(updated.toList())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- 1. COMPONENT HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Dietary Safeguards",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Dietary & Allergen Tags",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Assign tags for digital buffet tags & guest safety",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Counter Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (currentSelected.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "${currentSelected.size} Selected",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (currentSelected.isNotEmpty()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // --- 2. QUICK SMART PRESETS ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "SMART PRESETS",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Preset: Allergen-Free (Hypoallergenic)
                PresetActionChip(
                    title = "Allergen-Free Shield",
                    icon = "🛡️",
                    subtitle = "Safe from top 14 allergens",
                    color = Color(0xFF059669),
                    onClick = {
                        applyPreset(
                            presetTags = listOf("Allergen-Free", "Nut-Free", "Gluten-Free", "Dairy-Free"),
                            removeConflictingAllergens = true
                        )
                    }
                )

                // Preset: 100% Vegan
                PresetActionChip(
                    title = "100% Vegan",
                    icon = "🌱",
                    subtitle = "Plant-based + Dairy-free",
                    color = Color(0xFF16A34A),
                    onClick = {
                        applyPreset(
                            presetTags = listOf("Vegan", "Dairy-Free", "Eggless")
                        )
                    }
                )

                // Preset: Gluten-Free
                PresetActionChip(
                    title = "Gluten-Free",
                    icon = "🌾🚫",
                    subtitle = "Celiac safe claim",
                    color = Color(0xFFD97706),
                    onClick = {
                        val withoutGluten = currentSelected - "Gluten"
                        onTagsChanged((withoutGluten + "Gluten-Free").toList())
                    }
                )

                // Preset: Jain Dining
                PresetActionChip(
                    title = "Jain Friendly",
                    icon = "🪷",
                    subtitle = "Zero root veg + pure veg",
                    color = Color(0xFFEA580C),
                    onClick = {
                        applyPreset(
                            presetTags = listOf("Jain", "Vegetarian", "Eggless")
                        )
                    }
                )

                if (currentSelected.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { onTagsChanged(emptyList()) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .height(38.dp)
                            .minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear All", modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Clear All", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- 3. ACTIVE ASSIGNED TAGS SUMMARY ROW ---
        if (currentSelected.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASSIGNED TO BUFFET TAG (${currentSelected.size})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Tap tag to remove",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        currentSelected.forEach { tag ->
                            val info = getDietaryTagInfo(tag)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = info.badgeBg,
                                border = BorderStroke(1.dp, info.badgeBorder),
                                modifier = Modifier
                                    .clickable { toggleTag(tag) }
                                    .minimumInteractiveComponentSize()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AllergenIcon(allergen = tag, size = 15.dp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tag,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = info.badgeText
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove $tag",
                                        tint = info.badgeText.copy(alpha = 0.7f),
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. SEARCH & CATEGORY FILTER TABS ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_search_dietary_tags"),
                placeholder = { Text("Search tags: Vegan, Gluten-Free, Allergen-Free...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // Category Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DietaryTagCategory.values().forEach { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                }
            }
        }

        // --- 5. TAG SELECTION GRID ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AVAILABLE TAGS (${filteredTags.size})",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Tap to toggle",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredTags.forEach { tagInfo ->
                    val isChecked = currentSelected.contains(tagInfo.name)
                    DietaryTagSelectionChip(
                        tagInfo = tagInfo,
                        isSelected = isChecked,
                        onToggle = { toggleTag(tagInfo.name) }
                    )
                }
            }
        }

        // --- 6. CUSTOM TAG INPUT ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "ADD CUSTOM DIETARY TAG",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customTagInput,
                    onValueChange = { customTagInput = it },
                    placeholder = { Text("e.g. Zero Oil, Kid Friendly, Low Calorie", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_custom_dietary_tag"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (canAddCustomTag) {
                                toggleTag(customTagInput.trim())
                                customTagInput = ""
                            }
                        }
                    )
                )

                Button(
                    onClick = {
                        if (canAddCustomTag) {
                            toggleTag(customTagInput.trim())
                            customTagInput = ""
                        }
                    },
                    enabled = canAddCustomTag,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("btn_add_custom_dietary_tag")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Tag", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- 7. LIVE DIGITAL BUFFET TAG PREVIEW ---
        if (showBuffetTagPreview) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE DIGITAL BUFFET TAG PREVIEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "Display at Buffet Station",
                        fontSize = 9.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DigitalBuffetTagSimulationCard(
                    dishName = dishName,
                    course = dishCourse,
                    isVeg = isVeg,
                    tags = currentSelected.toList()
                )
            }
        }
    }
}

/**
 * Interactive Selection Chip for a Dietary Tag with accessibility and touch target.
 */
@Composable
fun DietaryTagSelectionChip(
    tagInfo: DietaryTagInfo,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) tagInfo.badgeBg else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) tagInfo.badgeBorder else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        label = "chipBorder"
    )
    val textColor = if (isSelected) tagInfo.badgeText else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("tag_chip_${tagInfo.name.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AllergenIcon(
                allergen = tagInfo.name,
                size = 18.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tagInfo.name,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = textColor
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = tagInfo.badgeBorder,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Text(
                    text = tagInfo.category.label,
                    fontSize = 9.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Preset action card button.
 */
@Composable
private fun PresetActionChip(
    title: String,
    icon: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = Modifier
            .height(48.dp)
            .minimumInteractiveComponentSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = subtitle,
                    fontSize = 8.5.sp,
                    color = color.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * Simulation Card showing how the tags will appear on a live digital buffet tag display.
 */
@Composable
fun DigitalBuffetTagSimulationCard(
    dishName: String,
    course: String,
    isVeg: Boolean,
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FssaiRegulatoryMark(isVeg = isVeg, size = 14.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = course.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "☁️ ESL READY",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HOTEL STUDIO DIGITAL BUFFET",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Dish Title
            Text(
                text = dishName,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF0F172A),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dietary & Allergen Badges Section
            if (tags.isNotEmpty()) {
                Text(
                    text = "DIETARY SAFEGUARDS & ALLERGENS:",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                DietaryBadgeRow(
                    tags = tags,
                    isCompact = true
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(
                        text = "No dietary tags assigned yet. Tap tags above to display on this tag.",
                        fontSize = 9.5.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Disclaimer & QR note
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Please inform service staff for any dietary requirements.",
                    fontSize = 7.5.sp,
                    color = Color(0xFF94A3B8),
                    fontFamily = FontFamily.SansSerif
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.QrCode2,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Scan QR",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

/**
 * Full Dialog container for assigning dietary tags to a menu item.
 */
@Composable
fun DietaryTagAssignerDialog(
    initialTags: List<String>,
    dishName: String,
    course: String = "Main Course",
    isVeg: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var workingTags by remember { mutableStateOf(initialTags) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .shadow(16.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp)),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Assign Dietary Tags",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = dishName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Scrollable Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    DietaryTagAssigner(
                        selectedTags = workingTags,
                        onTagsChanged = { workingTags = it },
                        dishName = dishName,
                        dishCourse = course,
                        isVeg = isVeg,
                        showBuffetTagPreview = true
                    )
                }

                // Bottom Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .minimumInteractiveComponentSize()
                        ) {
                            Text("Cancel", fontSize = 13.sp)
                        }

                        Button(
                            onClick = { onConfirm(workingTags) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("btn_save_dietary_tags")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Apply to Buffet Tag (${workingTags.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modal Bottom Sheet container for assigning dietary tags.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietaryTagAssignerSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    initialTags: List<String>,
    dishName: String,
    course: String = "Main Course",
    isVeg: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var workingTags by remember { mutableStateOf(initialTags) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Assign Dietary Tags",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = dishName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { onConfirm(workingTags) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Apply", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DietaryTagAssigner(
                selectedTags = workingTags,
                onTagsChanged = { workingTags = it },
                dishName = dishName,
                dishCourse = course,
                isVeg = isVeg,
                showBuffetTagPreview = true
            )
        }
    }
}

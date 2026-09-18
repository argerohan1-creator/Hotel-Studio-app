package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.RecipeEntity

data class FssaiRuleViolation(
    val triggerIngredient: String,
    val ruleDescription: String,
    val severity: ViolationSeverity
)

enum class ViolationSeverity {
    WARNING, MANDATORY_DECLARATION, BANNED
}

@Composable
fun FssaiAuditDialog(
    item: BuffetMenuItemEntity,
    recipe: RecipeEntity?,
    onDismiss: () -> Unit
) {
    val violations = performFssaiAudit(recipe, item.allergens)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Audit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FSSAI Compliance Audit",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Item: ${item.name}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (recipe == null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No linked recipe ingredients found to audit.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (violations.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7)) // Light Green
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF166534), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No apparent FSSAI regulatory violations detected in the ingredient list.",
                            color = Color(0xFF166534),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Detected Regulatory Flags:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(violations) { violation ->
                            FssaiViolationCard(violation)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Disclaimer: This automated audit relies on keyword matching against the recipe's ingredient list and is not a substitute for official legal/food safety compliance reviews.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FssaiViolationCard(violation: FssaiRuleViolation) {
    val (bgColor, contentColor, icon) = when (violation.severity) {
        ViolationSeverity.WARNING -> Triple(Color(0xFFFEF3C7), Color(0xFF92400E), Icons.Default.Warning)
        ViolationSeverity.MANDATORY_DECLARATION -> Triple(Color(0xFFFEE2E2), Color(0xFF991B1B), Icons.Default.Info)
        ViolationSeverity.BANNED -> Triple(Color(0xFFFFE4E6), Color(0xFFBE123C), Icons.Default.GppBad)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, contentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Flagged Ingredient: ${violation.triggerIngredient}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = violation.ruleDescription,
                fontSize = 11.sp,
                color = contentColor.copy(alpha = 0.9f)
            )
        }
    }
}

fun performFssaiAudit(recipe: RecipeEntity?, allergens: List<String>): List<FssaiRuleViolation> {
    if (recipe == null) return emptyList()
    val violations = mutableListOf<FssaiRuleViolation>()
    
    val ingredientsStr = recipe.ingredients.joinToString(" ").lowercase()

    // MSG Check
    if (ingredientsStr.contains("msg") || ingredientsStr.contains("monosodium glutamate") || ingredientsStr.contains("ajinomoto")) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Monosodium Glutamate (MSG)",
                ruleDescription = "FSSAI mandates explicit declaration: 'CONTAINS ADDED MONOSODIUM GLUTAMATE. NOT RECOMMENDED FOR INFANTS BELOW 12 MONTHS'. Must not be added to pastas, noodles, or freshly cut fruits.",
                severity = ViolationSeverity.MANDATORY_DECLARATION
            )
        )
    }
    
    // Synthetic Colors Check
    val syntheticColors = listOf("tartrazine", "yellow 5", "red 40", "sunset yellow", "carmoisine", "ponceau", "allura red", "brilliant blue", "synthetic color", "food color")
    val foundColor = syntheticColors.firstOrNull { ingredientsStr.contains(it) }
    if (foundColor != null) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Synthetic Color ($foundColor)",
                ruleDescription = "FSSAI requires declaration: 'CONTAINS PERMITTED SYNTHETIC FOOD COLOUR(S)'. Restricted to maximum limits (typically 100 ppm) in specific food categories.",
                severity = ViolationSeverity.WARNING
            )
        )
    }
    
    // Artificial Sweeteners Check
    val artificialSweeteners = listOf("aspartame", "sucralose", "saccharin", "neotame", "stevia", "erythritol", "artificial sweetener")
    val foundSweetener = artificialSweeteners.firstOrNull { ingredientsStr.contains(it) }
    if (foundSweetener != null) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Artificial Sweetener ($foundSweetener)",
                ruleDescription = "FSSAI requires declaration: 'CONTAINS NON-CALORIC SWEETENER'. If Aspartame, must add 'NOT RECOMMENDED FOR PHENYLKETONURICS'.",
                severity = ViolationSeverity.MANDATORY_DECLARATION
            )
        )
    }

    // Caffeine Check
    if (ingredientsStr.contains("caffeine") || ingredientsStr.contains("guarana") || ingredientsStr.contains("energy blend")) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "High Caffeine content",
                ruleDescription = "FSSAI requires declaration: 'CONTAINS CAFFEINE'. If over 145mg/kg, must add 'HIGH CAFFEINE: NOT RECOMMENDED FOR CHILDREN, PREGNANT OR LACTATING WOMEN'.",
                severity = ViolationSeverity.MANDATORY_DECLARATION
            )
        )
    }
    
    // Trans Fat Check
    val transFats = listOf("vanaspati", "margarine", "hydrogenated vegetable oil", "partially hydrogenated oil", "dalda")
    val foundTransFat = transFats.firstOrNull { ingredientsStr.contains(it) }
    if (foundTransFat != null) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Hydrogenated Fat ($foundTransFat)",
                ruleDescription = "FSSAI limits industrial trans fats to not more than 2% by mass of the total oils/fats present in the product.",
                severity = ViolationSeverity.WARNING
            )
        )
    }
    
    // Banned Additives Check (Example)
    val banned = listOf("potassium bromate", "rhodamine", "metanil yellow", "malachite green")
    val foundBanned = banned.firstOrNull { ingredientsStr.contains(it) }
    if (foundBanned != null) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Banned Substance ($foundBanned)",
                ruleDescription = "Strictly prohibited by FSSAI for use in food preparations.",
                severity = ViolationSeverity.BANNED
            )
        )
    }

    // Allergen generic check
    if (allergens.isNotEmpty()) {
        violations.add(
            FssaiRuleViolation(
                triggerIngredient = "Allergens (${allergens.joinToString()})",
                ruleDescription = "FSSAI mandates prominent declaration of all major allergens on the menu board or packaging.",
                severity = ViolationSeverity.MANDATORY_DECLARATION
            )
        )
    }

    return violations
}

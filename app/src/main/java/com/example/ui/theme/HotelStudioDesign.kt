package com.example.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ==========================================
// 3 LUXURY APP THEMES
// ==========================================

val MidnightDarkColorScheme = darkColorScheme(
    primary = Color(0xFF10B981), // Emerald 500
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF064E3B), // Emerald 900
    onPrimaryContainer = Color(0xFFA7F3D0), // Emerald 200
    secondary = Color(0xFF34D399), // Emerald 400
    onSecondary = Color(0xFF090D16),
    background = Color(0xFF090D16), // Dark Obsidian
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF111827), // Slate 900
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0x6610B981)
)

val TajGoldColorScheme = lightColorScheme(
    primary = Color(0xFF5C3E14), // Deep Royal Bronze (High-Contrast Taj / Oberoi, WCAG AAA 8.5:1)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF3E7D3),
    onPrimaryContainer = Color(0xFF281905),
    secondary = Color(0xFF6E4E1C), // Deep Antique Gold
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAF8F5), // Warm 300-thread Linen
    onBackground = Color(0xFF17120C), // High-Contrast Deep Caviar Espresso
    surface = Color(0xFFFFFFFF), // Crisp Fine China
    onSurface = Color(0xFF17120C), // High-Contrast Deep Caviar Espresso
    surfaceVariant = Color(0xFFF1EAE0), // Warm Parchment Card
    onSurfaceVariant = Color(0xFF2D241B), // Sharp High-Contrast Slate Bronze
    outline = Color(0xFFB89E72) // Delicate Handcrafted Gold Border
)

val LuxuryObsidianColorScheme = darkColorScheme(
    primary = Color(0xFFE5C158), // Radiant 24K Champagne Gold
    onPrimary = Color(0xFF140F04),
    primaryContainer = Color(0xFF3A2E0E),
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = Color(0xFFF5CE76), // Amber Gold Accent
    onSecondary = Color(0xFF140F04),
    background = Color(0xFF09090C), // Imperial Caviar Onyx
    onBackground = Color(0xFFFBF6EA), // Soft Luminescent Champagne
    surface = Color(0xFF131317), // Polished Basalt
    onSurface = Color(0xFFFBF6EA),
    surfaceVariant = Color(0xFF1D1D23), // Smoked Charcoal
    onSurfaceVariant = Color(0xFFC7BDAB), // Muted Sand Gold
    outline = Color(0xFF7A5E24) // Burnished Antique Gold Rim
)

val FineDiningIvoryColorScheme = lightColorScheme(
    primary = Color(0xFF084B2E), // Deep Sommelier Emerald (WCAG AAA > 8:1)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCEEBDF),
    onPrimaryContainer = Color(0xFF032616),
    secondary = Color(0xFF106040), // Deep Seafoam Accent
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF9FAFB), // Crisp Alabaster White
    onBackground = Color(0xFF0A0F1D), // Deep Navy-Black
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0A0F1D),
    surfaceVariant = Color(0xFFEEF3F0),
    onSurfaceVariant = Color(0xFF1E293B), // High-Contrast Deep Slate
    outline = Color(0xFF96CBB4)
)

val KitchenHighContrastColorScheme = darkColorScheme(
    primary = Color(0xFF00FF00), // High Visibility Neon Green
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF003300),
    onPrimaryContainer = Color(0xFF00FF00),
    secondary = Color(0xFFFFFF00), // Neon Yellow for accents
    onSecondary = Color(0xFF000000),
    background = Color(0xFF000000), // Pure Black for max contrast
    onBackground = Color(0xFFFFFFFF), // Pure White Text
    surface = Color(0xFF111111), // Very dark grey surface
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF222222),
    onSurfaceVariant = Color(0xFFDDDDDD),
    outline = Color(0xFF00FF00) // Bright border
)

// ==========================================
// 10 LUXURY HOSPITALITY TEMPLATE SKINS
// ==========================================

data class TemplateSkin(
    val id: String,
    val name: String,
    val description: String,
    val backgroundColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val textColor: Color,
    val secondaryTextColor: Color,
    val isDark: Boolean,
    val isDoubleBorder: Boolean = false
)

val TemplateSkins = listOf(
    TemplateSkin(
        id = "template-heritage-gold",
        name = "1. Heritage Gold",
        description = "Classic Luxury with Gold Accent",
        backgroundColor = Color(0xFFFFFDF9),
        borderColor = Color(0xFFB89648),
        borderWidth = 1.5.dp,
        textColor = Color(0xFF111827),
        secondaryTextColor = Color(0xFF2D3748),
        isDark = false
    ),
    TemplateSkin(
        id = "template-royal-palace",
        name = "2. Royal Palace",
        description = "Double Border Vintage Royal",
        backgroundColor = Color(0xFFFAF5ED),
        borderColor = Color(0xFF7A5F35),
        borderWidth = 2.dp,
        textColor = Color(0xFF19120B),
        secondaryTextColor = Color(0xFF382A1B),
        isDark = false,
        isDoubleBorder = true
    ),
    TemplateSkin(
        id = "template-contemporary-gold",
        name = "3. Contemporary Gold",
        description = "Clean Minimal Modern Luxury",
        backgroundColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFFCBD5E1),
        borderWidth = 1.dp,
        textColor = Color(0xFF0F172A),
        secondaryTextColor = Color(0xFF334155),
        isDark = false
    ),
    TemplateSkin(
        id = "template-ivory-marble",
        name = "4. Ivory Marble",
        description = "Parchment Tone with Polished Gold",
        backgroundColor = Color(0xFFFDFBF7),
        borderColor = Color(0xFFC5A059),
        borderWidth = 1.5.dp,
        textColor = Color(0xFF17120C),
        secondaryTextColor = Color(0xFF382E25),
        isDark = false
    ),
    TemplateSkin(
        id = "template-art-deco",
        name = "5. Art Deco",
        description = "Dark Elegant Obsidian & Amber",
        backgroundColor = Color(0xFF111827),
        borderColor = Color(0xFFFBBF24),
        borderWidth = 2.dp,
        textColor = Color(0xFFFEF3C7),
        secondaryTextColor = Color(0xFFFDE68A),
        isDark = true
    ),
    TemplateSkin(
        id = "template-signature-luxury",
        name = "6. Signature Luxury",
        description = "Jet Black with Fine Gold Trim",
        backgroundColor = Color(0xFF000000),
        borderColor = Color(0xFFD4AF37),
        borderWidth = 1.5.dp,
        textColor = Color(0xFFFFFFFF),
        secondaryTextColor = Color(0xFFD4D4D8),
        isDark = true
    ),
    TemplateSkin(
        id = "template-fine-dining",
        name = "7. Fine Dining",
        description = "Crisp White with Slate Trim",
        backgroundColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFFCBD5E1),
        borderWidth = 1.5.dp,
        textColor = Color(0xFF1E293B),
        secondaryTextColor = Color(0xFF64748B),
        isDark = false
    ),
    TemplateSkin(
        id = "template-crystal-elegance",
        name = "8. Crystal Elegance",
        description = "Cool White with Ocean Blue Border",
        backgroundColor = Color(0xFFF8FAFC),
        borderColor = Color(0xFF0284C7),
        borderWidth = 2.dp,
        textColor = Color(0xFF0F172A),
        secondaryTextColor = Color(0xFF475569),
        isDark = false
    ),
    TemplateSkin(
        id = "template-regal-black-gold",
        name = "9. Regal Black & Gold",
        description = "Deep Onyx with Radiant Gold",
        backgroundColor = Color(0xFF18181B),
        borderColor = Color(0xFFEAB308),
        borderWidth = 2.dp,
        textColor = Color(0xFFFACC15),
        secondaryTextColor = Color(0xFFFEF08A),
        isDark = true
    ),
    TemplateSkin(
        id = "template-presidential",
        name = "10. Presidential",
        description = "Regal Cream with Deep Burgundy",
        backgroundColor = Color(0xFFFDF8F6),
        borderColor = Color(0xFF991B1B),
        borderWidth = 2.5.dp,
        textColor = Color(0xFF450A0A),
        secondaryTextColor = Color(0xFF7F1D1D),
        isDark = false
    )
)

fun getTemplateSkinById(id: String): TemplateSkin {
    return TemplateSkins.find { it.id == id } ?: TemplateSkins[0]
}

// ==========================================
// FSSAI OFFICIAL REGULATORY BADGE
// ==========================================

@Composable
fun FssaiRegulatoryMark(
    isVeg: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp
) {
    val borderColor = if (isVeg) Color(0xFF15803D) else Color(0xFF991B1B)
    val fillColor = if (isVeg) Color(0xFF15803D) else Color(0xFF991B1B)

    Box(
        modifier = modifier
            .size(size)
            .background(Color.White)
            .border(width = 1.5.dp, color = borderColor),
        contentAlignment = Alignment.Center
    ) {
        if (isVeg) {
            // Veg: filled green circle
            Box(
                modifier = Modifier
                    .size(size * 0.45f)
                    .background(fillColor, shape = CircleShape)
            )
        } else {
            // Non-Veg: filled maroon triangle
            Canvas(modifier = Modifier.size(size * 0.5f)) {
                val path = Path().apply {
                    moveTo(this@Canvas.size.width / 2f, 0f)
                    lineTo(this@Canvas.size.width, this@Canvas.size.height)
                    lineTo(0f, this@Canvas.size.height)
                    close()
                }
                drawPath(path = path, color = fillColor, style = Fill)
            }
        }
    }
}

// ==========================================
// 14 FSSAI ALLERGENS AND CUSTOM ICON PAINTERS
// ==========================================

val FSSAI_ALLERGENS_LIST = listOf(
    "Crustaceans",
    "Molluscs",
    "Fish",
    "Soya",
    "Gluten",
    "Mustard",
    "Sesame",
    "Celery",
    "Eggs",
    "Milk",
    "Peanuts",
    "Nuts",
    "Sulphites",
    "Lupin"
)

@Composable
fun AllergenIcon(
    allergen: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    val norm = allergen.trim()
    val color = when {
        norm.equals("Allergen-Free", ignoreCase = true) || norm.equals("Hypoallergenic", ignoreCase = true) -> Color(0xFF059669)
        norm.equals("Vegan", ignoreCase = true) || norm.equals("Plant-Based", ignoreCase = true) -> Color(0xFF16A34A)
        norm.equals("Gluten-Free", ignoreCase = true) || norm.equals("No Gluten", ignoreCase = true) -> Color(0xFFD97706)
        norm.equals("Dairy-Free", ignoreCase = true) || norm.equals("Lactose-Free", ignoreCase = true) -> Color(0xFF0284C7)
        norm.equals("Nut-Free", ignoreCase = true) || norm.equals("Peanut-Free", ignoreCase = true) -> Color(0xFFEA580C)
        norm.equals("Eggless", ignoreCase = true) -> Color(0xFFD97706)
        norm.equals("Vegetarian", ignoreCase = true) || norm.equals("Veg", ignoreCase = true) -> Color(0xFF15803D)
        norm.equals("Jain", ignoreCase = true) -> Color(0xFFC2410C)
        norm.equals("Halal", ignoreCase = true) -> Color(0xFF854D0E)
        norm.equals("Kosher", ignoreCase = true) -> Color(0xFF1D4ED8)
        norm.equals("Sugar-Free", ignoreCase = true) -> Color(0xFFE11D48)
        norm.equals("Keto", ignoreCase = true) || norm.contains("Keto", ignoreCase = true) -> Color(0xFF7C3AED)
        norm.equals("Organic", ignoreCase = true) -> Color(0xFF0D9488)
        norm.equals("High-Protein", ignoreCase = true) -> Color(0xFF4F46E5)
        norm.equals("Low-Sodium", ignoreCase = true) -> Color(0xFF475569)
        norm.equals("Crustaceans", ignoreCase = true) -> Color(0xFFD84315)
        norm.equals("Molluscs", ignoreCase = true) -> Color(0xFF0288D1)
        norm.equals("Fish", ignoreCase = true) -> Color(0xFFE67E22)
        norm.equals("Soya", ignoreCase = true) || norm.equals("Soy", ignoreCase = true) -> Color(0xFF7CB342)
        norm.equals("Gluten", ignoreCase = true) -> Color(0xFFF39C12)
        norm.equals("Mustard", ignoreCase = true) -> Color(0xFFF1C40F)
        norm.equals("Sesame", ignoreCase = true) -> Color(0xFF374151)
        norm.equals("Celery", ignoreCase = true) -> Color(0xFF2ECC71)
        norm.equals("Eggs", ignoreCase = true) -> Color(0xFFF5B041)
        norm.equals("Milk", ignoreCase = true) -> Color(0xFF5DADE2)
        norm.equals("Peanuts", ignoreCase = true) -> Color(0xFFD35400)
        norm.equals("Nuts", ignoreCase = true) || norm.equals("Tree Nuts", ignoreCase = true) -> Color(0xFFAF601A)
        norm.equals("Sulphites", ignoreCase = true) || norm.equals("Sulfites", ignoreCase = true) -> Color(0xFF16A085)
        norm.equals("Lupin", ignoreCase = true) -> Color(0xFF8E44AD)
        else -> Color(0xFF10B981)
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            when {
                norm.equals("Allergen-Free", ignoreCase = true) || norm.equals("Hypoallergenic", ignoreCase = true) -> {
                    // Shield with checkmark
                    val shield = Path().apply {
                        moveTo(w * 0.5f, h * 0.12f)
                        lineTo(w * 0.85f, h * 0.25f)
                        quadraticTo(w * 0.85f, h * 0.65f, w * 0.5f, h * 0.92f)
                        quadraticTo(w * 0.15f, h * 0.65f, w * 0.15f, h * 0.25f)
                        close()
                    }
                    drawPath(shield, color = color, style = Fill)
                    // White checkmark
                    val check = Path().apply {
                        moveTo(w * 0.32f, h * 0.5f)
                        lineTo(w * 0.45f, h * 0.65f)
                        lineTo(w * 0.70f, h * 0.38f)
                    }
                    drawPath(check, color = Color.White, style = Stroke(width = 2.5f))
                }
                norm.equals("Vegan", ignoreCase = true) || norm.equals("Plant-Based", ignoreCase = true) -> {
                    // Sprouting dual leaves
                    val leaf1 = Path().apply {
                        moveTo(w * 0.5f, h * 0.85f)
                        quadraticTo(w * 0.2f, h * 0.6f, w * 0.25f, h * 0.25f)
                        quadraticTo(w * 0.6f, h * 0.35f, w * 0.5f, h * 0.85f)
                        close()
                    }
                    drawPath(leaf1, color = color, style = Fill)
                    val leaf2 = Path().apply {
                        moveTo(w * 0.5f, h * 0.85f)
                        quadraticTo(w * 0.8f, h * 0.6f, w * 0.75f, h * 0.25f)
                        quadraticTo(w * 0.4f, h * 0.35f, w * 0.5f, h * 0.85f)
                        close()
                    }
                    drawPath(leaf2, color = color.copy(alpha = 0.8f), style = Fill)
                    // stem
                    drawLine(color = color, start = Offset(w * 0.5f, h * 0.85f), end = Offset(w * 0.5f, h * 0.95f), strokeWidth = 2f)
                }
                norm.equals("Gluten-Free", ignoreCase = true) || norm.equals("No Gluten", ignoreCase = true) -> {
                    // Wheat stalk with prohibition slash
                    drawLine(color = color, start = Offset(w / 2f, h * 0.15f), end = Offset(w / 2f, h * 0.85f), strokeWidth = 2f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.35f), end = Offset(w * 0.75f, h * 0.25f), strokeWidth = 1.8f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.35f), end = Offset(w * 0.25f, h * 0.25f), strokeWidth = 1.8f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.55f), end = Offset(w * 0.75f, h * 0.45f), strokeWidth = 1.8f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.55f), end = Offset(w * 0.25f, h * 0.45f), strokeWidth = 1.8f)
                    // Red/Amber diagonal slash
                    drawLine(color = Color(0xFFDC2626), start = Offset(w * 0.18f, h * 0.82f), end = Offset(w * 0.82f, h * 0.18f), strokeWidth = 2.5f)
                }
                norm.equals("Dairy-Free", ignoreCase = true) || norm.equals("Lactose-Free", ignoreCase = true) -> {
                    // Milk bottle with diagonal slash
                    drawRoundRect(color = color, topLeft = Offset(w * 0.32f, h * 0.32f), size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.55f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f))
                    drawRect(color = color, topLeft = Offset(w * 0.4f, h * 0.18f), size = androidx.compose.ui.geometry.Size(w * 0.2f, h * 0.14f))
                    // Slash
                    drawLine(color = Color(0xFFDC2626), start = Offset(w * 0.15f, h * 0.85f), end = Offset(w * 0.85f, h * 0.15f), strokeWidth = 2.5f)
                }
                norm.equals("Nut-Free", ignoreCase = true) || norm.equals("Peanut-Free", ignoreCase = true) -> {
                    // Peanut lobes with diagonal slash
                    drawCircle(color = color, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.35f))
                    drawCircle(color = color, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.65f))
                    drawLine(color = Color(0xFFDC2626), start = Offset(w * 0.15f, h * 0.85f), end = Offset(w * 0.85f, h * 0.15f), strokeWidth = 2.5f)
                }
                norm.equals("Eggless", ignoreCase = true) -> {
                    drawOval(color = color, topLeft = Offset(w * 0.25f, h * 0.2f), size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.65f))
                    drawLine(color = Color(0xFFDC2626), start = Offset(w * 0.15f, h * 0.85f), end = Offset(w * 0.85f, h * 0.15f), strokeWidth = 2.5f)
                }
                norm.equals("Vegetarian", ignoreCase = true) || norm.equals("Veg", ignoreCase = true) -> {
                    // FSSAI green mark (square with circle)
                    drawRoundRect(color = color, topLeft = Offset(w * 0.15f, h * 0.15f), size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.7f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f), style = Stroke(width = 2f))
                    drawCircle(color = color, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.5f))
                }
                norm.equals("Jain", ignoreCase = true) -> {
                    // Sacred lotus petal motif
                    for (i in 0 until 5) {
                        val angle = (i * 72f - 90f) * (Math.PI / 180f).toFloat()
                        val cx = w * 0.5f + (w * 0.22f) * kotlin.math.cos(angle)
                        val cy = h * 0.5f + (h * 0.22f) * kotlin.math.sin(angle)
                        drawCircle(color = color, radius = w * 0.14f, center = Offset(cx, cy))
                    }
                    drawCircle(color = Color.White, radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.5f))
                }
                norm.equals("Halal", ignoreCase = true) -> {
                    // Crescent moon with star
                    drawCircle(color = color, radius = w * 0.36f, center = Offset(w * 0.48f, h * 0.5f))
                    drawCircle(color = Color.White, radius = w * 0.32f, center = Offset(w * 0.60f, h * 0.44f))
                    drawCircle(color = color, radius = w * 0.08f, center = Offset(w * 0.68f, h * 0.32f))
                }
                norm.equals("Sugar-Free", ignoreCase = true) -> {
                    val heart = Path().apply {
                        moveTo(w * 0.5f, h * 0.78f)
                        cubicTo(w * 0.15f, h * 0.55f, w * 0.15f, h * 0.25f, w * 0.35f, h * 0.25f)
                        cubicTo(w * 0.45f, h * 0.25f, w * 0.5f, h * 0.35f, w * 0.5f, h * 0.35f)
                        cubicTo(w * 0.5f, h * 0.35f, w * 0.55f, h * 0.25f, w * 0.65f, h * 0.25f)
                        cubicTo(w * 0.85f, h * 0.25f, w * 0.85f, h * 0.55f, w * 0.5f, h * 0.78f)
                        close()
                    }
                    drawPath(heart, color = color)
                }
                norm.equals("Gluten", ignoreCase = true) -> {
                    // Wheat stalk representation
                    drawLine(color = color, start = Offset(w / 2f, h * 0.1f), end = Offset(w / 2f, h * 0.9f), strokeWidth = 2.5f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.3f), end = Offset(w * 0.8f, h * 0.2f), strokeWidth = 2f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.3f), end = Offset(w * 0.2f, h * 0.2f), strokeWidth = 2f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.4f), strokeWidth = 2f)
                    drawLine(color = color, start = Offset(w / 2f, h * 0.5f), end = Offset(w * 0.2f, h * 0.4f), strokeWidth = 2f)
                }
                norm.equals("Milk", ignoreCase = true) || norm.equals("Dairy", ignoreCase = true) -> {
                    // Bottle / glass shape
                    drawRoundRect(color = color, topLeft = Offset(w * 0.3f, h * 0.3f), size = androidx.compose.ui.geometry.Size(w * 0.4f, h * 0.6f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
                    drawRect(color = color, topLeft = Offset(w * 0.38f, h * 0.15f), size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.15f))
                }
                norm.equals("Fish", ignoreCase = true) -> {
                    val fishPath = Path().apply {
                        moveTo(w * 0.85f, h * 0.5f)
                        quadraticTo(w * 0.5f, h * 0.15f, w * 0.15f, h * 0.5f)
                        quadraticTo(w * 0.5f, h * 0.85f, w * 0.85f, h * 0.5f)
                        lineTo(w * 0.95f, h * 0.25f)
                        lineTo(w * 0.95f, h * 0.75f)
                        close()
                    }
                    drawPath(fishPath, color = color)
                }
                norm.equals("Eggs", ignoreCase = true) || norm.equals("Egg", ignoreCase = true) -> {
                    // Oval egg
                    drawOval(color = color, topLeft = Offset(w * 0.2f, h * 0.15f), size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.75f))
                    drawCircle(color = Color.White.copy(alpha = 0.5f), radius = w * 0.12f, center = Offset(w * 0.45f, h * 0.45f))
                }
                norm.equals("Peanuts", ignoreCase = true) || norm.equals("Nuts", ignoreCase = true) || norm.equals("Tree Nuts", ignoreCase = true) -> {
                    // Peanut double lobe
                    drawCircle(color = color, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.35f))
                    drawCircle(color = color, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.65f))
                }
                norm.equals("Crustaceans", ignoreCase = true) || norm.equals("Shellfish", ignoreCase = true) -> {
                    // Prawn / Crab arc motif
                    drawCircle(color = color, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f))
                    drawLine(color = color, start = Offset(w*0.2f, h*0.5f), end = Offset(w*0.1f, h*0.3f), strokeWidth = 4f)
                    drawLine(color = color, start = Offset(w*0.8f, h*0.5f), end = Offset(w*0.9f, h*0.3f), strokeWidth = 4f)
                    // eyes
                    drawCircle(color = Color.White, radius = w*0.05f, center = Offset(w*0.4f, h*0.3f))
                    drawCircle(color = Color.White, radius = w*0.05f, center = Offset(w*0.6f, h*0.3f))
                }
                norm.equals("Molluscs", ignoreCase = true) -> {
                    // Seashell (scallop style)
                    val shellPath = Path().apply {
                        moveTo(w * 0.2f, h * 0.7f)
                        quadraticTo(w * 0.5f, -h * 0.1f, w * 0.8f, h * 0.7f)
                        lineTo(w * 0.6f, h * 0.9f)
                        lineTo(w * 0.4f, h * 0.9f)
                        close()
                    }
                    drawPath(shellPath, color = color)
                    drawLine(color = Color.White, start = Offset(w * 0.5f, h * 0.8f), end = Offset(w * 0.5f, h * 0.2f), strokeWidth = 2f)
                    drawLine(color = Color.White, start = Offset(w * 0.4f, h * 0.8f), end = Offset(w * 0.3f, h * 0.3f), strokeWidth = 2f)
                    drawLine(color = Color.White, start = Offset(w * 0.6f, h * 0.8f), end = Offset(w * 0.7f, h * 0.3f), strokeWidth = 2f)
                }
                norm.equals("Soya", ignoreCase = true) || norm.equals("Soy", ignoreCase = true) -> {
                    // Soya bean / pod
                    val podPath = Path().apply {
                        moveTo(w * 0.2f, h * 0.8f)
                        quadraticTo(w * 0.2f, h * 0.2f, w * 0.8f, h * 0.2f)
                        quadraticTo(w * 0.8f, h * 0.8f, w * 0.2f, h * 0.8f)
                        close()
                    }
                    drawPath(podPath, color = color)
                    drawCircle(color = Color.White.copy(alpha=0.5f), radius = w*0.1f, center = Offset(w*0.4f, h*0.4f))
                    drawCircle(color = Color.White.copy(alpha=0.5f), radius = w*0.1f, center = Offset(w*0.6f, h*0.6f))
                }
                norm.equals("Mustard", ignoreCase = true) -> {
                    // Mustard seeds (small circles) and a small leaf
                    drawCircle(color = color, radius = w * 0.15f, center = Offset(w * 0.4f, h * 0.6f))
                    drawCircle(color = color, radius = w * 0.12f, center = Offset(w * 0.6f, h * 0.7f))
                    drawCircle(color = color, radius = w * 0.14f, center = Offset(w * 0.7f, h * 0.4f))
                    drawCircle(color = color, radius = w * 0.1f, center = Offset(w * 0.3f, h * 0.4f))
                    drawCircle(color = Color(0xFFF39C12), radius = w * 0.15f, center = Offset(w * 0.5f, h * 0.3f))
                }
                norm.equals("Sesame", ignoreCase = true) -> {
                    // Sesame seeds (tear drop shapes)
                    val seed1 = Path().apply {
                        moveTo(w*0.4f, h*0.3f)
                        quadraticTo(w*0.6f, h*0.3f, w*0.6f, h*0.5f)
                        quadraticTo(w*0.5f, h*0.7f, w*0.4f, h*0.3f)
                        close()
                    }
                    drawPath(seed1, color = color)
                    val seed2 = Path().apply {
                        moveTo(w*0.7f, h*0.5f)
                        quadraticTo(w*0.9f, h*0.5f, w*0.9f, h*0.7f)
                        quadraticTo(w*0.8f, h*0.9f, w*0.7f, h*0.5f)
                        close()
                    }
                    drawPath(seed2, color = Color(0xFFD3D3D3))
                }
                norm.equals("Celery", ignoreCase = true) -> {
                    // Celery stalk
                    drawRoundRect(color = color, topLeft = Offset(w*0.4f, h*0.3f), size = androidx.compose.ui.geometry.Size(w*0.2f, h*0.6f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.35f, h*0.2f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.65f, h*0.2f))
                    drawCircle(color = color, radius = w*0.2f, center = Offset(w*0.5f, h*0.15f))
                }
                norm.equals("Sulphites", ignoreCase = true) || norm.equals("Sulfites", ignoreCase = true) -> {
                    // Wine bottle/flask
                    drawRoundRect(color = color, topLeft = Offset(w*0.4f, h*0.1f), size = androidx.compose.ui.geometry.Size(w*0.2f, h*0.3f))
                    drawRoundRect(color = color, topLeft = Offset(w*0.3f, h*0.4f), size = androidx.compose.ui.geometry.Size(w*0.4f, h*0.5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f))
                    drawCircle(color = Color.White.copy(alpha=0.4f), radius = w*0.08f, center = Offset(w*0.5f, h*0.6f))
                }
                norm.equals("Lupin", ignoreCase = true) -> {
                    // Lupin flower (tall stem with petals)
                    drawLine(color = Color(0xFF2ECC71), start = Offset(w*0.5f, h*0.2f), end = Offset(w*0.5f, h*0.9f), strokeWidth = 3f)
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.4f, h*0.3f))
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.6f, h*0.3f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.5f, h*0.2f))
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.35f, h*0.5f))
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.65f, h*0.5f))
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.4f, h*0.7f))
                    drawCircle(color = color, radius = w*0.12f, center = Offset(w*0.6f, h*0.7f))
                }
                else -> {
                    // General regulatory allergen circular badge with cross/symbol
                    drawCircle(color = color, radius = w * 0.4f, style = Stroke(width = 2.5f))
                    drawCircle(color = color, radius = w * 0.2f)
                }
            }
        }
    }
}

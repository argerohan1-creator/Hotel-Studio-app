package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.RadialGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.R
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic Food Station Signage Preset based on high-end buffet & restaurant signage.
 */
data class FoodStationPreset(
    val id: String,
    val scriptTitle: String,
    val stationTag: String,
    val category: String,
    val emoji: String,
    val dietaryDefault: String = "",
    val accentColorHex: String = "#9C7A4A", // Luxury Gold
    val theme: FoodVisualTheme,
    val foodDrawableRes: Int = R.drawable.food_dim_sum
)

enum class FoodVisualTheme {
    MIDNIGHT_OBSIDIAN,
    IVORY_SILK,
    BRUSHED_GOLD,
    EMERALD_VELVET,
    SAPPHIRE_GRADIENT,
    CRIMSON_DAMASK,
    CHARCOAL_MATTE,
    ROSE_GOLD,
    FROSTED_GLASS,
    PLATINUM_MESH,
    BRONZE_TEXTURE,
    PEARL_GLAZE,
    ROYAL_BURGUNDY,
    TITANIUM_WEAVE,
    CHAMPAGNE_SPARKLE
}

/**
 * Modern artwork style definition for restaurant signage, buffet boards, and culinary displays.
 */
data class ModernArtworkStyle(
    val id: String,
    val name: String,
    val category: String, // e.g. "Fine Dining", "Modern Bistro", "Artisan Bakery", "Bar & Lounge"
    val description: String,
    val primaryColor: Int,
    val secondaryColor: Int,
    val accentColor: Int,
    val textColor: Int,
    val backgroundGradientColors: IntArray,
    val badgeLabel: String,
    val visualMotif: ArtworkMotif
)

enum class ArtworkMotif {
    MICHELIN_SLATE,       // Dark textured slate with gold geometric luxury lines & circular frame
    MINIMAL_ORGANIC,      // Soft warm terracotta/sage shapes, clean Nordic typography
    ARTISAN_BOTANICAL,    // Deep emerald botanical flourishes with bronze accents
    NEON_BISTRO,          // Moody dark night gradient with glowing neon rim & sleek sans-serif
    ROYAL_DAMASK,         // Gilded gold filigree corners & royal burgundy/navy depth
    JAPANESE_ZEN,         // Clean washi cream, crimson sun circle stamp & sumi-e minimalist strokes
    COASTAL_MEDITERRANEAN // Crisp azure sea gradient, wave contours, citrus gold accents
}

object ModernArtRenderer {

    /**
     * All 15 authentic food station presets modeled directly after professional restaurant & buffet signage.
     */
    val FOOD_STATION_PRESETS = listOf(
        FoodStationPreset("dim_sum", "Dim Sum", "BAR", "Asian Specialty", "🥟", "Chef's Dim Sum Selection", "#9C7A4A", FoodVisualTheme.MIDNIGHT_OBSIDIAN, R.drawable.food_dim_sum),
        FoodStationPreset("live_pizza", "Live", "PIZZA", "Italian & Continental", "🍕", "Wood Fired Artisanal Slices", "#9C7A4A", FoodVisualTheme.IVORY_SILK, R.drawable.food_pizza),
        FoodStationPreset("pasta_station", "Pasta", "STATION", "Italian & Continental", "🍝", "Fresh Handcrafted Pasta", "#9C7A4A", FoodVisualTheme.BRUSHED_GOLD, R.drawable.food_pasta),
        FoodStationPreset("chaat_station", "Chaat", "STATION", "Indian Street Live", "🥘", "🟢 100% Pure Veg Live Counter", "#9C7A4A", FoodVisualTheme.EMERALD_VELVET, R.drawable.food_chaat),
        FoodStationPreset("salad_bar", "Healthy", "SALAD BAR", "Salad & Wellness", "🥗", "Organic Greens & Vinaigrettes", "#9C7A4A", FoodVisualTheme.SAPPHIRE_GRADIENT, R.drawable.food_salad),
        FoodStationPreset("mexican_wrap", "Mexican", "WRAP", "Global Street Food", "🌮", "Sizzling Fajitas & Warm Tortillas", "#9C7A4A", FoodVisualTheme.CRIMSON_DAMASK, R.drawable.food_mexican),
        FoodStationPreset("kulfi_corner", "Kulfi", "CORNER", "Dessert & Sweets", "🍦", "Royal Saffron & Pistachio Malai", "#9C7A4A", FoodVisualTheme.CHARCOAL_MATTE, R.drawable.food_dessert),
        FoodStationPreset("sweet_lovers", "For sweet", "LOVERS", "Dessert & Sweets", "🍬", "Artisanal Mithai & Sweet Delicacies", "#9C7A4A", FoodVisualTheme.ROSE_GOLD, R.drawable.food_dessert),
        FoodStationPreset("jain_food", "Jain", "FOOD", "Dietary Special", "🍲", "🌿 Pure Sattvic Jain Delicacies", "#9C7A4A", FoodVisualTheme.FROSTED_GLASS, R.drawable.food_salad),
        FoodStationPreset("arabic_corner", "Arabic", "CORNER", "Grill & Kebab", "🍢", "Charcoal Skewers & Creamy Mezze", "#9C7A4A", FoodVisualTheme.PLATINUM_MESH, R.drawable.food_kebab),
        FoodStationPreset("appam_station", "Appam", "STATION", "Regional Indian", "🥞", "Crispy Lace Appams & Coconut Stew", "#9C7A4A", FoodVisualTheme.BRONZE_TEXTURE, R.drawable.food_bakery),
        FoodStationPreset("tea_coffee", "Tea & Coffee", "STATION", "Beverage Bar", "☕", "Artisanal Brews & Gourmet Teas", "#9C7A4A", FoodVisualTheme.PEARL_GLAZE, R.drawable.food_coffee),
        FoodStationPreset("mongolian_counter", "Mongolian", "COUNTER", "Asian Specialty", "🍜", "Live Wok Tossed Sizzle Bowl", "#9C7A4A", FoodVisualTheme.ROYAL_BURGUNDY, R.drawable.food_dim_sum),
        FoodStationPreset("no_onion_garlic", "No Onion & Garlic", "FOOD", "Dietary Special", "🥦", "🟢 Satvik Homestyle Feast", "#9C7A4A", FoodVisualTheme.TITANIUM_WEAVE, R.drawable.food_salad),
        FoodStationPreset("asian_bar", "Asian", "BAR", "Asian Specialty", "🥢", "Authentic Oriental Wok Creations", "#9C7A4A", FoodVisualTheme.CHAMPAGNE_SPARKLE, R.drawable.food_sushi)
    )

    val STYLES = listOf(
        ModernArtworkStyle(
            id = "michelin_slate",
            name = "Michelin Slate & Gold",
            category = "Haute Cuisine",
            description = "Deep obsidian slate with hand-gilded geometric concentric arcs and gold badge",
            primaryColor = Color.parseColor("#0D1117"),
            secondaryColor = Color.parseColor("#161B22"),
            accentColor = Color.parseColor("#D4AF37"),
            textColor = Color.parseColor("#FFFDF5"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#121820"), Color.parseColor("#0A0D12")),
            badgeLabel = "HAUTE CUISINE",
            visualMotif = ArtworkMotif.MICHELIN_SLATE
        ),
        ModernArtworkStyle(
            id = "minimal_nordic",
            name = "Nordic Organic Stone",
            category = "Modern Bistro",
            description = "Earthy cashmere stone with abstract fluid pebble contours and terracotta accents",
            primaryColor = Color.parseColor("#F5F3EF"),
            secondaryColor = Color.parseColor("#E7E2DA"),
            accentColor = Color.parseColor("#C26D53"),
            textColor = Color.parseColor("#262220"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#FAF8F5"), Color.parseColor("#EDE7DF")),
            badgeLabel = "FARM TO TABLE",
            visualMotif = ArtworkMotif.MINIMAL_ORGANIC
        ),
        ModernArtworkStyle(
            id = "botanical_emerald",
            name = "Botanical Garden & Brass",
            category = "Artisan Cafe & Grill",
            description = "Deep British forest velvet with brushed brass geometric foliage borders",
            primaryColor = Color.parseColor("#062E20"),
            secondaryColor = Color.parseColor("#0A422F"),
            accentColor = Color.parseColor("#E5C158"),
            textColor = Color.parseColor("#F0FDF4"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#0B3828"), Color.parseColor("#041A12")),
            badgeLabel = "CHEF'S HARVEST",
            visualMotif = ArtworkMotif.ARTISAN_BOTANICAL
        ),
        ModernArtworkStyle(
            id = "neon_cocktail",
            name = "Cyberpunk Velvet Lounge",
            category = "Rooftop Bar & Lounge",
            description = "Atmospheric dark violet vignette with glowing neon coral & cyan light streaks",
            primaryColor = Color.parseColor("#0F0C20"),
            secondaryColor = Color.parseColor("#1A1438"),
            accentColor = Color.parseColor("#FF3366"),
            textColor = Color.parseColor("#FFFFFF"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#1A103C"), Color.parseColor("#090614")),
            badgeLabel = "SIGNATURE POUR",
            visualMotif = ArtworkMotif.NEON_BISTRO
        ),
        ModernArtworkStyle(
            id = "royal_gilded",
            name = "Imperial Bordeaux & Gold",
            category = "Grand Ballroom & Wine",
            description = "Regal deep burgundy damask with double etched gold fillet lines",
            primaryColor = Color.parseColor("#2B0914"),
            secondaryColor = Color.parseColor("#450E20"),
            accentColor = Color.parseColor("#F7D070"),
            textColor = Color.parseColor("#FFFBF0"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#3B0B1C"), Color.parseColor("#1A030A")),
            badgeLabel = "GRAND RESERVE",
            visualMotif = ArtworkMotif.ROYAL_DAMASK
        ),
        ModernArtworkStyle(
            id = "zen_minimal",
            name = "Kyoto Zen Washi",
            category = "Contemporary Asian",
            description = "Handmade warm ivory paper with vermilion seal, bamboo strokes and serene spacing",
            primaryColor = Color.parseColor("#FAF7EE"),
            secondaryColor = Color.parseColor("#EFE9D7"),
            accentColor = Color.parseColor("#B91C1C"),
            textColor = Color.parseColor("#1C1917"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#FCFAF4"), Color.parseColor("#F0EADC")),
            badgeLabel = "OMAKASE SELECTION",
            visualMotif = ArtworkMotif.JAPANESE_ZEN
        ),
        ModernArtworkStyle(
            id = "coastal_azure",
            name = "Riviera Coastal Azure",
            category = "Seafood & Grill",
            description = "Crisp Mediterranean cobalt to marine teal gradient with sunburst accents",
            primaryColor = Color.parseColor("#0C2D48"),
            secondaryColor = Color.parseColor("#145DA0"),
            accentColor = Color.parseColor("#F3C68F"),
            textColor = Color.parseColor("#F8FAFC"),
            backgroundGradientColors = intArrayOf(Color.parseColor("#144272"), Color.parseColor("#0A2647")),
            badgeLabel = "CATCH OF THE DAY",
            visualMotif = ArtworkMotif.COASTAL_MEDITERRANEAN
        )
    )

    /**
     * Renders a stunning high-resolution artwork bitmap ready for screen preview or PDF printing.
     */
    fun renderArtworkBitmap(
        title: String,
        subtitle: String,
        description: String,
        chefNote: String,
        style: ModernArtworkStyle,
        width: Int,
        height: Int,
        establishmentLogo: Bitmap? = null,
        includeBadge: Boolean = true
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val w = width.toFloat()
        val h = height.toFloat()

        // 1. Draw modern background gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, w, h,
                style.backgroundGradientColors,
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, w, h, bgPaint)

        // 2. Render motif-specific contemporary graphic art behind the text
        drawMotifGraphics(canvas, w, h, style)

        // 3. Render modern outer structural framing
        drawModernFraming(canvas, w, h, style)

        // 4. Render establishment branding / logo if provided
        val contentTop = renderHeaderBranding(canvas, w, h, style, establishmentLogo, includeBadge)

        // 5. Render typography (Title, Subtitle, Description, Chef Note)
        renderArtTypography(canvas, w, h, contentTop, title, subtitle, description, chefNote, style)

        return bitmap
    }

    private fun drawMotifGraphics(canvas: Canvas, w: Float, h: Float, artStyle: ModernArtworkStyle) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (artStyle.visualMotif) {
            ArtworkMotif.MICHELIN_SLATE -> {
                // Architectural gold geometric circles and radial ray accents
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.003f
                paint.color = artStyle.accentColor

                // Center decorative aura
                val cx = w * 0.5f
                val cy = h * 0.42f
                val baseR = w * 0.28f

                paint.alpha = 45
                canvas.drawCircle(cx, cy, baseR * 1.3f, paint)
                paint.alpha = 75
                canvas.drawCircle(cx, cy, baseR, paint)
                paint.alpha = 110
                canvas.drawCircle(cx, cy, baseR * 0.7f, paint)

                // Diamond crosshairs
                paint.alpha = 60
                canvas.drawLine(cx - baseR * 1.4f, cy, cx + baseR * 1.4f, cy, paint)
                canvas.drawLine(cx, cy - baseR * 1.4f, cx, cy + baseR * 1.4f, paint)

                // Corner gold chevrons
                drawModernChevron(canvas, w * 0.08f, h * 0.08f, w * 0.04f, 0f, paint)
                drawModernChevron(canvas, w * 0.92f, h * 0.08f, w * 0.04f, 90f, paint)
                drawModernChevron(canvas, w * 0.92f, h * 0.92f, w * 0.04f, 180f, paint)
                drawModernChevron(canvas, w * 0.08f, h * 0.92f, w * 0.04f, 270f, paint)
            }

            ArtworkMotif.MINIMAL_ORGANIC -> {
                // Fluid organic pebbles and warm minimalist blobs
                paint.style = Paint.Style.FILL
                paint.color = artStyle.accentColor
                paint.alpha = 28

                val path1 = Path().apply {
                    moveTo(w * 0.7f, 0f)
                    cubicTo(w * 0.95f, h * 0.1f, w * 1.05f, h * 0.35f, w * 0.8f, h * 0.45f)
                    cubicTo(w * 0.6f, h * 0.55f, w * 0.55f, h * 0.2f, w * 0.7f, 0f)
                    close()
                }
                canvas.drawPath(path1, paint)

                paint.color = artStyle.secondaryColor
                paint.alpha = 70
                val path2 = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.35f, h * 0.65f, w * 0.4f, h * 0.95f, w * 0.15f, h)
                    lineTo(0f, h)
                    close()
                }
                canvas.drawPath(path2, paint)

                // Arch outline
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.003f
                paint.color = artStyle.textColor
                paint.alpha = 40
                val archRect = RectF(w * 0.12f, h * 0.15f, w * 0.88f, h * 0.82f)
                canvas.drawRoundRect(archRect, w * 0.38f, w * 0.38f, paint)
            }

            ArtworkMotif.ARTISAN_BOTANICAL -> {
                // Brass botanical branch flourishes & leaves
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.004f
                paint.color = artStyle.accentColor
                paint.alpha = 90

                // Top flourish curve
                val topArch = Path().apply {
                    moveTo(w * 0.2f, h * 0.12f)
                    quadTo(w * 0.5f, h * 0.08f, w * 0.8f, h * 0.12f)
                }
                canvas.drawPath(topArch, paint)

                // Symmetrical decorative leaves
                paint.style = Paint.Style.FILL
                paint.alpha = 80
                drawStylizedLeaf(canvas, w * 0.5f, h * 0.09f, w * 0.025f, 0f, paint)
                drawStylizedLeaf(canvas, w * 0.45f, h * 0.095f, w * 0.018f, -30f, paint)
                drawStylizedLeaf(canvas, w * 0.55f, h * 0.095f, w * 0.018f, 30f, paint)

                // Bottom arch
                paint.style = Paint.Style.STROKE
                paint.alpha = 90
                val btmArch = Path().apply {
                    moveTo(w * 0.2f, h * 0.88f)
                    quadTo(w * 0.5f, h * 0.92f, w * 0.8f, h * 0.88f)
                }
                canvas.drawPath(btmArch, paint)

                paint.style = Paint.Style.FILL
                paint.alpha = 80
                drawStylizedLeaf(canvas, w * 0.5f, h * 0.91f, w * 0.025f, 180f, paint)
                drawStylizedLeaf(canvas, w * 0.45f, h * 0.905f, w * 0.018f, 150f, paint)
                drawStylizedLeaf(canvas, w * 0.55f, h * 0.905f, w * 0.018f, 210f, paint)
            }

            ArtworkMotif.NEON_BISTRO -> {
                // Sleek futuristic angled neon beams and subtle grid
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.004f
                paint.color = artStyle.accentColor
                paint.alpha = 140

                // Angled accent light bar
                canvas.drawLine(w * 0.1f, h * 0.2f, w * 0.9f, h * 0.2f, paint)
                paint.strokeWidth = w * 0.0015f
                paint.alpha = 60
                canvas.drawLine(w * 0.1f, h * 0.215f, w * 0.9f, h * 0.215f, paint)

                // Cyan secondary neon rim
                paint.color = Color.parseColor("#00F0FF")
                paint.alpha = 120
                paint.strokeWidth = w * 0.003f
                canvas.drawLine(w * 0.15f, h * 0.82f, w * 0.85f, h * 0.82f, paint)
            }

            ArtworkMotif.ROYAL_DAMASK -> {
                // Ornate Victorian-style border filigree corners
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.0035f
                paint.color = artStyle.accentColor
                paint.alpha = 110

                val pad = w * 0.07f
                val len = w * 0.14f

                // Top-Left ornamental corner
                canvas.drawLine(pad, pad, pad + len, pad, paint)
                canvas.drawLine(pad, pad, pad, pad + len, paint)
                canvas.drawCircle(pad + len, pad, w * 0.008f, paint)
                canvas.drawCircle(pad, pad + len, w * 0.008f, paint)

                // Top-Right
                canvas.drawLine(w - pad, pad, w - pad - len, pad, paint)
                canvas.drawLine(w - pad, pad, w - pad, pad + len, paint)
                canvas.drawCircle(w - pad - len, pad, w * 0.008f, paint)
                canvas.drawCircle(w - pad, pad + len, w * 0.008f, paint)

                // Bottom-Left
                canvas.drawLine(pad, h - pad, pad + len, h - pad, paint)
                canvas.drawLine(pad, h - pad, pad, h - pad - len, paint)
                canvas.drawCircle(pad + len, h - pad, w * 0.008f, paint)
                canvas.drawCircle(pad, h - pad - len, w * 0.008f, paint)

                // Bottom-Right
                canvas.drawLine(w - pad, h - pad, w - pad - len, h - pad, paint)
                canvas.drawLine(w - pad, h - pad, w - pad, h - pad - len, paint)
                canvas.drawCircle(w - pad - len, h - pad, w * 0.008f, paint)
                canvas.drawCircle(w - pad, h - pad, w * 0.008f, paint)
            }

            ArtworkMotif.JAPANESE_ZEN -> {
                // Minimalist Japanese red sun stamp circle and sumi ink serenity
                paint.style = Paint.Style.FILL
                paint.color = artStyle.accentColor
                paint.alpha = 160
                val sunRadius = w * 0.12f
                canvas.drawCircle(w * 0.5f, h * 0.32f, sunRadius, paint)

                // Concentric zen ripples
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.002f
                paint.color = artStyle.textColor
                paint.alpha = 30
                canvas.drawCircle(w * 0.5f, h * 0.32f, sunRadius * 1.5f, paint)
                canvas.drawCircle(w * 0.5f, h * 0.32f, sunRadius * 2.1f, paint)
            }

            ArtworkMotif.COASTAL_MEDITERRANEAN -> {
                // Fluid oceanic wave contours
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w * 0.003f
                paint.color = artStyle.accentColor
                paint.alpha = 90

                val wave1 = Path().apply {
                    moveTo(0f, h * 0.78f)
                    cubicTo(w * 0.25f, h * 0.74f, w * 0.45f, h * 0.82f, w * 0.7f, h * 0.76f)
                    cubicTo(w * 0.85f, h * 0.72f, w * 0.95f, h * 0.77f, w, h * 0.75f)
                }
                canvas.drawPath(wave1, paint)

                paint.alpha = 50
                val wave2 = Path().apply {
                    moveTo(0f, h * 0.81f)
                    cubicTo(w * 0.3f, h * 0.85f, w * 0.6f, h * 0.78f, w, h * 0.82f)
                }
                canvas.drawPath(wave2, paint)
            }
        }
    }

    private fun drawModernFraming(canvas: Canvas, w: Float, h: Float, artStyle: ModernArtworkStyle) {
        val framePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        framePaint.style = Paint.Style.STROKE
        framePaint.color = artStyle.accentColor
        framePaint.strokeWidth = w * 0.004f
        framePaint.alpha = 180

        val pad = w * 0.045f
        val rect = RectF(pad, pad, w - pad, h - pad)
        canvas.drawRoundRect(rect, w * 0.02f, w * 0.02f, framePaint)

        // Thin inner hairline
        framePaint.strokeWidth = w * 0.0015f
        framePaint.alpha = 70
        val innerPad = pad + w * 0.015f
        val innerRect = RectF(innerPad, innerPad, w - innerPad, h - innerPad)
        canvas.drawRoundRect(innerRect, w * 0.012f, w * 0.012f, framePaint)
    }

    private fun renderHeaderBranding(
        canvas: Canvas,
        w: Float,
        h: Float,
        artStyle: ModernArtworkStyle,
        logo: Bitmap?,
        includeBadge: Boolean
    ): Float {
        var currentY = h * 0.10f
        val cx = w * 0.5f

        // Draw custom logo if available
        if (logo != null) {
            val logoSize = (w * 0.12f).toInt()
            val scaledLogo = Bitmap.createScaledBitmap(logo, logoSize, logoSize, true)
            val left = cx - (logoSize / 2f)
            canvas.drawBitmap(scaledLogo, left, currentY, null)
            currentY += logoSize + (h * 0.02f)
        }

        // Draw category / signature badge pill
        if (includeBadge) {
            val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            badgePaint.style = Paint.Style.FILL
            badgePaint.color = artStyle.accentColor
            badgePaint.alpha = 40

            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = artStyle.accentColor
                textSize = w * 0.024f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                letterSpacing = 0.25f
            }

            val badgeText = artStyle.badgeLabel
            val textWidth = textPaint.measureText(badgeText)
            val pillWidth = textWidth + (w * 0.08f)
            val pillHeight = w * 0.055f
            val pillRect = RectF(
                cx - pillWidth / 2f,
                currentY,
                cx + pillWidth / 2f,
                currentY + pillHeight
            )

            canvas.drawRoundRect(pillRect, pillHeight / 2f, pillHeight / 2f, badgePaint)

            // Pill outline
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            borderPaint.style = Paint.Style.STROKE
            borderPaint.color = artStyle.accentColor
            borderPaint.strokeWidth = w * 0.002f
            borderPaint.alpha = 160

            canvas.drawRoundRect(pillRect, pillHeight / 2f, pillHeight / 2f, borderPaint)

            // Pill text
            canvas.drawText(
                badgeText,
                cx,
                currentY + pillHeight * 0.68f,
                textPaint
            )

            currentY += pillHeight + (h * 0.04f)
        }

        return currentY
    }

    private fun renderArtTypography(
        canvas: Canvas,
        w: Float,
        h: Float,
        startY: Float,
        title: String,
        subtitle: String,
        description: String,
        chefNote: String,
        artStyle: ModernArtworkStyle
    ) {
        val cx = w * 0.5f
        var curY = startY

        // 1. Subtitle / Origin Header (e.g. "CHEF'S SPECIALTY • RESERVE SELECTION")
        if (subtitle.isNotBlank()) {
            val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = artStyle.accentColor
                textSize = w * 0.026f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                letterSpacing = 0.3f
            }
            canvas.drawText(subtitle.uppercase(), cx, curY, subPaint)
            curY += h * 0.035f
        }

        // 2. Main Title (High-impact luxury typography with graceful wrapping)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = artStyle.textColor
            textSize = w * 0.065f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.08f
        }

        val titleLines = wrapText(title.ifBlank { "Culinary Masterpiece" }, titlePaint, w * 0.82f)
        for (line in titleLines) {
            canvas.drawText(line, cx, curY, titlePaint)
            curY += titlePaint.textSize * 1.25f
        }

        curY += h * 0.015f

        // 3. Contemporary divider motif (diamond or triple-dash)
        val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividerPaint.color = artStyle.accentColor
        dividerPaint.strokeWidth = w * 0.003f
        dividerPaint.style = Paint.Style.STROKE
        dividerPaint.alpha = 180

        val divLen = w * 0.12f
        canvas.drawLine(cx - divLen, curY, cx - w * 0.02f, curY, dividerPaint)
        canvas.drawLine(cx + w * 0.02f, curY, cx + divLen, curY, dividerPaint)

        // Center diamond
        val diamondPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        diamondPaint.color = artStyle.accentColor
        diamondPaint.style = Paint.Style.FILL

        val diaPath = Path().apply {
            moveTo(cx, curY - w * 0.012f)
            lineTo(cx + w * 0.012f, curY)
            lineTo(cx, curY + w * 0.012f)
            lineTo(cx - w * 0.012f, curY)
            close()
        }
        canvas.drawPath(diaPath, diamondPaint)

        curY += h * 0.045f

        // 4. Description Paragraph (Gastronomy tasting notes / preparation highlights)
        if (description.isNotBlank()) {
            val descPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = artStyle.textColor
                textSize = w * 0.032f
                typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.CENTER
                alpha = 210
            }

            val descLines = wrapText(description, descPaint, w * 0.78f)
            for (line in descLines) {
                canvas.drawText(line, cx, curY, descPaint)
                curY += descPaint.textSize * 1.35f
            }
            curY += h * 0.025f
        }

        // 5. Chef Note or Dietary/Tasting Stamp at the bottom
        if (chefNote.isNotBlank()) {
            val notePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = artStyle.accentColor
                textSize = w * 0.024f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                textAlign = Paint.Align.CENTER
                letterSpacing = 0.15f
                alpha = 220
            }

            val noteLines = wrapText("❖  ${chefNote.uppercase()}  ❖", notePaint, w * 0.82f)
            val noteY = (h * 0.93f).coerceAtLeast(curY + h * 0.02f)
            for ((idx, line) in noteLines.withIndex()) {
                canvas.drawText(line, cx, noteY + (idx * notePaint.textSize * 1.25f), notePaint)
            }
        }
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        for (word in words) {
            val candidate = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (paint.measureText(candidate) <= maxWidth) {
                currentLine = candidate
            } else {
                if (currentLine.isNotEmpty()) lines.add(currentLine)
                currentLine = word
            }
        }
        if (currentLine.isNotEmpty()) lines.add(currentLine)
        return lines
    }

    private fun drawStylizedLeaf(canvas: Canvas, x: Float, y: Float, size: Float, angleDeg: Float, paint: Paint) {
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(angleDeg)
        val path = Path().apply {
            moveTo(0f, -size)
            quadTo(size * 0.5f, 0f, 0f, size)
            quadTo(-size * 0.5f, 0f, 0f, -size)
            close()
        }
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun drawModernChevron(canvas: Canvas, x: Float, y: Float, size: Float, angleDeg: Float, paint: Paint) {
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(angleDeg)
        canvas.drawLine(-size, 0f, 0f, -size, paint)
        canvas.drawLine(0f, -size, size, 0f, paint)
        canvas.restore()
    }

    /**
     * Generates a ready-to-print vector PDF containing the rendered high-res restaurant artwork.
     */
    fun exportArtworkPdf(
        context: Context,
        title: String,
        subtitle: String,
        description: String,
        chefNote: String,
        style: ModernArtworkStyle,
        paperSize: String, // "A4 Portrait", "A4 Landscape", "A3 Portrait", "A3 Landscape"
        logo: Bitmap? = null,
        includeBadge: Boolean = true
    ): File {
        val isLandscape = paperSize.contains("Landscape", ignoreCase = true)
        val isA3 = paperSize.contains("A3", ignoreCase = true)

        // Standard PDF points (72 points per inch)
        val (baseWidth, baseHeight) = if (isA3) {
            if (isLandscape) Pair(1191, 842) else Pair(842, 1191)
        } else {
            if (isLandscape) Pair(842, 595) else Pair(595, 842)
        }

        // Render at 2x resolution for ultra sharp print fidelity
        val renderScale = 2
        val renderWidth = baseWidth * renderScale
        val renderHeight = baseHeight * renderScale

        val artworkBitmap = renderArtworkBitmap(
            title = title,
            subtitle = subtitle,
            description = description,
            chefNote = chefNote,
            style = style,
            width = renderWidth,
            height = renderHeight,
            establishmentLogo = logo,
            includeBadge = includeBadge
        )

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(baseWidth, baseHeight, 1).create()
        val page = document.startPage(pageInfo)
        val pdfCanvas = page.canvas

        // Draw scaled to fit PDF points
        val dstRect = android.graphics.Rect(0, 0, baseWidth, baseHeight)
        pdfCanvas.drawBitmap(artworkBitmap, null, dstRect, null)
        document.finishPage(page)

        val safeName = title.filter { it.isLetterOrDigit() }.ifBlank { "Artwork" }
        val outputFile = File(context.cacheDir, "Restaurant_Signage_${safeName}_${System.currentTimeMillis()}.pdf")
        FileOutputStream(outputFile).use { out ->
            document.writeTo(out)
        }
        document.close()

        return outputFile
    }

    /**
     * Exports the high-resolution artwork as a standalone PNG image file for sharing, digital signage displays, or menus.
     */
    fun exportArtworkImage(
        context: Context,
        title: String,
        subtitle: String,
        description: String,
        chefNote: String,
        style: ModernArtworkStyle,
        paperSize: String,
        logo: Bitmap? = null,
        includeBadge: Boolean = true
    ): File {
        val isLandscape = paperSize.contains("Landscape", ignoreCase = true)
        val isA3 = paperSize.contains("A3", ignoreCase = true)

        val (baseWidth, baseHeight) = if (isA3) {
            if (isLandscape) Pair(1754, 1240) else Pair(1240, 1754)
        } else {
            if (isLandscape) Pair(1240, 874) else Pair(874, 1240)
        }

        val artworkBitmap = renderArtworkBitmap(
            title = title,
            subtitle = subtitle,
            description = description,
            chefNote = chefNote,
            style = style,
            width = baseWidth,
            height = baseHeight,
            establishmentLogo = logo,
            includeBadge = includeBadge
        )

        val safeName = title.filter { it.isLetterOrDigit() }.ifBlank { "Artwork" }
        val outputFile = File(context.cacheDir, "Restaurant_Artwork_${safeName}_${System.currentTimeMillis()}.png")
        FileOutputStream(outputFile).use { out ->
            artworkBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return outputFile
    }

    /**
     * Renders an authentic food station signage bitmap with real food visual backdrop,
     * central white ribbon band, cursive calligraphy script title, and gold serif station tag.
     */
    fun renderFoodStationSignBitmap(
        scriptTitle: String,
        stationTag: String,
        dietaryTag: String = "",
        theme: FoodVisualTheme = FoodVisualTheme.MIDNIGHT_OBSIDIAN,
        customFoodBitmap: Bitmap? = null,
        bannerStyle: String = "White Ribbon",
        accentColorInt: Int = Color.parseColor("#9C7A4A"),
        width: Int,
        height: Int,
        establishmentLogo: Bitmap? = null,
        includeLogo: Boolean = false
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val w = width.toFloat()
        val h = height.toFloat()

        // 1. Draw Food Background Layer
        if (customFoodBitmap != null && !customFoodBitmap.isRecycled) {
            drawCenterCropBitmap(canvas, customFoodBitmap, w, h)
        } else {
            drawProceduralFoodBackground(canvas, w, h, theme)
        }

        // 2. Draw Central White Ribbon / Banner
        val ribbonHeight = h * 0.28f
        val ribbonTop = (h - ribbonHeight) / 2f
        val ribbonBottom = ribbonTop + ribbonHeight

        val isDarkBanner = bannerStyle.contains("Midnight", ignoreCase = true)
        val isGlass = bannerStyle.contains("Glass", ignoreCase = true)
        val hasGoldFrame = bannerStyle.contains("Gold", ignoreCase = true) || bannerStyle.contains("Frame", ignoreCase = true)

        val bannerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = when {
                isDarkBanner -> Color.parseColor("#151518")
                isGlass -> Color.parseColor("#F5FFFFFF")
                else -> Color.parseColor("#FFFFFF")
            }
            style = Paint.Style.FILL
        }

        // Soft drop shadow above and below the ribbon
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#44000000")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, ribbonTop - h * 0.008f, w, ribbonTop, shadowPaint)
        canvas.drawRect(0f, ribbonBottom, w, ribbonBottom + h * 0.008f, shadowPaint)

        // Draw main ribbon body
        canvas.drawRect(0f, ribbonTop, w, ribbonBottom, bannerPaint)

        // Gold decorative hairline accents along banner edges
        val goldLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isDarkBanner) Color.parseColor("#D4AF37") else Color.parseColor("#C5A059")
            strokeWidth = w * 0.0035f
            style = Paint.Style.STROKE
        }
        canvas.drawLine(0f, ribbonTop + h * 0.004f, w, ribbonTop + h * 0.004f, goldLinePaint)
        canvas.drawLine(0f, ribbonBottom - h * 0.004f, w, ribbonBottom - h * 0.004f, goldLinePaint)

        if (hasGoldFrame) {
            val innerGoldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#E5C158")
                strokeWidth = w * 0.0015f
                style = Paint.Style.STROKE
            }
            canvas.drawLine(w * 0.04f, ribbonTop + h * 0.012f, w * 0.96f, ribbonTop + h * 0.012f, innerGoldPaint)
            canvas.drawLine(w * 0.04f, ribbonBottom - h * 0.012f, w * 0.96f, ribbonBottom - h * 0.012f, innerGoldPaint)
        }

        // 3. Typography
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isDarkBanner) Color.parseColor("#FFFFFF") else Color.parseColor("#262626")
            textAlign = Paint.Align.CENTER
            // Use cursive or serif italic for elegant handwriting calligraphy script
            typeface = try {
                Typeface.create("cursive", Typeface.BOLD)
            } catch (e: Exception) {
                Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            }
            textSize = w * 0.088f
        }

        val tagPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentColorInt
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textSize = w * 0.082f
            letterSpacing = 0.28f // Sophisticated luxury tracking
        }

        val dietaryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isDarkBanner) Color.parseColor("#B3B3B3") else Color.parseColor("#6B7280")
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = w * 0.026f
            letterSpacing = 0.12f
        }

        // Center calculations
        val centerX = w * 0.5f
        val hasDietary = dietaryTag.isNotBlank()
        val hasLogo = includeLogo && establishmentLogo != null && !establishmentLogo.isRecycled

        // Calculate layout with dynamic wrapping
        val maxTextWidth = w * 0.88f
        val scriptLines = wrapText(scriptTitle, titlePaint, maxTextWidth)
        val tagLines = wrapText(stationTag.uppercase(), tagPaint, maxTextWidth)
        val dietaryLines = if (hasDietary) wrapText(dietaryTag.uppercase(), dietaryPaint, maxTextWidth) else emptyList()

        // Adaptive text scaling if too many lines
        if (scriptLines.size > 1) titlePaint.textSize *= 0.8f
        if (tagLines.size > 1) tagPaint.textSize *= 0.8f

        val totalTextHeight = (scriptLines.size * titlePaint.textSize * 1.1f) + 
                             (tagLines.size * tagPaint.textSize * 1.1f) + 
                             (if (hasDietary) dietaryLines.size * dietaryPaint.textSize * 1.2f else 0f)
        
        var curY = ribbonTop + (ribbonHeight - totalTextHeight) / 2f + titlePaint.textSize * 0.8f

        // Draw Cursive Script Title (Wrapped)
        for (line in scriptLines) {
            canvas.drawText(line, centerX, curY, titlePaint)
            curY += titlePaint.textSize * 1.1f
        }

        curY += h * 0.01f

        // Draw Gold Serif Station Tag (Wrapped)
        for (line in tagLines) {
            canvas.drawText(line, centerX, curY, tagPaint)
            curY += tagPaint.textSize * 1.1f
        }

        // Draw Dietary / Subtitle Tag if present (Wrapped)
        if (hasDietary) {
            curY += h * 0.015f
            for (line in dietaryLines) {
                canvas.drawText(line, centerX, curY, dietaryPaint)
                curY += dietaryPaint.textSize * 1.2f
            }
        }

        // 4. Establishment Logo if requested
        if (hasLogo && establishmentLogo != null) {
            val logoSize = (ribbonHeight * 0.28f).toInt().coerceAtLeast(32)
            val logoX = (centerX - logoSize / 2f).toInt()
            val logoY = (ribbonTop + h * 0.015f).toInt()
            val dstRect = Rect(logoX, logoY, logoX + logoSize, logoY + logoSize)
            canvas.drawBitmap(establishmentLogo, null, dstRect, Paint(Paint.ANTI_ALIAS_FLAG))
        }

        return bitmap
    }

    private fun drawCenterCropBitmap(canvas: Canvas, source: Bitmap, targetW: Float, targetH: Float) {
        val srcW = source.width.toFloat()
        val srcH = source.height.toFloat()
        val scale = maxOf(targetW / srcW, targetH / srcH)
        val scaledW = srcW * scale
        val scaledH = srcH * scale
        val dx = (targetW - scaledW) / 2f
        val dy = (targetH - scaledH) / 2f

        val matrix = Matrix().apply {
            postScale(scale, scale)
            postTranslate(dx, dy)
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(source, matrix, paint)

        // Luxury vignette gradient to enhance contrast behind central calligraphy ribbon
        val vignette = RadialGradient(
            targetW / 2f, targetH / 2f,
            maxOf(targetW, targetH) * 0.75f,
            intArrayOf(Color.TRANSPARENT, Color.argb(85, 0, 0, 0)),
            floatArrayOf(0.55f, 1.0f),
            Shader.TileMode.CLAMP
        )
        val vPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { shader = vignette }
        canvas.drawRect(0f, 0f, targetW, targetH, vPaint)
    }

    /**
     * Draws a mouth-watering, realistic culinary scene tailored to each food theme.
     */
    private fun drawProceduralFoodBackground(canvas: Canvas, w: Float, h: Float, theme: FoodVisualTheme) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = android.graphics.RectF(0f, 0f, w, h)

        when (theme) {
            FoodVisualTheme.MIDNIGHT_OBSIDIAN -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#0F172A"), Color.parseColor("#020617"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
                paint.shader = null
                paint.color = Color.parseColor("#33D4AF37")
                paint.strokeWidth = 3f
                canvas.drawLine(0f, h*0.2f, w, h*0.3f, paint)
                canvas.drawLine(0f, h*0.8f, w, h*0.7f, paint)
            }
            FoodVisualTheme.IVORY_SILK -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#FDFBF7"), Color.parseColor("#EBDDC1"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.BRUSHED_GOLD -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#D4AF37"), Color.parseColor("#9C7A4A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.EMERALD_VELVET -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#065F46"), Color.parseColor("#022C22"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.SAPPHIRE_GRADIENT -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#1E3A8A"), Color.parseColor("#0F172A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.CRIMSON_DAMASK -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#991B1B"), Color.parseColor("#450A0A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.CHARCOAL_MATTE -> {
                canvas.drawColor(Color.parseColor("#171717"))
            }
            FoodVisualTheme.ROSE_GOLD -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#F4C4C4"), Color.parseColor("#D39292"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.FROSTED_GLASS -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#E0F2FE"), Color.parseColor("#BAE6FD"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.PLATINUM_MESH -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#E2E8F0"), Color.parseColor("#94A3B8"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.BRONZE_TEXTURE -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#78350F"), Color.parseColor("#451A03"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.PEARL_GLAZE -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#FFFFFF"), Color.parseColor("#F1F5F9"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.ROYAL_BURGUNDY -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#831843"), Color.parseColor("#4C0519"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.TITANIUM_WEAVE -> {
                canvas.drawColor(Color.parseColor("#334155"))
            }
            FoodVisualTheme.CHAMPAGNE_SPARKLE -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#FEF3C7"), Color.parseColor("#FDE68A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
        }
    }

    // --- PROCEDURAL FOOD ELEMENT RENDERERS ---

    private fun drawBambooSteamerWithDimSum(canvas: Canvas, cx: Float, cy: Float, radius: Float, isTop: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Outer bamboo rim
        paint.color = Color.parseColor("#D4A373")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)

        paint.color = Color.parseColor("#A67C52")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = radius * 0.08f
        canvas.drawCircle(cx, cy, radius * 0.96f, paint)

        // Steamer paper interior
        paint.color = Color.parseColor("#EED7B8")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius * 0.90f, paint)

        // Steamer slats
        paint.color = Color.parseColor("#8A623D")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = radius * 0.03f
        val slatStep = radius * 0.28f
        for (x in -2..2) {
            canvas.drawLine(cx + x * slatStep, cy - radius * 0.8f, cx + x * slatStep, cy + radius * 0.8f, paint)
        }

        // Steamed dumplings inside
        val dumplingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FBF5E8")
            style = Paint.Style.FILL
        }
        val shadow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#33A67C52")
            style = Paint.Style.FILL
        }

        val dR = radius * 0.24f
        val positions = listOf(
            Pair(cx - radius * 0.45f, cy),
            Pair(cx + radius * 0.45f, cy),
            Pair(cx, cy - radius * 0.35f),
            Pair(cx, cy + radius * 0.35f),
            Pair(cx - radius * 0.25f, cy - radius * 0.45f),
            Pair(cx + radius * 0.25f, cy + radius * 0.45f)
        )

        for ((dx, dy) in positions) {
            canvas.drawCircle(dx + dR * 0.06f, dy + dR * 0.1f, dR, shadow)
            canvas.drawCircle(dx, dy, dR, dumplingPaint)
            // Dumpling pleats
            val pleatPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#D8C3A5")
                strokeWidth = dR * 0.12f
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
            }
            for (ang in 0 until 360 step 60) {
                val rad = Math.toRadians(ang.toDouble())
                val px = dx + (dR * 0.5f * cos(rad)).toFloat()
                val py = dy + (dR * 0.5f * sin(rad)).toFloat()
                canvas.drawLine(dx, dy, px, py, pleatPaint)
            }
        }
    }

    private fun drawPizzaHalf(canvas: Canvas, cx: Float, cy: Float, radius: Float, isTop: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Pizza crust
        paint.color = Color.parseColor("#C2884A")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)

        // Pizza char blisters
        paint.color = Color.parseColor("#693F19")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = radius * 0.05f
        canvas.drawCircle(cx, cy, radius * 0.97f, paint)

        // Tomato sauce & melted cheese body
        paint.color = Color.parseColor("#D94A26")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius * 0.88f, paint)

        paint.color = Color.parseColor("#FEE8A2")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius * 0.82f, paint)

        // Toppings: Pepperoni & Black olives
        val pepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#A82D1D")
            style = Paint.Style.FILL
        }
        val olivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1C1E18")
            style = Paint.Style.STROKE
            strokeWidth = radius * 0.04f
        }

        val toppingOffsets = listOf(
            Pair(-0.4f, 0.2f), Pair(0.4f, -0.2f), Pair(-0.2f, -0.35f), Pair(0.25f, 0.35f),
            Pair(-0.5f, -0.2f), Pair(0.5f, 0.2f), Pair(0f, 0.4f), Pair(0f, -0.4f)
        )
        for ((ox, oy) in toppingOffsets) {
            val tx = cx + ox * radius
            val ty = cy + oy * radius
            canvas.drawCircle(tx, ty, radius * 0.12f, pepPaint)
            canvas.drawCircle(tx + radius * 0.1f, ty - radius * 0.06f, radius * 0.06f, olivePaint)
        }
    }

    private fun drawPastaBowl(canvas: Canvas, cx: Float, cy: Float, radius: Float, isTop: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Terracotta bowl
        paint.color = Color.parseColor("#9C4828")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)

        paint.color = Color.parseColor("#5A2510")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = radius * 0.06f
        canvas.drawCircle(cx, cy, radius * 0.95f, paint)

        // Arrabbiata sauce
        paint.color = Color.parseColor("#B32918")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius * 0.88f, paint)

        // Penne pasta noodles
        val pastaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E6B15C")
            strokeWidth = radius * 0.09f
            strokeCap = Paint.Cap.ROUND
        }
        val pOffsets = listOf(
            Pair(-0.4f, 0.1f), Pair(0.3f, -0.2f), Pair(-0.1f, -0.4f), Pair(0.2f, 0.3f),
            Pair(-0.3f, -0.3f), Pair(0.4f, 0.2f), Pair(-0.2f, 0.4f), Pair(0f, 0f)
        )
        for ((ox, oy) in pOffsets) {
            val px = cx + ox * radius
            val py = cy + oy * radius
            canvas.drawLine(px - radius * 0.12f, py - radius * 0.05f, px + radius * 0.12f, py + radius * 0.05f, pastaPaint)
        }

        // Cherry tomatoes & basil
        val tomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#D32F2F") }
        canvas.drawCircle(cx - radius * 0.2f, cy + radius * 0.15f, radius * 0.1f, tomPaint)
        canvas.drawCircle(cx + radius * 0.25f, cy - radius * 0.15f, radius * 0.09f, tomPaint)
        drawHerbSprig(canvas, cx + radius * 0.1f, cy + radius * 0.25f, Color.parseColor("#388E3C"))
    }

    private fun drawChaatPlatter(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Plate
        paint.color = Color.parseColor("#B08953")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)

        // Crispy golden papdis
        val papdiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E0A954") }
        canvas.drawCircle(cx - radius * 0.3f, cy - radius * 0.1f, radius * 0.25f, papdiPaint)
        canvas.drawCircle(cx + radius * 0.25f, cy + radius * 0.1f, radius * 0.28f, papdiPaint)
        canvas.drawCircle(cx, cy + radius * 0.2f, radius * 0.24f, papdiPaint)

        // Curd / yogurt dollop
        paint.color = Color.parseColor("#F5F5F5")
        canvas.drawCircle(cx, cy, radius * 0.32f, paint)

        // Tamarind & mint chutney drizzles
        val tamarindPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#6A1B0C")
            strokeWidth = radius * 0.04f
            style = Paint.Style.STROKE
        }
        canvas.drawLine(cx - radius * 0.3f, cy - radius * 0.1f, cx + radius * 0.3f, cy + radius * 0.15f, tamarindPaint)

        // Pomegranate ruby jewels
        val pomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#C2185B") }
        val poms = listOf(Pair(-0.1f, -0.1f), Pair(0.15f, 0.05f), Pair(-0.05f, 0.2f), Pair(0.2f, -0.15f), Pair(-0.25f, 0.15f))
        for ((px, py) in poms) {
            canvas.drawCircle(cx + px * radius, cy + py * radius, radius * 0.04f, pomPaint)
        }
    }

    private fun drawFajitaTacos(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Tortilla wraps
        paint.color = Color.parseColor("#E8C897")
        canvas.drawOval(RectF(cx - radius * 0.7f, cy - radius * 0.35f, cx + radius * 0.7f, cy + radius * 0.35f), paint)

        // Grilled peppers (red, yellow, green)
        val pepperPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = radius * 0.06f
            strokeCap = Paint.Cap.ROUND
        }
        pepperPaint.color = Color.parseColor("#D32F2F")
        canvas.drawLine(cx - radius * 0.4f, cy - radius * 0.1f, cx, cy, pepperPaint)
        pepperPaint.color = Color.parseColor("#FBC02D")
        canvas.drawLine(cx - radius * 0.1f, cy - radius * 0.15f, cx + radius * 0.3f, cy, pepperPaint)
        pepperPaint.color = Color.parseColor("#388E3C")
        canvas.drawLine(cx - radius * 0.3f, cy + radius * 0.1f, cx + radius * 0.2f, cy + radius * 0.1f, pepperPaint)

        // Lime wedge
        val limePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#7CB342") }
        canvas.drawCircle(cx + radius * 0.45f, cy + radius * 0.15f, radius * 0.14f, limePaint)
    }

    private fun drawSaladBowl(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Turquoise ceramic bowl
        paint.color = Color.parseColor("#5FB3AB")
        canvas.drawCircle(cx, cy, radius, paint)

        // Greens
        paint.color = Color.parseColor("#4CAF50")
        canvas.drawCircle(cx, cy, radius * 0.88f, paint)

        // Grilled salmon strip with grill marks
        paint.color = Color.parseColor("#F07151")
        val salmonRect = RectF(cx - radius * 0.18f, cy - radius * 0.5f, cx + radius * 0.18f, cy + radius * 0.5f)
        canvas.drawRoundRect(salmonRect, radius * 0.08f, radius * 0.08f, paint)

        val grillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#6A2A1E")
            strokeWidth = radius * 0.035f
        }
        for (i in -3..3) {
            val y = cy + i * radius * 0.12f
            canvas.drawLine(cx - radius * 0.14f, y, cx + radius * 0.14f, y, grillPaint)
        }

        // Orange wedges & cherry tomatoes
        paint.color = Color.parseColor("#FF9800")
        canvas.drawCircle(cx + radius * 0.35f, cy - radius * 0.2f, radius * 0.12f, paint)
        paint.color = Color.parseColor("#E53935")
        canvas.drawCircle(cx - radius * 0.35f, cy + radius * 0.25f, radius * 0.08f, paint)
    }

    private fun drawKulfiPlate(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Dark ceramic plate with spiral rim
        paint.color = Color.parseColor("#382721")
        canvas.drawCircle(cx, cy, radius, paint)

        // Malai Kulfi on sticks
        paint.color = Color.parseColor("#F6E27A")
        val kulfiPath = Path().apply {
            moveTo(cx - radius * 0.4f, cy)
            lineTo(cx + radius * 0.2f, cy - radius * 0.25f)
            lineTo(cx + radius * 0.3f, cy + radius * 0.25f)
            close()
        }
        canvas.drawPath(kulfiPath, paint)

        // Wooden stick
        paint.color = Color.parseColor("#C29E75")
        paint.strokeWidth = radius * 0.06f
        canvas.drawLine(cx + radius * 0.25f, cy, cx + radius * 0.65f, cy, paint)

        // Saffron threads & emerald pistachio slivers
        val saffPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#D84315"); strokeWidth = radius * 0.02f }
        canvas.drawLine(cx - radius * 0.1f, cy - radius * 0.05f, cx, cy - radius * 0.1f, saffPaint)
        val pistPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#558B2F") }
        canvas.drawCircle(cx - radius * 0.2f, cy + radius * 0.05f, radius * 0.035f, pistPaint)
        canvas.drawCircle(cx + radius * 0.05f, cy + radius * 0.08f, radius * 0.03f, pistPaint)
    }

    private fun drawMithaiThali(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Brass thali
        paint.color = Color.parseColor("#C79D4C")
        canvas.drawCircle(cx, cy, radius, paint)

        // Golden Besan & Boondi Laddoos
        val laddooPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#F9A825") }
        canvas.drawCircle(cx - radius * 0.25f, cy - radius * 0.15f, radius * 0.16f, laddooPaint)
        canvas.drawCircle(cx + radius * 0.2f, cy - radius * 0.15f, radius * 0.16f, laddooPaint)
        canvas.drawCircle(cx, cy + radius * 0.18f, radius * 0.18f, laddooPaint)

        // Layered Soan Papdi & Pistachio Peda
        val pedaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E6C280") }
        canvas.drawRect(cx + radius * 0.15f, cy + radius * 0.05f, cx + radius * 0.45f, cy + radius * 0.35f, pedaPaint)
    }

    private fun drawPalakPaneer(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Cast iron pan
        paint.color = Color.parseColor("#212121")
        canvas.drawCircle(cx, cy, radius, paint)

        // Spiced spinach palak puree
        paint.color = Color.parseColor("#2E5A27")
        canvas.drawCircle(cx, cy, radius * 0.88f, paint)

        // Seared white paneer cubes
        val paneerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FFFDF5") }
        val pBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#C49A45")
            style = Paint.Style.STROKE
            strokeWidth = radius * 0.02f
        }

        val pCubes = listOf(Pair(-0.35f, -0.1f), Pair(0.25f, -0.2f), Pair(-0.1f, 0.25f), Pair(0.2f, 0.2f))
        for ((cxOffset, cyOffset) in pCubes) {
            val px = cx + cxOffset * radius
            val py = cy + cyOffset * radius
            val size = radius * 0.18f
            val rect = RectF(px - size / 2, py - size / 2, px + size / 2, py + size / 2)
            canvas.drawRoundRect(rect, radius * 0.03f, radius * 0.03f, paneerPaint)
            canvas.drawRoundRect(rect, radius * 0.03f, radius * 0.03f, pBorder)
        }
    }

    private fun drawArabicSkewers(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // White platter
        paint.color = Color.parseColor("#ECEFF1")
        canvas.drawRoundRect(RectF(cx - radius * 0.85f, cy - radius * 0.4f, cx + radius * 0.85f, cy + radius * 0.4f), radius * 0.2f, radius * 0.2f, paint)

        // Skewer metal bar
        paint.color = Color.parseColor("#78909C")
        paint.strokeWidth = radius * 0.04f
        canvas.drawLine(cx - radius * 0.8f, cy, cx + radius * 0.8f, cy, paint)

        // Charred grilled kebab chunks
        val kebabPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#8D381E") }
        for (i in -3..3) {
            val kx = cx + i * radius * 0.2f
            canvas.drawRoundRect(RectF(kx - radius * 0.08f, cy - radius * 0.15f, kx + radius * 0.08f, cy + radius * 0.15f), radius * 0.04f, radius * 0.04f, kebabPaint)
        }

        // Fresh mint tahini dip & onion rings
        paint.color = Color.parseColor("#689F38")
        canvas.drawCircle(cx + radius * 0.55f, cy + radius * 0.18f, radius * 0.12f, paint)
    }

    private fun drawLacyAppams(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Lacy fermented appam
        paint.color = Color.parseColor("#D7A863")
        canvas.drawCircle(cx, cy, radius, paint)

        paint.color = Color.parseColor("#FFFDF5")
        canvas.drawCircle(cx, cy, radius * 0.72f, paint)

        // Lacey perforations
        val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#B5823E") }
        for (i in 0 until 360 step 30) {
            val rad = Math.toRadians(i.toDouble())
            val hx = cx + (radius * 0.84f * cos(rad)).toFloat()
            val hy = cy + (radius * 0.84f * sin(rad)).toFloat()
            canvas.drawCircle(hx, hy, radius * 0.04f, holePaint)
        }
    }

    private fun drawVintageTeaSetting(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Porcelain saucer
        paint.color = Color.parseColor("#F5F5F5")
        canvas.drawCircle(cx, cy, radius, paint)

        // Gold rim
        paint.color = Color.parseColor("#D4AF37")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = radius * 0.03f
        canvas.drawCircle(cx, cy, radius * 0.94f, paint)

        // Amber tea
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#BF6017")
        canvas.drawCircle(cx, cy, radius * 0.62f, paint)

        // Tea cake slice
        val cakePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E59D42") }
        canvas.drawRoundRect(RectF(cx + radius * 0.45f, cy - radius * 0.3f, cx + radius * 0.8f, cy + radius * 0.1f), radius * 0.05f, radius * 0.05f, cakePaint)
    }

    private fun drawMongolianCasserole(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // White ceramic pot
        paint.color = Color.parseColor("#EEEEEE")
        canvas.drawCircle(cx, cy, radius, paint)

        // Savory glazed braised meat
        paint.color = Color.parseColor("#6A2E12")
        canvas.drawCircle(cx, cy, radius * 0.84f, paint)

        // Quail eggs and green scallions
        val eggPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E0A96D") }
        canvas.drawCircle(cx - radius * 0.25f, cy - radius * 0.1f, radius * 0.14f, eggPaint)
        canvas.drawCircle(cx + radius * 0.2f, cy + radius * 0.15f, radius * 0.13f, eggPaint)

        val scallion = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#43A047"); strokeWidth = radius * 0.03f }
        canvas.drawLine(cx - radius * 0.1f, cy, cx + radius * 0.1f, cy + radius * 0.1f, scallion)
    }

    private fun drawPurisAndSubzi(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Kadhai
        paint.color = Color.parseColor("#2D241E")
        canvas.drawCircle(cx, cy, radius, paint)

        // Golden puffed puris
        val puriPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E5A64E") }
        canvas.drawCircle(cx - radius * 0.3f, cy, radius * 0.35f, puriPaint)
        canvas.drawCircle(cx + radius * 0.25f, cy + radius * 0.15f, radius * 0.32f, puriPaint)
    }

    private fun drawAsianWokStirFry(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Dark carbon wok
        paint.color = Color.parseColor("#1B1A19")
        canvas.drawCircle(cx, cy, radius, paint)

        // Sizzling stir-fry elements: green beans, peppers, chicken
        val beanPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#33691E")
            strokeWidth = radius * 0.06f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(cx - radius * 0.3f, cy - radius * 0.1f, cx - radius * 0.1f, cy + radius * 0.2f, beanPaint)
        canvas.drawLine(cx + radius * 0.1f, cy - radius * 0.2f, cx + radius * 0.35f, cy, beanPaint)

        val chickenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#D39247") }
        canvas.drawOval(RectF(cx - radius * 0.15f, cy - radius * 0.1f, cx + radius * 0.15f, cy + radius * 0.1f), chickenPaint)

        val pepper = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#C62828"); strokeWidth = radius * 0.05f }
        canvas.drawLine(cx - radius * 0.35f, cy + radius * 0.1f, cx - radius * 0.15f, cy + radius * 0.3f, pepper)
    }

    private fun drawHerbSprig(canvas: Canvas, x: Float, y: Float, color: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }
        canvas.drawCircle(x, y, 14f, paint)
        canvas.drawCircle(x + 10f, y - 8f, 10f, paint)
        canvas.drawCircle(x - 8f, y + 6f, 10f, paint)
    }

    private fun drawBasilLeaves(canvas: Canvas, x: Float, y: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#2E7D32") }
        canvas.drawOval(RectF(x - 18f, y - 10f, x + 18f, y + 10f), paint)
    }

    /**
     * Exports the authentic Food Station Sign as a print-ready 300 DPI PDF.
     */
    fun exportFoodStationPdf(
        context: Context,
        scriptTitle: String,
        stationTag: String,
        dietaryTag: String = "",
        theme: FoodVisualTheme = FoodVisualTheme.MIDNIGHT_OBSIDIAN,
        customFoodBitmap: Bitmap? = null,
        bannerStyle: String = "White Ribbon",
        accentColorInt: Int = Color.parseColor("#9C7A4A"),
        paperSize: String = "A4 Portrait",
        logo: Bitmap? = null,
        includeLogo: Boolean = false
    ): File {
        val isLandscape = paperSize.contains("Landscape", ignoreCase = true)
        val isA3 = paperSize.contains("A3", ignoreCase = true)

        val (baseWidth, baseHeight) = if (isA3) {
            if (isLandscape) Pair(1191, 842) else Pair(842, 1191)
        } else {
            if (isLandscape) Pair(842, 595) else Pair(595, 842)
        }

        val renderScale = 2
        val renderWidth = baseWidth * renderScale
        val renderHeight = baseHeight * renderScale

        val bitmap = renderFoodStationSignBitmap(
            scriptTitle = scriptTitle,
            stationTag = stationTag,
            dietaryTag = dietaryTag,
            theme = theme,
            customFoodBitmap = customFoodBitmap,
            bannerStyle = bannerStyle,
            accentColorInt = accentColorInt,
            width = renderWidth,
            height = renderHeight,
            establishmentLogo = logo,
            includeLogo = includeLogo
        )

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(baseWidth, baseHeight, 1).create()
        val page = document.startPage(pageInfo)
        val pdfCanvas = page.canvas

        val dstRect = Rect(0, 0, baseWidth, baseHeight)
        pdfCanvas.drawBitmap(bitmap, null, dstRect, null)
        document.finishPage(page)

        val safeName = "${scriptTitle}_${stationTag}".filter { it.isLetterOrDigit() }.ifBlank { "Signage" }
        val outputFile = File(context.cacheDir, "FoodStation_Signage_${safeName}_${System.currentTimeMillis()}.pdf")
        FileOutputStream(outputFile).use { out ->
            document.writeTo(out)
        }
        document.close()

        return outputFile
    }

    /**
     * Exports the authentic Food Station Sign as a high-resolution PNG image.
     */
    fun exportFoodStationImage(
        context: Context,
        scriptTitle: String,
        stationTag: String,
        dietaryTag: String = "",
        theme: FoodVisualTheme = FoodVisualTheme.MIDNIGHT_OBSIDIAN,
        customFoodBitmap: Bitmap? = null,
        bannerStyle: String = "White Ribbon",
        accentColorInt: Int = Color.parseColor("#9C7A4A"),
        paperSize: String = "A4 Portrait",
        logo: Bitmap? = null,
        includeLogo: Boolean = false
    ): File {
        val isLandscape = paperSize.contains("Landscape", ignoreCase = true)
        val isA3 = paperSize.contains("A3", ignoreCase = true)

        val (baseWidth, baseHeight) = if (isA3) {
            if (isLandscape) Pair(1754, 1240) else Pair(1240, 1754)
        } else {
            if (isLandscape) Pair(1240, 874) else Pair(874, 1240)
        }

        val bitmap = renderFoodStationSignBitmap(
            scriptTitle = scriptTitle,
            stationTag = stationTag,
            dietaryTag = dietaryTag,
            theme = theme,
            customFoodBitmap = customFoodBitmap,
            bannerStyle = bannerStyle,
            accentColorInt = accentColorInt,
            width = baseWidth,
            height = baseHeight,
            establishmentLogo = logo,
            includeLogo = includeLogo
        )

        val safeName = "${scriptTitle}_${stationTag}".filter { it.isLetterOrDigit() }.ifBlank { "Signage" }
        val outputFile = File(context.cacheDir, "FoodStation_${safeName}_${System.currentTimeMillis()}.png")
        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return outputFile
    }
}


package com.example.util

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.util.Base64
import java.io.File
import java.io.FileOutputStream

object NameTagPdfGenerator {

    enum class BorderStyle {
        DOUBLE_GOLD,
        TRIPLE_EXECUTIVE,
        PRESIDENTIAL_DUAL,
        ROMAN_BRONZE,
        ROYAL_EMBOSSED,
        GEOMETRIC_DIAMOND,
        MODERN_MINIMAL,
        VINTAGE_FILIGREE,
        STONE_FRAME,
        EMERALD_CREST
    }

    enum class DividerStyle {
        GOLD_DIAMOND,
        TRIPLE_STAR,
        DIPLOMATIC_BAR,
        CLASSIC_LEAF,
        ROYAL_CROWN,
        GEOMETRIC_DOTS,
        MODERN_HAIRLINE,
        VICTORIAN_FLOURISH,
        MINIMAL_DASH,
        HERALDIC_BAR
    }

    enum class CornerStyle {
        CLASSIC_BRACKET,
        DIAMOND_LOZENGE,
        DIPLOMATIC_STEP,
        ROMAN_CORNER,
        ROYAL_CREST,
        PRECISION_DIAMOND,
        MODERN_L,
        VICTORIAN_SWIRL,
        CHAMFER_TICK,
        EMERALD_SHIELD
    }

    data class DeskTentTheme(
        val id: Int,
        val name: String,
        val subtitle: String,
        val backgroundColor: Int,
        val outerBorderColor: Int,
        val innerBorderColor: Int,
        val cornerAccentColor: Int,
        val nameTextColor: Int,
        val designationTextColor: Int,
        val dividerColor: Int,
        val isDarkTheme: Boolean = false,
        val borderStyle: BorderStyle = BorderStyle.DOUBLE_GOLD,
        val dividerStyle: DividerStyle = DividerStyle.GOLD_DIAMOND,
        val cornerStyle: CornerStyle = CornerStyle.CLASSIC_BRACKET
    )

    val THEMES = listOf(
        DeskTentTheme(
            id = 0,
            name = "Signature Ivory",
            subtitle = "Classic Gold on Warm Ivory",
            backgroundColor = Color.parseColor("#FFF9EE"),
            outerBorderColor = Color.parseColor("#D4AF37"),
            innerBorderColor = Color.parseColor("#E5C158"),
            cornerAccentColor = Color.parseColor("#D4AF37"),
            nameTextColor = Color.parseColor("#1A1A1A"),
            designationTextColor = Color.parseColor("#8C6B1B"),
            dividerColor = Color.parseColor("#D4AF37"),
            isDarkTheme = false,
            borderStyle = BorderStyle.DOUBLE_GOLD,
            dividerStyle = DividerStyle.GOLD_DIAMOND,
            cornerStyle = CornerStyle.CLASSIC_BRACKET
        ),
        DeskTentTheme(
            id = 1,
            name = "Executive Gold",
            subtitle = "Midnight Navy with Gilded Gold",
            backgroundColor = Color.parseColor("#0F172A"),
            outerBorderColor = Color.parseColor("#FFD700"),
            innerBorderColor = Color.parseColor("#F59E0B"),
            cornerAccentColor = Color.parseColor("#FFE066"),
            nameTextColor = Color.parseColor("#FFFDF0"),
            designationTextColor = Color.parseColor("#FBBF24"),
            dividerColor = Color.parseColor("#FFD700"),
            isDarkTheme = true,
            borderStyle = BorderStyle.TRIPLE_EXECUTIVE,
            dividerStyle = DividerStyle.TRIPLE_STAR,
            cornerStyle = CornerStyle.DIAMOND_LOZENGE
        ),
        DeskTentTheme(
            id = 2,
            name = "Presidential Fold",
            subtitle = "Diplomatic Navy & Crimson Inset",
            backgroundColor = Color.parseColor("#FFFFFF"),
            outerBorderColor = Color.parseColor("#1E3A8A"),
            innerBorderColor = Color.parseColor("#991B1B"),
            cornerAccentColor = Color.parseColor("#1E3A8A"),
            nameTextColor = Color.parseColor("#0F172A"),
            designationTextColor = Color.parseColor("#1E3A8A"),
            dividerColor = Color.parseColor("#991B1B"),
            isDarkTheme = false,
            borderStyle = BorderStyle.PRESIDENTIAL_DUAL,
            dividerStyle = DividerStyle.DIPLOMATIC_BAR,
            cornerStyle = CornerStyle.DIPLOMATIC_STEP
        ),
        DeskTentTheme(
            id = 3,
            name = "Imperial Serif",
            subtitle = "Aged Parchment with Burnished Bronze",
            backgroundColor = Color.parseColor("#FDF6E2"),
            outerBorderColor = Color.parseColor("#78350F"),
            innerBorderColor = Color.parseColor("#B45309"),
            cornerAccentColor = Color.parseColor("#78350F"),
            nameTextColor = Color.parseColor("#291307"),
            designationTextColor = Color.parseColor("#92400E"),
            dividerColor = Color.parseColor("#B45309"),
            isDarkTheme = false,
            borderStyle = BorderStyle.ROMAN_BRONZE,
            dividerStyle = DividerStyle.CLASSIC_LEAF,
            cornerStyle = CornerStyle.ROMAN_CORNER
        ),
        DeskTentTheme(
            id = 4,
            name = "Royal Accents",
            subtitle = "Sapphire Pearl with Crown Gold",
            backgroundColor = Color.parseColor("#F0F4FA"),
            outerBorderColor = Color.parseColor("#1E40AF"),
            innerBorderColor = Color.parseColor("#D97706"),
            cornerAccentColor = Color.parseColor("#D97706"),
            nameTextColor = Color.parseColor("#0F2557"),
            designationTextColor = Color.parseColor("#B45309"),
            dividerColor = Color.parseColor("#D97706"),
            isDarkTheme = false,
            borderStyle = BorderStyle.ROYAL_EMBOSSED,
            dividerStyle = DividerStyle.ROYAL_CROWN,
            cornerStyle = CornerStyle.ROYAL_CREST
        ),
        DeskTentTheme(
            id = 5,
            name = "Classic Diamond",
            subtitle = "Alabaster with Platinum Slate",
            backgroundColor = Color.parseColor("#F8FAFC"),
            outerBorderColor = Color.parseColor("#334155"),
            innerBorderColor = Color.parseColor("#94A3B8"),
            cornerAccentColor = Color.parseColor("#334155"),
            nameTextColor = Color.parseColor("#0F172A"),
            designationTextColor = Color.parseColor("#475569"),
            dividerColor = Color.parseColor("#334155"),
            isDarkTheme = false,
            borderStyle = BorderStyle.GEOMETRIC_DIAMOND,
            dividerStyle = DividerStyle.GEOMETRIC_DOTS,
            cornerStyle = CornerStyle.PRECISION_DIAMOND
        ),
        DeskTentTheme(
            id = 6,
            name = "Modern Classic",
            subtitle = "Minimalist Studio Black & White",
            backgroundColor = Color.parseColor("#FFFFFF"),
            outerBorderColor = Color.parseColor("#18181B"),
            innerBorderColor = Color.parseColor("#E4E4E7"),
            cornerAccentColor = Color.parseColor("#18181B"),
            nameTextColor = Color.parseColor("#09090B"),
            designationTextColor = Color.parseColor("#71717A"),
            dividerColor = Color.parseColor("#18181B"),
            isDarkTheme = false,
            borderStyle = BorderStyle.MODERN_MINIMAL,
            dividerStyle = DividerStyle.MODERN_HAIRLINE,
            cornerStyle = CornerStyle.MODERN_L
        ),
        DeskTentTheme(
            id = 7,
            name = "Vintage Luxury",
            subtitle = "Warm Linen & Victorian Filigree",
            backgroundColor = Color.parseColor("#FAF4E8"),
            outerBorderColor = Color.parseColor("#92400E"),
            innerBorderColor = Color.parseColor("#D97706"),
            cornerAccentColor = Color.parseColor("#92400E"),
            nameTextColor = Color.parseColor("#3A1F10"),
            designationTextColor = Color.parseColor("#854D0E"),
            dividerColor = Color.parseColor("#D97706"),
            isDarkTheme = false,
            borderStyle = BorderStyle.VINTAGE_FILIGREE,
            dividerStyle = DividerStyle.VICTORIAN_FLOURISH,
            cornerStyle = CornerStyle.VICTORIAN_SWIRL
        ),
        DeskTentTheme(
            id = 8,
            name = "Minimalist Elite",
            subtitle = "Warm Stone & Precision Inset",
            backgroundColor = Color.parseColor("#F5F5F4"),
            outerBorderColor = Color.parseColor("#57534E"),
            innerBorderColor = Color.parseColor("#D6D3D1"),
            cornerAccentColor = Color.parseColor("#57534E"),
            nameTextColor = Color.parseColor("#1C1917"),
            designationTextColor = Color.parseColor("#78716C"),
            dividerColor = Color.parseColor("#57534E"),
            isDarkTheme = false,
            borderStyle = BorderStyle.STONE_FRAME,
            dividerStyle = DividerStyle.MINIMAL_DASH,
            cornerStyle = CornerStyle.CHAMFER_TICK
        ),
        DeskTentTheme(
            id = 9,
            name = "Majestic Standard",
            subtitle = "Imperial Emerald with Radiant Gold",
            backgroundColor = Color.parseColor("#F0FDF4"),
            outerBorderColor = Color.parseColor("#065F46"),
            innerBorderColor = Color.parseColor("#D4AF37"),
            cornerAccentColor = Color.parseColor("#065F46"),
            nameTextColor = Color.parseColor("#064E3B"),
            designationTextColor = Color.parseColor("#B45309"),
            dividerColor = Color.parseColor("#065F46"),
            isDarkTheme = false,
            borderStyle = BorderStyle.EMERALD_CREST,
            dividerStyle = DividerStyle.HERALDIC_BAR,
            cornerStyle = CornerStyle.EMERALD_SHIELD
        )
    )

    fun getTheme(index: Int): DeskTentTheme {
        return THEMES.getOrElse(index) { THEMES[0] }
    }

    data class ValidationResult(
        val isNameTooLong: Boolean,
        val isDesignationTooLong: Boolean,
        val nameWidth: Float,
        val designationWidth: Float,
        val maxAllowedWidth: Float,
        val suggestedScale: Float,
        val currentScale: Float
    ) {
        val hasOverflow: Boolean get() = isNameTooLong || isDesignationTooLong
    }

    fun validateDimensions(
        name: String,
        designation: String,
        pageSize: String = "A5",
        templateIndex: Int = 0,
        fontScale: Float = 1.0f,
        salutation: String? = null
    ): ValidationResult {
        val pageWidth = if (pageSize == "A4") 842f else 595f
        // Safe printable width avoiding borders & corner accents per theme
        val safeMargin = when (templateIndex) {
            1, 4, 7 -> 75f // Executive, Royal Accents, Vintage Luxury have larger ornate corners
            6, 8 -> 55f    // Minimalist styles have sleeker margins
            else -> 65f
        }
        val maxAllowedWidth = pageWidth - (safeMargin * 2)

        val resolvedSalutation = salutation ?: SalutationHelper.detectSalutation(name, designation)
        val formattedName = SalutationHelper.formatDisplay(resolvedSalutation, name)

        val baseNameSize = if (pageSize == "A4") 36f else 28f
        val baseDescSize = if (pageSize == "A4") 20f else 16f

        val namePaint = Paint().apply {
            textSize = baseNameSize * fontScale
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        val descPaint = Paint().apply {
            textSize = baseDescSize * fontScale
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            isAntiAlias = true
        }

        val nameW = namePaint.measureText(formattedName.uppercase())
        val desigW = descPaint.measureText(designation.trim())

        // Calculate suggested scale from base sizes (1.0f scale)
        val unscaledNamePaint = Paint().apply {
            textSize = baseNameSize
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        val unscaledDescPaint = Paint().apply {
            textSize = baseDescSize
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        }
        val unscaledNameW = unscaledNamePaint.measureText(formattedName.uppercase())
        val unscaledDesigW = unscaledDescPaint.measureText(designation.trim())

        val maxRatio = maxOf(
            if (unscaledNameW > 0) unscaledNameW / maxAllowedWidth else 1.0f,
            if (unscaledDesigW > 0) unscaledDesigW / maxAllowedWidth else 1.0f,
            1.0f
        )
        val rawSuggested = if (maxRatio > 1.0f) (1.0f / maxRatio) * 0.95f else 1.0f
        val suggestedScale = rawSuggested.coerceIn(0.45f, 1.0f)

        return ValidationResult(
            isNameTooLong = nameW > maxAllowedWidth,
            isDesignationTooLong = desigW > maxAllowedWidth,
            nameWidth = nameW,
            designationWidth = desigW,
            maxAllowedWidth = maxAllowedWidth,
            suggestedScale = suggestedScale,
            currentScale = fontScale
        )
    }

    fun generateDeskTent(
        context: Context,
        name: String,
        designation: String,
        logoBase64: String?,
        templateIndex: Int,
        pageSize: String = "A5",
        fontScale: Float = 1.0f,
        salutation: String? = null
    ): File {
        val pdfDocument = PdfDocument()
        val theme = getTheme(templateIndex)

        // A5 (Landscape): 595 x 420
        // A4 (Landscape): 842 x 595
        val pageWidth = if (pageSize == "A4") 842 else 595
        val pageHeight = if (pageSize == "A4") 595 else 420

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Automatically add or use selected salutation as per the name
        val resolvedSalutation = salutation ?: SalutationHelper.detectSalutation(name, designation)
        val formattedName = SalutationHelper.formatDisplay(resolvedSalutation, name)

        // 1. Draw theme-specific canvas background
        val bgPaint = Paint().apply {
            color = theme.backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

        // 2. Draw Top Half Tent (back side, rotated 180 degrees so it faces outside when folded)
        canvas.save()
        canvas.translate(pageWidth.toFloat(), (pageHeight / 2).toFloat())
        canvas.rotate(180f)
        drawHalfTent(canvas, pageWidth, pageHeight / 2, formattedName, designation, logoBase64, theme, pageSize, fontScale)
        canvas.restore()

        // 3. Draw Bottom Half Tent (front side, right-side up)
        canvas.save()
        canvas.translate(0f, (pageHeight / 2).toFloat())
        drawHalfTent(canvas, pageWidth, pageHeight / 2, formattedName, designation, logoBase64, theme, pageSize, fontScale)
        canvas.restore()

        // 4. Draw central fold creasing guide line (subtle dashed line)
        val foldPaint = Paint().apply {
            color = if (theme.isDarkTheme) Color.parseColor("#334155") else Color.parseColor("#D1D5DB")
            strokeWidth = 0.8f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(6f, 6f), 0f)
            isAntiAlias = true
        }
        val midY = (pageHeight / 2).toFloat()
        canvas.drawLine(15f, midY, pageWidth.toFloat() - 15f, midY, foldPaint)

        pdfDocument.finishPage(page)

        // Save to cache directory
        val safeFileName = name.replace(" ", "_").replace(Regex("[^a-zA-Z0-9_]"), "")
        val file = File(context.cacheDir, "Desk_Tent_${safeFileName.ifBlank { "Guest" }}_${theme.name.replace(" ", "_")}.pdf")
        val out = FileOutputStream(file)
        pdfDocument.writeTo(out)
        out.close()
        pdfDocument.close()

        return file
    }

    private fun drawHalfTent(
        canvas: Canvas,
        width: Int,
        height: Int,
        name: String,
        designation: String,
        logoBase64: String?,
        theme: DeskTentTheme,
        pageSize: String,
        fontScale: Float
    ) {
        val marginX = 20f
        val marginY = 20f
        val innerMarginX = 26f
        val innerMarginY = 26f

        // 1. Draw Theme Borders
        drawBorders(canvas, width, height, marginX, marginY, innerMarginX, innerMarginY, theme)

        // 2. Draw Theme Corner Accents
        drawCornerAccents(canvas, innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, theme)

        // 3. Draw Logo or Theme Emblem at Top Center
        val logoBottomY = drawLogoOrEmblem(canvas, width, innerMarginY, logoBase64, theme)

        // 4. Draw Name (Centered, uppercase, styled per theme)
        val baseNameSize = if (pageSize == "A4") 36f else 28f
        val namePaint = Paint().apply {
            color = theme.nameTextColor
            textSize = baseNameSize * fontScale
            typeface = if (theme.id == 6 || theme.id == 8) {
                Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            } else {
                Typeface.create(Typeface.SERIF, Typeface.BOLD)
            }
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val nameY = logoBottomY + (if (pageSize == "A4") 65f else 50f)
        canvas.drawText(name.uppercase(), width / 2f, nameY, namePaint)

        // 5. Draw Theme-Specific Central Divider
        val dividerY = nameY + (15f * fontScale).coerceAtLeast(10f)
        val halfDividerLength = (105f * fontScale).coerceIn(35f, width / 2f - 45f)
        drawThemeDivider(canvas, width / 2f, dividerY, halfDividerLength, theme, fontScale)

        // 6. Draw Designation (Centered, styled per theme)
        val baseDescSize = if (pageSize == "A4") 20f else 16f
        val descPaint = Paint().apply {
            color = theme.designationTextColor
            textSize = baseDescSize * fontScale
            typeface = if (theme.id == 6 || theme.id == 8) {
                Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            } else {
                Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            }
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val desigY = dividerY + (25f * fontScale).coerceAtLeast(18f)
        canvas.drawText(designation, width / 2f, desigY, descPaint)
    }

    private fun drawBorders(
        canvas: Canvas,
        width: Int,
        height: Int,
        marginX: Float,
        marginY: Float,
        innerMarginX: Float,
        innerMarginY: Float,
        theme: DeskTentTheme
    ) {
        val outerPaint = Paint().apply {
            color = theme.outerBorderColor
            style = Paint.Style.STROKE
            strokeWidth = 2.2f
            isAntiAlias = true
        }

        val innerPaint = Paint().apply {
            color = theme.innerBorderColor
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        when (theme.borderStyle) {
            BorderStyle.TRIPLE_EXECUTIVE -> {
                // Outer gold, middle inset, inner gold
                outerPaint.strokeWidth = 2.8f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 0.8f
                canvas.drawRect(marginX + 4f, marginY + 4f, width - marginX - 4f, height - marginY - 4f, innerPaint)
                canvas.drawRect(innerMarginX + 2f, innerMarginY + 2f, width - innerMarginX - 2f, height - innerMarginY - 2f, outerPaint)
            }
            BorderStyle.PRESIDENTIAL_DUAL -> {
                // Navy outer border (thick), Crimson inner border (fine)
                outerPaint.strokeWidth = 3f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1.2f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.ROMAN_BRONZE -> {
                // Double bronze lines with corner notches
                outerPaint.strokeWidth = 2.2f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1.2f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.ROYAL_EMBOSSED -> {
                // Sapphire outer with royal gold inner
                outerPaint.strokeWidth = 2.5f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1.5f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.GEOMETRIC_DIAMOND -> {
                outerPaint.strokeWidth = 2f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 0.8f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.MODERN_MINIMAL -> {
                // Sleek matte black single crisp frame with fine interior border
                outerPaint.strokeWidth = 2.2f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 0.6f
                canvas.drawRect(innerMarginX + 3f, innerMarginY + 3f, width - innerMarginX - 3f, height - innerMarginY - 3f, innerPaint)
            }
            BorderStyle.VINTAGE_FILIGREE -> {
                // Chestnut outer with ornate gold inner
                outerPaint.strokeWidth = 2f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1.2f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.STONE_FRAME -> {
                outerPaint.strokeWidth = 1.8f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 0.8f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.EMERALD_CREST -> {
                outerPaint.strokeWidth = 2.5f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1.5f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
            BorderStyle.DOUBLE_GOLD -> {
                // Classic Signature Ivory double gold
                outerPaint.strokeWidth = 2f
                canvas.drawRect(marginX, marginY, width - marginX, height - marginY, outerPaint)
                innerPaint.strokeWidth = 1f
                canvas.drawRect(innerMarginX, innerMarginY, width - innerMarginX, height - innerMarginY, innerPaint)
            }
        }
    }

    private fun drawCornerAccents(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        theme: DeskTentTheme
    ) {
        val paint = Paint().apply {
            color = theme.cornerAccentColor
            isAntiAlias = true
        }

        when (theme.cornerStyle) {
            CornerStyle.DIAMOND_LOZENGE -> {
                // Solid diamond lozenges at 4 corners
                paint.style = Paint.Style.FILL
                val dSize = 6f
                val corners = listOf(
                    PointF(left, top),
                    PointF(right, top),
                    PointF(left, bottom),
                    PointF(right, bottom)
                )
                for (pt in corners) {
                    val path = Path().apply {
                        moveTo(pt.x, pt.y - dSize)
                        lineTo(pt.x + dSize, pt.y)
                        lineTo(pt.x, pt.y + dSize)
                        lineTo(pt.x - dSize, pt.y)
                        close()
                    }
                    canvas.drawPath(path, paint)
                }
            }
            CornerStyle.DIPLOMATIC_STEP -> {
                // Stepped formal corner brackets
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.5f
                val step = 8f
                // Top-left
                canvas.drawLine(left, top + step, left + step, top + step, paint)
                canvas.drawLine(left + step, top, left + step, top + step, paint)
                // Top-right
                canvas.drawLine(right - step, top + step, right, top + step, paint)
                canvas.drawLine(right - step, top, right - step, top + step, paint)
                // Bottom-left
                canvas.drawLine(left, bottom - step, left + step, bottom - step, paint)
                canvas.drawLine(left + step, bottom - step, left + step, bottom, paint)
                // Bottom-right
                canvas.drawLine(right - step, bottom - step, right, bottom - step, paint)
                canvas.drawLine(right - step, bottom - step, right - step, bottom, paint)
            }
            CornerStyle.PRECISION_DIAMOND -> {
                // Dual precision diagonal ticks with center dot
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.2f
                val len = 12f
                canvas.drawLine(left, top + len, left + len, top, paint)
                canvas.drawLine(right - len, top, right, top + len, paint)
                canvas.drawLine(left, bottom - len, left + len, bottom, paint)
                canvas.drawLine(right - len, bottom, right, bottom - len, paint)

                paint.style = Paint.Style.FILL
                canvas.drawCircle(left + 5f, top + 5f, 2f, paint)
                canvas.drawCircle(right - 5f, top + 5f, 2f, paint)
                canvas.drawCircle(left + 5f, bottom - 5f, 2f, paint)
                canvas.drawCircle(right - 5f, bottom - 5f, 2f, paint)
            }
            CornerStyle.ROYAL_CREST -> {
                // Regal triple-line flourish corner
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.4f
                val cSize = 14f
                canvas.drawLine(left, top + cSize, left + cSize, top, paint)
                canvas.drawLine(right - cSize, top, right, top + cSize, paint)
                canvas.drawLine(left, bottom - cSize, left + cSize, bottom, paint)
                canvas.drawLine(right - cSize, bottom, right, bottom - cSize, paint)

                paint.style = Paint.Style.FILL
                canvas.drawCircle(left + cSize * 0.5f, top + cSize * 0.5f, 2.5f, paint)
                canvas.drawCircle(right - cSize * 0.5f, top + cSize * 0.5f, 2.5f, paint)
                canvas.drawCircle(left + cSize * 0.5f, bottom - cSize * 0.5f, 2.5f, paint)
                canvas.drawCircle(right - cSize * 0.5f, bottom - cSize * 0.5f, 2.5f, paint)
            }
            CornerStyle.MODERN_L -> {
                // Clean modern L-bracket offsets
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.6f
                val lLen = 10f
                canvas.drawLine(left + 4f, top + 4f, left + 4f + lLen, top + 4f, paint)
                canvas.drawLine(left + 4f, top + 4f, left + 4f, top + 4f + lLen, paint)

                canvas.drawLine(right - 4f - lLen, top + 4f, right - 4f, top + 4f, paint)
                canvas.drawLine(right - 4f, top + 4f, right - 4f, top + 4f + lLen, paint)

                canvas.drawLine(left + 4f, bottom - 4f - lLen, left + 4f, bottom - 4f, paint)
                canvas.drawLine(left + 4f, bottom - 4f, left + 4f + lLen, bottom - 4f, paint)

                canvas.drawLine(right - 4f - lLen, bottom - 4f, right - 4f, bottom - 4f, paint)
                canvas.drawLine(right - 4f, bottom - 4f - lLen, right - 4f, bottom - 4f, paint)
            }
            CornerStyle.VICTORIAN_SWIRL -> {
                // Victorian dual arc corner
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.4f
                val s = 16f
                canvas.drawLine(left, top + s, left + s, top, paint)
                canvas.drawLine(left + 4f, top + s, left + s, top + 4f, paint)

                canvas.drawLine(right - s, top, right, top + s, paint)
                canvas.drawLine(right - s, top + 4f, right - 4f, top + s, paint)

                canvas.drawLine(left, bottom - s, left + s, bottom, paint)
                canvas.drawLine(left + 4f, bottom - s, left + s, bottom - 4f, paint)

                canvas.drawLine(right - s, bottom, right, bottom - s, paint)
                canvas.drawLine(right - s, bottom - 4f, right - 4f, bottom - s, paint)
            }
            CornerStyle.CHAMFER_TICK -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.5f
                val s = 10f
                canvas.drawLine(left, top + s, left + s, top, paint)
                canvas.drawLine(right - s, top, right, top + s, paint)
                canvas.drawLine(left, bottom - s, left + s, bottom, paint)
                canvas.drawLine(right - s, bottom, right, bottom - s, paint)
            }
            CornerStyle.EMERALD_SHIELD -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.8f
                val s = 14f
                canvas.drawLine(left, top + s, left + s, top, paint)
                canvas.drawLine(right - s, top, right, top + s, paint)
                canvas.drawLine(left, bottom - s, left + s, bottom, paint)
                canvas.drawLine(right - s, bottom, right, bottom - s, paint)

                paint.style = Paint.Style.FILL
                canvas.drawRect(left + 3f, top + 3f, left + 7f, top + 7f, paint)
                canvas.drawRect(right - 7f, top + 3f, right - 3f, top + 7f, paint)
                canvas.drawRect(left + 3f, bottom - 7f, left + 7f, bottom - 3f, paint)
                canvas.drawRect(right - 7f, bottom - 7f, right - 3f, bottom - 3f, paint)
            }
            else -> {
                // CLASSIC_BRACKET & ROMAN_CORNER
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.2f
                val size = 15f
                canvas.drawLine(left, top + size, left + size, top, paint)
                canvas.drawLine(right - size, top, right, top + size, paint)
                canvas.drawLine(left, bottom - size, left + size, bottom, paint)
                canvas.drawLine(right - size, bottom, right, bottom - size, paint)
            }
        }
    }

    private fun drawLogoOrEmblem(
        canvas: Canvas,
        width: Int,
        innerMarginY: Float,
        logoBase64: String?,
        theme: DeskTentTheme
    ): Float {
        if (!logoBase64.isNullOrEmpty()) {
            try {
                val decodedString = Base64.decode(logoBase64, Base64.DEFAULT)
                val originalBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                if (originalBitmap != null) {
                    val logoHeight = 40f
                    val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                    val logoWidth = logoHeight * aspectRatio

                    val logoRect = RectF(
                        (width - logoWidth) / 2f,
                        innerMarginY + 10f,
                        (width + logoWidth) / 2f,
                        innerMarginY + 10f + logoHeight
                    )
                    canvas.drawBitmap(originalBitmap, null, logoRect, Paint(Paint.FILTER_BITMAP_FLAG))
                    return logoRect.bottom
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Draw refined theme-specific emblem if no custom logo provided
        val emblemPaint = Paint().apply {
            color = theme.cornerAccentColor
            isAntiAlias = true
        }

        val centerX = width / 2f
        val centerY = innerMarginY + 22f

        when (theme.id) {
            1 -> {
                // Executive Gold: Gilded Crest Medallion
                emblemPaint.style = Paint.Style.STROKE
                emblemPaint.strokeWidth = 1.5f
                canvas.drawCircle(centerX, centerY, 12f, emblemPaint)
                emblemPaint.style = Paint.Style.FILL
                canvas.drawCircle(centerX, centerY, 5f, emblemPaint)
            }
            2 -> {
                // Presidential: Dual Diplomatic Ring
                emblemPaint.style = Paint.Style.STROKE
                emblemPaint.strokeWidth = 1.8f
                canvas.drawCircle(centerX, centerY, 11f, emblemPaint)
                emblemPaint.strokeWidth = 0.8f
                canvas.drawCircle(centerX, centerY, 7f, emblemPaint)
            }
            4 -> {
                // Royal Accents: Crown Emblem Silhouette
                emblemPaint.style = Paint.Style.FILL
                val path = Path().apply {
                    moveTo(centerX - 10f, centerY + 6f)
                    lineTo(centerX - 12f, centerY - 4f)
                    lineTo(centerX - 5f, centerY + 1f)
                    lineTo(centerX, centerY - 6f)
                    lineTo(centerX + 5f, centerY + 1f)
                    lineTo(centerX + 12f, centerY - 4f)
                    lineTo(centerX + 10f, centerY + 6f)
                    close()
                }
                canvas.drawPath(path, emblemPaint)
            }
            6, 8 -> {
                // Modern Minimalist / Elite: Sleek Geometric Micro-Badge
                emblemPaint.style = Paint.Style.STROKE
                emblemPaint.strokeWidth = 1.5f
                canvas.drawRect(centerX - 7f, centerY - 7f, centerX + 7f, centerY + 7f, emblemPaint)
            }
            9 -> {
                // Majestic Standard: Emerald Heraldic Diamond
                emblemPaint.style = Paint.Style.STROKE
                emblemPaint.strokeWidth = 1.5f
                val path = Path().apply {
                    moveTo(centerX, centerY - 9f)
                    lineTo(centerX + 9f, centerY)
                    lineTo(centerX, centerY + 9f)
                    lineTo(centerX - 9f, centerY)
                    close()
                }
                canvas.drawPath(path, emblemPaint)
                emblemPaint.style = Paint.Style.FILL
                canvas.drawCircle(centerX, centerY, 3f, emblemPaint)
            }
            else -> {
                // Signature Ivory & Classics: Golden Heraldic Dot with concentric ring
                emblemPaint.style = Paint.Style.STROKE
                emblemPaint.strokeWidth = 1.2f
                canvas.drawCircle(centerX, centerY, 10f, emblemPaint)
                emblemPaint.style = Paint.Style.FILL
                canvas.drawCircle(centerX, centerY, 4f, emblemPaint)
            }
        }

        return innerMarginY + 42f
    }

    private fun drawThemeDivider(
        canvas: Canvas,
        centerX: Float,
        dividerY: Float,
        halfLength: Float,
        theme: DeskTentTheme,
        fontScale: Float
    ) {
        val paint = Paint().apply {
            color = theme.dividerColor
            isAntiAlias = true
        }

        when (theme.dividerStyle) {
            DividerStyle.TRIPLE_STAR -> {
                // Gold starlets in center
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.2f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 18f, dividerY, paint)
                canvas.drawLine(centerX + 18f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val starSize = (4f * fontScale).coerceAtLeast(2.5f)
                draw4PointStar(canvas, centerX, dividerY, starSize * 1.3f, paint)
                draw4PointStar(canvas, centerX - 10f, dividerY, starSize * 0.7f, paint)
                draw4PointStar(canvas, centerX + 10f, dividerY, starSize * 0.7f, paint)
            }
            DividerStyle.ROYAL_CROWN -> {
                // Royal Crown silhouette center
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.4f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 16f, dividerY, paint)
                canvas.drawLine(centerX + 16f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val cW = 10f * fontScale
                val cH = 5f * fontScale
                val crownPath = Path().apply {
                    moveTo(centerX - cW, dividerY + cH)
                    lineTo(centerX - cW * 1.1f, dividerY - cH)
                    lineTo(centerX - cW * 0.4f, dividerY - cH * 0.2f)
                    lineTo(centerX, dividerY - cH * 1.3f)
                    lineTo(centerX + cW * 0.4f, dividerY - cH * 0.2f)
                    lineTo(centerX + cW * 1.1f, dividerY - cH)
                    lineTo(centerX + cW, dividerY + cH)
                    close()
                }
                canvas.drawPath(crownPath, paint)
            }
            DividerStyle.DIPLOMATIC_BAR -> {
                // Double bars with center round crest
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.2f
                canvas.drawLine(centerX - halfLength, dividerY - 1.5f, centerX - 12f, dividerY - 1.5f, paint)
                canvas.drawLine(centerX + 12f, dividerY - 1.5f, centerX + halfLength, dividerY - 1.5f, paint)

                paint.strokeWidth = 0.8f
                canvas.drawLine(centerX - halfLength + 15f, dividerY + 1.5f, centerX - 12f, dividerY + 1.5f, paint)
                canvas.drawLine(centerX + 12f, dividerY + 1.5f, centerX + halfLength - 15f, dividerY + 1.5f, paint)

                paint.style = Paint.Style.FILL
                canvas.drawCircle(centerX, dividerY, 3.5f * fontScale, paint)
            }
            DividerStyle.MODERN_HAIRLINE -> {
                // Ultra-clean single hairline with micro-square
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 0.8f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 8f, dividerY, paint)
                canvas.drawLine(centerX + 8f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val sq = 2.5f * fontScale
                canvas.drawRect(centerX - sq, dividerY - sq, centerX + sq, dividerY + sq, paint)
            }
            DividerStyle.GEOMETRIC_DOTS -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 20f, dividerY, paint)
                canvas.drawLine(centerX + 20f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val r = 2.2f * fontScale
                canvas.drawCircle(centerX - 10f, dividerY, r, paint)
                canvas.drawCircle(centerX, dividerY, r * 1.4f, paint)
                canvas.drawCircle(centerX + 10f, dividerY, r, paint)
            }
            DividerStyle.VICTORIAN_FLOURISH -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.2f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 15f, dividerY, paint)
                canvas.drawLine(centerX + 15f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val d = 4.5f * fontScale
                val diamond = Path().apply {
                    moveTo(centerX, dividerY - d)
                    lineTo(centerX + d * 1.5f, dividerY)
                    lineTo(centerX, dividerY + d)
                    lineTo(centerX - d * 1.5f, dividerY)
                    close()
                }
                canvas.drawPath(diamond, paint)
            }
            DividerStyle.HERALDIC_BAR -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1.5f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 14f, dividerY, paint)
                canvas.drawLine(centerX + 14f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val d = 4f * fontScale
                val p = Path().apply {
                    moveTo(centerX, dividerY - d * 1.2f)
                    lineTo(centerX + d, dividerY)
                    lineTo(centerX, dividerY + d * 1.2f)
                    lineTo(centerX - d, dividerY)
                    close()
                }
                canvas.drawPath(p, paint)
            }
            else -> {
                // GOLD_DIAMOND / CLASSIC_LEAF
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 1f
                canvas.drawLine(centerX - halfLength, dividerY, centerX - 10f, dividerY, paint)
                canvas.drawLine(centerX + 10f, dividerY, centerX + halfLength, dividerY, paint)

                paint.style = Paint.Style.FILL
                val diamondSize = (4f * fontScale).coerceAtLeast(2.5f)
                val path = Path().apply {
                    moveTo(centerX, dividerY - diamondSize)
                    lineTo(centerX + diamondSize, dividerY)
                    lineTo(centerX, dividerY + diamondSize)
                    lineTo(centerX - diamondSize, dividerY)
                    close()
                }
                canvas.drawPath(path, paint)
            }
        }
    }

    private fun draw4PointStar(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val inner = size * 0.35f
        val path = Path().apply {
            moveTo(cx, cy - size)
            lineTo(cx + inner, cy - inner)
            lineTo(cx + size, cy)
            lineTo(cx + inner, cy + inner)
            lineTo(cx, cy + size)
            lineTo(cx - inner, cy + inner)
            lineTo(cx - size, cy)
            lineTo(cx - inner, cy - inner)
            close()
        }
        canvas.drawPath(path, paint)
    }
}

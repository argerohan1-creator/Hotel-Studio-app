package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.RecipeEntity

object BuffetMenuExporter {

    /**
     * Triggers Android's Native PrintManager with clean HTML rendering.
     * Allows saving as PDF or printing to any physical/network printer.
     */
    fun printBuffetMenu(
        context: Context,
        day: String,
        session: String,
        paxCount: Int,
        establishmentName: String,
        fssaiLicense: String,
        items: List<BuffetMenuItemEntity>,
        includeCalories: Boolean = true,
        includeAllergens: Boolean = true,
        includeStations: Boolean = true,
        theme: String = "ink_saver", // "ink_saver", "luxury_gold", "slate_modern"
        paperSize: String = "A4",
        pageLayout: String = "1 Page",
        customLogoUri: String? = null
    ) {
        try {
            val logoBase64 = ImageUtils.uriToBase64(context, customLogoUri)
            val html = generatePrinterFriendlyHtml(
                day = day,
                session = session,
                paxCount = paxCount,
                establishmentName = establishmentName,
                fssaiLicense = fssaiLicense,
                items = items,
                includeCalories = includeCalories,
                includeAllergens = includeAllergens,
                includeStations = includeStations,
                theme = theme,
                paperSize = paperSize,
                pageLayout = pageLayout,
                logoBase64 = logoBase64
            )

            val webView = WebView(context)
            webView.settings.javaScriptEnabled = false
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    if (printManager != null) {
                        val jobName = "${establishmentName.replace(" ", "_")}_${day}_${session}_Buffet_Menu"
                        val printAdapter = webView.createPrintDocumentAdapter(jobName)
                        val builder = PrintAttributes.Builder()
                        if (paperSize.equals("A5", ignoreCase = true)) {
                            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5)
                        } else if (paperSize.equals("A3", ignoreCase = true)) {
                            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A3)
                        } else {
                            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        }
                        printManager.print(jobName, printAdapter, builder.build())
                    } else {
                        Toast.makeText(context, "Print service is unavailable on this device", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            Toast.makeText(context, "Error launching print service: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun buildCoursesHtmlFragment(
        categories: List<Pair<String, List<BuffetMenuItemEntity>>>,
        includeCalories: Boolean,
        includeAllergens: Boolean,
        includeStations: Boolean
    ): String {
        val coursesHtml = StringBuilder()
        for ((course, courseItems) in categories) {
            if (courseItems.isEmpty()) continue
            coursesHtml.append("""
                <div class="course-section">
                    <div class="course-title">$course</div>
                    <div class="dishes-grid">
            """)
            for (item in courseItems) {
                val isVeg = item.type.equals("veg", ignoreCase = true)
                val fssaiDot = if (isVeg) {
                    """<span class="fssai-box veg-box"><span class="fssai-circle"></span></span>"""
                } else {
                    """<span class="fssai-box nonveg-box"><span class="fssai-triangle"></span></span>"""
                }
                val calsTag = if (includeCalories && item.cals.isNotBlank()) {
                    """<span class="calorie-tag">${item.cals}</span>"""
                } else ""
                val stationTag = if (includeStations && item.station.isNotBlank()) {
                    """<span class="station-tag">📍 ${item.station}</span>"""
                } else ""
                val allergensTag = if (includeAllergens && item.allergens.isNotEmpty()) {
                    val list = item.allergens.joinToString(", ")
                    """<div class="allergens-note">Allergens: $list</div>"""
                } else ""
                val descText = if (item.desc.isNotBlank()) {
                    """<div class="dish-desc">${item.desc}</div>"""
                } else ""
                coursesHtml.append("""
                    <div class="dish-item">
                        <div class="dish-header">
                            <span class="dish-name-row">
                                $fssaiDot
                                <strong class="dish-name">${item.name}</strong>
                            </span>
                            $calsTag
                        </div>
                        $descText
                        <div class="dish-meta">
                            $stationTag
                            $allergensTag
                        </div>
                    </div>
                """)
            }
            coursesHtml.append("""
                    </div>
                </div>
            """)
        }
        return coursesHtml.toString()
    }

    /**
     * Generates a clean, high-contrast, ink-saving or elegant HTML document
     * compliant with @media print standards and food regulatory guidelines.
     */
    fun generatePrinterFriendlyHtml(
        day: String,
        session: String,
        paxCount: Int,
        establishmentName: String,
        fssaiLicense: String,
        items: List<BuffetMenuItemEntity>,
        includeCalories: Boolean,
        includeAllergens: Boolean,
        includeStations: Boolean,
        theme: String,
        paperSize: String,
        pageLayout: String = "1 Page",
        logoBase64: String? = null
    ): String {
        val isDark = false // Print layout is always clean white for ink efficiency
        val accentColor = when (theme) {
            "luxury_gold" -> "#B45309"
            "slate_modern" -> "#0284C7"
            else -> "#0F172A" // Ink-saver crisp black/dark slate
        }
        val borderColor = when (theme) {
            "luxury_gold" -> "#D97706"
            "slate_modern" -> "#38BDF8"
            else -> "#CBD5E1"
        }
        
        val strictSequence = listOf(
            "Mocktails / Non-Alcoholic Beverages",
            "Starters / Pass Arounds",
            "Salads & Accompaniments",
            "Soup",
            "Main Course",
            "Desserts"
        )
        // Group and order categories
        val orderedCategories = strictSequence.mapNotNull { cat ->
            val itemsInCat = items.filter { it.courseSection.equals(cat, ignoreCase = true) }
            if (itemsInCat.isNotEmpty()) cat to itemsInCat else null
        }
        val otherCategories = items.groupBy { it.courseSection }
            .filter { (cat, _) -> strictSequence.none { it.equals(cat, ignoreCase = true) } }
            .map { it.key to it.value }
        val groupedCategories = orderedCategories + otherCategories

        val breakStyle = if (pageLayout == "1 Page") "page-break-inside: avoid;" else "page-break-inside: auto;"

        val partitionCss = if (pageLayout == "1 Page") {
            """
            .menu-partition {
                display: flex;
                gap: 20px;
            }
            .menu-col {
                flex: 1;
            }
            .menu-divider {
                width: 1px;
                background-color: $borderColor;
            }
            .dishes-grid {
                display: block;
            }
            .dish-item {
                margin-bottom: 8px;
            }
            """
        } else {
            """
            .dishes-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 10px 18px;
            }
            """
        }

        val coursesHtml = if (pageLayout == "1 Page") {
            val halfIndex = (groupedCategories.size + 1) / 2
            val col1Categories = groupedCategories.take(halfIndex)
            val col2Categories = groupedCategories.drop(halfIndex)
            val col1Html = buildCoursesHtmlFragment(col1Categories, includeCalories, includeAllergens, includeStations)
            val col2Html = buildCoursesHtmlFragment(col2Categories, includeCalories, includeAllergens, includeStations)
            """
            <div class="menu-partition">
                <div class="menu-col">$col1Html</div>
                <div class="menu-divider"></div>
                <div class="menu-col">$col2Html</div>
            </div>
            """
        } else {
            buildCoursesHtmlFragment(groupedCategories, includeCalories, includeAllergens, includeStations)
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>${establishmentName} - ${day} ${session} Buffet Menu</title>
            <style>
                @page {
                    size: $paperSize;
                    margin: 12mm;
                }
                * {
                    box-sizing: border-box;
                    -webkit-print-color-adjust: exact;
                    print-color-adjust: exact;
                }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                    background-color: #ffffff;
                    color: #0f172a;
                    margin: 0;
                    padding: 10px;
                    line-height: 1.35;
                }
                .menu-container {
                    border: 2px solid $borderColor;
                    padding: 18px 24px;
                    background: #ffffff;
                }
                .course-section {
                    margin-bottom: 14px;
                    $breakStyle
                }
                .header-section {
                    text-align: center;
                    border-bottom: 2px solid $accentColor;
                    padding-bottom: 12px;
                    margin-bottom: 16px;
                }
                .hotel-name {
                    font-family: Georgia, serif;
                    font-size: 22pt;
                    font-weight: bold;
                    letter-spacing: 1.5px;
                    color: $accentColor;
                    margin: 0 0 4px 0;
                    text-transform: uppercase;
                }
                .service-title {
                    font-size: 13pt;
                    font-weight: 700;
                    letter-spacing: 1px;
                    color: #1e293b;
                    margin: 0 0 4px 0;
                    text-transform: uppercase;
                }
                .meta-bar {
                    display: flex;
                    justify-content: space-between;
                    font-size: 9pt;
                    color: #64748b;
                    font-weight: 600;
                    margin-top: 6px;
                    padding: 4px 8px;
                    background: #f8fafc;
                    border-radius: 4px;
                }
                .course-title {
                    font-family: Georgia, serif;
                    font-size: 11pt;
                    font-weight: bold;
                    color: $accentColor;
                    text-transform: uppercase;
                    border-bottom: 1px solid #e2e8f0;
                    padding-bottom: 3px;
                    margin-bottom: 8px;
                    letter-spacing: 0.5px;
                }
                $partitionCss
                .dish-item {
                    font-size: 9pt;
                    break-inside: avoid;
                }
                .dish-header {
                    display: flex;
                    justify-content: space-between;
                    align-items: baseline;
                }
                .dish-name-row {
                    display: flex;
                    align-items: center;
                    gap: 6px;
                }
                .dish-name {
                    font-size: 9.5pt;
                    color: #0f172a;
                }
                .dish-desc {
                    font-size: 8pt;
                    color: #475569;
                    font-style: italic;
                    margin-top: 2px;
                    margin-left: 18px;
                }
                .dish-meta {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 6px;
                    align-items: center;
                    margin-top: 2px;
                    margin-left: 18px;
                }
                .calorie-tag {
                    font-size: 7.5pt;
                    font-weight: 700;
                    color: #0369a1;
                    background: #e0f2fe;
                    padding: 1px 5px;
                    border-radius: 3px;
                    white-space: nowrap;
                }
                .station-tag {
                    font-size: 7.5pt;
                    color: #475569;
                    background: #f1f5f9;
                    padding: 1px 4px;
                    border-radius: 3px;
                }
                .allergens-note {
                    font-size: 7pt;
                    color: #be123c;
                    background: #ffe4e6;
                    padding: 1px 4px;
                    border-radius: 3px;
                    font-weight: 600;
                }

                /* FSSAI Symbols */
                .fssai-box {
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                    width: 12px;
                    height: 12px;
                    border-width: 1.5px;
                    border-style: solid;
                    background: #ffffff;
                    vertical-align: middle;
                }
                .veg-box {
                    border-color: #15803d;
                }
                .nonveg-box {
                    border-color: #991b1b;
                }
                .fssai-circle {
                    width: 6px;
                    height: 6px;
                    border-radius: 50%;
                    background: #15803d;
                }
                .fssai-triangle {
                    width: 0;
                    height: 0;
                    border-left: 3.5px solid transparent;
                    border-right: 3.5px solid transparent;
                    border-bottom: 7px solid #991b1b;
                }

                /* Regulatory Compliance Section */
                .compliance-container {
                    margin-top: 18px;
                    padding-top: 10px;
                    border-top: 1.5px solid #cbd5e1;
                    font-size: 7pt;
                    color: #475569;
                    line-height: 1.35;
                    page-break-inside: avoid;
                }
                .compliance-grid {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr);
                    gap: 8px 14px;
                    margin-bottom: 8px;
                }
                .compliance-block {
                    background: #f8fafc;
                    border-left: 2.5px solid $accentColor;
                    padding: 5px 8px;
                    border-radius: 2px;
                }
                .compliance-title {
                    font-weight: 800;
                    font-size: 7.5pt;
                    color: #0f172a;
                    margin-bottom: 2px;
                    text-transform: uppercase;
                    letter-spacing: 0.3px;
                }
                .license-row {
                    display: flex;
                    justify-content: space-between;
                    font-weight: 700;
                    color: #0f172a;
                    padding: 4px 0;
                    border-top: 1px dashed #cbd5e1;
                    font-size: 7.5pt;
                }
            </style>
        </head>
        <body>
            <div class="menu-container">
                <div class="header-section">
                    <h1 class="hotel-name">
                        ${if (logoBase64 != null) "<img src=\"$logoBase64\" alt=\"Logo\" style=\"max-height: 60px; margin-bottom: 8px;\" /><br>" else ""}
                        $establishmentName
                    </h1>
                    <div class="service-title">Executive Daily Buffet — $day $session</div>
                    <div class="meta-bar">
                        <span>👥 Service Covers: $paxCount Pax</span>
                        <span>📅 Service Schedule: $day ($session)</span>
                        <span>🍽️ Total Offerings: ${items.size} Dishes</span>
                        <span>🏅 Executive Chef Curation</span>
                    </div>
                </div>

                <div class="menu-partition">
                    $coursesHtml
                </div>

                <div class="compliance-container">
                    <div class="compliance-grid">
                        <div class="compliance-block">
                            <div class="compliance-title">⚖️ Statutory Energy & Calorie Declaration</div>
                            <div>An average active adult requires 2,000 kcal energy per day, however, individual calorie needs may vary. Calorie values indicated are approximate estimates based on standard portion yields (FSSAI Food Safety & Standards Regulations, 2020).</div>
                        </div>

                        <div class="compliance-block">
                            <div class="compliance-title">⚠️ Food Allergens & Intolerance Warning</div>
                            <div>Kindly inform our banquet associates of any food allergies or dietary intolerances before dining. All eight major allergens (Milk, Gluten, Tree Nuts, Peanuts, Soya, Fish, Crustaceans, Eggs) are handled in our kitchen and potential cross-contact cannot be fully eliminated.</div>
                        </div>

                        <div class="compliance-block">
                            <div class="compliance-title">🟢 🟤 Regulatory Food Classification</div>
                            <div>Standard FSSAI symbols: Green circle in square indicates 100% Vegetarian cuisine; Brown triangle in square indicates Non-Vegetarian preparation. Ingredients are segregated with dedicated cookware and utensils.</div>
                        </div>

                        <div class="compliance-block">
                            <div class="compliance-title">🌡️ HACCP & Hygiene Holding Standard</div>
                            <div>Dishes are continuously monitored and maintained at safe holding temperatures (Hot items ≥ 63°C, Chilled items ≤ 5°C). Prepared fresh for consumption within the specified meal window. GST as applicable.</div>
                        </div>
                    </div>

                    <div class="license-row">
                        <span>FBO FSSAI License Number: <strong>$fssaiLicense</strong></span>
                        <span>Central Food Safety Regulatory Compliance Certified</span>
                        <span>Hotel Studio Culinary Suite QA Verified</span>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    /**
     * Generates a clean, plain-text or markdown formatted printable layout
     * suitable for sharing via Email, WhatsApp, or pasting into kitchen banquet sheets.
     */
    fun generatePrintablePlainText(
        day: String,
        session: String,
        paxCount: Int,
        establishmentName: String,
        fssaiLicense: String,
        items: List<BuffetMenuItemEntity>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("==========================================================")
        sb.appendLine("${establishmentName.uppercase()}")
        sb.appendLine("EXECUTIVE DAILY BUFFET MENU • $day $session")
        sb.appendLine("Service Covers: $paxCount Pax | Total Dishes: ${items.size}")
        sb.appendLine("==========================================================")
        sb.appendLine()

        val grouped = items.groupBy { it.courseSection }
        for ((course, courseItems) in grouped) {
            sb.appendLine("----------------------------------------------------------")
            sb.appendLine(course.uppercase())
            sb.appendLine("----------------------------------------------------------")
            for (item in courseItems) {
                val mark = if (item.type.equals("veg", ignoreCase = true)) "[VEG]" else "[NON-VEG]"
                val cal = if (item.cals.isNotBlank()) " (${item.cals})" else ""
                sb.appendLine("• $mark ${item.name}$cal")
                if (item.desc.isNotBlank()) {
                    sb.appendLine("   Desc: ${item.desc}")
                }
                val metaParts = mutableListOf<String>()
                if (item.station.isNotBlank()) metaParts.add("Station: ${item.station}")
                if (item.allergens.isNotEmpty()) metaParts.add("Allergens: ${item.allergens.joinToString(", ")}")
                if (metaParts.isNotEmpty()) {
                    sb.appendLine("   [${metaParts.joinToString(" | ")}]")
                }
            }
            sb.appendLine()
        }

        sb.appendLine("==========================================================")
        sb.appendLine("MANDATORY REGULATORY COMPLIANCE DISCLAIMERS")
        sb.appendLine("==========================================================")
        sb.appendLine("1. STATUTORY CALORIE ADVISORY:")
        sb.appendLine("   An average active adult requires 2,000 kcal energy per day, however, individual calorie needs may vary.")
        sb.appendLine("2. ALLERGEN DECLARATION & SENSITIVITY WARNING:")
        sb.appendLine("   Please inform our service team of any food allergies or food intolerances. Potential cross-contact with Gluten, Milk, Nuts, Soy, Egg, and Fish may occur.")
        sb.appendLine("3. FOOD SAFETY & TEMPERATURE HYGIENE (HACCP/FSSAI):")
        sb.appendLine("   Hot buffet dishes are held at >= 63°C and chilled items at <= 5°C. Prepared fresh daily for immediate service.")
        sb.appendLine("4. REGULATORY IDENTIFICATION:")
        sb.appendLine("   FSSAI License No: $fssaiLicense")
        sb.appendLine("   Food Business Operator: $establishmentName")
        sb.appendLine("==========================================================")

        return sb.toString()
    }

    fun printQRTags(
        context: Context,
        day: String,
        session: String,
        establishmentName: String,
        fssaiLicense: String,
        items: List<BuffetMenuItemEntity>,
        customLogoUri: String? = null
    ) {
        try {
            val logoBase64 = ImageUtils.uriToBase64(context, customLogoUri)
            val html = QRTagsHtmlGenerator.generateHtml(
                day = day,
                session = session,
                establishmentName = establishmentName,
                fssaiLicense = fssaiLicense,
                items = items,
                logoBase64 = logoBase64
            )
            val webView = WebView(context)
            webView.settings.javaScriptEnabled = false
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    if (printManager != null) {
                        val jobName = "${establishmentName.replace(" ", "_")}_QR_Tags"
                        val printAdapter = webView.createPrintDocumentAdapter(jobName)
                        val builder = PrintAttributes.Builder()
                            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        printManager.print(jobName, printAdapter, builder.build())
                    } else {
                        Toast.makeText(context, "Print service is unavailable on this device", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            Toast.makeText(context, "Error launching print service: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Share formatted menu via Android Intent.ACTION_SEND
     */
    fun shareMenu(context: Context, text: String, subject: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Share Buffet Menu via"))
    }

    /**
     * Copy formatted menu to clipboard
     */
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Buffet Menu", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Printer-friendly menu copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
fun printKitchenPrepSheet(
        context: Context,
        day: String,
        session: String,
        paxCount: Int,
        items: List<BuffetMenuItemEntity>,
        recipes: List<RecipeEntity>
    ) {
        try {
            val sb = java.lang.StringBuilder()
            sb.appendLine("<!DOCTYPE html>")
            sb.appendLine("<html><head><style>")
            sb.appendLine("body { font-family: sans-serif; margin: 40px; color: #333; }")
            sb.appendLine("h1 { text-align: center; color: #111; }")
            sb.appendLine(".header-sub { text-align: center; margin-bottom: 30px; font-size: 14px; color: #666; }")
            sb.appendLine("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }")
            sb.appendLine("th, td { border: 1px solid #ccc; padding: 8px 10px; text-align: left; font-size: 13px; }")
            sb.appendLine("th { background-color: #f5f5f5; font-weight: bold; }")
            sb.appendLine(".station-header { background-color: #e0e0e0; font-weight: bold; padding: 10px; margin-top: 20px; font-size: 14px; border-radius: 4px; }")
            sb.appendLine(".detail-box { font-size: 11px; color: #444; margin-top: 4px; background: #fafafa; padding: 6px; border-radius: 4px; border-left: 3px solid #0284c7; }")
            sb.appendLine(".routine-banner { background: #f0f9ff; border: 1px solid #bae6fd; color: #0369a1; padding: 10px 14px; border-radius: 6px; margin-bottom: 20px; font-size: 12px; }")
            sb.appendLine("</style></head><body>")
            
            sb.appendLine("<h1>Kitchen Prep & Run-Sheet</h1>")
            val dateStr = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            sb.appendLine("<div class='header-sub'>Day: $day | Session: $session | Target Pax: $paxCount | Date: $dateStr</div>")
            sb.appendLine("<div class='routine-banner'><strong>Chef Service Plan:</strong> Verify mise en place, batch scaling, and cooking methods for timely banquet & buffet synchronization.</div>")
            
            val groupedByStation = items.groupBy { it.station.ifBlank { "Main Buffet Line" } }
            
            groupedByStation.forEach { (stationName, stationItems) ->
                sb.appendLine("<div class='station-header'>Station: $stationName</div>")
                sb.appendLine("<table>")
                sb.appendLine("<tr><th style='width: 30%;'>Dish & Course</th><th style='width: 15%;'>Prep Time</th><th style='width: 15%;'>Batch Scaling</th><th style='width: 40%;'>Ingredients & Cooking Methods</th></tr>")
                
                stationItems.forEach { itm ->
                    val recipe = recipes.find { r -> r.id == itm.recipeId }
                    val baseYieldStr = recipe?.yieldPortions ?: "1"
                    var baseYield = 1
                    try {
                        baseYield = baseYieldStr.replace("[^0-9]".toRegex(), "").toInt()
                        if (baseYield <= 0) baseYield = 1
                    } catch (e: Exception) {}
                    
                    val multiplier = String.format("%.1fx", paxCount.toFloat() / baseYield.toFloat())
                    val ingsText = recipe?.ingredients?.joinToString(", ") ?: "Standard executive chef recipe card."
                    val stepsText = recipe?.steps?.joinToString(" ➔ ") ?: "Prepare fresh following HACCP standards."

                    sb.appendLine("<tr>")
                    sb.appendLine("<td><strong>${itm.name}</strong><br/><small style='color: #666;'>${itm.courseSection} (${if (itm.type == "nonveg") "Non-Veg" else "Veg"})</small></td>")
                    sb.appendLine("<td>${itm.prepTime}</td>")
                    sb.appendLine("<td>Base Yield: $baseYieldStr<br/><strong>Need: $multiplier</strong></td>")
                    sb.appendLine("<td><div class='detail-box'><strong>Ingredients:</strong> $ingsText<br/><br/><strong>Method:</strong> $stepsText</div></td>")
                    sb.appendLine("</tr>")
                }
                sb.appendLine("</table>")
            }
            
            sb.appendLine("</body></html>")

            val html = sb.toString()
            val webView = WebView(context)
            webView.settings.javaScriptEnabled = false
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    if (printManager != null) {
                        val jobName = "Kitchen_Prep_Sheet_${day}_$session"
                        val printAdapter = webView.createPrintDocumentAdapter(jobName)
                        val builder = PrintAttributes.Builder()
                        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        printManager.print(jobName, printAdapter, builder.build())
                    } else {
                        Toast.makeText(context, "Print service is unavailable", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            Toast.makeText(context, "Error launching print service", Toast.LENGTH_SHORT).show()
        }
    }

}

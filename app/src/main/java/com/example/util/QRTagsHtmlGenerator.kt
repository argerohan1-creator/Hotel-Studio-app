package com.example.util

import com.example.data.model.BuffetMenuItemEntity

object QRTagsHtmlGenerator {
    fun generateHtml(
        day: String,
        session: String,
        establishmentName: String,
        fssaiLicense: String,
        items: List<BuffetMenuItemEntity>,
        logoBase64: String? = null
    ): String {
        val css = """
            body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #f3f4f6; padding: 20px; color: #000; }
            .grid-container { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
            .tag-card { background: #fff; border: 2px solid #e5e7eb; border-radius: 12px; padding: 20px; display: flex; flex-direction: row; justify-content: space-between; page-break-inside: avoid; }
            .left-panel { flex: 1; display: flex; flex-direction: column; padding-right: 15px; }
            .right-panel { display: flex; flex-direction: column; align-items: center; justify-content: center; }
            .fssai-circle { display: inline-block; width: 12px; height: 12px; border: 2px solid; border-radius: 3px; position: relative; margin-right: 8px; vertical-align: middle; }
            .fssai-circle.veg { border-color: #15803d; }
            .fssai-circle.veg::after { content: ''; position: absolute; top: 2px; left: 2px; right: 2px; bottom: 2px; background-color: #15803d; border-radius: 50%; }
            .fssai-circle.nonveg { border-color: #b91c1c; }
            .fssai-circle.nonveg::after { content: ''; position: absolute; top: 2px; left: 2px; right: 2px; bottom: 2px; background-color: #b91c1c; border-radius: 50%; }
            .course-header { font-size: 11px; font-weight: bold; color: #6b7280; letter-spacing: 1px; text-transform: uppercase; }
            .dish-name { font-family: 'Georgia', serif; font-size: 22px; font-weight: bold; margin: 8px 0; color: #111827; }
            .dish-desc { font-size: 13px; color: #4b5563; line-height: 1.4; margin-bottom: 12px; }
            .meta-row { display: flex; align-items: center; margin-bottom: 15px; font-size: 12px; }
            .calories { font-weight: bold; color: #991b1b; margin-right: 4px; }
            .meta-label { color: #6b7280; margin-right: 15px; }
            .allergens { display: flex; align-items: center; gap: 4px; flex-wrap: wrap; }
            .allergen-badge { background: #fee2e2; color: #991b1b; padding: 2px 6px; border-radius: 4px; font-size: 10px; font-weight: bold; border: 1px solid #fecaca; }
            .dietary-badge-safe { background: #d1fae5; color: #065f46; padding: 2px 6px; border-radius: 4px; font-size: 10px; font-weight: bold; border: 1px solid #10b981; }
            .footer { margin-top: auto; font-size: 10px; color: #9ca3af; }
            .qr-box { width: 100px; height: 100px; background: #fff; border: 2px solid #111827; display: flex; align-items: center; justify-content: center; border-radius: 8px; }
            .qr-text { font-size: 10px; font-weight: bold; color: #111827; text-align: center; margin-top: 8px; }
            @media print {
                body { padding: 0; background-color: #fff; }
                .tag-card { border-color: #000; break-inside: avoid; }
                .fssai-circle { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
                .allergen-badge { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
                .dietary-badge-safe { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
            }
        """

        val cardsHtml = items.joinToString("\n") { item ->
            val vegClass = if (item.type == "veg") "veg" else "nonveg"
            val allergensHtml = if (item.allergens.isNotEmpty()) {
                val tags = item.allergens.joinToString("") { tag ->
                    val isSafeOrLifestyle = tag.contains("free", ignoreCase = true) ||
                            tag.equals("Vegan", ignoreCase = true) ||
                            tag.equals("Vegetarian", ignoreCase = true) ||
                            tag.equals("Jain", ignoreCase = true) ||
                            tag.equals("Halal", ignoreCase = true) ||
                            tag.equals("Organic", ignoreCase = true) ||
                            tag.equals("Sugar-Free", ignoreCase = true)
                    val badgeClass = if (isSafeOrLifestyle) "dietary-badge-safe" else "allergen-badge"
                    "<span class='$badgeClass'>$tag</span>"
                }
                "<div class='meta-label'>Dietary / Allergens:</div><div class='allergens'>$tags</div>"
            } else ""

            val calsVal = if (item.cals.isNotBlank()) item.cals else "N/A"

            """
            <div class="tag-card">
                <div class="left-panel">
                    <div style="display: flex; align-items: center;">
                        <span class="fssai-circle $vegClass"></span>
                        <span class="course-header">${item.courseSection}</span>
                    </div>
                    <div class="dish-name">${item.name}</div>
                    ${if (item.desc.isNotBlank()) "<div class='dish-desc'>${item.desc}</div>" else ""}
                    <div class="meta-row">
                        <span class="calories">$calsVal</span><span class="meta-label">Calories</span>
                        $allergensHtml
                    </div>
                    <div class="footer">
                        ${if (logoBase64 != null) "<img src=\"$logoBase64\" alt=\"Logo\" style=\"max-height: 24px; vertical-align: middle; margin-right: 8px;\" />" else ""}
                        Lic. No. $fssaiLicense • $establishmentName
                    </div>
                </div>
                <div class="right-panel">
                    <div class="qr-box">
                        <!-- Placeholder for actual QR code image. We'll use a generated data URI or just a static image for mock. -->
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/QR_code_for_mobile_English_Wikipedia.svg/220px-QR_code_for_mobile_English_Wikipedia.svg.png" width="80" height="80" alt="QR" style="filter: grayscale(100%);" />
                    </div>
                    <div class="qr-text">Scan for Nutrition<br>& Story</div>
                </div>
            </div>
            """
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>QR Display Tags</title>
                <style>$css</style>
            </head>
            <body>
                <div class="grid-container">
                    $cardsHtml
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}

import sys

file_path = "app/src/main/java/com/example/util/BuffetMenuExporter.kt"
with open(file_path, "r") as f:
    text = f.read()

imports = """import com.example.data.model.RecipeEntity
"""
text = text.replace("import com.example.data.model.BuffetMenuItemEntity\n", "import com.example.data.model.BuffetMenuItemEntity\n" + imports)

new_method = """
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
            sb.appendLine("th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }")
            sb.appendLine("th { background-color: #f5f5f5; font-weight: bold; }")
            sb.appendLine(".station-header { background-color: #e0e0e0; font-weight: bold; padding: 10px; margin-top: 20px; }")
            sb.appendLine("</style></head><body>")
            
            sb.appendLine("<h1>Kitchen Prep & Run-Sheet</h1>")
            val dateStr = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            sb.appendLine("<div class='header-sub'>Day: $day | Session: $session | Target Pax: $paxCount | Date: $dateStr</div>")
            
            val groupedByStation = items.groupBy { it.station.ifBlank { "Main Buffet Line" } }
            
            groupedByStation.forEach { (stationName, stationItems) ->
                sb.appendLine("<div class='station-header'>Station: $stationName</div>")
                sb.appendLine("<table>")
                sb.appendLine("<tr><th>Dish Name</th><th>Course</th><th>Prep Time</th><th>Recipe Yield</th><th>Required Multiplier</th><th>Status / Notes</th></tr>")
                
                stationItems.forEach { itm ->
                    val recipe = recipes.find { r -> r.id == itm.recipeId }
                    val baseYieldStr = recipe?.yieldPortions ?: "1"
                    var baseYield = 1
                    try {
                        baseYield = baseYieldStr.replace("[^0-9]".toRegex(), "").toInt()
                        if (baseYield <= 0) baseYield = 1
                    } catch (e: Exception) {}
                    
                    val multiplier = String.format("%.1fx", paxCount.toFloat() / baseYield.toFloat())
                    
                    sb.appendLine("<tr>")
                    sb.appendLine("<td><strong>${itm.name}</strong></td>")
                    sb.appendLine("<td>${itm.courseSection}</td>")
                    sb.appendLine("<td>${itm.prepTime}</td>")
                    sb.appendLine("<td>$baseYieldStr</td>")
                    sb.appendLine("<td><strong>$multiplier</strong></td>")
                    sb.appendLine("<td></td>") // empty for notes
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
"""

text = text + "\n" + new_method

with open(file_path, "w") as f:
    f.write(text)

print("Patched BuffetMenuExporter")

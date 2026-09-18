import sys

file_path = "app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

import_ex = "import com.example.util.BuffetMenuExporter"
if import_ex not in text:
    text = text.replace("import com.example.util.ModernArtRenderer", "import com.example.util.ModernArtRenderer\nimport com.example.util.BuffetMenuExporter")

# Add recipes parameter to KitchenPrepSheetDialog signature
old_sig = """fun KitchenPrepSheetDialog(
    day: String,
    session: String,
    paxCount: Int,
    items: List<BuffetMenuItemEntity>,
    onDismiss: () -> Unit,
    onPrint: () -> Unit
)"""

new_sig = """fun KitchenPrepSheetDialog(
    day: String,
    session: String,
    paxCount: Int,
    items: List<BuffetMenuItemEntity>,
    recipes: List<RecipeEntity>,
    onDismiss: () -> Unit,
    onPrint: () -> Unit
)"""
text = text.replace(old_sig, new_sig)

# Update item rendering inside the dialog
old_item = """                                    Text(
                                        text = "${itm.prepTime} | ${itm.portions}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )"""
new_item = """                                    val recipe = recipes.find { r -> r.id == itm.recipeId }
                                    val baseYieldStr = recipe?.yieldPortions ?: "1"
                                    var baseYield = 1
                                    try {
                                        baseYield = baseYieldStr.replace("[^0-9]".toRegex(), "").toInt()
                                        if (baseYield <= 0) baseYield = 1
                                    } catch (e: Exception) {}
                                    val multiplier = String.format("%.1fx Batches", paxCount.toFloat() / baseYield.toFloat())
                                    
                                    Text(
                                        text = "${itm.prepTime} | Need: $multiplier",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )"""
text = text.replace(old_item, new_item)

# Update the call site
old_call = """    if (showPrepSheetDialog) {
        KitchenPrepSheetDialog(
            day = selectedDay,
            session = selectedSession,
            paxCount = paxCount,
            items = currentItems,
            onDismiss = { showPrepSheetDialog = false },
            onPrint = {
                viewModel.showToast("Sending $selectedDay $selectedSession kitchen prep sheet to banquet printer...")
                showPrepSheetDialog = false
            }
        )
    }"""
new_call = """    if (showPrepSheetDialog) {
        KitchenPrepSheetDialog(
            day = selectedDay,
            session = selectedSession,
            paxCount = paxCount,
            items = currentItems,
            recipes = recipes,
            onDismiss = { showPrepSheetDialog = false },
            onPrint = {
                BuffetMenuExporter.printKitchenPrepSheet(
                    context = context,
                    day = selectedDay,
                    session = selectedSession,
                    paxCount = paxCount,
                    items = currentItems,
                    recipes = recipes
                )
                showPrepSheetDialog = false
            }
        )
    }"""
text = text.replace(old_call, new_call)

with open(file_path, "w") as f:
    f.write(text)

print("Patched MenuBuilderScreen")

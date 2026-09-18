import sys

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    lines = f.readlines()

# 1. Add itemToAudit state
for i, line in enumerate(lines):
    if 'var itemToEdit by' in line:
        lines.insert(i+1, "    var itemToAudit by remember { mutableStateOf<BuffetMenuItemEntity?>(null) }\n")
        break

# 2. Pass onAuditClick to CourseSectionCard
for i, line in enumerate(lines):
    if 'onEdit = { itemToEdit = it }' in line:
        lines[i] = lines[i].replace('\n', ',\n')
        lines.insert(i+1, "                        onAuditClick = { itemToAudit = it }\n")
        break

# 3. Render FssaiAuditDialog
# Find a place at the bottom of MenuBuilderScreen (before CustomBuffetItemDialog)
for i, line in enumerate(lines):
    if 'if (showCustomItemDialog) {' in line:
        lines.insert(i, """    if (itemToAudit != null) {
        val recipeForAudit = itemToAudit?.recipeId?.let { rId -> recipes.find { it.id == rId } }
        com.example.ui.components.FssaiAuditDialog(
            item = itemToAudit!!,
            recipe = recipeForAudit,
            onDismiss = { itemToAudit = null }
        )
    }

""")
        break

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.writelines(lines)

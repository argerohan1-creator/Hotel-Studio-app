import re
with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    content = f.read()

# Replace the title and plannerMode check
old_title = """                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "3. Menu Header & Dish Names",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (plannerMode == 1) "Fill dishes with Culinary Agent auto-check" else "Auto-designed by F&B assistant (switch to manual above)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (plannerMode == 0) {
                        TextButton(
                            onClick = { plannerMode = 1 },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Edit Manually", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        TextButton(
                            onClick = { plannerMode = 0 },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Switch to AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }"""

new_title = """                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "3. Menu Header & Dish Names",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Fill dishes with Culinary Agent auto-check",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = {
                            val updated = manualDrafts.toMutableList()
                            for (i in 0 until updated.size) {
                                val current = updated[i]
                                val suggestions = com.example.util.CulinaryAgent.searchAppDirectory(current.type, existingRecipes, existingDishes)
                                val suggestedName = suggestions.filter { it.category == current.category }.randomOrNull()?.name 
                                    ?: suggestions.randomOrNull()?.name ?: current.name
                                updated[i] = current.copy(name = suggestedName)
                            }
                            manualDrafts = updated
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auto Suggest", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }"""

content = content.replace(old_title, new_title)

# Remove `if (plannerMode == 1) {` and matching closing brace.
# First, replace `if (plannerMode == 1) {` with nothing (or keep it simple, regex).
old_if_mode_1 = """                if (plannerMode == 1) {
                    // Chef's Manual Choice Section with Culinary Intelligence Agent & App Directory Suggestions"""
new_if_mode_1 = """                    // Chef's Manual Choice Section with Culinary Intelligence Agent & App Directory Suggestions"""
content = content.replace(old_if_mode_1, new_if_mode_1)

# Also fix the Text display
old_text = """                    Text(
                        text = "Chef's Dishes ($vegCount Veg + $nonVegCount Non-Veg = ${manualDrafts.size} Dishes)",
                        fontSize = 12.sp,"""
new_text = """                    Text(
                        text = "Chef's Dishes ($soupCount Soup + $saladCount Salad + $vegCount Veg + $nonVegCount Non-Veg = ${manualDrafts.size} Dishes)",
                        fontSize = 12.sp,"""
content = content.replace(old_text, new_text)

with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.write(content)

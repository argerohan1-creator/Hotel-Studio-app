import re
with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    content = f.read()

# Replace the block from `} else {` to the end of the AI Assistant Summary Card
old_else_block = """                } else {
                    // AI Assistant Summary Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Automatic Curation Ready",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = "The F&B assistant will curate $currentDay $currentSession with $vegCount Veg and $nonVegCount Non-Veg dishes, $actionStationCount Action Stations, and $beverageCount Welcome Beverages. Every dish includes Calories per 100g/ml, FSSAI/FDA Allergens, and Veg/Non-Veg signage.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }"""

# I need to keep the closing brace of the Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { manualDrafts... } if it was not included in my removal of `if (plannerMode == 1) {`. Wait, I removed the `if (plannerMode == 1) {` completely, so the `}` that corresponds to it should ALSO be removed.
# The code was:
# if (plannerMode == 1) {
#   Card(...)
#   Text(...)
#   Column(...) { ... }
# } else {
#   Card(...)
# }

# So I should remove `} else { ... }` ENTIRELY, because it is just removing the closing brace of `if (plannerMode == 1)` and the `else` block.

content = content.replace(old_else_block, "")

with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.write(content)

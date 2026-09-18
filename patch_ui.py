import sys

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    lines = f.readlines()

start_idx = -1
end_idx = -1

for i, line in enumerate(lines):
    if 'listOf("ALL" to "All Diets", "VEG" to "Veg Only", "NONVEG" to "Non-Veg").forEach { (key, label) ->' in line:
        start_idx = i
    if start_idx != -1 and 'Spacer(modifier = Modifier.height(8.dp))' in line and i > start_idx:
        end_idx = i
        break

if start_idx != -1 and end_idx != -1:
    replacement = """            // Diet Toggle Pills
            val dietScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(dietScroll),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("ALL" to "All Diets", "VEG" to "Veg Only", "NONVEG" to "Non-Veg", "VEGAN" to "Vegan", "GF" to "Gluten-Free").forEach { (key, label) ->
                    val isSelected = dietFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { viewModel.setRecipePickerDiet(key) }
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Prep Time Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val prepScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(prepScroll),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("ALL" to "Any Time", "QUICK" to "< 20 mins", "MEDIUM" to "20-45 mins", "LONG" to "> 45 mins").forEach { (key, label) ->
                    val isSelected = prepTimeFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable { viewModel.setRecipePickerPrepTime(key) }
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
"""
    # Replace lines from `// Diet Toggle Pills` down to `Spacer(modifier = Modifier.height(8.dp))`
    # Wait, the search loop found the start at the listOf... line. Let's adjust to the `listOf` line
    # Actually, the python code looks for `listOf("ALL" to "All Diets", "VEG" to "Veg Only", "NONVEG" to "Non-Veg").forEach { (key, label) ->`
    
    # We need to replace from `listOf` up to the closing brace of the forEach loop, then add our new row.
    pass


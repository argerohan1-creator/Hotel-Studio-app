#!/bin/bash
sed -i '/var courseDropdownExpanded/a \    var allergensText by remember { mutableStateOf(item.allergens.joinToString(", ")) }' app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt

cat << 'INNER_EOF' > edit_dialog_patch.txt
                // Allergens Checklist UI
                Column {
                    Text("FSSAI Approved Allergens", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val chunkedList = FSSAI_ALLERGENS_LIST.chunked(2)
                        chunkedList.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { allergen ->
                                    val isChecked = allergensText.contains(allergen)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                val currentList = allergensText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                                val updated = if (isChecked) {
                                                    currentList - allergen
                                                } else {
                                                    currentList + allergen
                                                }
                                                allergensText = updated.joinToString(", ")
                                            }
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checked ->
                                                val currentList = allergensText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                                val updated = if (checked) {
                                                    currentList + allergen
                                                } else {
                                                    currentList - allergen
                                                }
                                                allergensText = updated.joinToString(", ")
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        AllergenIcon(allergen = allergen, size = 16.dp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(allergen, fontSize = 10.sp)
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
INNER_EOF

# Insert the patch right before the Row with Cancel/Save Changes buttons
sed -i -e '/Row(/,/horizontalArrangement = Arrangement.End/ {
    /horizontalArrangement = Arrangement.End/ {
        i \                // Allergens UI injected here\n' -e 'r edit_dialog_patch.txt' -e '\
    }
}' app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt

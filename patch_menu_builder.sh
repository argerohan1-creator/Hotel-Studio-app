#!/bin/bash
cat << 'INNER_EOF' > import_patch.txt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import com.example.ui.theme.FSSAI_ALLERGENS_LIST
INNER_EOF

sed -i '/import androidx.compose.material3.CardDefaults/r import_patch.txt' app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt

cat << 'INNER_EOF' > add_dialog_patch.txt
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

# Replace AddCustomItemDialog text field
sed -i -e '/OutlinedTextField(/,/singleLine = true\n                )/ {
    /value = allergensText/!b
    r add_dialog_patch.txt
    d
}' app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt

# Replace take(3)
sed -i 's/item.allergens.take(3).forEach { allergen ->/item.allergens.forEach { allergen ->/' app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt


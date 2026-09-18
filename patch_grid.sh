#!/bin/bash
cat << 'INNER_EOF' > replacement.txt
                            // Custom 2-column grid for Allergens
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
                                            val isChecked = activeDish.allergens.contains(allergen)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                    val updated = if (isChecked) {
                                                        activeDish.allergens - allergen
                                                    } else {
                                                        activeDish.allergens + allergen
                                                    }
                                                    viewModel.updateActiveDish(activeDish.copy(allergens = updated))
                                                }
                                            ) {
                                                Checkbox(
                                                    checked = isChecked,
                                                    onCheckedChange = { checked ->
                                                        val updated = if (checked) {
                                                            activeDish.allergens + allergen
                                                        } else {
                                                            activeDish.allergens - allergen
                                                        }
                                                        viewModel.updateActiveDish(activeDish.copy(allergens = updated))
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
INNER_EOF
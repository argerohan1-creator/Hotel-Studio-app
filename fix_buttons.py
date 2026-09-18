import re
with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    content = f.read()

old_buttons = """                    if (plannerMode == 1) {
                        Button(
                            onClick = {
                                val p = paxText.toIntOrNull() ?: 80
                                onSaveManual(headerNameText, p, selectedActionStations, selectedBeverages, manualDrafts)
                            },
                            enabled = !isAiLoading,
                            modifier = Modifier.testTag("btn_save_chef_menu"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Chef's Menu")
                        }
                    } else {
                        Button(
                            onClick = {
                                val p = paxText.toIntOrNull() ?: 80
                                onGenerate(p, vegCount, nonVegCount, selectedActionStations, selectedBeverages)
                            },
                            enabled = !isAiLoading,
                            modifier = Modifier.testTag("btn_confirm_generate_menu"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isAiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate Menu")
                            }
                        }
                    }"""

new_buttons = """                    Button(
                        onClick = {
                            val p = paxText.toIntOrNull() ?: 80
                            onSaveManual(headerNameText, p, selectedActionStations, selectedBeverages, manualDrafts)
                        },
                        enabled = !isAiLoading,
                        modifier = Modifier.testTag("btn_confirm_generate_menu"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate Menu")
                        }
                    }"""

content = content.replace(old_buttons, new_buttons)

with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.write(content)

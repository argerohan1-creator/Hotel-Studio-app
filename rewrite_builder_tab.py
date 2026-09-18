import os

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

# 1. Add Imports
imports_to_add = """
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.UploadFile
import android.provider.OpenableColumns
"""
if "rememberLauncherForActivityResult" not in text:
    text = text.replace("import android.widget.Toast\n", "import android.widget.Toast\n" + imports_to_add)

old_block = """                    var builderTab by remember { mutableStateOf(0) } // 0: AI, 1: Excel
                    TabRow(selectedTabIndex = builderTab, modifier = Modifier.padding(vertical = 8.dp)) {
                        Tab(selected = builderTab == 0, onClick = { builderTab = 0 }) {
                            Text("F&B Assistant (AI)", modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                        }
                        Tab(selected = builderTab == 1, onClick = { builderTab = 1 }) {
                            Text("Paste Excel/CSV", modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                        }
                    }
                    
                    if (builderTab == 0) {
                        OutlinedTextField(
                            value = aiPrompt,
                            onValueChange = { aiPrompt = it },
                            label = { Text("E.g. Rooftop Pool Bar $selectedShift checklist") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.generateOutletChecklist(aiPrompt) { generatedTasks ->
                                    checklistItems = generatedTasks.map { OutletChecklistItem(task = it) }
                                }
                            },
                            enabled = !isAiLoading && aiPrompt.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isAiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate with AI")
                        }
                    } else {
                        OutlinedTextField(
                            value = excelPasteData,
                            onValueChange = { excelPasteData = it },
                            label = { Text("Paste rows from Excel (one task per line)") },
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val parsedTasks = excelPasteData.lines()
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }
                                    .map { OutletChecklistItem(task = it) }
                                checklistItems = parsedTasks
                                Toast.makeText(context, "Imported ${parsedTasks.size} tasks", Toast.LENGTH_SHORT).show()
                            },
                            enabled = excelPasteData.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Import Tasks")
                        }
                    }"""


new_block = """                    var builderTab by remember { mutableStateOf(0) } // 0: AI, 1: Paste, 2: Upload
                    
                    var selectedFileName by remember { mutableStateOf<String?>(null) }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let {
                            var name = "Uploaded_Document"
                            val cursor = context.contentResolver.query(it, null, null, null, null)
                            cursor?.use { c ->
                                if (c.moveToFirst()) {
                                    val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                    if (nameIndex != -1) name = c.getString(nameIndex)
                                }
                            }
                            selectedFileName = name
                            
                            val generatedPrompt = "Analyze the uploaded file named '$name' and extract a professional F&B checklist. Format it as a JSON array of task strings."
                            viewModel.generateOutletChecklist(generatedPrompt) { generatedTasks ->
                                checklistItems = generatedTasks.map { OutletChecklistItem(task = it) }
                            }
                        }
                    }

                    TabRow(selectedTabIndex = builderTab, modifier = Modifier.padding(vertical = 8.dp)) {
                        Tab(selected = builderTab == 0, onClick = { builderTab = 0 }) {
                            Text("AI Prompt", modifier = Modifier.padding(8.dp), fontSize = 11.sp)
                        }
                        Tab(selected = builderTab == 1, onClick = { builderTab = 1 }) {
                            Text("Paste Text", modifier = Modifier.padding(8.dp), fontSize = 11.sp)
                        }
                        Tab(selected = builderTab == 2, onClick = { builderTab = 2 }) {
                            Text("Upload File", modifier = Modifier.padding(8.dp), fontSize = 11.sp)
                        }
                    }
                    
                    if (builderTab == 0) {
                        OutlinedTextField(
                            value = aiPrompt,
                            onValueChange = { aiPrompt = it },
                            label = { Text("E.g. Rooftop Pool Bar $selectedShift checklist") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.generateOutletChecklist(aiPrompt) { generatedTasks ->
                                    checklistItems = generatedTasks.map { OutletChecklistItem(task = it) }
                                }
                            },
                            enabled = !isAiLoading && aiPrompt.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isAiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate with AI")
                        }
                    } else if (builderTab == 1) {
                        OutlinedTextField(
                            value = excelPasteData,
                            onValueChange = { excelPasteData = it },
                            label = { Text("Paste rows from Excel (one task per line)") },
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val parsedTasks = excelPasteData.lines()
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }
                                    .map { OutletChecklistItem(task = it) }
                                checklistItems = parsedTasks
                                Toast.makeText(context, "Imported ${parsedTasks.size} tasks", Toast.LENGTH_SHORT).show()
                            },
                            enabled = excelPasteData.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Import Tasks")
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, 
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { launcher.launch("*/*") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Select PDF or Excel File")
                            }
                            
                            if (isAiLoading) {
                                Spacer(modifier = Modifier.height(16.dp))
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("F&B Assistant is analyzing ${selectedFileName ?: "the file"}...", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            } else if (selectedFileName != null && checklistItems.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("✅ Successfully extracted from $selectedFileName", color = Color(0xFF16A34A), fontSize = 12.sp)
                            }
                        }
                    }"""

if old_block in text:
    text = text.replace(old_block, new_block)
    with open(file_path, "w") as f:
        f.write(text)
    print("Success")
else:
    print("Failed to find old block")


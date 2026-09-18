package com.example.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.SavedNameTag
import com.example.viewmodel.HotelStudioViewModel
import com.example.util.DocumentNameExtractor
import com.example.util.ExtractedPerson
import com.example.util.NameTagPdfGenerator
import com.example.util.NameTagStorage
import com.example.util.PdfPreviewUtil
import com.example.util.SalutationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTagCreatorScreen(viewModel: HotelStudioViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    var nameInput by remember { mutableStateOf("") }
    var designationInput by remember { mutableStateOf("") }
    var salutationInput by remember { mutableStateOf("Mr.") }
    var isSalutationCustomized by remember { mutableStateOf(false) }
    var selectedTemplateIndex by remember { mutableStateOf(0) }
    var selectedPageSize by remember { mutableStateOf("A5") }
    var fontScale by remember { mutableFloatStateOf(1.0f) }
    var autoScaleEnabled by remember { mutableStateOf(false) }
    
    var savedTags by remember { mutableStateOf(NameTagStorage.getSavedTags(context)) }
    var extractedDelegates by remember { mutableStateOf<List<ExtractedPerson>>(emptyList()) }
    var activeDelegateIndex by remember { mutableIntStateOf(-1) }
    
    var isGenerating by remember { mutableStateOf(false) }
    var isExtracting by remember { mutableStateOf(false) }
    var reviewState by remember { mutableStateOf(0) }
    val userProfile by viewModel.userProfile.collectAsState()
    
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val themes = NameTagPdfGenerator.THEMES
    val templateNames = themes.map { it.name }

    // File Picker for Name Extractor (PDF and Images)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                isExtracting = true
                viewModel.showToast("Extracting all names & designations from document...")
                try {
                    val extracted = DocumentNameExtractor.extractFromUri(context, uri)
                    extractedDelegates = extracted
                    if (extracted.isNotEmpty()) {
                        activeDelegateIndex = 0
                        val first = extracted.first()
                        nameInput = first.name
                        designationInput = first.designation
                        salutationInput = first.salutation
                        isSalutationCustomized = false
                        viewModel.showToast("Extracted ${extracted.size} attendees with auto-salutation!")
                    } else {
                        viewModel.showToast("No names detected in document.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    viewModel.showToast("Extraction error: ${e.message}")
                } finally {
                    isExtracting = false
                }
            }
        }
    }

    // Real-time validation check
    val validationResult = remember(nameInput, designationInput, salutationInput, selectedPageSize, selectedTemplateIndex, fontScale) {
        NameTagPdfGenerator.validateDimensions(
            name = nameInput.ifBlank { "John Doe" },
            designation = designationInput.ifBlank { "General Manager" },
            pageSize = selectedPageSize,
            templateIndex = selectedTemplateIndex,
            fontScale = fontScale,
            salutation = salutationInput
        )
    }

    // Auto-apply suggested scale if auto-scale is enabled
    LaunchedEffect(autoScaleEnabled, validationResult.suggestedScale) {
        if (autoScaleEnabled) {
            fontScale = validationResult.suggestedScale
        }
    }
    
    // Auto-update preview
    LaunchedEffect(nameInput, designationInput, salutationInput, selectedTemplateIndex, selectedPageSize, userProfile?.customLogoBase64, fontScale) {
        val n = nameInput.ifBlank { "John Doe" }
        val d = designationInput.ifBlank { "General Manager" }
        withContext(Dispatchers.IO) {
            try {
                val tempFile = NameTagPdfGenerator.generateDeskTent(
                    context = context,
                    name = n,
                    designation = d,
                    logoBase64 = userProfile?.customLogoBase64,
                    templateIndex = selectedTemplateIndex,
                    pageSize = selectedPageSize,
                    fontScale = fontScale,
                    salutation = salutationInput
                )
                val bmp = PdfPreviewUtil.renderPdfToBitmap(tempFile)
                withContext(Dispatchers.Main) {
                    previewBitmap = bmp
                }
            } catch(e:Exception) {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name Extractor Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("F&B Assistant Document Name & Designation Extractor", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Upload a PDF or Image (guest rosters, event sheets, attendee lists, seating charts). Automatically detects all guest names and designations (checking and using the Organisation name if no designation is found) with automatic salutation as per name.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { filePickerLauncher.launch("*/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary, contentColor = MaterialTheme.colorScheme.onTertiary),
                    enabled = !isExtracting
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isExtracting) "Extracting All Names..." else "Upload PDF or Image")
                }
                if (isExtracting) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Analyzing document, extracting all names & designations with auto-salutations...",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onTertiaryContainer)
                }
            }
        }

        // Extracted Attendees Roster List
        if (extractedDelegates.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Groups,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Extracted Attendees (${extractedDelegates.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Auto-salutation & Org-fallback applied",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        TextButton(
                            onClick = {
                                extractedDelegates = emptyList()
                                activeDelegateIndex = -1
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Clear", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Batch Action: Save All to Desk-Tents
                    Button(
                        onClick = {
                            var count = 0
                            for (person in extractedDelegates) {
                                val tag = SavedNameTag(
                                    name = person.name,
                                    designation = person.designation,
                                    salutation = person.salutation,
                                    templateIndex = selectedTemplateIndex,
                                    pageSize = selectedPageSize,
                                    fontScale = fontScale
                                )
                                savedTags = NameTagStorage.saveTag(context, tag)
                                count++
                            }
                            viewModel.showToast("Saved all $count extracted attendees to Saved Desk-Tents!")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.BookmarkAdded, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Save All (${extractedDelegates.size}) to Desk-Tents",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(extractedDelegates.size) { idx ->
                            val person = extractedDelegates[idx]
                            val isSelected = activeDelegateIndex == idx
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                border = BorderStroke(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier
                                    .width(230.dp)
                                    .clickable {
                                        activeDelegateIndex = idx
                                        nameInput = person.name
                                        designationInput = person.designation
                                        salutationInput = person.salutation
                                        isSalutationCustomized = false
                                        viewModel.showToast("Loaded ${person.salutation} ${person.name} into editor")
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.tertiaryContainer
                                        ) {
                                            Text(
                                                text = "✨ ${person.salutation}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        if (isSelected) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.primary
                                            ) {
                                                Text(
                                                    text = "In Editor",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "${person.salutation} ${person.name}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = person.designation,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    if (person.isOrgFallback) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFEFF6FF),
                                            border = BorderStroke(0.5.dp, Color(0xFF93C5FD))
                                        ) {
                                            Text(
                                                text = "🏢 Org Name Used",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF1D4ED8),
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isSelected) "Active" else "Tap to edit",
                                            fontSize = 10.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                        )
                                        IconButton(
                                            onClick = {
                                                val singleTag = SavedNameTag(
                                                    name = person.name,
                                                    designation = person.designation,
                                                    salutation = person.salutation,
                                                    templateIndex = selectedTemplateIndex,
                                                    pageSize = selectedPageSize,
                                                    fontScale = fontScale
                                                )
                                                savedTags = NameTagStorage.saveTag(context, singleTag)
                                                viewModel.showToast("Saved ${person.salutation} ${person.name} to desk-tents")
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.BookmarkAdd,
                                                contentDescription = "Save Tag",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Details Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Desk-Tent Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (!isSalutationCustomized) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (!isSalutationCustomized) "✨ Auto: $salutationInput" else "✏️ Custom: $salutationInput",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isSalutationCustomized) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { newName ->
                        nameInput = newName
                        val explicit = SalutationHelper.extractTitle(newName)
                        if (explicit != null) {
                            salutationInput = explicit
                        } else if (!isSalutationCustomized) {
                            salutationInput = SalutationHelper.detectSalutation(newName, designationInput)
                        }
                    },
                    label = { Text("Guest/Staff Name") },
                    placeholder = { Text("e.g. Alexander Pierce, Emily Watson, Chef Marco") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Salutation selector picked up automatically as per name
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Salutation (Auto-selected as per name):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (isSalutationCustomized) {
                            TextButton(
                                onClick = {
                                    isSalutationCustomized = false
                                    salutationInput = SalutationHelper.detectSalutation(nameInput, designationInput)
                                },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp)
                            ) {
                                Text("Reset to Auto", fontSize = 10.sp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val salutationOptions = listOf("Mr.", "Ms.", "Mrs.", "Dr.", "Chef", "Prof.", "Capt.", "None")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(salutationOptions.size) { idx ->
                            val option = salutationOptions[idx]
                            val isSelected = salutationInput == option
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    salutationInput = option
                                    isSalutationCustomized = true
                                },
                                label = {
                                    Text(
                                        text = if (isSelected && !isSalutationCustomized) "$option ✨" else option,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = designationInput,
                    onValueChange = { newDesig ->
                        designationInput = newDesig
                        if (!isSalutationCustomized) {
                            salutationInput = SalutationHelper.detectSalutation(nameInput, newDesig)
                        }
                    },
                    label = { Text("Designation") },
                    placeholder = { Text("e.g. Chief Executive Officer, Executive Chef, Medical Director") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                )

                // SAVE BUTTON RIGHT AFTER DESIGNATION
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val cleaned = SalutationHelper.cleanName(nameInput)
                        if (cleaned.isBlank()) {
                            viewModel.showToast("Please enter a guest name before saving")
                            return@Button
                        }
                        val tag = SavedNameTag(
                            name = cleaned,
                            designation = designationInput.trim(),
                            salutation = salutationInput,
                            templateIndex = selectedTemplateIndex,
                            pageSize = selectedPageSize,
                            fontScale = fontScale
                        )
                        savedTags = NameTagStorage.saveTag(context, tag)
                        viewModel.showToast("Saved desk-tent for $salutationInput $cleaned!")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.BookmarkAdd, contentDescription = "Save Name Tag", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Name Tag", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Saved Name Tags Collection
        if (savedTags.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Bookmarks,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Saved Desk-Tents (${savedTags.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(
                            onClick = {
                                savedTags = NameTagStorage.clearAll(context)
                                viewModel.showToast("Cleared all saved tags")
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Text("Clear All", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(savedTags.size) { idx ->
                            val tag = savedTags[idx]
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                                modifier = Modifier
                                    .width(220.dp)
                                    .clickable {
                                        nameInput = tag.name
                                        designationInput = tag.designation
                                        salutationInput = tag.salutation
                                        isSalutationCustomized = true
                                        selectedTemplateIndex = tag.templateIndex
                                        selectedPageSize = tag.pageSize
                                        fontScale = tag.fontScale
                                        viewModel.showToast("Loaded \"${tag.salutation} ${tag.name}\"")
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${tag.salutation} ${tag.name}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = tag.designation.ifBlank { "No designation" },
                                            fontSize = 10.sp,
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${tag.pageSize} • ${templateNames.getOrElse(tag.templateIndex) { "Ivory" }}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            savedTags = NameTagStorage.deleteTag(context, tag.id)
                                            viewModel.showToast("Removed from saved list")
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Real-Time Layout Health & Overflow Warning Banner
        if (validationResult.hasOverflow) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = BorderStroke(1.5.dp, Color(0xFFD97706)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.WarningAmber,
                            contentDescription = "Text Overflow Warning",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Layout Overflow Warning",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    if (validationResult.isNameTooLong) {
                        Text(
                            text = "• Guest Name (${validationResult.nameWidth.toInt()}pt) exceeds printable width (${validationResult.maxAllowedWidth.toInt()}pt) for $selectedPageSize and will breach the luxury double-gold border.",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                    if (validationResult.isDesignationTooLong) {
                        Text(
                            text = "• Designation (${validationResult.designationWidth.toInt()}pt) exceeds safe margins (${validationResult.maxAllowedWidth.toInt()}pt) and may crowd the corner ornaments.",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Suggested Scaling:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "${(validationResult.suggestedScale * 100).toInt()}% font size",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309)
                                )
                            }
                            Button(
                                onClick = { 
                                    fontScale = validationResult.suggestedScale
                                    autoScaleEnabled = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD97706),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Apply ${(validationResult.suggestedScale * 100).toInt()}% Scale", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (fontScale < 1.0f) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (fontScale < 1.0f) Color(0xFF86EFAC) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Layout Safe",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (fontScale < 1.0f)
                                "Layout Safe: Scaled to ${(fontScale * 100).toInt()}% (Clear of borders)"
                            else
                                "Layout Safe: Fits comfortably within $selectedPageSize frame",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (fontScale < 1.0f) Color(0xFF15803D) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (fontScale < 1.0f) {
                        TextButton(
                            onClick = { 
                                fontScale = 1.0f
                                autoScaleEnabled = false 
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Reset 100%", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Font Size Scaling & Layout Control Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FormatSize,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Font Size Scaling", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (validationResult.hasOverflow) Color(0xFFFDE68A) else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${(fontScale * 100).toInt()}% Scale",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (validationResult.hasOverflow) Color(0xFF78350F) else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dynamically adjusts typography to prevent long names from breaking the ornamental double-gold border.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = fontScale,
                    onValueChange = {
                        fontScale = it
                        autoScaleEnabled = false
                    },
                    valueRange = 0.5f..1.0f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("50% (Compact)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                    Text("75% (Balanced)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                    Text("100% (Standard)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        1.0f to "100%",
                        0.85f to "85%",
                        0.70f to "70%"
                    ).forEach { (scaleVal, label) ->
                        val isSelected = kotlin.math.abs(fontScale - scaleVal) < 0.02f
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                fontScale = scaleVal
                                autoScaleEnabled = false
                            },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }

                    FilterChip(
                        selected = autoScaleEnabled,
                        onClick = {
                            autoScaleEnabled = !autoScaleEnabled
                            if (autoScaleEnabled) {
                                fontScale = validationResult.suggestedScale
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        label = {
                            Text(
                                "Auto-Fit (${(validationResult.suggestedScale * 100).toInt()}%)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }
        
        // Page Configuration
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Page & Template Configuration", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Print Size:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selectedPageSize == "A5", onClick = { selectedPageSize = "A5" })
                        Text("A5 (Desk-Tent)", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selectedPageSize == "A4", onClick = { selectedPageSize = "A4" })
                        Text("A4 (Full Sheet)", fontSize = 14.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Theme / Style (Luxury Collection)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = themes.getOrNull(selectedTemplateIndex)?.name ?: "Theme",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(themes.size) { index ->
                        val theme = themes[index]
                        val isSelected = selectedTemplateIndex == index
                        val themeBg = Color(theme.backgroundColor)
                        val themeBorder = Color(theme.outerBorderColor)
                        val themeText = Color(theme.nameTextColor)
                        val themeAccent = Color(theme.cornerAccentColor)

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = themeBg,
                            border = BorderStroke(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else themeBorder.copy(alpha = 0.8f)
                            ),
                            shadowElevation = if (isSelected) 4.dp else 1.dp,
                            modifier = Modifier
                                .width(150.dp)
                                .height(76.dp)
                                .clickable { 
                                    selectedTemplateIndex = index
                                    viewModel.showToast("Applied theme: ${theme.name}")
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Palette Swatches
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(themeBorder)
                                                .border(0.5.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(themeAccent)
                                                .border(0.5.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                                        )
                                    }

                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                Column {
                                    Text(
                                        text = theme.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = themeText,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = theme.subtitle,
                                        fontSize = 9.sp,
                                        color = if (theme.isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF64748B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Active Theme Description Ribbon
                val activeTheme = themes.getOrElse(selectedTemplateIndex) { themes[0] }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Theme: ${activeTheme.name} • ${activeTheme.subtitle}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // PDF Live Preview
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Preview, contentDescription = null, tint = Color.DarkGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Live PDF Preview", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                if (previewBitmap != null) {
                    Image(
                        bitmap = previewBitmap!!.asImageBitmap(),
                        contentDescription = "PDF Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray)
                            .background(Color.White),
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        
        // Generate Button
        Button(
            onClick = {
                val finalName = nameInput.ifBlank { "John Doe" }
                val finalDesig = designationInput.ifBlank { "General Manager" }
                
                scope.launch {
                    isGenerating = true
                    reviewState = 1
                    delay(1200)
                    reviewState = 2
                    delay(1200)
                    reviewState = 3
                    
                    try {
                        val pdfFile = NameTagPdfGenerator.generateDeskTent(
                            context = context,
                            name = finalName,
                            designation = finalDesig,
                            logoBase64 = userProfile?.customLogoBase64,
                            templateIndex = selectedTemplateIndex,
                            pageSize = selectedPageSize,
                            fontScale = fontScale,
                            salutation = salutationInput
                        )
                        
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            pdfFile
                        )
                        
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isGenerating = false
                        reviewState = 0
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = !isGenerating
        ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isGenerating) "Processing..." else "Generate Final PDF ($selectedPageSize)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        if (isGenerating) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Two-Agent Review Process", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (reviewState >= 2) Icons.Default.CheckCircle else Icons.Default.SupportAgent, 
                            contentDescription = null,
                            tint = if (reviewState >= 2) Color(0xFF10B981) else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (reviewState >= 2) "Agent 1: Branding review approved" else "Agent 1: Checking branding...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (reviewState >= 3) Icons.Default.CheckCircle else Icons.Default.SupportAgent, 
                            contentDescription = null,
                            tint = if (reviewState >= 3) Color(0xFF10B981) else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (reviewState >= 3) "Agent 2: Spelling/Layout print-ready" else if (reviewState >= 2) "Agent 2: Verifying print-readiness..." else "Agent 2: Waiting...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

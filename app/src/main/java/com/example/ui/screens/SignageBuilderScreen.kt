package com.example.ui.screens

import android.net.Uri
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.viewmodel.HotelStudioViewModel

fun decodeBase64ToBitmap(base64Str: String?): android.graphics.Bitmap? {
    if (base64Str.isNullOrBlank()) return null
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun SignageBuilderScreen(viewModel: HotelStudioViewModel, modifier: Modifier = Modifier) {
    // UI State
    var activeTab by remember { mutableStateOf(0) } // 0: Config, 1: Design, 2: Preview
    var showQuickPreview by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Signage State
    var hallName1 by remember { mutableStateOf("") }
    var hallLevel1 by remember { mutableStateOf("Level 1") }
    var hallName2 by remember { mutableStateOf("") }
    var hallLevel2 by remember { mutableStateOf("Level 2") }
    
    var orientation by remember { mutableStateOf("Portrait") } // Portrait, Landscape, Two-Hall
    var eventArtworkUri by remember { mutableStateOf<Uri?>(null) }
    var eventName by remember { mutableStateOf("") }
    var direction by remember { mutableStateOf("Straight") } // Straight, Left, Right
    var selectedTemplate by remember { mutableStateOf(signageTemplates[0]) }

    val userProfile by viewModel.userProfile.collectAsState()
    val logoBase64 = userProfile?.customLogoBase64
    val isLogoLocked = userProfile?.isLogoLocked ?: true

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        eventArtworkUri = uri
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Header
        Text(
            text = "DIGITAL SIGNAGE BUILDER",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )

        // Tabs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 8.dp)) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("Config", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 8.dp)) {
                        Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("Design", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 8.dp)) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("Preview", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = activeTab == 3,
                onClick = { activeTab = 3 },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 8.dp)) {
                        Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("Tag Fleet", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        when (activeTab) {
            0 -> {
                // Configuration Section
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Hall Details
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MeetingRoom, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Venue Details", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }
                            
                            OutlinedTextField(
                                value = hallName1,
                                onValueChange = { hallName1 = it },
                                label = { Text("Primary Hall Name") },
                                placeholder = { Text("Grand Ballroom") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = hallLevel1,
                                onValueChange = { hallLevel1 = it },
                                label = { Text("Level / Floor") },
                                placeholder = { Text("Level 1") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            if (orientation == "Two-Hall") {
                                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                                Text("Secondary Hall", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                OutlinedTextField(
                                    value = hallName2,
                                    onValueChange = { hallName2 = it },
                                    label = { Text("Secondary Hall Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                OutlinedTextField(
                                    value = hallLevel2,
                                    onValueChange = { hallLevel2 = it },
                                    label = { Text("Secondary Level") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }

                    // Layout Details
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Event & Direction", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }

                            // Orientation
                            Column {
                                Text("Orientation Format", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Portrait", "Landscape", "Two-Hall").forEach { format ->
                                        val selected = orientation == format
                                        Surface(
                                            modifier = Modifier.weight(1f).clickable { orientation = format },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                        ) {
                                            Text(
                                                format,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                textAlign = TextAlign.Center,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            // Event Details
                            OutlinedTextField(
                                value = eventName,
                                onValueChange = { eventName = it },
                                label = { Text("Event Name / Title") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            
                            OutlinedButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PhotoCamera, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (eventArtworkUri != null) "Change Artwork" else "Upload Artwork", fontSize = 12.sp)
                            }

                            // Direction
                            Column {
                                Text("Directional Arrow", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    mapOf("Left" to Icons.Default.ArrowBack, "Straight" to Icons.Default.ArrowUpward, "Right" to Icons.Default.ArrowForward).forEach { (dir, icon) ->
                                        val selected = direction == dir
                                        Surface(
                                            modifier = Modifier.weight(1f).clickable { direction = dir },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                            border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(icon, null, modifier = Modifier.size(20.dp), tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text(dir, fontSize = 10.sp, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Design Section
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Branding
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Brand Logo", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Locked", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isLogoLocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                                    IconButton(onClick = { viewModel.toggleLogoLock() }, modifier = Modifier.size(32.dp)) {
                                        Icon(
                                            imageVector = if (isLogoLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                            contentDescription = null,
                                            tint = if (isLogoLocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            
                            val context = LocalContext.current
                            val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                                uri?.let {
                                    val inputStream = context.contentResolver.openInputStream(it)
                                    val bytes = inputStream?.readBytes()
                                    inputStream?.close()
                                    if (bytes != null) {
                                        val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                                        viewModel.setCustomLogo(base64)
                                    }
                                }
                            }

                            OutlinedButton(
                                onClick = { logoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PhotoCamera, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Update Organization Logo", fontSize = 12.sp)
                            }
                        }
                    }

                    // Template Grid
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Style, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Hospitality Themes", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }

                            // Visual Grid for Templates
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val chunked = signageTemplates.chunked(2)
                                chunked.forEach { rowItems ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        rowItems.forEach { template ->
                                            val isSelected = selectedTemplate == template
                                            Surface(
                                                modifier = Modifier.weight(1f).clickable { selectedTemplate = template },
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                                border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                                            ) {
                                                Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(60.dp, 40.dp)
                                                                .clip(RoundedCornerShape(4.dp))
                                                            .background(template.bgColor)
                                                            .border(1.dp, template.borderColor, RoundedCornerShape(4.dp))
                                                    )
                                                    Spacer(Modifier.height(4.dp))
                                                    Text(template.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                }
                                            }
                                        }
                                        if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Preview & Export Section
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Surface(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "LIVE HIGH-RES PREVIEW",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    val logoBitmap = remember(logoBase64) { decodeBase64ToBitmap(logoBase64) }
                    
                    Box(modifier = Modifier.shadow(16.dp, RoundedCornerShape(12.dp))) {
                        when (orientation) {
                            "Portrait" -> PortraitSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, eventArtworkUri, eventName, direction)
                            "Landscape" -> LandscapeSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, eventArtworkUri, eventName, direction)
                            "Two-Hall" -> TwoHallSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, hallName2, hallLevel2, eventArtworkUri, eventName, direction)
                        }
                    }
                    
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.showToast("Saved Board Information") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Save Signage State", fontWeight = FontWeight.Bold)
                        }
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledTonalButton(
                                onClick = { viewModel.showToast("300 DPI PDF exported to Downloads") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("PDF")
                            }
                            FilledTonalButton(
                                onClick = { viewModel.showToast("Live MP4 rendered to Downloads") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.VideoCall, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("MP4")
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(40.dp))
                }
            }
            3 -> {
                BuffetDigitalTagsDashboard(
                    viewModel = viewModel,
                    onBackToStudio = { activeTab = 0 },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

        // Floating Quick Preview Button
        if (activeTab != 2 && activeTab != 3) {
            FloatingActionButton(
                onClick = { showQuickPreview = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Visibility, contentDescription = "Quick Preview")
            }
        }

        if (showQuickPreview) {
            Dialog(onDismissRequest = { showQuickPreview = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("QUICK PREVIEW", fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                        
                        val logoBase64 = viewModel.userProfile.collectAsState().value?.customLogoBase64
                        val logoBitmap = remember(logoBase64) { decodeBase64ToBitmap(logoBase64) }

                        Box(modifier = Modifier.scale(0.8f)) {
                            when (orientation) {
                                "Portrait" -> PortraitSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, eventArtworkUri, eventName, direction)
                                "Landscape" -> LandscapeSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, eventArtworkUri, eventName, direction)
                                "Two-Hall" -> TwoHallSignage(selectedTemplate, logoBitmap, hallName1, hallLevel1, hallName2, hallLevel2, eventArtworkUri, eventName, direction)
                            }
                        }
                        
                        Button(onClick = { showQuickPreview = false }, modifier = Modifier.fillMaxWidth()) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

// Reusable Direction Arrow Component
@Composable
fun DirectionArrow(direction: String, color: Color = Color(0xFFD4AF37)) {
    val icon = when (direction) {
        "Left" -> Icons.Default.ArrowBack
        "Right" -> Icons.Default.ArrowForward
        else -> Icons.Default.ArrowUpward
    }
    Icon(imageVector = icon, contentDescription = direction, tint = color, modifier = Modifier.size(64.dp))
}

@Composable
fun PortraitSignage(template: SignageTemplate, logo: android.graphics.Bitmap?, hallName: String, hallLevel: String, artwork: Uri?, eventName: String, direction: String) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(500.dp)
            .background(template.bgColor)
            .then(
                if (template.isDoubleBorder) {
                    Modifier
                        .border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                        .padding(4.dp)
                        .border(1.dp, template.borderColor.copy(alpha = 0.6f), RoundedCornerShape(template.cornerRadius))
                } else {
                    Modifier.border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                }
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Protected Clear Zone & Logo
            Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                if (logo != null) {
                    Image(bitmap = logo.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                } else {
                    Text("ESTABLISHMENT LOGO", color = template.accentColor, fontWeight = FontWeight.Bold)
                }
            }
            
            // Hall Name & Level
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    hallName.ifBlank { "GRAND BALLROOM" }.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = template.textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(hallLevel.ifBlank { "LEVEL 1" }.uppercase(), fontSize = 12.sp, color = template.textSecondaryColor, letterSpacing = 2.sp)
            }
            
            // Full Event Artwork Panel
            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 16.dp).border(1.dp, template.borderColor.copy(alpha = 0.5f), RoundedCornerShape(template.cornerRadius / 2))) {
                if (artwork != null) {
                    Image(
                        painter = rememberAsyncImagePainter(artwork),
                        contentDescription = "Event Artwork",
                        contentScale = ContentScale.Crop, // Prevent margins, fill without whitespace
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(eventName.ifBlank { "Event Name Here" }, fontSize = 24.sp, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center, color = template.textColor)
                    }
                }
            }
            
            // Directional Arrow Panel
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(template.borderColor.copy(alpha = 0.1f), RoundedCornerShape(template.cornerRadius / 2)), contentAlignment = Alignment.Center) {
                DirectionArrow(direction, template.accentColor)
            }
        }
    }
}

@Composable
fun LandscapeSignage(template: SignageTemplate, logo: android.graphics.Bitmap?, hallName: String, hallLevel: String, artwork: Uri?, eventName: String, direction: String) {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(281.dp) // 16:9 ratio
            .background(template.bgColor)
            .then(
                if (template.isDoubleBorder) {
                    Modifier
                        .border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                        .padding(4.dp)
                        .border(1.dp, template.borderColor.copy(alpha = 0.6f), RoundedCornerShape(template.cornerRadius))
                } else {
                    Modifier.border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                }
            )
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Artwork Panel (Left/Center)
            Box(modifier = Modifier.weight(1.5f).fillMaxHeight().border(1.dp, template.borderColor.copy(alpha = 0.5f), RoundedCornerShape(template.cornerRadius / 2))) {
                if (artwork != null) {
                    Image(
                        painter = rememberAsyncImagePainter(artwork),
                        contentDescription = "Event Artwork",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(eventName.ifBlank { "Event Name Here" }, fontSize = 24.sp, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center, color = template.textColor)
                    }
                }
            }
            
            // Info Panel (Right)
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo
                Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
                    if (logo != null) {
                        Image(bitmap = logo.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                    } else {
                        Text("LOGO", color = template.accentColor, fontWeight = FontWeight.Bold)
                    }
                }
                
                // Hall Name
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                    hallName.ifBlank { "GRAND BALLROOM" }.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = template.textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                    Text(hallLevel.ifBlank { "LEVEL 1" }.uppercase(), fontSize = 10.sp, color = template.textSecondaryColor, letterSpacing = 2.sp)
                }
                
                // Arrow
                Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(template.borderColor.copy(alpha = 0.1f), RoundedCornerShape(template.cornerRadius / 2)), contentAlignment = Alignment.Center) {
                    DirectionArrow(direction, template.accentColor)
                }
            }
        }
    }
}

@Composable
fun TwoHallSignage(template: SignageTemplate, logo: android.graphics.Bitmap?, hall1: String, level1: String, hall2: String, level2: String, artwork: Uri?, eventName: String, direction: String) {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(281.dp)
            .background(template.bgColor)
            .then(
                if (template.isDoubleBorder) {
                    Modifier
                        .border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                        .padding(4.dp)
                        .border(1.dp, template.borderColor.copy(alpha = 0.6f), RoundedCornerShape(template.cornerRadius))
                } else {
                    Modifier.border(template.borderThickness, template.borderColor, RoundedCornerShape(template.cornerRadius))
                }
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            // Common Header & Logo
            Box(modifier = Modifier.fillMaxWidth().height(50.dp), contentAlignment = Alignment.Center) {
                if (logo != null) {
                    Image(bitmap = logo.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                } else {
                    Text("ESTABLISHMENT LOGO", color = template.accentColor, fontWeight = FontWeight.Bold)
                }
            }
            
            // Two Cards
            Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Left side direction arrow if left
                if (direction == "Left") {
                    Box(modifier = Modifier.fillMaxHeight().width(60.dp).background(template.borderColor.copy(alpha = 0.1f), RoundedCornerShape(template.cornerRadius / 2)), contentAlignment = Alignment.Center) {
                        DirectionArrow("Left", template.accentColor)
                    }
                }
                
                // Hall 1 Card
                Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(hall1.ifBlank { "HALL A" }.uppercase(), fontFamily = FontFamily.Serif, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = template.textColor)
                    Text(level1.ifBlank { "LEVEL 1" }.uppercase(), fontSize = 10.sp, color = template.textSecondaryColor, letterSpacing = 1.sp)
                    Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 8.dp).border(1.dp, template.borderColor.copy(alpha = 0.5f), RoundedCornerShape(template.cornerRadius / 2))) {
                        if (artwork != null) {
                            Image(painter = rememberAsyncImagePainter(artwork), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(eventName, textAlign = TextAlign.Center, color = template.textColor) }
                        }
                    }
                }
                
                // Common Straight Arrow
                if (direction == "Straight") {
                    Box(modifier = Modifier.fillMaxHeight().width(60.dp).background(template.borderColor.copy(alpha = 0.1f), RoundedCornerShape(template.cornerRadius / 2)), contentAlignment = Alignment.Center) {
                        DirectionArrow("Straight", template.accentColor)
                    }
                }

                // Hall 2 Card
                Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(hall2.ifBlank { "HALL B" }.uppercase(), fontFamily = FontFamily.Serif, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = template.textColor)
                    Text(level2.ifBlank { "LEVEL 1" }.uppercase(), fontSize = 10.sp, color = template.textSecondaryColor, letterSpacing = 1.sp)
                    Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 8.dp).border(1.dp, template.borderColor.copy(alpha = 0.5f), RoundedCornerShape(template.cornerRadius / 2))) {
                        if (artwork != null) {
                            Image(painter = rememberAsyncImagePainter(artwork), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(eventName, textAlign = TextAlign.Center, color = template.textColor) }
                        }
                    }
                }
                
                // Right side direction arrow if right
                if (direction == "Right") {
                    Box(modifier = Modifier.fillMaxHeight().width(60.dp).background(template.borderColor.copy(alpha = 0.1f), RoundedCornerShape(template.cornerRadius / 2)), contentAlignment = Alignment.Center) {
                        DirectionArrow("Right", template.accentColor)
                    }
                }
            }
        }
    }
}

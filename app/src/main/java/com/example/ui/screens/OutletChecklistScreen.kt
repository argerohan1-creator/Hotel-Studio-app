package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Color as AndroidColor
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.OutlinedButton

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.TextButton
import com.example.util.ChecklistDeadlineReceiver

import androidx.compose.material.icons.filled.UploadFile
import android.provider.OpenableColumns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.HotelStudioViewModel
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OutletChecklistItem(
    val task: String,
    var isDone: Boolean = false,
    var remarks: String = ""
)

val OUTLETS = listOf("Coffee Shop", "Banquet", "Speciality Restaurant", "Tea/Coffee Lounge & Patisserie", "Custom (AI / Import)")
val SHIFTS = listOf("Opening", "Closing", "Night")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutletChecklistScreen(
    viewModel: HotelStudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    
    var selectedOutlet by remember { mutableStateOf(OUTLETS[0]) }
    var customOutletName by remember { mutableStateOf("") }
    var selectedShift by remember { mutableStateOf(SHIFTS[0]) }
    
    var teamLeaderName by remember { mutableStateOf("") }
    var managerPhone by remember { mutableStateOf("") }
    
    var checklistItems by remember { mutableStateOf(getTasksForOutletAndShift(selectedOutlet, selectedShift)) }
    
    var aiPrompt by remember { mutableStateOf("") }
    var excelPasteData by remember { mutableStateOf("") }

    LaunchedEffect(selectedOutlet, selectedShift) {
        if (selectedOutlet != "Custom (AI / Import)") {
            checklistItems = getTasksForOutletAndShift(selectedOutlet, selectedShift)
        } else {
            checklistItems = emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Outlet Duty Checklist",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Generate and share shift reports",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Outlet Dropdown
            var expandedOutlet by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedOutlet,
                onExpandedChange = { expandedOutlet = it }
            ) {
                OutlinedTextField(
                    value = selectedOutlet,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Outlet") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOutlet) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedOutlet,
                    onDismissRequest = { expandedOutlet = false }
                ) {
                    OUTLETS.forEach { outlet ->
                        DropdownMenuItem(
                            text = { Text(outlet) },
                            onClick = {
                                selectedOutlet = outlet
                                expandedOutlet = false
                            }
                        )
                    }
                }
            }
            
            AnimatedVisibility(visible = selectedOutlet == "Custom (AI / Import)") {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = customOutletName,
                        onValueChange = { customOutletName = it },
                        label = { Text("Custom Outlet Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Shift Selection
            Row(modifier = Modifier.fillMaxWidth()) {
                SHIFTS.forEach { shift ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selectedShift = shift }
                            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        RadioButton(
                            selected = (shift == selectedShift),
                            onClick = { selectedShift = shift }
                        )
                        Text(text = shift, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            
            // Custom Builder Section
            AnimatedVisibility(visible = selectedOutlet == "Custom (AI / Import)") {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Checklist Builder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                    
                    var builderTab by remember { mutableStateOf(0) } // 0: AI, 1: Paste, 2: Upload
                    
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
                    }
                }
            }
        }

        HorizontalDivider()

        // Checklist
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(checklistItems) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newList = checklistItems.toMutableList()
                                    newList[index] = item.copy(isDone = !item.isDone)
                                    checklistItems = newList
                                }
                        ) {
                            Icon(
                                imageVector = if (item.isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (item.isDone) Color(0xFF16A34A) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.task,
                                fontSize = 14.sp,
                                fontWeight = if (item.isDone) FontWeight.Normal else FontWeight.Medium,
                                color = if (item.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        if (!item.isDone) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = item.remarks,
                                onValueChange = { newRemarks ->
                                    val newList = checklistItems.toMutableList()
                                    newList[index] = item.copy(remarks = newRemarks)
                                    checklistItems = newList
                                },
                                placeholder = { Text("Remarks for incomplete task...", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Report Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = teamLeaderName,
                    onValueChange = { teamLeaderName = it },
                    label = { Text("Team Leader Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = managerPhone,
                    onValueChange = { managerPhone = it },
                    label = { Text("Manager WhatsApp Number (Optional for Direct)") },
                    placeholder = { Text("e.g. 919876543210") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                val finalOutletName = if (selectedOutlet == "Custom (AI / Import)") customOutletName.ifBlank { "Custom Outlet" } else selectedOutlet
                

                var hasNotificationPermission by remember { mutableStateOf(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) }
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    hasNotificationPermission = isGranted
                }

                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    TextButton(
                        onClick = {
                            if (hasNotificationPermission) {
                                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                val intent = Intent(context, ChecklistDeadlineReceiver::class.java).apply {
                                    putExtra("EXTRA_OUTLET", finalOutletName)
                                    putExtra("EXTRA_SHIFT", selectedShift)
                                }
                                val pendingIntent = PendingIntent.getBroadcast(
                                    context, 
                                    0, 
                                    intent, 
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                
                                // Schedule for 5 seconds from now to mock "30 mins before deadline"
                                val triggerTime = System.currentTimeMillis() + 5000 
                                
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                                        Toast.makeText(context, "Exact alarm permission required", Toast.LENGTH_SHORT).show()
                                    } else {
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                                        Toast.makeText(context, "Deadline tracked. Will notify if incomplete.", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: SecurityException) {
                                    Toast.makeText(context, "Missing exact alarm permission", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Notification permission required", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Alarm, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start Shift Timer & Track Deadline")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            sendWhatsAppReport(
                                context = context,
                                outlet = finalOutletName,
                                shift = selectedShift,
                                teamLeader = teamLeaderName,
                                phone = managerPhone,
                                items = checklistItems,
                                isGroupShare = false
                            )
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Direct", fontSize = 11.sp)
                    }
                    
                    Button(
                        onClick = {
                            sendWhatsAppReport(
                                context = context,
                                outlet = finalOutletName,
                                shift = selectedShift,
                                teamLeader = teamLeaderName,
                                phone = "",
                                items = checklistItems,
                                isGroupShare = true
                            )
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("To Group", fontSize = 11.sp)
                    }
                    
                    OutlinedButton(
                        onClick = {
                            exportAndSharePdfReport(
                                context = context,
                                outlet = finalOutletName,
                                shift = selectedShift,
                                teamLeader = teamLeaderName,
                                items = checklistItems
                            )
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PDF", fontSize = 11.sp)
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

fun getTasksForOutletAndShift(outlet: String, shift: String): List<OutletChecklistItem> {
    val tasks = mutableListOf<String>()
    
    // Generic tasks for all outlets
    when (shift) {
        "Opening" -> {
            tasks.addAll(listOf(
                "Keys collected and outlet unlocked securely",
                "Lights, AC, and music system turned on",
                "POS system booted up and daily float verified",
                "Staff briefing conducted and grooming standards checked",
                "Tables wiped, set up, and aligned properly",
                "Condiments, salt/pepper, and sugar caddies refilled",
                "Allergen charts and menus clean and presentable"
            ))
        }
        "Closing" -> {
            tasks.addAll(listOf(
                "Final billings settled and POS batched out",
                "Cash float secured and deposited to front office/accounts",
                "Tables cleared, wiped, and chairs arranged",
                "Perishables returned to main kitchen or stored properly",
                "Lights, AC, and music system turned off",
                "Outlet locked and keys handed over to security"
            ))
        }
        "Night" -> {
            tasks.addAll(listOf(
                "Verify night staff attendance and brief on VIPs in-house",
                "Ensure all access doors and non-24h areas are securely locked",
                "Perform midnight POS system audit and rollover",
                "Deep cleaning of front-of-house floors and upholstery",
                "Restock all supplies for morning opening shift",
                "Check all chillers and freezers are at correct temperatures",
                "Prepare early bird breakfast boxes for early departures"
            ))
        }
    }

    // Specific tasks
    when (outlet) {
        "Coffee Shop" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Coffee machines calibrated and tested (espresso shot check)",
                        "Display fridge stocked with fresh pastries & sandwiches",
                        "Buffet counters heated/cooled and ready for service",
                        "Juice dispensers filled and chilled"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Coffee machines backflushed and steam wands cleaned",
                        "Display fridge items wrapped and labelled for expiry",
                        "Buffet counters cleaned and sanitized"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Run auto-clean cycles on all espresso machines",
                        "Sanitize juicers and blenders thoroughly",
                        "Stock up on takeaway cups, lids, and stirrers",
                        "Ensure pastry cases are clean and ready for morning load"
                    ))
                }
            }
        }
        "Banquet" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Function sheet (BEO) reviewed with the team",
                        "A/V equipment (mics, projectors) tested",
                        "Stage, podium, and seating arranged as per BEO",
                        "Chafing dishes placed with fuel cells ready"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Leftover food properly discarded or returned",
                        "Linens counted and sent to laundry",
                        "A/V equipment switched off and secured",
                        "Hall cleared of all event debris"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Set up tables and chairs for next morning's breakfast/conference",
                        "Polish all cutlery and glassware for next day's events",
                        "Check banquet hall AC and lighting automation schedules",
                        "Deep clean carpets in pre-function area"
                    ))
                }
            }
        }
        "Speciality Restaurant" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Reservations list checked and tables assigned",
                        "Wine chillers temperature checked",
                        "Special chef's specials board updated",
                        "Fine dining cutlery and glassware polished"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Open wine bottles vacuum-sealed and dated",
                        "Linen napkins separated for laundry",
                        "Inventory of high-value items (cigars, premium liquor) verified",
                        "Next day's reservations reviewed"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Deep clean the host/maitre d' station",
                        "Update the digital wine list and POS with 86'd items",
                        "Perform weekly silver polishing (if scheduled)",
                        "Secure all fine dining silverware in lockboxes"
                    ))
                }
            }
        }
        "Tea/Coffee Lounge & Patisserie" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Cake display vitrine temperature checked (2-4°C)",
                        "Fresh bakes arrayed with correct FSSAI tags",
                        "Tea selection boxes refilled and organized",
                        "Takeaway packaging stocked at the counter"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Unsold perishable pastries discarded/logged as wastage",
                        "Cake display vitrine defrosted/cleaned",
                        "Coffee/Tea stations thoroughly sanitized",
                        "Takeaway materials restocked for next day"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Clean the cake display vitrine glass inside and out",
                        "Receive and date early morning bakery deliveries",
                        "Stock and organize retail tea/coffee beans displays",
                        "Verify temperatures of all dessert chillers"
                    ))
                }
            }
        }
    }
    
    return tasks.map { OutletChecklistItem(task = it) }
}

fun sendWhatsAppReport(context: Context, outlet: String, shift: String, teamLeader: String, phone: String, items: List<OutletChecklistItem>, isGroupShare: Boolean) {
    if (teamLeader.isBlank()) {
        Toast.makeText(context, "Please enter Team Leader name", Toast.LENGTH_SHORT).show()
        return
    }
    
    if (items.isEmpty()) {
        Toast.makeText(context, "Checklist is empty", Toast.LENGTH_SHORT).show()
        return
    }
    
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
    
    val sb = StringBuilder()
    sb.append("*$outlet - $shift Checklist Report*\n")
    sb.append("Date: $dateStr\n")
    sb.append("Team Leader: $teamLeader\n\n")
    
    sb.append("*Completed Tasks:*\n")
    val completed = items.filter { it.isDone }
    if (completed.isEmpty()) sb.append("None\n")
    completed.forEach { sb.append("✅ ${it.task}\n") }
    
    sb.append("\n*Pending / Issues:*\n")
    val pending = items.filter { !it.isDone }
    if (pending.isEmpty()) sb.append("None (All tasks completed)\n")
    pending.forEach { 
        sb.append("❌ ${it.task}\n")
        if (it.remarks.isNotBlank()) {
            sb.append("   ↳ _Remark: ${it.remarks}_\n")
        }
    }
    
    sb.append("\n_Report generated via Hotel Studio F&B Assistant_")
    
    val text = sb.toString()
    
    if (isGroupShare) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            val fallbackIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }, "Share Report")
            context.startActivity(fallbackIntent)
        }
    } else {
        val encodedText = URLEncoder.encode(text, "UTF-8")
        try {
            val uriStr = if (phone.isNotBlank()) {
                val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                "https://wa.me/$cleanPhone?text=$encodedText"
            } else {
                "https://wa.me/?text=$encodedText"
            }
            
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriStr))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}



fun exportAndSharePdfReport(context: Context, outlet: String, shift: String, teamLeader: String, items: List<OutletChecklistItem>) {
    if (teamLeader.isBlank()) {
        Toast.makeText(context, "Please enter Team Leader name", Toast.LENGTH_SHORT).show()
        return
    }
    
    if (items.isEmpty()) {
        Toast.makeText(context, "Checklist is empty", Toast.LENGTH_SHORT).show()
        return
    }

    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
    var page = document.startPage(pageInfo)
    var canvas = page.canvas

    val paint = Paint()
    val titlePaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 18f
        color = AndroidColor.BLACK
    }
    val headerPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 14f
        color = AndroidColor.DKGRAY
    }
    val normalPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 12f
        color = AndroidColor.BLACK
    }
    val greenPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 12f
        color = AndroidColor.parseColor("#15803d")
    }
    val redPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 12f
        color = AndroidColor.parseColor("#b91c1c")
    }
    val italicPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        textSize = 10f
        color = AndroidColor.GRAY
    }

    val margin = 50f
    var yPos = margin
    val lineHeight = 20f

    val dateStr = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())

    canvas.drawText("$outlet - $shift Checklist Report", margin, yPos, titlePaint)
    yPos += lineHeight * 1.5f
    canvas.drawText("Date: $dateStr", margin, yPos, normalPaint)
    yPos += lineHeight
    canvas.drawText("Team Leader: $teamLeader", margin, yPos, normalPaint)
    yPos += lineHeight * 2f

    canvas.drawText("Completed Tasks:", margin, yPos, headerPaint)
    yPos += lineHeight * 1.2f
    val completed = items.filter { it.isDone }
    if (completed.isEmpty()) {
        canvas.drawText("None", margin + 10f, yPos, normalPaint)
        yPos += lineHeight
    } else {
        for (item in completed) {
            val text = "✓ ${item.task}"
            if (yPos > 800) {
                document.finishPage(page)
                page = document.startPage(pageInfo)
                canvas = page.canvas
                yPos = margin
            }
            canvas.drawText(text, margin + 10f, yPos, greenPaint)
            yPos += lineHeight
        }
    }

    yPos += lineHeight
    if (yPos > 780) {
        document.finishPage(page)
        page = document.startPage(pageInfo)
        canvas = page.canvas
        yPos = margin
    }

    canvas.drawText("Pending / Issues:", margin, yPos, headerPaint)
    yPos += lineHeight * 1.2f
    val pending = items.filter { !it.isDone }
    if (pending.isEmpty()) {
        canvas.drawText("None (All tasks completed)", margin + 10f, yPos, normalPaint)
        yPos += lineHeight
    } else {
        for (item in pending) {
            if (yPos > 780) {
                document.finishPage(page)
                page = document.startPage(pageInfo)
                canvas = page.canvas
                yPos = margin
            }
            canvas.drawText("✗ ${item.task}", margin + 10f, yPos, redPaint)
            yPos += lineHeight
            if (item.remarks.isNotBlank()) {
                canvas.drawText("Remark: ${item.remarks}", margin + 25f, yPos, italicPaint)
                yPos += lineHeight
            }
        }
    }

    yPos += lineHeight * 2f
    if (yPos > 800) {
        document.finishPage(page)
        page = document.startPage(pageInfo)
        canvas = page.canvas
        yPos = margin
    }
    
    val footerPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        textSize = 10f
        color = AndroidColor.LTGRAY
    }
    canvas.drawText("Report generated via Hotel Studio F&B Assistant", margin, yPos, footerPaint)

    document.finishPage(page)

    try {
        val file = File(context.cacheDir, "Checklist_Report.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = android.content.Intent.createChooser(intent, "Share PDF Report")
        context.startActivity(chooser)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show()
    }
}

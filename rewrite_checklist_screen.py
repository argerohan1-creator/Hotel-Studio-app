import sys

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"

content = """package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
val SHIFTS = listOf("Opening", "Closing")

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
                    
                    var builderTab by remember { mutableStateOf(0) } // 0: AI, 1: Excel
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
                        Text("Direct", fontSize = 12.sp)
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
                        Text("To Group", fontSize = 12.sp)
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
    if (shift == "Opening") {
        tasks.addAll(listOf(
            "Keys collected and outlet unlocked securely",
            "Lights, AC, and music system turned on",
            "POS system booted up and daily float verified",
            "Staff briefing conducted and grooming standards checked",
            "Tables wiped, set up, and aligned properly",
            "Condiments, salt/pepper, and sugar caddies refilled",
            "Allergen charts and menus clean and presentable"
        ))
    } else {
        tasks.addAll(listOf(
            "Final billings settled and POS batched out",
            "Cash float secured and deposited to front office/accounts",
            "Tables cleared, wiped, and chairs arranged",
            "Perishables returned to main kitchen or stored properly",
            "Lights, AC, and music system turned off",
            "Outlet locked and keys handed over to security"
        ))
    }

    // Specific tasks
    when (outlet) {
        "Coffee Shop" -> {
            if (shift == "Opening") {
                tasks.addAll(listOf(
                    "Coffee machines calibrated and tested (espresso shot check)",
                    "Display fridge stocked with fresh pastries & sandwiches",
                    "Buffet counters heated/cooled and ready for service",
                    "Juice dispensers filled and chilled"
                ))
            } else {
                tasks.addAll(listOf(
                    "Coffee machines backflushed and steam wands cleaned",
                    "Display fridge items wrapped and labelled for expiry",
                    "Buffet counters cleaned and sanitized"
                ))
            }
        }
        "Banquet" -> {
            if (shift == "Opening") {
                tasks.addAll(listOf(
                    "Function sheet (BEO) reviewed with the team",
                    "A/V equipment (mics, projectors) tested",
                    "Stage, podium, and seating arranged as per BEO",
                    "Chafing dishes placed with fuel cells ready"
                ))
            } else {
                tasks.addAll(listOf(
                    "Leftover food properly discarded or returned",
                    "Linens counted and sent to laundry",
                    "A/V equipment switched off and secured",
                    "Hall cleared of all event debris"
                ))
            }
        }
        "Speciality Restaurant" -> {
            if (shift == "Opening") {
                tasks.addAll(listOf(
                    "Reservations list checked and tables assigned",
                    "Wine chillers temperature checked",
                    "Special chef's specials board updated",
                    "Fine dining cutlery and glassware polished"
                ))
            } else {
                tasks.addAll(listOf(
                    "Open wine bottles vacuum-sealed and dated",
                    "Linen napkins separated for laundry",
                    "Inventory of high-value items (cigars, premium liquor) verified",
                    "Next day's reservations reviewed"
                ))
            }
        }
        "Tea/Coffee Lounge & Patisserie" -> {
            if (shift == "Opening") {
                tasks.addAll(listOf(
                    "Cake display vitrine temperature checked (2-4°C)",
                    "Fresh bakes arrayed with correct FSSAI tags",
                    "Tea selection boxes refilled and organized",
                    "Takeaway packaging stocked at the counter"
                ))
            } else {
                tasks.addAll(listOf(
                    "Unsold perishable pastries discarded/logged as wastage",
                    "Cake display vitrine defrosted/cleaned",
                    "Coffee/Tea stations thoroughly sanitized",
                    "Takeaway materials restocked for next day"
                ))
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
            // Fallback to standard share sheet
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
"""

with open(file_path, "w") as f:
    f.write(content)

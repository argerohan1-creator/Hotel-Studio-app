import sys

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

# I will just write a python script that replaces the broken function with a clean one
new_func = """
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
    sb.append("*$outlet - $shift Checklist Report*\\n")
    sb.append("Date: $dateStr\\n")
    sb.append("Team Leader: $teamLeader\\n\\n")
    
    sb.append("*Completed Tasks:*\\n")
    val completed = items.filter { it.isDone }
    if (completed.isEmpty()) sb.append("None\\n")
    completed.forEach { sb.append("✅ ${it.task}\\n") }
    
    sb.append("\\n*Pending / Issues:*\\n")
    val pending = items.filter { !it.isDone }
    if (pending.isEmpty()) sb.append("None (All tasks completed)\\n")
    pending.forEach { 
        sb.append("❌ ${it.task}\\n")
        if (it.remarks.isNotBlank()) {
            sb.append("   ↳ _Remark: ${it.remarks}_\\n")
        }
    }
    
    sb.append("\\n_Report generated via Hotel Studio F&B Assistant_")
    
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
"""

start_idx = text.find("fun sendWhatsAppReport(")
if start_idx != -1:
    text = text[:start_idx] + new_func

with open(file_path, "w") as f:
    f.write(text)

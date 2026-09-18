import sys

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

imports = """
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Color as AndroidColor
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.OutlinedButton
"""
text = text.replace("import android.widget.Toast\n", "import android.widget.Toast\n" + imports)

# We need to add a Share PDF button
old_buttons = """                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                }"""

new_buttons = """                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                }"""

text = text.replace(old_buttons, new_buttons)

pdf_func = """
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
"""

text = text + "\n\n" + pdf_func

with open(file_path, "w") as f:
    f.write(text)

print("Script finished")

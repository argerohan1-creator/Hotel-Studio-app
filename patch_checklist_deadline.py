import sys

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

imports = """
import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.TextButton
import com.example.util.ChecklistDeadlineReceiver
"""
text = text.replace("import androidx.compose.material3.OutlinedButton\n", "import androidx.compose.material3.OutlinedButton\n" + imports)

# We will add a button to the bottom section, right above the share buttons
# Let's find the section where share buttons are
share_section = """                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {"""

deadline_ui = """
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
                
"""
text = text.replace(share_section, deadline_ui + share_section)

with open(file_path, "w") as f:
    f.write(text)
print("Screen Patched")

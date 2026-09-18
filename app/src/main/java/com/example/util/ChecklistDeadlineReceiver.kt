package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ChecklistDeadlineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val outlet = intent.getStringExtra("EXTRA_OUTLET") ?: "Outlet"
        val shift = intent.getStringExtra("EXTRA_SHIFT") ?: "Shift"
        
        // In a real app we'd query the DB for incomplete items, but for now we assume we fired this alarm
        // because there are incomplete items 30 mins prior to the deadline.
        NotificationHelper.createNotificationChannel(context)
        NotificationHelper.showNotification(
            context,
            "Critical Tasks Incomplete",
            "Urgent: $shift checklist for $outlet has critical pending items. Deadline in 30 mins!"
        )
    }
}

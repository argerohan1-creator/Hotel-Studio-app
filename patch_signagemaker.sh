#!/bin/bash
# Insert tab 5 definition
sed -i 's/Tab(selected = activeTab == 4, onClick = { activeTab = 4 }, text = { Text("Preview", fontSize = 11.sp, fontWeight = FontWeight.Bold) })/Tab(selected = activeTab == 4, onClick = { activeTab = 4 }, text = { Text("Preview", fontSize = 11.sp, fontWeight = FontWeight.Bold) })\n            Tab(selected = activeTab == 5, onClick = { activeTab = 5 }, text = { Text("Creative Maker", fontSize = 11.sp, fontWeight = FontWeight.Bold) })/' app/src/main/java/com/example/ui/screens/SignageBuilderScreen.kt

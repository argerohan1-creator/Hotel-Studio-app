import sys

file_path = "app/src/main/java/com/example/ui/screens/AuditNutritionScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

import re
text = re.sub(
    r"com\.example\.ui\.components\.AnalyticsHeatmap\(\)\s*Spacer\(modifier = Modifier\.height\(50\.dp\)\)",
    "com.example.ui.components.AnalyticsHeatmap()\n        com.example.ui.components.FssaiComplianceTrendsChart()\n        Spacer(modifier = Modifier.height(50.dp))",
    text
)

with open(file_path, "w") as f:
    f.write(text)

print("Patched AuditNutritionScreen")

import os

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    lines = f.readlines()

# 1. Add import
import_line = "import com.example.ui.screens.OutletChecklistScreen\n"
for i, line in enumerate(lines):
    if "import com.example.ui.screens.BuffetTagMasterScreen" in line:
        lines.insert(i, import_line)
        break

# 2. Add to moduleTitles map
for i, line in enumerate(lines):
    if '"about" to "About & Legal"' in line:
        lines.insert(i, '        "checklists" to "Outlet Checklists",\n')
        break

# 3. Add to when (activeModule) inside Box
for i, line in enumerate(lines):
    if '"about" -> AboutUsScreen()' in line:
        lines.insert(i, '                    "checklists" -> OutletChecklistScreen(viewModel = viewModel)\n')
        break

with open(file_path, "w") as f:
    f.writelines(lines)

import os

file_path = "app/src/main/java/com/example/ui/components/AppHeaderAndDrawer.kt"
with open(file_path, "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if 'import androidx.compose.material.icons.filled.SupportAgent' in line:
        lines.insert(i, "import androidx.compose.material.icons.filled.ChecklistRtl\n")
        break

for i, line in enumerate(lines):
    if 'onClick = { onNavigate("settings"); onCloseDrawer() }' in line:
        lines.insert(i+2, """        DrawerNavItem(
            icon = Icons.Default.ChecklistRtl,
            label = "Outlet Checklists",
            isSelected = activeModule == "checklists",
            onClick = { onNavigate("checklists"); onCloseDrawer() }
        )
""")
        break

with open(file_path, "w") as f:
    f.writelines(lines)

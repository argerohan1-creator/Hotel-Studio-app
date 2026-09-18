import sys

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    lines = f.readlines()

# 1. Update BuffetItemRow signature
start_idx = -1
for i, line in enumerate(lines):
    if 'fun BuffetItemRow(' in line:
        start_idx = i
        break

if start_idx != -1:
    for j in range(start_idx, start_idx+30):
        if 'onDelete: () -> Unit' in lines[j]:
            # Add onAuditClick: () -> Unit
            lines.insert(j+1, "    onAuditClick: () -> Unit,\n")
            break

# 2. Add icon to BuffetItemRow
for i, line in enumerate(lines):
    if '// Edit & Delete Actions' in line:
        insert_idx = i + 2 # inside the Row
        # insert before IconButton(onEdit)
        lines.insert(insert_idx, """                IconButton(
                    onClick = onAuditClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "FSSAI Audit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
""")
        break

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.writelines(lines)

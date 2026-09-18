import sys

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    lines = f.readlines()

# Update CourseSectionCard
start_idx = -1
for i, line in enumerate(lines):
    if 'fun CourseSectionCard(' in line:
        start_idx = i
        break

if start_idx != -1:
    for j in range(start_idx, start_idx+30):
        if 'onDelete: (BuffetMenuItemEntity) -> Unit' in lines[j]:
            lines.insert(j+1, "    onAuditClick: (BuffetMenuItemEntity) -> Unit\n")
            break

# Update BuffetItemRow call
for i, line in enumerate(lines):
    if 'BuffetItemRow(' in line and 'item = item' in lines[i+1]:
        for j in range(i, i+15):
            if 'onDelete = { onDelete(item) }' in lines[j]:
                lines[j] = lines[j].replace('\n', ',\n')
                lines.insert(j+1, "                            onAuditClick = { onAuditClick(item) }\n")
                break
        break

with open('app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.writelines(lines)

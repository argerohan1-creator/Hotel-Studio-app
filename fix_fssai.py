import sys

with open('app/src/main/java/com/example/ui/components/FssaiAuditDialog.kt', 'r') as f:
    text = f.read()

text = text.replace('crossAxisAlignment = CrossAxisAlignment.Start', 'verticalAlignment = Alignment.Top')

with open('app/src/main/java/com/example/ui/components/FssaiAuditDialog.kt', 'w') as f:
    f.write(text)

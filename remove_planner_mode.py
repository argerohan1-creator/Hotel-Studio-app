import re
with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    content = f.read()

# I will find the Mode Tabs: AI Assistant vs Chef's Manual Choice and remove it.
# It starts at `// Mode Tabs: AI Assistant vs Chef's Manual Choice` and ends at `HorizontalDivider()`

start_idx = content.find("// Mode Tabs: AI Assistant vs Chef's Manual Choice")
if start_idx != -1:
    end_idx = content.find("HorizontalDivider()", start_idx)
    if end_idx != -1:
        # include HorizontalDivider() in removal? No, keep the divider.
        # Wait, the code has:
        # // Mode Tabs: ...
        # Card(...) { ... }
        # 
        # HorizontalDivider()
        
        content = content[:start_idx] + content[end_idx:]

with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.write(content)

import re
with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'r') as f:
    content = f.read()

imports_to_add = """import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Eco
"""

content = content.replace("import androidx.compose.material.icons.filled.Add", imports_to_add + "import androidx.compose.material.icons.filled.Add")

with open('./app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt', 'w') as f:
    f.write(content)

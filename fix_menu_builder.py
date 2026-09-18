import sys

file_path = "app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

imports = """import androidx.compose.ui.platform.LocalContext
import com.example.util.BuffetMenuExporter
"""
text = text.replace("import androidx.compose.animation.AnimatedVisibility\n", imports + "import androidx.compose.animation.AnimatedVisibility\n")

if "val context = LocalContext.current" not in text:
    # find `fun MenuBuilderScreen(`
    start = text.find("fun MenuBuilderScreen(")
    if start != -1:
        # find the first opening brace after that
        brace = text.find("{", start)
        if brace != -1:
            text = text[:brace+1] + "\n    val context = LocalContext.current" + text[brace+1:]

with open(file_path, "w") as f:
    f.write(text)

print("Fixed context and imports")

import sys

file_path = "app/src/main/java/com/example/util/ModernArtRenderer.kt"
with open(file_path, "r") as f:
    text = f.read()

text = text.replace('FoodVisualTheme.DIM_SUM_BAMBOO', 'FoodVisualTheme.MIDNIGHT_OBSIDIAN')

if "import android.graphics.RadialGradient" not in text:
    text = text.replace("import android.graphics.LinearGradient", "import android.graphics.LinearGradient\nimport android.graphics.RadialGradient")

with open(file_path, "w") as f:
    f.write(text)
    
print("Fixed missing references and imports")

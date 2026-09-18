import os
import re

files_to_modify = [
    "app/src/main/java/com/example/data/remote/HotelStudioAiService.kt",
    "app/src/main/java/com/example/ui/screens/NameTagCreatorScreen.kt",
    "app/src/main/java/com/example/ui/screens/CreativeMakerScreen.kt",
    "app/src/main/java/com/example/ui/screens/HelpDeskScreen.kt",
    "app/src/main/java/com/example/ui/screens/RecipeBankScreen.kt",
    "app/src/main/java/com/example/ui/screens/LegalAndAboutScreens.kt",
    "app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt",
    "app/src/main/java/com/example/ui/components/ExportBuffetMenuDialog.kt",
    "app/src/main/java/com/example/ui/components/AppHeaderAndDrawer.kt",
    "app/src/main/java/com/example/viewmodel/HotelStudioViewModel.kt",
    "app/src/main/java/com/example/MainActivity.kt"
]

for file_path in files_to_modify:
    if os.path.exists(file_path):
        with open(file_path, "r") as f:
            content = f.read()
        
        # We want to replace the word "AI" with "F&B Assistant"
        # Using a regex to match word boundaries, case-sensitive
        new_content = re.sub(r'\bAI\b', 'F&B Assistant', content)
        
        with open(file_path, "w") as f:
            f.write(new_content)

print("Done.")

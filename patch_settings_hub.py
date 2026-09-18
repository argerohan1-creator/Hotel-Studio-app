import sys

file_path = "app/src/main/java/com/example/ui/screens/SettingsHubScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

new_theme_option = """                    ThemeOptionCard(
                        title = "Taj Level 3 Crystal",
                        desc = "Warm ivory with deep gold accents",
                        isSelected = activeTheme == "taj",
                        accentColor = Color(0xFF8A6F42),
                        onClick = { viewModel.setAppTheme("taj") }
                    )
                    ThemeOptionCard(
                        title = "Midnight Slate (Default)",
"""
text = text.replace("""                    ThemeOptionCard(
                        title = "Midnight Slate (Default)",""", new_theme_option)

with open(file_path, "w") as f:
    f.write(text)

print("Patched SettingsHubScreen.kt")

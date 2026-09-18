import sys

file_path = "app/src/main/java/com/example/ui/theme/Theme.kt"
with open(file_path, "r") as f:
    text = f.read()

# Update Theme.kt to include "taj" and make it the default for "MyApplicationTheme"
old_when = """    val colorScheme = when (activeTheme.lowercase()) {
        "luxury" -> LuxuryObsidianColorScheme
        "ivory" -> FineDiningIvoryColorScheme
        "kitchen" -> KitchenHighContrastColorScheme
        else -> MidnightDarkColorScheme
    }"""
new_when = """    val colorScheme = when (activeTheme.lowercase()) {
        "taj" -> TajGoldColorScheme
        "luxury" -> LuxuryObsidianColorScheme
        "ivory" -> FineDiningIvoryColorScheme
        "kitchen" -> KitchenHighContrastColorScheme
        "midnight" -> MidnightDarkColorScheme
        else -> TajGoldColorScheme
    }"""
text = text.replace(old_when, new_when)

text = text.replace("""@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    HotelStudioTheme(activeTheme = "midnight", content = content)
}""", """@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    HotelStudioTheme(activeTheme = "taj", content = content)
}""")

with open(file_path, "w") as f:
    f.write(text)

print("Patched Theme.kt")

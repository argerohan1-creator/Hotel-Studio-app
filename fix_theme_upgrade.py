import sys

file_path = "app/src/main/java/com/example/ui/theme/Theme.kt"
with open(file_path, "r") as f:
    text = f.read()

text = text.replace('"midnight" -> MidnightDarkColorScheme', '"slate" -> MidnightDarkColorScheme\n        "midnight" -> TajGoldColorScheme')

with open(file_path, "w") as f:
    f.write(text)


file_path2 = "app/src/main/java/com/example/ui/screens/SettingsHubScreen.kt"
with open(file_path2, "r") as f:
    text2 = f.read()

text2 = text2.replace('activeTheme == "midnight"', 'activeTheme == "slate"')
text2 = text2.replace('viewModel.setAppTheme("midnight")', 'viewModel.setAppTheme("slate")')

with open(file_path2, "w") as f:
    f.write(text2)

print("Upgraded theme gracefully")

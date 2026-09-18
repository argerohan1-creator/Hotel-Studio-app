import sys

file_path = "app/src/main/java/com/example/ui/theme/HotelStudioDesign.kt"
with open(file_path, "r") as f:
    text = f.read()

new_scheme = """val TajGoldColorScheme = lightColorScheme(
    primary = Color(0xFF8A6F42), // Deep Gold/Bronze
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEBDDC1),
    onPrimaryContainer = Color(0xFF3E311A),
    secondary = Color(0xFFA68A56), // Lighter Gold
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFDFBF7), // Warm Ivory
    onBackground = Color(0xFF2B2317), // Dark Bronze/Brown
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF2B2317),
    surfaceVariant = Color(0xFFF5F2E9), // Slightly darker ivory for cards
    onSurfaceVariant = Color(0xFF5E4B2F),
    outline = Color(0xFF8A6F42)
)

"""

text = text.replace("val LuxuryObsidianColorScheme = darkColorScheme(", new_scheme + "val LuxuryObsidianColorScheme = darkColorScheme(")

with open(file_path, "w") as f:
    f.write(text)

print("Patched HotelStudioDesign.kt")

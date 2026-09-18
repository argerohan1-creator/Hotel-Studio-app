import sys

file_path = "app/src/main/java/com/example/ui/screens/CreativeMakerScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

old_theme_options = """                            val themeOptions = listOf(
                                Pair("🥟 Dim Sum Bamboo Steamer", FoodVisualTheme.DIM_SUM_BAMBOO),
                                Pair("🍕 Wood Fired Artisanal Pizza", FoodVisualTheme.WOOD_FIRED_PIZZA),
                                Pair("🍝 Penne Arrabbiata Pasta Bowl", FoodVisualTheme.PENNE_PASTA),
                                Pair("🥘 Papdi Chaat & Curd Swirl", FoodVisualTheme.CHAAT_PATTER),
                                Pair("🌮 Sizzling Fajita Mexican Wraps", FoodVisualTheme.MEXICAN_TACOS),
                                Pair("🥗 Healthy Grilled Salmon Greens", FoodVisualTheme.SALAD_GARDEN),
                                Pair("🍦 Saffron Malai Kulfi Sticks", FoodVisualTheme.KULFI_SAFFRON),
                                Pair("🍬 Royal Indian Mithai Thali", FoodVisualTheme.ROYAL_MITHAI),
                                Pair("🍲 Sattvic Jain Palak Paneer", FoodVisualTheme.JAIN_PALAK_PANEER),
                                Pair("🍢 Charcoal Grilled Arabic Skewers", FoodVisualTheme.ARABIC_KEBAB),
                                Pair("🥞 Golden Lacy Kerala Appams", FoodVisualTheme.APPAM_KERALA),
                                Pair("☕ Vintage Porcelain Tea & Cakes", FoodVisualTheme.TEA_COFFEE_VINTAGE),
                                Pair("🍜 Mongolian Hot Pot Casserole", FoodVisualTheme.MONGOLIAN_WOK),
                                Pair("🥦 Satvik Puffed Puris & Subzi", FoodVisualTheme.NO_ONION_GARLIC),
                                Pair("🥢 Asian Carbon Steel Stir-Fry", FoodVisualTheme.ASIAN_STIR_FRY)
                            )"""

new_theme_options = """                            val themeOptions = listOf(
                                Pair("⬛ Midnight Obsidian", FoodVisualTheme.MIDNIGHT_OBSIDIAN),
                                Pair("⚪ Ivory Silk", FoodVisualTheme.IVORY_SILK),
                                Pair("🟡 Brushed Gold", FoodVisualTheme.BRUSHED_GOLD),
                                Pair("🟢 Emerald Velvet", FoodVisualTheme.EMERALD_VELVET),
                                Pair("🔵 Sapphire Gradient", FoodVisualTheme.SAPPHIRE_GRADIENT),
                                Pair("🔴 Crimson Damask", FoodVisualTheme.CRIMSON_DAMASK),
                                Pair("⚫ Charcoal Matte", FoodVisualTheme.CHARCOAL_MATTE),
                                Pair("🌸 Rose Gold", FoodVisualTheme.ROSE_GOLD),
                                Pair("🧊 Frosted Glass", FoodVisualTheme.FROSTED_GLASS),
                                Pair("🤍 Platinum Mesh", FoodVisualTheme.PLATINUM_MESH),
                                Pair("🟤 Bronze Texture", FoodVisualTheme.BRONZE_TEXTURE),
                                Pair("✨ Pearl Glaze", FoodVisualTheme.PEARL_GLAZE),
                                Pair("🍷 Royal Burgundy", FoodVisualTheme.ROYAL_BURGUNDY),
                                Pair("⚙️ Titanium Weave", FoodVisualTheme.TITANIUM_WEAVE),
                                Pair("🥂 Champagne Sparkle", FoodVisualTheme.CHAMPAGNE_SPARKLE)
                            )"""

text = text.replace(old_theme_options, new_theme_options)
text = text.replace("var selectedTheme by remember { mutableStateOf(FoodVisualTheme.DIM_SUM_BAMBOO) }", "var selectedTheme by remember { mutableStateOf(FoodVisualTheme.MIDNIGHT_OBSIDIAN) }")
text = text.replace("Text(\"Or Select Curated Food Art Style:\",", "Text(\"Or Select Luxury Background Theme:\",")

with open(file_path, "w") as f:
    f.write(text)
    
print("Patched CreativeMakerScreen.kt")

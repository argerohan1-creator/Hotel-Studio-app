import sys

file_path = "app/src/main/java/com/example/util/ModernArtRenderer.kt"
with open(file_path, "r") as f:
    text = f.read()

old_enum = """enum class FoodVisualTheme {
    DIM_SUM_BAMBOO,       // Steamed dumplings in bamboo steamer with red chopsticks
    WOOD_FIRED_PIZZA,     // Bubbling cheese, olives, pepperoni & fresh basil
    PENNE_PASTA,          // Clay bowl of penne arrabbiata with fresh cherry tomatoes & parsley
    CHAAT_PATTER,         // Crispy papdis, curd swirl, sev & ruby pomegranate jewels
    MEXICAN_TACOS,        // Sizzling grilled fajita wraps with charred peppers & limes
    SALAD_GARDEN,         // Vibrant salmon/chicken bowl with fresh oranges, cucumber & herbs
    KULFI_SAFFRON,        // Golden saffron kulfi sticks with roasted pistachios & almond slivers
    ROYAL_MITHAI,         // Rich Indian sweets: golden laddoos, soan papdi, pistachio pedas
    JAIN_PALAK_PANEER,    // Cast iron skillet with rich spinach gravy and pure paneer cubes
    ARABIC_KEBAB,         // Grilled tandoori skewers with mint dip and sliced onions
    APPAM_KERALA,         // Lacy South Indian hopper appams with coconut chutney
    TEA_COFFEE_VINTAGE,   // Freshly brewed golden tea/coffee in ceramic cup with tea cakes
    MONGOLIAN_WOK,        // Braised sweet-soy hot pot bowl with scallions and sesame
    NO_ONION_GARLIC,      // Crispy golden puris with earthen pot dal / potato subzi
    ASIAN_STIR_FRY        // Sizzling wok with bell peppers, chicken strips & green onions
}"""

new_enum = """enum class FoodVisualTheme {
    MIDNIGHT_OBSIDIAN,
    IVORY_SILK,
    BRUSHED_GOLD,
    EMERALD_VELVET,
    SAPPHIRE_GRADIENT,
    CRIMSON_DAMASK,
    CHARCOAL_MATTE,
    ROSE_GOLD,
    FROSTED_GLASS,
    PLATINUM_MESH,
    BRONZE_TEXTURE,
    PEARL_GLAZE,
    ROYAL_BURGUNDY,
    TITANIUM_WEAVE,
    CHAMPAGNE_SPARKLE
}"""

text = text.replace(old_enum, new_enum)

# Now fix the presets list
old_presets = """    val FOOD_STATION_PRESETS = listOf(
        FoodStationPreset("dim_sum", "Dim Sum", "BAR", "Asian Specialty", "🥟", "Chef's Dim Sum Selection", "#9C7A4A", FoodVisualTheme.DIM_SUM_BAMBOO),
        FoodStationPreset("live_pizza", "Live", "PIZZA", "Italian & Continental", "🍕", "Wood Fired Artisanal Slices", "#9C7A4A", FoodVisualTheme.WOOD_FIRED_PIZZA),
        FoodStationPreset("pasta_station", "Pasta", "STATION", "Italian & Continental", "🍝", "Fresh Handcrafted Pasta", "#9C7A4A", FoodVisualTheme.PENNE_PASTA),
        FoodStationPreset("chaat_station", "Chaat", "STATION", "Indian Street Live", "🥘", "🟢 100% Pure Veg Live Counter", "#9C7A4A", FoodVisualTheme.CHAAT_PATTER),
        FoodStationPreset("salad_bar", "Healthy", "SALAD BAR", "Salad & Wellness", "🥗", "Organic Greens & Vinaigrettes", "#9C7A4A", FoodVisualTheme.SALAD_GARDEN),
        FoodStationPreset("mexican_wrap", "Mexican", "WRAP", "Global Street Food", "🌮", "Sizzling Fajitas & Warm Tortillas", "#9C7A4A", FoodVisualTheme.MEXICAN_TACOS),
        FoodStationPreset("kulfi_corner", "Kulfi", "CORNER", "Dessert & Sweets", "🍦", "Royal Saffron & Pistachio Malai", "#9C7A4A", FoodVisualTheme.KULFI_SAFFRON),
        FoodStationPreset("sweet_lovers", "For sweet", "LOVERS", "Dessert & Sweets", "🍬", "Artisanal Mithai & Sweet Delicacies", "#9C7A4A", FoodVisualTheme.ROYAL_MITHAI),
        FoodStationPreset("jain_food", "Jain", "FOOD", "Dietary Special", "🍲", "🌿 Pure Sattvic Jain Delicacies", "#9C7A4A", FoodVisualTheme.JAIN_PALAK_PANEER),
        FoodStationPreset("arabic_corner", "Arabic", "CORNER", "Grill & Kebab", "🍢", "Charcoal Skewers & Creamy Mezze", "#9C7A4A", FoodVisualTheme.ARABIC_KEBAB),
        FoodStationPreset("appam_station", "Appam", "STATION", "Regional Indian", "🥞", "Crispy Lace Appams & Coconut Stew", "#9C7A4A", FoodVisualTheme.APPAM_KERALA),
        FoodStationPreset("tea_coffee", "Tea & Coffee", "STATION", "Beverage Bar", "☕", "Artisanal Brews & Gourmet Teas", "#9C7A4A", FoodVisualTheme.TEA_COFFEE_VINTAGE),
        FoodStationPreset("mongolian_counter", "Mongolian", "COUNTER", "Asian Specialty", "🍜", "Live Wok Tossed Sizzle Bowl", "#9C7A4A", FoodVisualTheme.MONGOLIAN_WOK),
        FoodStationPreset("no_onion_garlic", "No Onion & Garlic", "FOOD", "Dietary Special", "🥦", "🟢 Satvik Homestyle Feast", "#9C7A4A", FoodVisualTheme.NO_ONION_GARLIC),
        FoodStationPreset("asian_bar", "Asian", "BAR", "Asian Specialty", "🥢", "Authentic Oriental Wok Creations", "#9C7A4A", FoodVisualTheme.ASIAN_STIR_FRY)
    )"""

new_presets = """    val FOOD_STATION_PRESETS = listOf(
        FoodStationPreset("dim_sum", "Dim Sum", "BAR", "Asian Specialty", "🥟", "Chef's Dim Sum Selection", "#9C7A4A", FoodVisualTheme.MIDNIGHT_OBSIDIAN),
        FoodStationPreset("live_pizza", "Live", "PIZZA", "Italian & Continental", "🍕", "Wood Fired Artisanal Slices", "#9C7A4A", FoodVisualTheme.IVORY_SILK),
        FoodStationPreset("pasta_station", "Pasta", "STATION", "Italian & Continental", "🍝", "Fresh Handcrafted Pasta", "#9C7A4A", FoodVisualTheme.BRUSHED_GOLD),
        FoodStationPreset("chaat_station", "Chaat", "STATION", "Indian Street Live", "🥘", "🟢 100% Pure Veg Live Counter", "#9C7A4A", FoodVisualTheme.EMERALD_VELVET),
        FoodStationPreset("salad_bar", "Healthy", "SALAD BAR", "Salad & Wellness", "🥗", "Organic Greens & Vinaigrettes", "#9C7A4A", FoodVisualTheme.SAPPHIRE_GRADIENT),
        FoodStationPreset("mexican_wrap", "Mexican", "WRAP", "Global Street Food", "🌮", "Sizzling Fajitas & Warm Tortillas", "#9C7A4A", FoodVisualTheme.CRIMSON_DAMASK),
        FoodStationPreset("kulfi_corner", "Kulfi", "CORNER", "Dessert & Sweets", "🍦", "Royal Saffron & Pistachio Malai", "#9C7A4A", FoodVisualTheme.CHARCOAL_MATTE),
        FoodStationPreset("sweet_lovers", "For sweet", "LOVERS", "Dessert & Sweets", "🍬", "Artisanal Mithai & Sweet Delicacies", "#9C7A4A", FoodVisualTheme.ROSE_GOLD),
        FoodStationPreset("jain_food", "Jain", "FOOD", "Dietary Special", "🍲", "🌿 Pure Sattvic Jain Delicacies", "#9C7A4A", FoodVisualTheme.FROSTED_GLASS),
        FoodStationPreset("arabic_corner", "Arabic", "CORNER", "Grill & Kebab", "🍢", "Charcoal Skewers & Creamy Mezze", "#9C7A4A", FoodVisualTheme.PLATINUM_MESH),
        FoodStationPreset("appam_station", "Appam", "STATION", "Regional Indian", "🥞", "Crispy Lace Appams & Coconut Stew", "#9C7A4A", FoodVisualTheme.BRONZE_TEXTURE),
        FoodStationPreset("tea_coffee", "Tea & Coffee", "STATION", "Beverage Bar", "☕", "Artisanal Brews & Gourmet Teas", "#9C7A4A", FoodVisualTheme.PEARL_GLAZE),
        FoodStationPreset("mongolian_counter", "Mongolian", "COUNTER", "Asian Specialty", "🍜", "Live Wok Tossed Sizzle Bowl", "#9C7A4A", FoodVisualTheme.ROYAL_BURGUNDY),
        FoodStationPreset("no_onion_garlic", "No Onion & Garlic", "FOOD", "Dietary Special", "🥦", "🟢 Satvik Homestyle Feast", "#9C7A4A", FoodVisualTheme.TITANIUM_WEAVE),
        FoodStationPreset("asian_bar", "Asian", "BAR", "Asian Specialty", "🥢", "Authentic Oriental Wok Creations", "#9C7A4A", FoodVisualTheme.CHAMPAGNE_SPARKLE)
    )"""

text = text.replace(old_presets, new_presets)

with open(file_path, "w") as f:
    f.write(text)

print("Patched ModernArtRenderer Enum & Presets")

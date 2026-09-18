package com.example.util

import com.example.data.model.DishEntity
import com.example.data.model.RecipeEntity

data class CulinaryDirectoryItem(
    val name: String,
    val type: String, // "veg" or "nonveg"
    val cals: String,
    val allergens: List<String>,
    val category: String,
    val source: String = "Culinary Directory"
)

data class DishAnalysisResult(
    val dishName: String,
    val type: String, // "veg" or "nonveg"
    val isNonVeg: Boolean,
    val cals: String,
    val allergens: List<String>,
    val category: String,
    val confidenceNote: String,
    val isAgentVerified: Boolean = true
)

object CulinaryAgent {

    val ALLERGEN_OPTIONS = listOf(
        "Milk",
        "Gluten",
        "Nuts",
        "Eggs",
        "Fish",
        "Crustaceans",
        "Soya",
        "Mustard",
        "Sesame",
        "Peanuts",
        "Sulphites"
    )

    val POPULAR_ACTION_STATIONS = listOf(
        "Live Tandoor & Chaat Counter",
        "Live Pasta & Pizza Hearth",
        "Live Dosa & Appam Station",
        "Live Dim Sum & Stir-Fry Wok",
        "Live Egg & Omelette Station",
        "Carvery & Roast Station",
        "Live Sushi & Teppanyaki Grill",
        "Live Churros & Dessert Flambé",
        "Live Shawarma & Mezze Counter",
        "Live Belgian Waffle & Crepe Station",
        "Live Sandwich & Toastie Bar",
        "Live Slider & Burger Station",
        "Live Pasta & Risotto Station"
    )

    val POPULAR_BEVERAGES = listOf(
        "Virgin Mojito & Spiced Buttermilk",
        "Sunrise Citrus Cooler & Mint Spritzer",
        "Blueberry Basil Smash & Fresh Lime Soda",
        "Fresh Orange Juice & Masala Chai",
        "Tropical Pineapple Punch & Sweet Lassi",
        "Smoked Rosemary Cranberry Spritz",
        "Artisan Cold Brew & Iced Matcha",
        "Kesar Badam Milk & Rose Sharbat"
    )

    private val masterDirectory = listOf(
        // Indian Vegetarian Mains
        CulinaryDirectoryItem("Paneer Butter Masala", "veg", "230 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Dal Makhani", "veg", "190 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Dal Tadka", "veg", "135 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Palak Paneer", "veg", "175 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Paneer Tikka Masala", "veg", "225 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Paneer Lababdar", "veg", "235 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Malai Kofta", "veg", "260 kcal / 100 g", listOf("Milk", "Gluten", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Chole Bhature", "veg", "285 kcal / 100 g", listOf("Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Aloo Gobi Adraki", "veg", "120 kcal / 100 g", listOf("None Detected"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Baingan Bharta", "veg", "110 kcal / 100 g", listOf("None Detected"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Kaju Curry", "veg", "295 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Subz Panchmel", "veg", "130 kcal / 100 g", listOf("None Detected"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Dum Aloo Kashmiri", "veg", "170 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Bhindi Do Pyaza", "veg", "115 kcal / 100 g", listOf("None Detected"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Hyderabadi Subz Biryani", "veg", "195 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Veg)"),

        // Indian Non-Veg Mains
        CulinaryDirectoryItem("Butter Chicken (Murgh Makhani)", "nonveg", "260 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Chicken Tikka Masala", "nonveg", "240 kcal / 100 g", listOf("Milk", "Nuts"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Awadhi Murgh Dum Biryani", "nonveg", "230 kcal / 100 g", listOf("Milk"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Mutton Rogan Josh", "nonveg", "275 kcal / 100 g", listOf("Milk"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Chicken Kolhapuri", "nonveg", "245 kcal / 100 g", listOf("Mustard"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Goan Fish Curry", "nonveg", "185 kcal / 100 g", listOf("Fish", "Mustard"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Amritsari Machhi Curry", "nonveg", "190 kcal / 100 g", listOf("Fish", "Gluten"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Kolkata Prawn Malai Curry", "nonveg", "215 kcal / 100 g", listOf("Crustaceans", "Milk", "Mustard"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Bhuna Gosht", "nonveg", "285 kcal / 100 g", listOf("Milk"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Egg Curry Homestyle", "nonveg", "165 kcal / 100 g", listOf("Eggs"), "Main Course (Non-Veg)"),

        // Continental & Italian
        CulinaryDirectoryItem("Truffle Penne Alfredo", "veg", "250 kcal / 100 g", listOf("Milk", "Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Margherita Pizza Rustica", "veg", "240 kcal / 100 g", listOf("Milk", "Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Four Cheese Ravioli", "veg", "265 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Wild Mushroom Risotto", "veg", "190 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Eggplant Parmigiana", "veg", "180 kcal / 100 g", listOf("Milk", "Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Spaghetti Carbonara", "nonveg", "280 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Herb Roasted Chicken", "nonveg", "210 kcal / 100 g", listOf("None Detected"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Pan-Seared Atlantic Salmon", "nonveg", "220 kcal / 100 g", listOf("Fish"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Slow-Braised Lamb Shank", "nonveg", "260 kcal / 100 g", listOf("Sulphites"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Grilled Prime Ribeye", "nonveg", "270 kcal / 100 g", listOf("None Detected"), "Main Course (Non-Veg)"),

        // Starters & Chaat
        CulinaryDirectoryItem("Pani Puri (Golgappa)", "veg", "150 kcal / 100 g", listOf("Gluten"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Dahi Papdi Chaat", "veg", "195 kcal / 100 g", listOf("Milk", "Gluten"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Samosa with Tamarind Chutney", "veg", "270 kcal / 100 g", listOf("Gluten"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Paneer Tikka Angara", "veg", "210 kcal / 100 g", listOf("Milk"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Masala Dosa with Sambar", "veg", "175 kcal / 100 g", listOf("Mustard"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Steamed Idli Sambar", "veg", "130 kcal / 100 g", listOf("Mustard"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Medu Vada", "veg", "260 kcal / 100 g", listOf("None Detected"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Murgh Malai Tikka", "nonveg", "230 kcal / 100 g", listOf("Milk", "Nuts"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Tandoori Chicken Wings", "nonveg", "225 kcal / 100 g", listOf("Milk"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Golden Fish Amritsari", "nonveg", "205 kcal / 100 g", listOf("Fish", "Gluten"), "Starters & Live Counter"),
        CulinaryDirectoryItem("Crispy Calamari Rings", "nonveg", "240 kcal / 100 g", listOf("Molluscs", "Gluten", "Eggs"), "Starters & Live Counter"),

        // Soups & Salads
        CulinaryDirectoryItem("Creamy Tomato Basil Bisque", "veg", "85 kcal / 100 ml", listOf("Milk"), "Soups & Breads"),
        CulinaryDirectoryItem("Classic French Onion Soup", "veg", "75 kcal / 100 ml", listOf("Milk", "Gluten"), "Soups & Breads"),
        CulinaryDirectoryItem("Sweet Corn Vegetable Soup", "veg", "60 kcal / 100 ml", listOf("None Detected"), "Soups & Breads"),
        CulinaryDirectoryItem("Thai Tom Yum Goong", "nonveg", "65 kcal / 100 ml", listOf("Crustaceans", "Fish"), "Soups & Breads"),
        CulinaryDirectoryItem("Classic Caesar Salad", "veg", "145 kcal / 100 g", listOf("Milk", "Gluten", "Eggs", "Mustard"), "Salad Bar"),
        CulinaryDirectoryItem("Greek Feta Salad", "veg", "125 kcal / 100 g", listOf("Milk"), "Salad Bar"),
        CulinaryDirectoryItem("Quinoa Pomegranate Tabouleh", "veg", "110 kcal / 100 g", listOf("None Detected"), "Salad Bar"),
        CulinaryDirectoryItem("Smoked Chicken Arugula Salad", "nonveg", "160 kcal / 100 g", listOf("Mustard"), "Salad Bar"),

        // Desserts
        CulinaryDirectoryItem("Kesar Gulab Jamun", "veg", "360 kcal / 100 g", listOf("Milk", "Gluten", "Nuts"), "Desserts & Sweets"),
        CulinaryDirectoryItem("Rasmalai with Pistachio", "veg", "240 kcal / 100 g", listOf("Milk", "Nuts"), "Desserts & Sweets"),
        CulinaryDirectoryItem("Classic Tiramisu", "veg", "380 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Desserts & Sweets"),
        CulinaryDirectoryItem("Molten Chocolate Lava Cake", "veg", "420 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Desserts & Sweets"),
        CulinaryDirectoryItem("New York Cheesecake", "veg", "390 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Desserts & Sweets"),
        CulinaryDirectoryItem("Moong Dal Halwa", "veg", "385 kcal / 100 g", listOf("Milk", "Nuts"), "Desserts & Sweets"),

        // Breakfast Specials
        CulinaryDirectoryItem("Indori Poha", "veg", "160 kcal / 100 g", listOf("Peanuts"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Aloo Poori Bhaji", "veg", "220 kcal / 100 g", listOf("Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Paneer Paratha", "veg", "265 kcal / 100 g", listOf("Milk", "Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Misal Pav", "veg", "210 kcal / 100 g", listOf("Gluten"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Classic Scrambled Eggs", "nonveg", "155 kcal / 100 g", listOf("Eggs", "Milk"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Chicken Breakfast Sausages", "nonveg", "195 kcal / 100 g", listOf("None Detected"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Eggs Benedict", "nonveg", "240 kcal / 100 g", listOf("Eggs", "Milk", "Gluten"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Oatmeal with Honey & Berries", "veg", "140 kcal / 100 g", listOf("None Detected"), "Main Course (Veg)"),

        // Hi Tea Specials
        CulinaryDirectoryItem("Cucumber & Cream Cheese Sandwiches", "veg", "180 kcal / 100 g", listOf("Milk", "Gluten"), "Hi Tea Snacks"),
        CulinaryDirectoryItem("Assorted Vegetable Pakoras", "veg", "260 kcal / 100 g", listOf("None Detected"), "Hi Tea Snacks"),
        CulinaryDirectoryItem("Chicken Mayo Sliders", "nonveg", "280 kcal / 100 g", listOf("Eggs", "Gluten", "Milk"), "Hi Tea Snacks"),
        CulinaryDirectoryItem("Paneer Tikka Crostini", "veg", "210 kcal / 100 g", listOf("Milk", "Gluten"), "Hi Tea Snacks"),
        CulinaryDirectoryItem("Blueberry Muffins", "veg", "310 kcal / 100 g", listOf("Milk", "Gluten", "Eggs"), "Desserts & Sweets"),
        CulinaryDirectoryItem("Mini Samosas", "veg", "275 kcal / 100 g", listOf("Gluten"), "Hi Tea Snacks"),
        CulinaryDirectoryItem("English Scones with Clotted Cream", "veg", "340 kcal / 100 g", listOf("Milk", "Gluten"), "Desserts & Sweets"),

        // Supper Specials
        CulinaryDirectoryItem("Light Vegetable Clear Soup", "veg", "45 kcal / 100 ml", listOf("None Detected"), "Soups & Breads"),
        CulinaryDirectoryItem("Grilled Fish with Lemon Butter", "nonveg", "190 kcal / 100 g", listOf("Fish", "Milk"), "Main Course (Non-Veg)"),
        CulinaryDirectoryItem("Mushroom Risotto (Supper)", "veg", "175 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),
        CulinaryDirectoryItem("Chicken Consomme", "nonveg", "55 kcal / 100 ml", listOf("None Detected"), "Soups & Breads"),
        CulinaryDirectoryItem("Baked Jacket Potato", "veg", "150 kcal / 100 g", listOf("Milk"), "Main Course (Veg)"),

        // Beverages
        CulinaryDirectoryItem("Virgin Mint Mojito", "veg", "55 kcal / 100 ml", listOf("None Detected"), "Welcome Beverages"),
        CulinaryDirectoryItem("Spiced Buttermilk (Chaas)", "veg", "40 kcal / 100 ml", listOf("Milk"), "Welcome Beverages"),
        CulinaryDirectoryItem("Sweet Mango Lassi", "veg", "115 kcal / 100 ml", listOf("Milk"), "Welcome Beverages"),
        CulinaryDirectoryItem("Fresh Cold-Pressed Orange Juice", "veg", "50 kcal / 100 ml", listOf("None Detected"), "Welcome Beverages"),
        CulinaryDirectoryItem("Masala Chai", "veg", "65 kcal / 100 ml", listOf("Milk"), "Welcome Beverages"),
        CulinaryDirectoryItem("Blueberry Basil Smash", "veg", "60 kcal / 100 ml", listOf("None Detected"), "Welcome Beverages")
    )

    /**
     * Search app directory and known recipes for autocomplete suggestions
     */
    fun searchAppDirectory(
        query: String,
        existingDishes: List<DishEntity> = emptyList(),
        existingRecipes: List<RecipeEntity> = emptyList(),
        preferredType: String? = null
    ): List<CulinaryDirectoryItem> {
        val trimmed = query.trim().lowercase()

        val results = mutableListOf<CulinaryDirectoryItem>()
        val addedNames = mutableSetOf<String>()

        // 1. Check existing dishes from Room DB
        for (dish in existingDishes) {
            val name = dish.name.trim()
            if (name.isBlank() || addedNames.contains(name.lowercase())) continue
            if (trimmed.isEmpty() || name.lowercase().contains(trimmed)) {
                if (preferredType == null || dish.type.equals(preferredType, ignoreCase = true)) {
                    results.add(
                        CulinaryDirectoryItem(
                            name = name,
                            type = dish.type,
                            cals = dish.cals.ifBlank { if (dish.type == "nonveg") "240 kcal / 100 g" else "180 kcal / 100 g" },
                            allergens = if (dish.allergens.isNotEmpty()) dish.allergens else listOf("None Detected"),
                            category = dish.category.ifBlank { "Main Course" },
                            source = "Saved Dish"
                        )
                    )
                    addedNames.add(name.lowercase())
                }
            }
        }

        // 2. Check existing recipes from Room DB
        for (recipe in existingRecipes) {
            val name = recipe.name.trim()
            if (name.isBlank() || addedNames.contains(name.lowercase())) continue
            if (trimmed.isEmpty() || name.lowercase().contains(trimmed)) {
                val isNonVeg = isLikelyNonVeg(name, recipe.ingredients.joinToString(" "))
                val type = if (isNonVeg) "nonveg" else "veg"
                if (preferredType == null || type.equals(preferredType, ignoreCase = true)) {
                    val allergens = extractAllergensFromIngredients(recipe.ingredients.joinToString(" "), name)
                    results.add(
                        CulinaryDirectoryItem(
                            name = name,
                            type = type,
                            cals = recipe.cals.ifBlank { if (isNonVeg) "240 kcal / 100 g" else "185 kcal / 100 g" },
                            allergens = allergens,
                            category = recipe.category.ifBlank { "Main Course" },
                            source = "Master Recipe"
                        )
                    )
                    addedNames.add(name.lowercase())
                }
            }
        }

        // 3. Check master culinary directory
        for (item in masterDirectory) {
            if (addedNames.contains(item.name.lowercase())) continue
            if (trimmed.isEmpty() || item.name.lowercase().contains(trimmed)) {
                if (preferredType == null || item.type.equals(preferredType, ignoreCase = true)) {
                    results.add(item)
                    addedNames.add(item.name.lowercase())
                }
            }
        }

        // Return up to 10 best matches
        return if (trimmed.isEmpty()) {
            results.take(8)
        } else {
            results.sortedBy {
                val idx = it.name.lowercase().indexOf(trimmed)
                if (idx == 0) 0 else if (idx > 0) 1 else 2
            }.take(8)
        }
    }

    /**
     * Automatic Agent: Inspects dish name, ingredients, and recipe knowledge to determine:
     * 1. Veg / Non-Veg signage
     * 2. Calorie count per 100g | ml
     * 3. FSSAI/FDA compliant allergens
     */
    fun analyzeDish(
        dishName: String,
        forcedType: String? = null,
        existingDishes: List<DishEntity> = emptyList(),
        existingRecipes: List<RecipeEntity> = emptyList()
    ): DishAnalysisResult {
        val trimmed = dishName.trim()
        if (trimmed.isBlank()) {
            val isNonVeg = forcedType?.equals("nonveg", ignoreCase = true) == true
            return DishAnalysisResult(
                dishName = "",
                type = if (isNonVeg) "nonveg" else "veg",
                isNonVeg = isNonVeg,
                cals = if (isNonVeg) "240 kcal / 100 g" else "180 kcal / 100 g",
                allergens = if (isNonVeg) listOf("Gluten", "Milk") else listOf("Milk"),
                category = if (isNonVeg) "Main Course (Non-Veg)" else "Main Course (Veg)",
                confidenceNote = "Default template initialized",
                isAgentVerified = false
            )
        }

        val lowerName = trimmed.lowercase()

        // 1. Try finding in existing dishes
        val matchedDish = existingDishes.find { it.name.equals(trimmed, ignoreCase = true) }
            ?: existingDishes.find { it.name.contains(trimmed, ignoreCase = true) || trimmed.contains(it.name, ignoreCase = true) }

        // 2. Try finding in recipes
        val matchedRecipe = existingRecipes.find { it.name.equals(trimmed, ignoreCase = true) }
            ?: existingRecipes.find { it.name.contains(trimmed, ignoreCase = true) || trimmed.contains(it.name, ignoreCase = true) }

        // 3. Try master directory
        val masterItem = masterDirectory.find { it.name.equals(trimmed, ignoreCase = true) }
            ?: masterDirectory.find { it.name.contains(trimmed, ignoreCase = true) || trimmed.contains(it.name, ignoreCase = true) }

        // Determine Veg vs Non-Veg
        val isNonVeg = when {
            forcedType != null -> forcedType.equals("nonveg", ignoreCase = true)
            matchedDish != null -> matchedDish.type.equals("nonveg", ignoreCase = true)
            matchedRecipe != null -> isLikelyNonVeg(matchedRecipe.name, matchedRecipe.ingredients.joinToString(" "))
            masterItem != null -> masterItem.type == "nonveg"
            else -> isLikelyNonVeg(trimmed, "")
        }
        val type = if (isNonVeg) "nonveg" else "veg"

        // Determine Allergens
        val allergens = when {
            matchedDish != null && matchedDish.allergens.isNotEmpty() && !matchedDish.allergens.contains("None Detected") -> matchedDish.allergens
            matchedRecipe != null -> extractAllergensFromIngredients(matchedRecipe.ingredients.joinToString(" "), trimmed)
            masterItem != null -> masterItem.allergens
            else -> extractAllergensFromIngredients("", trimmed)
        }

        // Determine Calories per 100g | ml
        val cals = when {
            matchedDish != null && matchedDish.cals.isNotBlank() -> normalizeCalorieUnit(matchedDish.cals, lowerName)
            matchedRecipe != null && matchedRecipe.cals.isNotBlank() -> normalizeCalorieUnit(matchedRecipe.cals, lowerName)
            masterItem != null -> masterItem.cals
            else -> estimateCaloriesForDish(lowerName, isNonVeg)
        }

        // Category
        val category = when {
            masterItem != null -> masterItem.category
            matchedDish != null && matchedDish.category.isNotBlank() -> matchedDish.category
            matchedRecipe != null && matchedRecipe.category.isNotBlank() -> matchedRecipe.category
            isBeverage(lowerName) -> "Welcome Beverages"
            isSoup(lowerName) -> "Soups & Breads"
            isSalad(lowerName) -> "Salad Bar"
            isDessert(lowerName) -> "Desserts & Sweets"
            isStarter(lowerName) -> "Starters & Live Counter"
            isNonVeg -> "Main Course (Non-Veg)"
            else -> "Main Course (Veg)"
        }

        val allergenText = if (allergens.isEmpty() || allergens.contains("None Detected")) "Allergen-Free" else allergens.joinToString(", ")
        val note = if (matchedDish != null || matchedRecipe != null || masterItem != null) {
            "Verified in App Directory: ${if (isNonVeg) "Non-Veg" else "Veg"} • $cals • Allergens: $allergenText"
        } else {
            "Agent Auto-Detected: ${if (isNonVeg) "Non-Veg" else "Veg"} • $cals • Allergens: $allergenText"
        }

        return DishAnalysisResult(
            dishName = trimmed,
            type = type,
            isNonVeg = isNonVeg,
            cals = cals,
            allergens = allergens,
            category = category,
            confidenceNote = note,
            isAgentVerified = true
        )
    }

    fun isLikelyNonVeg(dishName: String, ingredients: String = ""): Boolean {
        val combined = "$dishName $ingredients".lowercase()

        // Vegan or mock meat overrides
        if (combined.contains("vegan") || combined.contains("plant based") || combined.contains("mock meat") || combined.contains("soya chaap")) {
            return false
        }

        val nonVegKeywords = listOf(
            "chicken", "murgh", "mutton", "gosht", "lamb", "fish", "machhi", "prawn", "jhinga", "shrimp",
            "crab", "lobster", "calamari", "squid", "seafood", "salmon", "tuna", "pomfret", "bacon", "pork",
            "ham", "beef", "steak", "duck", "turkey", "egg", "anda", "chorizo", "pepperoni", "salami",
            "prosciutto", "veal", "anchovy", "meatball", "sausage", "oyster", "clam", "scallop", "pepperoni",
            "frittata", "omelette", "benedict", "bhurji", "kebab", "kabab"
        )

        // Strict non-veg override for Indian context (Eggs are non-veg)
        if (combined.contains("egg") || combined.contains("anda") || combined.contains("omelette") || combined.contains("frittata")) {
            return true
        }

        return nonVegKeywords.any { combined.contains(it) }
    }

    fun extractAllergensFromIngredients(ingredients: String, dishName: String): List<String> {
        val text = "$dishName $ingredients".lowercase()
        val detected = mutableListOf<String>()

        // Milk / Dairy
        if (text.contains("milk") || text.contains("butter") || text.contains("cream") || text.contains("paneer") ||
            text.contains("cheese") || text.contains("curd") || text.contains("dahi") || text.contains("yogurt") ||
            text.contains("ghee") || text.contains("malai") || text.contains("makhani") || text.contains("rabri") ||
            text.contains("kheer") || text.contains("lassi") || text.contains("buttermilk") || text.contains("ricotta") ||
            text.contains("mascarpone") || text.contains("parmesan") || text.contains("mozzarella")
        ) {
            detected.add("Milk")
        }

        // Gluten / Wheat
        if (text.contains("wheat") || text.contains("flour") || text.contains("maida") || text.contains("naan") ||
            text.contains("roti") || text.contains("bread") || text.contains("paratha") || text.contains("kulcha") ||
            text.contains("pasta") || text.contains("spaghetti") || text.contains("penne") || text.contains("noodles") ||
            text.contains("pizza") || text.contains("crouton") || text.contains("toast") || text.contains("brioche") ||
            text.contains("croissant") || text.contains("sourdough") || text.contains("batter") || text.contains("bhature") ||
            text.contains("poori") || text.contains("waffle") || text.contains("pancake")
        ) {
            detected.add("Gluten")
        }

        // Tree Nuts / Peanuts
        if (text.contains("cashew") || text.contains("kaju") || text.contains("almond") || text.contains("badam") ||
            text.contains("walnut") || text.contains("akhrot") || text.contains("pista") || text.contains("pistachio") ||
            text.contains("hazelnut") || text.contains("pine nut") || text.contains("pesto") || text.contains("nut")
        ) {
            detected.add("Nuts")
        }
        if (text.contains("peanut") || text.contains("groundnut") || text.contains("mungfali")) {
            detected.add("Peanuts")
        }

        // Eggs
        if (text.contains("egg") || text.contains("anda") || text.contains("omelette") || text.contains("mayonnaise") ||
            text.contains("mayo") || text.contains("custard") || text.contains("meringue") || text.contains("frittata") ||
            text.contains("benedict")
        ) {
            detected.add("Eggs")
        }

        // Fish
        if (text.contains("fish") || text.contains("salmon") || text.contains("tuna") || text.contains("machhi") ||
            text.contains("pomfret") || text.contains("cod") || text.contains("sea bass") || text.contains("anchovy")
        ) {
            detected.add("Fish")
        }

        // Crustaceans
        if (text.contains("prawn") || text.contains("jhinga") || text.contains("shrimp") || text.contains("crab") ||
            text.contains("lobster") || text.contains("crayfish") || text.contains("scampi")
        ) {
            detected.add("Crustaceans")
        }

        // Soya
        if (text.contains("soya") || text.contains("soy") || text.contains("tofu") || text.contains("edamame") || text.contains("miso")) {
            detected.add("Soya")
        }

        // Mustard
        if (text.contains("mustard") || text.contains("sarson") || text.contains("rai") || text.contains("kasundi") || text.contains("dijon")) {
            detected.add("Mustard")
        }

        // Sesame
        if (text.contains("sesame") || text.contains("til") || text.contains("tahini") || text.contains("hummus")) {
            detected.add("Sesame")
        }

        return if (detected.isEmpty()) listOf("None Detected") else detected.distinct()
    }

    private fun normalizeCalorieUnit(rawCals: String, lowerName: String): String {
        val trimmed = rawCals.trim()
        val isDrink = isBeverage(lowerName) || isSoup(lowerName)
        val defaultUnit = if (isDrink) "100 ml" else "100 g"

        return when {
            trimmed.contains("/ 100", ignoreCase = true) -> trimmed
            trimmed.contains("kcal", ignoreCase = true) -> "$trimmed / $defaultUnit"
            trimmed.all { it.isDigit() || it == ' ' } && trimmed.isNotBlank() -> "$trimmed kcal / $defaultUnit"
            else -> trimmed
        }
    }

    private fun estimateCaloriesForDish(lowerName: String, isNonVeg: Boolean): String {
        return when {
            isBeverage(lowerName) -> {
                when {
                    lowerName.contains("chai") || lowerName.contains("tea") || lowerName.contains("coffee") -> "65 kcal / 100 ml"
                    lowerName.contains("lassi") || lowerName.contains("shake") -> "110 kcal / 100 ml"
                    lowerName.contains("buttermilk") || lowerName.contains("chaas") -> "40 kcal / 100 ml"
                    else -> "55 kcal / 100 ml"
                }
            }
            isSoup(lowerName) -> {
                if (lowerName.contains("cream") || lowerName.contains("bisque") || lowerName.contains("chowder")) "90 kcal / 100 ml"
                else "55 kcal / 100 ml"
            }
            isSalad(lowerName) -> {
                if (lowerName.contains("caesar") || lowerName.contains("mayo") || lowerName.contains("feta")) "145 kcal / 100 g"
                else "95 kcal / 100 g"
            }
            isDessert(lowerName) -> {
                if (lowerName.contains("chocolate") || lowerName.contains("cake") || lowerName.contains("jamun")) "380 kcal / 100 g"
                else "280 kcal / 100 g"
            }
            lowerName.contains("biryani") || lowerName.contains("pulao") || lowerName.contains("rice") -> {
                if (isNonVeg) "230 kcal / 100 g" else "185 kcal / 100 g"
            }
            lowerName.contains("naan") || lowerName.contains("paratha") || lowerName.contains("roti") || lowerName.contains("bread") -> "275 kcal / 100 g"
            lowerName.contains("pasta") || lowerName.contains("pizza") -> "245 kcal / 100 g"
            lowerName.contains("samosa") || lowerName.contains("pakora") || lowerName.contains("vada") || lowerName.contains("fry") -> "280 kcal / 100 g"
            isNonVeg -> "240 kcal / 100 g"
            lowerName.contains("paneer") || lowerName.contains("makhani") || lowerName.contains("korma") -> "220 kcal / 100 g"
            else -> "160 kcal / 100 g"
        }
    }

    private fun isBeverage(name: String): Boolean {
        val keywords = listOf("juice", "mojito", "cooler", "smash", "lassi", "spritz", "chai", "tea", "coffee", "smoothie", "shake", "drink", "punch", "buttermilk", "chaas", "soda", "sharbat", "brew")
        return keywords.any { name.contains(it) }
    }

    private fun isSoup(name: String): Boolean {
        val keywords = listOf("soup", "bisque", "broth", "chowder", "shorba", "bouillon", "gazpacho")
        return keywords.any { name.contains(it) }
    }

    private fun isSalad(name: String): Boolean {
        val keywords = listOf("salad", "slaw", "tabouleh", "caprese", "caesar", "tartare")
        return keywords.any { name.contains(it) }
    }

    private fun isDessert(name: String): Boolean {
        val keywords = listOf("cake", "jamun", "halwa", "rasmalai", "pie", "tiramisu", "pudding", "tart", "ice cream", "kulfi", "sweet", "pastry", "brownie", "cheesecake", "brûlée", "brule")
        return keywords.any { name.contains(it) }
    }

    private fun isStarter(name: String): Boolean {
        val keywords = listOf("tikka", "kebab", "kabab", "chaat", "samosa", "pakora", "vada", "dosa", "idli", "roll", "bruschetta", "crostini", "dim sum", "wing", "satay", "taco", "nacho", "finger")
        return keywords.any { name.contains(it) }
    }

    /**
     * Approximate costing per pax (INR/Global Currency Unit) for fallback use
     */
    fun getEstimatedCostPerPax(dishName: String): Double {
        val lower = dishName.lowercase()
        return when {
            lower.contains("lobster") || lower.contains("prawn") || lower.contains("shrimp") || lower.contains("crab") -> 450.0
            lower.contains("mutton") || lower.contains("lamb") || lower.contains("steak") || lower.contains("beef") -> 350.0
            lower.contains("chicken") || lower.contains("fish") || lower.contains("egg") -> 180.0
            lower.contains("paneer") || lower.contains("mushroom") || lower.contains("broccoli") -> 120.0
            lower.contains("dal") || lower.contains("aloo") || lower.contains("gobi") || lower.contains("rice") -> 45.0
            lower.contains("juice") || lower.contains("mocktail") -> 65.0
            lower.contains("soup") || lower.contains("salad") -> 55.0
            lower.contains("dessert") || lower.contains("sweet") || lower.contains("cake") -> 85.0
            else -> 75.0
        }
    }
}

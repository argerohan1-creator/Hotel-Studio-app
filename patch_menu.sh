#!/bin/bash
cat << 'INNER_EOF' > replacement.txt
    val dietFilter by viewModel.recipePickerDiet.collectAsState()
    val prepTimeFilter by viewModel.recipePickerPrepTime.collectAsState()
    var courseDropdownExpanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "ALL", "Starters & Appetizers", "Soups", "Salads", "Main Course", "Sweets & Confectionery", "Beverages & Mocktails"
    )

    val filteredRecipes = recipes.filter { recipe ->
        val matchesSearch = searchQuery.isBlank() ||
                recipe.name.contains(searchQuery, ignoreCase = true) ||
                recipe.desc.contains(searchQuery, ignoreCase = true) ||
                recipe.ingredients.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesCategory = selectedCategory == "ALL" || recipe.category.equals(selectedCategory, ignoreCase = true)

        val nonVegKeywords = listOf("Chicken", "Fish", "Mutton", "Beef", "Pork", "Prawn", "Shrimp", "Seafood")
        val isVeg = !recipe.category.contains("Non-Veg", ignoreCase = true) &&
                !nonVegKeywords.any { recipe.name.contains(it, ignoreCase = true) }

        val nonVeganKeywords = nonVegKeywords + listOf("cheese", "milk", "butter", "cream", "yogurt", "honey", "egg", "ghee", "paneer", "mayo")
        val isVegan = isVeg && !nonVeganKeywords.any { kw ->
            recipe.name.contains(kw, ignoreCase = true) || recipe.ingredients.any { it.contains(kw, ignoreCase = true) }
        }

        val glutenKeywords = listOf("bread", "flour", "wheat", "pasta", "baguette", "noodle", "soy sauce", "naan", "roti", "pita", "couscous", "macaroni", "tart", "pie", "cake")
        val isGf = !glutenKeywords.any { kw ->
            recipe.name.contains(kw, ignoreCase = true) || recipe.ingredients.any { it.contains(kw, ignoreCase = true) }
        }

        val matchesDiet = when (dietFilter) {
            "VEG" -> isVeg
            "NONVEG" -> !isVeg
            "VEGAN" -> isVegan
            "GF" -> isGf
            else -> true
        }

        val matchesPrepTime = if (prepTimeFilter == "ALL") true else {
            val digits = recipe.prepTime.filter { it.isDigit() }.toIntOrNull() ?: 0
            val isHour = recipe.prepTime.contains("hr", ignoreCase = true) || recipe.prepTime.contains("hour", ignoreCase = true)
            val totalMins = if (isHour && digits < 10) digits * 60 else digits
            
            when (prepTimeFilter) {
                "QUICK" -> totalMins in 1..20
                "MEDIUM" -> totalMins in 21..45
                "LONG" -> totalMins > 45
                else -> true
            }
        }

        matchesSearch && matchesCategory && matchesDiet && matchesPrepTime
    }
INNER_EOF

sed -i -e '/val dietFilter by viewModel.recipePickerDiet.collectAsState()/,/matchesSearch && matchesCategory && matchesDiet/c\' -e "$(cat replacement.txt)" app/src/main/java/com/example/ui/screens/MenuBuilderScreen.kt

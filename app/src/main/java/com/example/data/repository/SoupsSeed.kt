package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedSoups = listOf(
    RecipeEntity(
        name = "Classic French Onion Soup",
        category = "Soups",
        prepTime = "45 mins",
        yieldPortions = "4 Portions",
        cals = "320 kcal",
        desc = "Rich beef broth with deeply caramelized onions, topped with a gruyere crouton.",
        story = "A Parisian bistro classic that turns humble onions into culinary gold.",
        ingredients = listOf("Yellow Onions", "Beef Broth", "Gruyere Cheese", "Baguette", "Thyme", "Butter"),
        steps = listOf("Caramelize onions slowly in butter.", "Add broth and thyme, simmer.", "Ladle into bowls, top with baguette and cheese.", "Broil until cheese is bubbly and golden.")
    ),
    RecipeEntity(
        name = "Creamy Tomato Basil Bisque",
        category = "Soups",
        prepTime = "30 mins",
        yieldPortions = "4 Portions",
        cals = "280 kcal",
        desc = "Silky tomato soup infused with fresh basil and finished with heavy cream.",
        story = "Comfort food elevated to fine dining standards.",
        ingredients = listOf("San Marzano Tomatoes", "Fresh Basil", "Heavy Cream", "Garlic", "Onion", "Vegetable Broth"),
        steps = listOf("Saute onion and garlic.", "Add tomatoes and broth, simmer.", "Blend until smooth.", "Stir in cream and fresh basil.")
    ),
    RecipeEntity(
        name = "Wild Mushroom Truffle Soup",
        category = "Soups",
        prepTime = "40 mins",
        yieldPortions = "4 Portions",
        cals = "310 kcal",
        desc = "Earthy wild mushrooms blended into a creamy veloute with white truffle oil.",
        story = "A luxurious soup capturing the essence of the autumn forest.",
        ingredients = listOf("Cremini & Shiitake Mushrooms", "Truffle Oil", "Cream", "Thyme", "Garlic", "Chicken or Veg Broth"),
        steps = listOf("Saute mushrooms and garlic.", "Add broth and thyme, simmer.", "Puree half the soup for texture.", "Finish with cream and a drizzle of truffle oil.")
    ),
    RecipeEntity(
        name = "Thai Tom Yum Goong",
        category = "Soups",
        prepTime = "25 mins",
        yieldPortions = "4 Portions",
        cals = "210 kcal",
        desc = "Spicy, sour, and aromatic Thai broth with prawns, lemongrass, and galangal.",
        story = "The quintessential Thai soup balancing heat, acidity, and umami.",
        ingredients = listOf("Shrimp", "Lemongrass", "Galangal", "Kaffir Lime Leaves", "Chili Paste (Nam Prik Pao)", "Fish Sauce", "Lime Juice"),
        steps = listOf("Boil broth with lemongrass, galangal, and lime leaves.", "Add chili paste and shrimp.", "Simmer until shrimp are cooked.", "Season with lime juice and fish sauce.")
    ),
    RecipeEntity(
        name = "Roasted Butternut Squash Soup",
        category = "Soups",
        prepTime = "50 mins",
        yieldPortions = "6 Portions",
        cals = "240 kcal",
        desc = "Velvety roasted squash soup with warm spices and a hint of maple.",
        story = "A staple of autumnal and winter banquet menus.",
        ingredients = listOf("Butternut Squash", "Onion", "Nutmeg", "Cinnamon", "Vegetable Stock", "Maple Syrup", "Cream"),
        steps = listOf("Roast halved squash until tender.", "Scoop flesh and saute with onions and spices.", "Add stock and blend.", "Stir in a touch of maple and cream.")
    ),
    RecipeEntity(
        name = "Andalusian Gazpacho",
        category = "Soups",
        prepTime = "15 mins",
        yieldPortions = "4 Portions",
        cals = "150 kcal",
        desc = "Chilled Spanish soup of blended ripe tomatoes, cucumbers, and peppers.",
        story = "A refreshing, raw soup perfect for hot summer days.",
        ingredients = listOf("Ripe Tomatoes", "Cucumber", "Bell Pepper", "Red Onion", "Garlic", "Sherry Vinegar", "Olive Oil"),
        steps = listOf("Roughly chop all vegetables.", "Blend with vinegar and olive oil until smooth.", "Chill for at least 2 hours before serving.")
    ),
    RecipeEntity(
        name = "Lobster Bisque",
        category = "Soups",
        prepTime = "60 mins",
        yieldPortions = "4 Portions",
        cals = "420 kcal",
        desc = "A highly seasoned, creamy French soup based on a strained broth of lobster shells.",
        story = "The pinnacle of seafood elegance in classical French cuisine.",
        ingredients = listOf("Lobster Meat & Shells", "Cognac", "Heavy Cream", "Tomato Paste", "Mirepoix", "Tarragon"),
        steps = listOf("Roast shells with mirepoix.", "Flambe with cognac.", "Simmer with tomato paste and broth.", "Strain, thicken, and finish with cream and lobster meat.")
    )
)

package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedStarters = listOf(
    RecipeEntity(
        name = "Classic Italian Bruschetta",
        category = "Starters & Appetizers",
        prepTime = "15 mins",
        yieldPortions = "4 Portions",
        cals = "150 kcal",
        desc = "Toasted artisanal bread rubbed with garlic and topped with fresh tomatoes, basil, and olive oil.",
        story = "Originating in 15th century Italy, it was a way to salvage bread that was going stale.",
        ingredients = listOf("Baguette", "Roma Tomatoes", "Fresh Basil", "Garlic", "Extra Virgin Olive Oil", "Balsamic Glaze"),
        steps = listOf("Slice and toast the baguette.", "Rub toasted bread with a halved garlic clove.", "Dice tomatoes and mix with torn basil, oil, salt, and pepper.", "Top the bread with the tomato mixture and drizzle with balsamic glaze.")
    ),
    RecipeEntity(
        name = "Punjabi Samosa",
        category = "Starters & Appetizers",
        prepTime = "45 mins",
        yieldPortions = "6 Portions",
        cals = "220 kcal",
        desc = "Crispy, flaky pastry filled with a spiced mixture of potatoes and green peas.",
        story = "A popular Indian street food that traveled from the Middle East to South Asia centuries ago.",
        ingredients = listOf("All-Purpose Flour", "Ghee", "Potatoes (boiled & mashed)", "Green Peas", "Cumin Seeds", "Garam Masala", "Coriander", "Oil for frying"),
        steps = listOf("Prepare a stiff dough using flour, ghee, and water. Rest for 30 mins.", "Sauté cumin, mashed potatoes, peas, and spices for the filling.", "Roll dough, cut into semi-circles, form a cone, and stuff.", "Deep fry on low heat until golden and crisp.")
    ),
    RecipeEntity(
        name = "Crispy Fried Calamari",
        category = "Starters & Appetizers",
        prepTime = "20 mins",
        yieldPortions = "4 Portions",
        cals = "280 kcal",
        desc = "Tender rings of squid lightly coated in seasoned flour and fried to golden perfection.",
        story = "A Mediterranean seaside staple that has become a globally beloved appetizer.",
        ingredients = listOf("Squid Rings", "All-Purpose Flour", "Cornstarch", "Garlic Powder", "Paprika", "Lemon Wedges", "Marinara Sauce", "Oil for frying"),
        steps = listOf("Mix flour, cornstarch, and spices.", "Toss squid rings in the seasoned flour mixture until coated.", "Fry in hot oil for 2-3 minutes until lightly golden.", "Drain and serve immediately with lemon and marinara.")
    ),
    RecipeEntity(
        name = "Vietnamese Summer Rolls",
        category = "Starters & Appetizers",
        prepTime = "25 mins",
        yieldPortions = "4 Portions",
        cals = "120 kcal",
        desc = "Fresh rice paper rolls filled with shrimp, rice vermicelli, and crisp herbs.",
        story = "A refreshing, un-fried alternative to spring rolls, highlighting Southeast Asian fresh herbs.",
        ingredients = listOf("Rice Paper Wrappers", "Cooked Shrimp (halved)", "Rice Vermicelli Noodles", "Mint", "Cilantro", "Lettuce", "Peanut Dipping Sauce"),
        steps = listOf("Soak rice paper in warm water until pliable.", "Lay flat and arrange lettuce, herbs, noodles, and shrimp.", "Fold the sides inward and roll tightly.", "Serve chilled with a side of peanut sauce.")
    ),
    RecipeEntity(
        name = "Chicken Satay with Peanut Sauce",
        category = "Starters & Appetizers",
        prepTime = "40 mins",
        yieldPortions = "4 Portions",
        cals = "310 kcal",
        desc = "Skewered and grilled chicken marinated in lemongrass and turmeric, served with a rich peanut sauce.",
        story = "An iconic Southeast Asian street food known for its aromatic marinade and charred flavor.",
        ingredients = listOf("Chicken Thighs (sliced)", "Lemongrass", "Turmeric", "Coconut Milk", "Brown Sugar", "Bamboo Skewers", "Peanut Sauce"),
        steps = listOf("Marinate chicken in lemongrass, turmeric, coconut milk, and sugar.", "Thread onto soaked bamboo skewers.", "Grill over high heat until cooked through and charred.", "Serve with warm peanut sauce and cucumber relish.")
    ),
    RecipeEntity(
        name = "Spinach and Artichoke Dip",
        category = "Starters & Appetizers",
        prepTime = "30 mins",
        yieldPortions = "6 Portions",
        cals = "350 kcal",
        desc = "A creamy, cheesy baked dip loaded with spinach and tender artichoke hearts.",
        story = "A modern American classic that transforms simple greens into an irresistible party appetizer.",
        ingredients = listOf("Cream Cheese", "Sour Cream", "Spinach", "Artichoke Hearts", "Mozzarella", "Parmesan", "Garlic", "Tortilla Chips"),
        steps = listOf("Mix cream cheese, sour cream, and garlic until smooth.", "Stir in chopped spinach, artichokes, and half the cheeses.", "Transfer to a baking dish and top with remaining cheese.", "Bake until bubbly and golden. Serve with chips.")
    ),
    RecipeEntity(
        name = "Paneer Tikka Skewers",
        category = "Starters & Appetizers",
        prepTime = "45 mins",
        yieldPortions = "4 Portions",
        cals = "260 kcal",
        desc = "Chunks of paneer cheese and vegetables marinated in spiced yogurt and grilled.",
        story = "A North Indian tandoori specialty, offering a smoky, vegetarian alternative to chicken tikka.",
        ingredients = listOf("Paneer Cubes", "Bell Peppers", "Onions", "Thick Yogurt", "Tandoori Masala", "Ginger Garlic Paste", "Mustard Oil", "Lemon Juice"),
        steps = listOf("Whisk yogurt with spices, ginger garlic paste, and oil.", "Coat paneer and vegetables in the marinade and rest for 30 mins.", "Thread onto skewers.", "Grill or bake at high heat until charred at the edges.")
    ),
    RecipeEntity(
        name = "Shrimp Cocktail",
        category = "Starters & Appetizers",
        prepTime = "15 mins",
        yieldPortions = "4 Portions",
        cals = "110 kcal",
        desc = "Chilled, perfectly poached jumbo shrimp served with a zesty horseradish cocktail sauce.",
        story = "A symbol of mid-20th-century luxury dining, remaining a timeless starter.",
        ingredients = listOf("Jumbo Shrimp (peeled & deveined)", "Lemon", "Peppercorns", "Ketchup", "Prepared Horseradish", "Worcestershire Sauce", "Hot Sauce"),
        steps = listOf("Poach shrimp in water seasoned with lemon and peppercorns for 2-3 minutes.", "Immediately chill in an ice bath.", "Mix ketchup, horseradish, Worcestershire, and hot sauce for the dip.", "Serve shrimp hooked over the edge of a glass with sauce.")
    ),
    RecipeEntity(
        name = "Caprese Skewers",
        category = "Starters & Appetizers",
        prepTime = "10 mins",
        yieldPortions = "4 Portions",
        cals = "140 kcal",
        desc = "Bite-sized cherry tomatoes, fresh mozzarella balls, and basil leaves drizzled with balsamic.",
        story = "A fun, portable version of the classic Italian salad.",
        ingredients = listOf("Cherry Tomatoes", "Bocconcini (Mini Mozzarella)", "Fresh Basil Leaves", "Balsamic Glaze", "Olive Oil", "Salt & Pepper", "Small Skewers"),
        steps = listOf("Thread a tomato, basil leaf, and mozzarella ball onto each skewer.", "Arrange on a platter.", "Drizzle with olive oil and balsamic glaze.", "Season with salt and freshly cracked pepper.")
    ),
    RecipeEntity(
        name = "Deviled Eggs",
        category = "Starters & Appetizers",
        prepTime = "20 mins",
        yieldPortions = "6 Portions",
        cals = "130 kcal",
        desc = "Hard-boiled eggs stuffed with a creamy, tangy filling of yolk, mayonnaise, and mustard.",
        story = "A staple of retro American dining, the term 'deviled' refers to the spicy kick of mustard and paprika.",
        ingredients = listOf("Eggs", "Mayonnaise", "Dijon Mustard", "White Vinegar", "Salt & Pepper", "Paprika", "Chives"),
        steps = listOf("Hard boil eggs, cool, peel, and halve lengthwise.", "Scoop out yolks and mash until smooth.", "Stir in mayonnaise, mustard, vinegar, salt, and pepper.", "Pipe filling back into egg whites and garnish with paprika and chives.")
    )
)

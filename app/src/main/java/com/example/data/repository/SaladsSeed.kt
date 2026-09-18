package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedSalads = listOf(
    RecipeEntity(
        name = "Classic Greek Salad (Horiatiki)",
        category = "Salads",
        prepTime = "15 mins",
        yieldPortions = "4 Portions",
        cals = "280 kcal",
        desc = "Crisp cucumbers, tomatoes, red onions, kalamata olives, and a block of feta.",
        story = "A rustic, refreshing staple of Mediterranean coastal dining.",
        ingredients = listOf("Tomatoes", "Cucumbers", "Red Onion", "Kalamata Olives", "Feta Cheese Block", "Oregano", "Olive Oil"),
        steps = listOf("Chop tomatoes, cucumbers, and onions.", "Toss with olives in a shallow bowl.", "Top with a block of feta.", "Drizzle generously with olive oil and sprinkle oregano.")
    ),
    RecipeEntity(
        name = "Caprese Salad",
        category = "Salads",
        prepTime = "10 mins",
        yieldPortions = "2 Portions",
        cals = "250 kcal",
        desc = "Slices of fresh mozzarella, ripe tomatoes, and sweet basil.",
        story = "The colors of the Italian flag on a plate, celebrating simple, high-quality ingredients.",
        ingredients = listOf("Fresh Mozzarella", "Heirloom Tomatoes", "Fresh Basil", "Balsamic Glaze", "Extra Virgin Olive Oil", "Sea Salt"),
        steps = listOf("Slice tomatoes and mozzarella.", "Arrange alternately on a platter.", "Tuck basil leaves between slices.", "Drizzle with oil and balsamic glaze. Season with salt.")
    ),
    RecipeEntity(
        name = "Waldorf Salad",
        category = "Salads",
        prepTime = "15 mins",
        yieldPortions = "4 Portions",
        cals = "320 kcal",
        desc = "Crisp apples, celery, and walnuts dressed in mayonnaise, served over lettuce.",
        story = "Created in the 1890s at the Waldorf-Astoria Hotel in New York City.",
        ingredients = listOf("Apples", "Celery", "Walnuts", "Grapes", "Mayonnaise", "Lemon Juice", "Lettuce Cups"),
        steps = listOf("Chop apples and celery.", "Halve grapes.", "Toss with mayonnaise and lemon juice.", "Serve in crisp lettuce cups topped with walnuts.")
    ),
    RecipeEntity(
        name = "Nicoise Salad",
        category = "Salads",
        prepTime = "25 mins",
        yieldPortions = "4 Portions",
        cals = "380 kcal",
        desc = "Composed salad with tuna, green beans, hard-boiled eggs, tomatoes, and potatoes.",
        story = "A composed masterpiece from the French Riviera.",
        ingredients = listOf("Seared Tuna or Quality Canned Tuna", "Green Beans", "Hard-Boiled Eggs", "Cherry Tomatoes", "Baby Potatoes", "Nicoise Olives", "Vinaigrette"),
        steps = listOf("Blanch green beans and boil potatoes.", "Sear tuna if using fresh.", "Arrange all ingredients neatly on a platter.", "Drizzle with a Dijon vinaigrette.")
    ),
    RecipeEntity(
        name = "Lebanese Fattoush",
        category = "Salads",
        prepTime = "20 mins",
        yieldPortions = "4 Portions",
        cals = "220 kcal",
        desc = "Mixed greens and vegetables topped with crispy toasted pita bread and sumac dressing.",
        story = "A vibrant Middle Eastern salad known for its tangy, citrusy profile.",
        ingredients = listOf("Toasted Pita Bread", "Romaine Lettuce", "Cucumbers", "Tomatoes", "Radishes", "Sumac", "Pomegranate Molasses", "Olive Oil"),
        steps = listOf("Tear toasted pita into bite-sized pieces.", "Chop all vegetables.", "Whisk sumac, pomegranate molasses, and oil.", "Toss everything together right before serving.")
    ),
    RecipeEntity(
        name = "Quinoa Superfood Salad",
        category = "Salads",
        prepTime = "20 mins",
        yieldPortions = "4 Portions",
        cals = "310 kcal",
        desc = "Fluffy quinoa mixed with avocado, kale, pomegranate seeds, and a lemon tahini dressing.",
        story = "A modern cafe favorite, packed with nutrients and vibrant colors.",
        ingredients = listOf("Cooked Quinoa", "Massaged Kale", "Avocado", "Pomegranate Seeds", "Toasted Pumpkin Seeds", "Lemon Tahini Dressing"),
        steps = listOf("Massage kale with olive oil until tender.", "Toss with cooked quinoa, avocado cubes, and pomegranate seeds.", "Drizzle with tahini dressing and top with seeds.")
    ),
    RecipeEntity(
        name = "Green Papaya Salad (Som Tum)",
        category = "Salads",
        prepTime = "20 mins",
        yieldPortions = "2 Portions",
        cals = "180 kcal",
        desc = "Shredded unripe papaya pounded with chili, lime, peanuts, and fish sauce.",
        story = "A street-food sensation from Isaan, Thailand, combining all fundamental Thai flavors.",
        ingredients = listOf("Shredded Green Papaya", "Cherry Tomatoes", "Green Beans", "Roasted Peanuts", "Bird's Eye Chilis", "Lime Juice", "Fish Sauce", "Palm Sugar"),
        steps = listOf("Pound chilis, garlic, and palm sugar in a mortar.", "Add beans and tomatoes, bruise lightly.", "Add papaya, lime juice, and fish sauce.", "Toss well and top with peanuts.")
    )
)

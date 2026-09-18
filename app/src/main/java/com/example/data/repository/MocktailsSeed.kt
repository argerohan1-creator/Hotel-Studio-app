package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedMocktails = listOf(
    RecipeEntity(
        name = "Sunrise Citrus Cooler",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "65 kcal",
        desc = "A vibrant blend of fresh orange, lemon, and a hint of grenadine, served over ice.",
        story = "Inspired by summer mornings, perfect for refreshing the palate.",
        ingredients = listOf("Fresh Orange Juice", "Lemon Juice", "Grenadine Syrup", "Sparkling Water", "Ice"),
        steps = listOf("Fill a tall glass with ice.", "Pour orange and lemon juices.", "Slowly add grenadine so it sinks.", "Top with sparkling water.")
    ),
    RecipeEntity(
        name = "Blueberry Basil Smash",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "55 kcal",
        desc = "Muddled fresh blueberries and aromatic basil topped with ginger ale.",
        story = "A herbaceous, fruity concoction that elevates evening gatherings.",
        ingredients = listOf("Fresh Blueberries", "Basil Leaves", "Lime Juice", "Ginger Ale", "Agave Syrup"),
        steps = listOf("Muddle blueberries and basil with agave and lime juice.", "Add ice to a rocks glass.", "Strain the mixture.", "Top with ginger ale.")
    ),
    RecipeEntity(
        name = "Tropical Pineapple Punch",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "80 kcal",
        desc = "A rich, creamy mocktail with pineapple juice, coconut cream, and crushed ice.",
        story = "A virgin take on the classic Piña Colada.",
        ingredients = listOf("Pineapple Juice", "Coconut Cream", "Lime Juice", "Crushed Ice", "Pineapple Wedge"),
        steps = listOf("Blend pineapple juice, coconut cream, and lime juice with ice until smooth.", "Pour into a hurricane glass.", "Garnish with a pineapple wedge.")
    ),
    RecipeEntity(
        name = "Cucumber Mint Spritzer",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "30 kcal",
        desc = "Cool cucumber slices and fresh mint topped with soda water.",
        story = "The ultimate spa-day inspired refreshment.",
        ingredients = listOf("Cucumber Slices", "Mint Leaves", "Simple Syrup", "Soda Water", "Ice"),
        steps = listOf("Muddle cucumber and mint lightly.", "Add ice and simple syrup.", "Top with soda water and stir gently.")
    ),
    RecipeEntity(
        name = "Spicy Jalapeno Margarita Mocktail",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "40 kcal",
        desc = "Tangy lime, a hint of agave, and a spicy kick of jalapeno with a salted rim.",
        story = "All the fiesta, none of the tequila.",
        ingredients = listOf("Lime Juice", "Agave Nectar", "Jalapeno Slices", "Sparkling Water", "Tajin Rim"),
        steps = listOf("Rim a glass with tajin.", "Muddle jalapeno with lime and agave.", "Shake with ice, strain, and top with sparkling water.")
    ),
    RecipeEntity(
        name = "Watermelon Breeze",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "50 kcal",
        desc = "Freshly blended watermelon chunks with a squeeze of lime and coconut water.",
        story = "Hydrating and sweet, a summer favorite.",
        ingredients = listOf("Fresh Watermelon Chunks", "Coconut Water", "Lime Juice", "Mint Garnish"),
        steps = listOf("Blend watermelon with coconut water and lime juice.", "Strain if desired.", "Serve over ice with a mint sprig.")
    ),
    RecipeEntity(
        name = "Ginger Peach Fizz",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "70 kcal",
        desc = "Sweet peach puree balanced with the spicy bite of ginger beer.",
        story = "A sophisticated balance of sweet and spice.",
        ingredients = listOf("Peach Puree", "Ginger Beer", "Lemon Juice", "Ice"),
        steps = listOf("Add peach puree and lemon juice to a glass.", "Stir in ice.", "Top generously with ginger beer.")
    ),
    RecipeEntity(
        name = "Lavender Lemonade",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "60 kcal",
        desc = "Classic lemonade infused with floral culinary lavender syrup.",
        story = "A delicate and aromatic twist on the traditional lemonade.",
        ingredients = listOf("Fresh Lemon Juice", "Lavender Syrup", "Water", "Ice", "Lemon Wheel"),
        steps = listOf("Mix lemon juice and lavender syrup.", "Dilute with water to taste.", "Serve over ice garnished with a lemon wheel.")
    ),
    RecipeEntity(
        name = "Pomegranate Rosemary Spritz",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "55 kcal",
        desc = "Tart pomegranate juice with a savory hint of rosemary and sparkling tonic.",
        story = "A vibrant red mocktail with a complex, herbaceous profile.",
        ingredients = listOf("Pomegranate Juice", "Rosemary Sprig", "Tonic Water", "Ice"),
        steps = listOf("Bruise the rosemary sprig and place in glass.", "Add ice and pomegranate juice.", "Top with tonic water.")
    ),
    RecipeEntity(
        name = "Cranberry Apple Mule",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "75 kcal",
        desc = "Crisp apple cider and tart cranberry juice mixed with ginger beer.",
        story = "A festive and warming cooler for autumn and winter.",
        ingredients = listOf("Apple Cider", "Cranberry Juice", "Ginger Beer", "Lime Wedge", "Ice"),
        steps = listOf("Fill a copper mug with ice.", "Pour in apple cider and cranberry juice.", "Top with ginger beer and squeeze a lime wedge.")
    ),
    RecipeEntity(
        name = "Shirley Temple",
        category = "Beverages & Mocktails",
        prepTime = "3 mins",
        yieldPortions = "1 Glass",
        cals = "90 kcal",
        desc = "The legendary mocktail made with ginger ale, grenadine, and maraschino cherries.",
        story = "The world's most famous virgin drink, loved across generations.",
        ingredients = listOf("Ginger Ale", "Grenadine", "Maraschino Cherries", "Ice"),
        steps = listOf("Fill glass with ice.", "Pour ginger ale.", "Add a splash of grenadine and garnish with cherries.")
    ),
    RecipeEntity(
        name = "Roy Rogers",
        category = "Beverages & Mocktails",
        prepTime = "3 mins",
        yieldPortions = "1 Glass",
        cals = "100 kcal",
        desc = "Cola mixed with grenadine and garnished with a cherry.",
        story = "The counterpart to the Shirley Temple, rich and fizzy.",
        ingredients = listOf("Cola", "Grenadine", "Maraschino Cherries", "Ice"),
        steps = listOf("Fill glass with ice.", "Pour in cola.", "Add grenadine and stir, garnishing with a cherry.")
    ),
    RecipeEntity(
        name = "Strawberry Chamomile Iced Tea",
        category = "Beverages & Mocktails",
        prepTime = "10 mins",
        yieldPortions = "1 Glass",
        cals = "45 kcal",
        desc = "Cold-brewed chamomile tea lightly sweetened with strawberry syrup.",
        story = "A calming and fruity iced tea variant.",
        ingredients = listOf("Brewed Chamomile Tea (Chilled)", "Strawberry Syrup", "Fresh Strawberries", "Ice"),
        steps = listOf("Combine chilled tea and strawberry syrup.", "Stir well.", "Serve over ice with fresh strawberry slices.")
    ),
    RecipeEntity(
        name = "Mango Mule Mocktail",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "80 kcal",
        desc = "Sweet mango puree mixed with spicy ginger beer and fresh lime.",
        story = "A tropical spin on the classic Moscow Mule.",
        ingredients = listOf("Mango Puree", "Lime Juice", "Ginger Beer", "Ice", "Mint Garnish"),
        steps = listOf("Shake mango puree and lime juice with ice.", "Strain into a copper mug.", "Top with ginger beer and garnish with mint.")
    ),
    RecipeEntity(
        name = "Grapefruit Kombucha Cooler",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "35 kcal",
        desc = "Probiotic-rich plain kombucha mixed with fresh squeezed ruby red grapefruit juice.",
        story = "A tangy, gut-friendly refreshment.",
        ingredients = listOf("Plain Kombucha", "Grapefruit Juice", "Rosemary Sprig", "Ice"),
        steps = listOf("Fill a glass with ice.", "Mix equal parts kombucha and grapefruit juice.", "Garnish with rosemary.")
    ),
    RecipeEntity(
        name = "Hibiscus Iced Tea Sparkler",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "20 kcal",
        desc = "Tart and floral hibiscus tea mixed with sparkling water and a squeeze of lime.",
        story = "A vibrant magenta drink that is as beautiful as it is thirst-quenching.",
        ingredients = listOf("Brewed Hibiscus Tea (Chilled)", "Sparkling Water", "Lime Slices", "Ice"),
        steps = listOf("Pour chilled hibiscus tea over ice.", "Top with sparkling water.", "Garnish with lime slices.")
    ),
    RecipeEntity(
        name = "Iced Vanilla Matcha Latte",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "95 kcal",
        desc = "Premium ceremonial grade matcha whisked and poured over iced vanilla oat milk.",
        story = "An energizing and creamy green tea beverage.",
        ingredients = listOf("Matcha Powder", "Warm Water", "Vanilla Syrup", "Oat Milk", "Ice"),
        steps = listOf("Whisk matcha powder with warm water until frothy.", "Fill glass with ice and add vanilla syrup and oat milk.", "Pour matcha over the milk to layer.")
    ),
    RecipeEntity(
        name = "Berry Mojito Mocktail",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "60 kcal",
        desc = "A mix of seasonal berries muddled with mint and lime, topped with soda.",
        story = "A fruity variation of the classic mojito, bursting with berry flavors.",
        ingredients = listOf("Mixed Berries", "Mint Leaves", "Lime Juice", "Simple Syrup", "Soda Water"),
        steps = listOf("Muddle berries, mint, lime juice, and syrup.", "Add ice to the glass.", "Top with soda water and stir gently.")
    ),
    RecipeEntity(
        name = "Kiwi Apple Spritz",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "70 kcal",
        desc = "Muddled fresh kiwi combined with crisp apple juice and sparkling water.",
        story = "A tart, sweet, and visually striking green mocktail.",
        ingredients = listOf("Fresh Kiwi", "Apple Juice", "Sparkling Water", "Ice"),
        steps = listOf("Muddle half a kiwi in a glass.", "Add ice and apple juice.", "Top with sparkling water and stir.")
    ),
    RecipeEntity(
        name = "Coconut Mint Cooler",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "45 kcal",
        desc = "Hydrating coconut water blended with fresh mint and a squeeze of lemon.",
        story = "A simple, ultra-hydrating beverage perfect for hot climates.",
        ingredients = listOf("Coconut Water", "Fresh Mint", "Lemon Juice", "Ice"),
        steps = listOf("Blend coconut water, mint, and lemon juice briefly.", "Pour over ice in a tall glass.")
    ),
    RecipeEntity(
        name = "Sparkling Pear & Thyme",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "65 kcal",
        desc = "Pear nectar mixed with a savory thyme simple syrup and sparkling water.",
        story = "An elegant, herbaceous mocktail suitable for fine dining.",
        ingredients = listOf("Pear Nectar", "Thyme Syrup", "Sparkling Water", "Ice", "Thyme Sprig"),
        steps = listOf("Combine pear nectar and thyme syrup in a glass with ice.", "Top with sparkling water.", "Garnish with a fresh thyme sprig.")
    ),
    RecipeEntity(
        name = "Blood Orange Italian Soda",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "85 kcal",
        desc = "Blood orange syrup mixed with club soda and a splash of cream.",
        story = "A creamy, dreamy, and citrusy cafe classic.",
        ingredients = listOf("Blood Orange Syrup", "Club Soda", "Heavy Cream (or Coconut Cream)", "Ice"),
        steps = listOf("Fill a glass with ice.", "Add blood orange syrup and club soda, leaving room at the top.", "Gently float cream on top.")
    ),
    RecipeEntity(
        name = "Passionfruit Lemonade",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "75 kcal",
        desc = "Fresh lemonade enhanced with tropical passionfruit pulp.",
        story = "A sweet and tart exotic thirst quencher.",
        ingredients = listOf("Lemonade", "Passionfruit Pulp", "Ice", "Lemon Wedge"),
        steps = listOf("Mix lemonade and passionfruit pulp in a shaker.", "Pour over ice.", "Garnish with a lemon wedge.")
    ),
    RecipeEntity(
        name = "Cinnamon Plum Spritz",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "60 kcal",
        desc = "Plum juice spiced with cinnamon syrup and topped with tonic.",
        story = "A rich, spiced drink perfect for transitional seasons.",
        ingredients = listOf("Plum Juice", "Cinnamon Syrup", "Tonic Water", "Ice", "Cinnamon Stick"),
        steps = listOf("Combine plum juice and cinnamon syrup in a glass.", "Add ice and top with tonic.", "Stir with a cinnamon stick.")
    ),
    RecipeEntity(
        name = "Lychee Rose Mocktail",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "70 kcal",
        desc = "Sweet lychee juice with a hint of rose water and sparkling club soda.",
        story = "A highly aromatic, delicate beverage evoking Eastern romance.",
        ingredients = listOf("Lychee Juice", "Rose Water", "Club Soda", "Lychee Fruit", "Ice"),
        steps = listOf("Muddle a lychee fruit in the glass.", "Add ice, lychee juice, and a drop of rose water.", "Top with club soda.")
    ),
    RecipeEntity(
        name = "Spiced Cranberry Hot Toddy (Alcohol-Free)",
        category = "Beverages & Mocktails",
        prepTime = "10 mins",
        yieldPortions = "1 Mug",
        cals = "90 kcal",
        desc = "Warm cranberry juice simmered with star anise, cloves, and cinnamon.",
        story = "A comforting, steaming beverage to warm up chilly evenings.",
        ingredients = listOf("Cranberry Juice", "Star Anise", "Cloves", "Cinnamon Stick", "Orange Peel"),
        steps = listOf("Simmer cranberry juice with spices and orange peel for 10 minutes.", "Strain into a warm mug.")
    ),
    RecipeEntity(
        name = "Yuzu Citrus Smash",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "40 kcal",
        desc = "Tart Japanese yuzu juice mixed with mint and soda.",
        story = "An incredibly bright and acidic refresher.",
        ingredients = listOf("Yuzu Juice", "Mint Leaves", "Simple Syrup", "Soda Water", "Ice"),
        steps = listOf("Muddle mint with simple syrup.", "Add yuzu juice and ice.", "Top with soda water.")
    ),
    RecipeEntity(
        name = "Cold Brew Tonic",
        category = "Beverages & Mocktails",
        prepTime = "2 mins",
        yieldPortions = "1 Glass",
        cals = "20 kcal",
        desc = "Strong cold brew coffee served over ice with premium tonic water.",
        story = "An effervescent, slightly bitter daytime pick-me-up.",
        ingredients = listOf("Cold Brew Coffee", "Tonic Water", "Orange Slice", "Ice"),
        steps = listOf("Fill glass with ice.", "Fill 2/3 with tonic water.", "Gently pour cold brew over the top.", "Garnish with an orange slice.")
    ),
    RecipeEntity(
        name = "Melon Mint Agua Fresca",
        category = "Beverages & Mocktails",
        prepTime = "10 mins",
        yieldPortions = "1 Glass",
        cals = "55 kcal",
        desc = "Blended cantaloupe melon with mint, lime, and water.",
        story = "A classic Mexican-inspired light refreshment.",
        ingredients = listOf("Cantaloupe Melon Chunks", "Mint Leaves", "Lime Juice", "Water", "Agave"),
        steps = listOf("Blend melon, water, mint, lime juice, and a touch of agave.", "Strain if a smoother texture is desired.", "Serve over ice.")
    ),
    RecipeEntity(
        name = "Cherry Vanilla Soda",
        category = "Beverages & Mocktails",
        prepTime = "3 mins",
        yieldPortions = "1 Glass",
        cals = "80 kcal",
        desc = "Tart cherry juice mixed with vanilla syrup and sparkling water.",
        story = "A sophisticated take on the classic cherry soda.",
        ingredients = listOf("Tart Cherry Juice", "Vanilla Syrup", "Sparkling Water", "Ice", "Fresh Cherry"),
        steps = listOf("Add cherry juice and vanilla syrup to a glass.", "Stir in ice.", "Top with sparkling water and garnish with a cherry.")
    ),
    RecipeEntity(
        name = "Green Tea Lemonade",
        category = "Beverages & Mocktails",
        prepTime = "5 mins",
        yieldPortions = "1 Glass",
        cals = "50 kcal",
        desc = "Equal parts brewed green tea and fresh lemonade served over ice.",
        story = "An earthy, citrusy thirst quencher, popular in summer cafes.",
        ingredients = listOf("Brewed Green Tea (Chilled)", "Lemonade", "Ice", "Lemon Slice"),
        steps = listOf("Fill a tall glass with ice.", "Pour equal parts green tea and lemonade.", "Stir and garnish with a lemon slice.")
    )
)

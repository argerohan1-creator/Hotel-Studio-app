package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedDesserts = listOf(
    RecipeEntity(
        name = "Classic Tiramisu",
        category = "Sweets & Confectionery",
        prepTime = "30 mins",
        yieldPortions = "8 Portions",
        cals = "450 kcal",
        desc = "Layers of espresso-soaked ladyfingers and rich mascarpone cream, dusted with cocoa.",
        story = "An Italian 'pick-me-up' that conquered the world's dessert menus.",
        ingredients = listOf("Ladyfingers (Savoiardi)", "Mascarpone Cheese", "Espresso", "Eggs", "Sugar", "Cocoa Powder", "Marsala Wine (Optional)"),
        steps = listOf("Whisk egg yolks and sugar until pale, fold in mascarpone.", "Whip egg whites and gently fold into the mixture.", "Dip ladyfingers in espresso and layer in a dish.", "Spread cream over ladyfingers. Repeat. Dust with cocoa and chill.")
    ),
    RecipeEntity(
        name = "Molten Chocolate Lava Cake",
        category = "Sweets & Confectionery",
        prepTime = "25 mins",
        yieldPortions = "4 Portions",
        cals = "550 kcal",
        desc = "Warm chocolate cake with a rich, flowing liquid chocolate center.",
        story = "The ultimate indulgence, offering a perfect textural contrast of cake and fudge.",
        ingredients = listOf("Dark Chocolate", "Butter", "Eggs", "Sugar", "Flour", "Vanilla Extract"),
        steps = listOf("Melt chocolate and butter together.", "Whisk eggs and sugar, then mix in chocolate.", "Fold in a small amount of flour.", "Bake in ramekins at high heat for 12 minutes until edges are set but center jiggles.")
    ),
    RecipeEntity(
        name = "Classic Crème Brûlée",
        category = "Sweets & Confectionery",
        prepTime = "60 mins",
        yieldPortions = "6 Portions",
        cals = "400 kcal",
        desc = "Silky vanilla bean custard topped with a brittle, caramelized sugar crust.",
        story = "A quintessential French dessert famous for the satisfying 'crack' of its topping.",
        ingredients = listOf("Heavy Cream", "Vanilla Bean", "Egg Yolks", "Granulated Sugar", "Raw Sugar (for topping)"),
        steps = listOf("Heat cream with scraped vanilla bean.", "Temper hot cream into whisked yolks and sugar.", "Pour into ramekins and bake in a water bath.", "Chill, then coat with raw sugar and torch until caramelized.")
    ),
    RecipeEntity(
        name = "New York Style Cheesecake",
        category = "Sweets & Confectionery",
        prepTime = "90 mins",
        yieldPortions = "12 Portions",
        cals = "600 kcal",
        desc = "Dense, rich, and creamy cream cheese filling baked on a graham cracker crust.",
        story = "The pride of NYC bakeries, known for its towering height and dense texture.",
        ingredients = listOf("Cream Cheese", "Graham Cracker Crumbs", "Butter", "Sugar", "Eggs", "Sour Cream", "Vanilla"),
        steps = listOf("Press graham crumb and butter mixture into a springform pan.", "Beat cream cheese and sugar until smooth, add eggs one at a time.", "Mix in sour cream and vanilla.", "Bake in a water bath, then cool slowly.")
    ),
    RecipeEntity(
        name = "Traditional Baklava",
        category = "Sweets & Confectionery",
        prepTime = "60 mins",
        yieldPortions = "16 Portions",
        cals = "350 kcal",
        desc = "Layers of flaky phyllo dough filled with crushed nuts and sweetened with honey syrup.",
        story = "An opulent pastry shared across Middle Eastern and Mediterranean cultures.",
        ingredients = listOf("Phyllo Dough", "Walnuts & Pistachios", "Butter (clarified)", "Sugar", "Honey", "Cinnamon", "Water"),
        steps = listOf("Layer phyllo sheets in a pan, brushing each with butter.", "Add a layer of spiced, chopped nuts in the middle.", "Top with more buttered phyllo sheets and score into diamonds.", "Bake until golden, then pour cold honey syrup over the hot pastry.")
    ),
    RecipeEntity(
        name = "Gulab Jamun",
        category = "Sweets & Confectionery",
        prepTime = "40 mins",
        yieldPortions = "10 Portions",
        cals = "300 kcal",
        desc = "Deep-fried milk solid dumplings soaked in a fragrant cardamom and rose syrup.",
        story = "India's most beloved festival sweet, soft, syrupy, and melt-in-the-mouth.",
        ingredients = listOf("Khoya (Milk Solids)", "All Purpose Flour", "Sugar", "Water", "Cardamom", "Rose Water", "Ghee for frying"),
        steps = listOf("Knead khoya and flour into a soft dough and roll into smooth balls.", "Fry gently in ghee on low heat until dark golden.", "Make a syrup of sugar, water, cardamom, and rose water.", "Soak the warm balls in the hot syrup for a few hours.")
    ),
    RecipeEntity(
        name = "Authentic Churros",
        category = "Sweets & Confectionery",
        prepTime = "30 mins",
        yieldPortions = "4 Portions",
        cals = "400 kcal",
        desc = "Crispy, star-shaped fried dough coated in cinnamon sugar, served with chocolate dip.",
        story = "A Spanish street food staple, perfect for dipping into thick hot chocolate.",
        ingredients = listOf("Water", "Butter", "Flour", "Eggs", "Sugar", "Cinnamon", "Oil for frying", "Dark Chocolate"),
        steps = listOf("Boil water and butter, stir in flour to make a dough.", "Beat eggs into the cooled dough.", "Pipe through a star tip into hot oil and fry until golden.", "Roll in cinnamon sugar and serve with melted chocolate.")
    ),
    RecipeEntity(
        name = "Matcha Mille Crepe Cake",
        category = "Sweets & Confectionery",
        prepTime = "120 mins",
        yieldPortions = "10 Portions",
        cals = "420 kcal",
        desc = "Twenty layers of paper-thin green tea crepes separated by light matcha pastry cream.",
        story = "A modern fusion dessert showcasing incredible delicacy and patience.",
        ingredients = listOf("Flour", "Matcha Powder", "Eggs", "Milk", "Butter", "Heavy Cream", "Powdered Sugar"),
        steps = listOf("Whisk a smooth matcha crepe batter and chill.", "Cook individual, very thin crepes on a non-stick pan.", "Whip heavy cream with sugar and matcha.", "Layer crepes and cream repeatedly. Chill before slicing.")
    ),
    RecipeEntity(
        name = "Banoffee Pie",
        category = "Sweets & Confectionery",
        prepTime = "45 mins",
        yieldPortions = "8 Portions",
        cals = "550 kcal",
        desc = "A buttery biscuit base filled with thick caramel, fresh bananas, and whipped cream.",
        story = "A British invention that perfectly balances fruit, toffee, and cream.",
        ingredients = listOf("Digestive Biscuits", "Butter", "Dulce de Leche (Caramel)", "Bananas", "Heavy Cream", "Chocolate Shavings"),
        steps = listOf("Press crushed biscuits and butter into a pie dish.", "Spread a thick layer of dulce de leche.", "Slice bananas and layer over the caramel.", "Top generously with whipped cream and chocolate shavings.")
    ),
    RecipeEntity(
        name = "Sticky Toffee Pudding",
        category = "Sweets & Confectionery",
        prepTime = "60 mins",
        yieldPortions = "6 Portions",
        cals = "650 kcal",
        desc = "A very moist sponge cake made with finely chopped dates, covered in a toffee sauce.",
        story = "A comforting British classic, best served warm with vanilla custard or ice cream.",
        ingredients = listOf("Medjool Dates", "Baking Soda", "Flour", "Butter", "Brown Sugar", "Eggs", "Heavy Cream"),
        steps = listOf("Soak dates in boiling water and baking soda, then mash.", "Cream butter and sugar, add eggs, flour, and date mixture.", "Bake until set.", "Simmer butter, brown sugar, and cream to make toffee sauce and pour over warm cake.")
    ),
    RecipeEntity(
        name = "Lemon Tart (Tarte au Citron)",
        category = "Sweets & Confectionery",
        prepTime = "90 mins",
        yieldPortions = "8 Portions",
        cals = "380 kcal",
        desc = "A crisp, buttery pastry shell filled with a sharp, sweet lemon curd.",
        story = "A Parisian patisserie favorite showcasing the perfect balance of tart and sweet.",
        ingredients = listOf("Sweet Pastry Dough (Pate Sucree)", "Lemons (Juice & Zest)", "Eggs", "Sugar", "Butter", "Powdered Sugar"),
        steps = listOf("Blind bake the pastry shell until golden.", "Whisk lemon juice, zest, eggs, and sugar over a double boiler until thick.", "Whisk in butter until smooth.", "Pour curd into the shell and chill until set.")
    ),
    RecipeEntity(
        name = "Rasmalai",
        category = "Sweets & Confectionery",
        prepTime = "60 mins",
        yieldPortions = "6 Portions",
        cals = "250 kcal",
        desc = "Flattened paneer discs poached in sugar syrup and served in sweetened, thickened milk.",
        story = "A Bengali delicacy that translates to 'juice' (ras) and 'cream' (malai).",
        ingredients = listOf("Milk (for Paneer/Chenna)", "Lemon Juice", "Sugar", "Full Cream Milk", "Saffron", "Cardamom", "Pistachios"),
        steps = listOf("Curdle milk to make chenna, knead until smooth, and flatten into discs.", "Poach discs in boiling sugar syrup until spongy.", "Boil full cream milk until thickened, flavor with saffron and cardamom.", "Soak the spongy discs in the thickened milk and chill.")
    ),
    RecipeEntity(
        name = "Macarons (Raspberry & Vanilla)",
        category = "Sweets & Confectionery",
        prepTime = "120 mins",
        yieldPortions = "20 Cookies",
        cals = "100 kcal",
        desc = "Delicate almond meringue sandwich cookies with a crisp shell and chewy interior.",
        story = "The crown jewel of French baking, requiring precise technique and timing.",
        ingredients = listOf("Almond Flour", "Powdered Sugar", "Egg Whites", "Granulated Sugar", "Vanilla Bean", "Raspberry Jam", "Buttercream"),
        steps = listOf("Sift almond flour and powdered sugar.", "Whip egg whites and sugar to stiff peaks, then fold in dry ingredients (macaronage).", "Pipe onto trays, let rest to form a skin, then bake.", "Sandwich the cooled shells with buttercream and jam.")
    ),
    RecipeEntity(
        name = "Black Forest Gateau",
        category = "Sweets & Confectionery",
        prepTime = "90 mins",
        yieldPortions = "10 Portions",
        cals = "520 kcal",
        desc = "Chocolate sponge cake layered with whipped cream and Kirsch-soaked cherries.",
        story = "A striking German cake named after the tart cherry liquor (Kirschwasser) used to flavor it.",
        ingredients = listOf("Chocolate Sponge Cake", "Cherries in Syrup", "Kirsch (Cherry Liqueur)", "Heavy Cream", "Powdered Sugar", "Dark Chocolate Shavings"),
        steps = listOf("Slice sponge cake into three layers.", "Brush layers generously with cherry syrup and Kirsch.", "Spread whipped cream and cherries between layers.", "Frost entire cake with cream and coat in chocolate shavings.")
    ),
    RecipeEntity(
        name = "Cannoli",
        category = "Sweets & Confectionery",
        prepTime = "90 mins",
        yieldPortions = "10 Pastries",
        cals = "320 kcal",
        desc = "Crispy, tube-shaped fried pastry dough filled with sweet, creamy ricotta.",
        story = "A staple of Sicilian cuisine, originally prepared as a treat for Carnevale.",
        ingredients = listOf("Flour", "Marsala Wine", "Lard or Butter", "Ricotta Cheese", "Powdered Sugar", "Chocolate Chips", "Pistachios"),
        steps = listOf("Make dough with flour and wine, rest, roll thin, and wrap around cannoli tubes.", "Fry until blistered and golden, then cool.", "Whip drained ricotta with powdered sugar and fold in chocolate chips.", "Pipe filling into shells just before serving to maintain crunch.")
    )
)

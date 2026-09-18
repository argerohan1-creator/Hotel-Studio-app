package com.example.data.repository

import com.example.data.model.RecipeEntity

val extendedMainCourses = listOf(
    RecipeEntity(
        name = "Authentic Butter Chicken (Murgh Makhani)",
        category = "Main Course",
        prepTime = "60 mins",
        yieldPortions = "4 Portions",
        cals = "650 kcal",
        desc = "Tandoor-roasted chicken simmered in a rich, creamy tomato and fenugreek gravy.",
        story = "Born in Delhi, this dish has become the global ambassador of Indian cuisine.",
        ingredients = listOf("Chicken Tikka", "Tomato Puree", "Heavy Cream", "Butter", "Kasuri Methi (Fenugreek)", "Garam Masala", "Ginger Garlic Paste"),
        steps = listOf("Roast marinated chicken in a tandoor or oven.", "Simmer tomato puree with spices and butter.", "Add chicken to the gravy.", "Finish with cream and crushed kasuri methi.")
    ),
    RecipeEntity(
        name = "Classic Beef Wellington",
        category = "Main Course",
        prepTime = "120 mins",
        yieldPortions = "6 Portions",
        cals = "850 kcal",
        desc = "Prime beef tenderloin coated with mushroom duxelles, wrapped in prosciutto and puff pastry.",
        story = "A show-stopping British centerpiece demanding high culinary precision.",
        ingredients = listOf("Beef Tenderloin", "Puff Pastry", "Mushrooms (Duxelles)", "Prosciutto", "English Mustard", "Egg Wash"),
        steps = listOf("Sear beef tenderloin and brush with mustard.", "Spread duxelles over prosciutto and wrap the beef tightly.", "Chill, then wrap in puff pastry.", "Bake until pastry is golden and beef is medium-rare.")
    ),
    RecipeEntity(
        name = "Pan-Seared Salmon with Lemon Caper Butter",
        category = "Main Course",
        prepTime = "20 mins",
        yieldPortions = "2 Portions",
        cals = "480 kcal",
        desc = "Crispy-skinned salmon fillets topped with a bright, tangy butter sauce.",
        story = "A quintessential seafood dish that balances rich fish with acidic accents.",
        ingredients = listOf("Salmon Fillets", "Butter", "Capers", "Lemon Juice", "White Wine", "Parsley"),
        steps = listOf("Sear salmon skin-side down until crispy.", "Flip and cook to desired doneness, then remove.", "Deglaze pan with wine, add lemon juice and capers.", "Mount with butter and pour over salmon.")
    ),
    RecipeEntity(
        name = "Pad Thai",
        category = "Main Course",
        prepTime = "30 mins",
        yieldPortions = "2 Portions",
        cals = "550 kcal",
        desc = "Stir-fried rice noodles with egg, tofu, shrimp, and a sweet tamarind sauce.",
        story = "Thailand's national dish, characterized by its symphony of sweet, sour, and salty.",
        ingredients = listOf("Rice Noodles", "Shrimp", "Firm Tofu", "Eggs", "Bean Sprouts", "Tamarind Paste", "Peanuts", "Fish Sauce"),
        steps = listOf("Soak noodles until pliable.", "Stir fry tofu, shrimp, and eggs.", "Add noodles and tamarind-fish sauce mixture.", "Toss with bean sprouts and garnish with crushed peanuts.")
    ),
    RecipeEntity(
        name = "Vegetable Dum Biryani",
        category = "Main Course",
        prepTime = "90 mins",
        yieldPortions = "6 Portions",
        cals = "420 kcal",
        desc = "Fragrant basmati rice layered with spiced mixed vegetables, cooked in a sealed pot.",
        story = "A royal Mughlai delicacy requiring patience and perfect spice blending.",
        ingredients = listOf("Basmati Rice", "Mixed Vegetables", "Biryani Masala", "Yogurt", "Saffron Milk", "Fried Onions (Birista)", "Mint & Coriander"),
        steps = listOf("Parboil rice with whole spices.", "Marinate and cook vegetables.", "Layer vegetables, rice, herbs, and saffron milk in a heavy pot.", "Seal with dough (dum) and cook on very low heat.")
    ),
    RecipeEntity(
        name = "Spaghetti Carbonara",
        category = "Main Course",
        prepTime = "20 mins",
        yieldPortions = "2 Portions",
        cals = "620 kcal",
        desc = "Traditional Roman pasta tossed with egg, Pecorino Romano, guanciale, and black pepper.",
        story = "An Italian masterpiece built on the emulsion of fat, starch, and cheese.",
        ingredients = listOf("Spaghetti", "Guanciale", "Pecorino Romano", "Eggs", "Black Pepper"),
        steps = listOf("Boil pasta in salted water.", "Crisp guanciale in a pan.", "Whisk eggs and cheese together.", "Toss hot pasta with guanciale and egg mixture off the heat to create a creamy sauce.")
    ),
    RecipeEntity(
        name = "Eggplant Parmesan (Melanzane alla Parmigiana)",
        category = "Main Course",
        prepTime = "60 mins",
        yieldPortions = "4 Portions",
        cals = "450 kcal",
        desc = "Baked layers of breaded eggplant, rich marinara, mozzarella, and parmesan.",
        story = "A hearty vegetarian Italian classic that satisfies like a meat dish.",
        ingredients = listOf("Eggplants", "Marinara Sauce", "Mozzarella Cheese", "Parmesan", "Breadcrumbs", "Fresh Basil"),
        steps = listOf("Slice, salt, and bread eggplant slices.", "Fry or bake until golden.", "Layer in a baking dish with sauce and cheeses.", "Bake until bubbly and golden on top.")
    ),
    RecipeEntity(
        name = "Miso Black Cod",
        category = "Main Course",
        prepTime = "72 hours (marination)",
        yieldPortions = "4 Portions",
        cals = "380 kcal",
        desc = "Silky black cod fillets marinated in a sweet and savory miso glaze.",
        story = "Popularized by Chef Nobu Matsuhisa, it's a modern Japanese fine-dining icon.",
        ingredients = listOf("Black Cod Fillets", "White Miso Paste", "Mirin", "Sake", "Sugar"),
        steps = listOf("Boil sake and mirin, whisk in miso and sugar.", "Cool marinade and coat fish completely.", "Marinate in fridge for 2-3 days.", "Wipe off excess miso and broil until caramelized and flaky.")
    ),
    RecipeEntity(
        name = "Coq au Vin",
        category = "Main Course",
        prepTime = "120 mins",
        yieldPortions = "4 Portions",
        cals = "550 kcal",
        desc = "Chicken braised with wine, lardons, mushrooms, and pearl onions.",
        story = "A rustic French dish demonstrating the magic of slow braising.",
        ingredients = listOf("Chicken Pieces", "Red Burgundy Wine", "Bacon Lardons", "Pearl Onions", "Cremini Mushrooms", "Chicken Stock", "Bouquet Garni"),
        steps = listOf("Brown bacon, then brown chicken in the fat.", "Saute vegetables.", "Add wine and stock, bring to a simmer.", "Braise slowly until chicken is tender.")
    ),
    RecipeEntity(
        name = "Peking Duck",
        category = "Main Course",
        prepTime = "24 hours",
        yieldPortions = "4 Portions",
        cals = "800 kcal",
        desc = "Roast duck with exquisitely crispy skin, served with pancakes, hoisin, and scallions.",
        story = "An imperial dish from Beijing, famous for its glass-like skin.",
        ingredients = listOf("Whole Duck", "Maltose Syrup", "Soy Sauce", "Five Spice", "Mandarin Pancakes", "Hoisin Sauce", "Scallions & Cucumber"),
        steps = listOf("Separate duck skin from meat using air.", "Glaze with maltose syrup and air-dry overnight.", "Roast until skin is crispy.", "Carve skin and meat, serve with pancakes and condiments.")
    ),
    RecipeEntity(
        name = "Tacos al Pastor",
        category = "Main Course",
        prepTime = "4 hours",
        yieldPortions = "4 Portions",
        cals = "480 kcal",
        desc = "Marinated pork roasted on a vertical spit, served with pineapple and onions.",
        story = "A beautiful fusion of Lebanese shawarma techniques and Mexican flavors.",
        ingredients = listOf("Pork Shoulder", "Achiote Paste", "Guajillo Chilis", "Pineapple", "Corn Tortillas", "Cilantro & Onion", "Lime"),
        steps = listOf("Marinate pork in achiote and chili adobo.", "Stack on a skewer with pineapple and roast.", "Slice thinly onto warm corn tortillas.", "Garnish with pineapple, onion, cilantro, and lime.")
    ),
    RecipeEntity(
        name = "Filet Mignon with Peppercorn Sauce",
        category = "Main Course",
        prepTime = "30 mins",
        yieldPortions = "2 Portions",
        cals = "600 kcal",
        desc = "Tender center-cut beef filet, pan-seared and topped with a creamy cognac peppercorn sauce.",
        story = "The steakhouse standard for luxury and tenderness.",
        ingredients = listOf("Filet Mignon Steaks", "Whole Black Peppercorns", "Cognac", "Heavy Cream", "Beef Demi-Glace", "Butter"),
        steps = listOf("Crust steaks with cracked peppercorns.", "Sear to medium-rare in a hot skillet.", "Deglaze with cognac, add demi-glace and cream.", "Simmer until sauce coats the back of a spoon.")
    ),
    RecipeEntity(
        name = "Chicken Parmesan",
        category = "Main Course",
        prepTime = "45 mins",
        yieldPortions = "4 Portions",
        cals = "700 kcal",
        desc = "Breaded and fried chicken cutlets smothered in marinara and melted cheese.",
        story = "An Italian-American classic, beloved for its comforting crunch and gooey cheese.",
        ingredients = listOf("Chicken Breasts", "Breadcrumbs", "Eggs", "Marinara Sauce", "Mozzarella", "Parmesan", "Basil"),
        steps = listOf("Pound chicken thin, bread, and pan-fry.", "Top with marinara and cheeses.", "Broil until cheese is bubbling.", "Serve alongside pasta or a crisp salad.")
    ),
    RecipeEntity(
        name = "Pan-Seared Scallops",
        category = "Main Course",
        prepTime = "15 mins",
        yieldPortions = "2 Portions",
        cals = "350 kcal",
        desc = "Jumbo sea scallops seared to a golden crust, served over pea puree.",
        story = "A delicate and elegant seafood dish requiring exact timing.",
        ingredients = listOf("Sea Scallops", "Butter", "Olive Oil", "Green Peas", "Cream", "Lemon", "Microgreens"),
        steps = listOf("Pat scallops thoroughly dry.", "Sear in a screaming hot pan with oil until crust forms (1 min).", "Flip, baste with butter, and remove.", "Serve over a warm, blended pea puree.")
    ),
    RecipeEntity(
        name = "Vegetable Lasagna",
        category = "Main Course",
        prepTime = "90 mins",
        yieldPortions = "8 Portions",
        cals = "400 kcal",
        desc = "Layers of pasta, roasted vegetables, ricotta, and marinara, baked to perfection.",
        story = "A hearty, crowd-pleasing vegetarian centerpiece.",
        ingredients = listOf("Lasagna Noodles", "Zucchini & Bell Peppers", "Spinach", "Ricotta Cheese", "Mozzarella", "Marinara Sauce", "Garlic"),
        steps = listOf("Roast the vegetables.", "Mix ricotta with spinach and garlic.", "Layer noodles, ricotta, veggies, sauce, and mozzarella in a dish.", "Bake covered, then uncover to brown the cheese.")
    )
)

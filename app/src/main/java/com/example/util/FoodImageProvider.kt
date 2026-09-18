package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

data class FoodPhotoItem(
    val id: String,
    val title: String,
    val category: String,
    @DrawableRes val drawableRes: Int,
    val emoji: String,
    val searchKeywords: List<String>
)

data class CounterDemandOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val defaultStationTag: String,
    val sampleDishes: List<String>,
    val defaultDietary: String,
    val recommendedTheme: FoodVisualTheme,
    val accentColorHex: String
)

data class DishPhotoResult(
    val bitmap: Bitmap,
    val sourceDescription: String,
    val dishName: String,
    val matchedDemand: String,
    val isAiGenerated: Boolean = false
)

object FoodImageProvider {

    // In-memory cache for fast reuse without disk/decoding overhead
    private val bitmapCache = mutableMapOf<Int, Bitmap>()
    private val onlineBitmapCache = mutableMapOf<String, Bitmap>()

    val CURATED_FOOD_PHOTOS = listOf(
        FoodPhotoItem(
            id = "dosa",
            title = "Crispy South Indian Dosa & Idli",
            category = "South Indian & Dosa",
            drawableRes = R.drawable.food_dosa,
            emoji = "🥞",
            searchKeywords = listOf("dosa", "masala dosa", "mysore masala", "rava dosa", "idli", "idly", "vada", "medu vada", "appam", "uttapam", "upma", "south indian", "ghee roast")
        ),
        FoodPhotoItem(
            id = "curry",
            title = "Royal Paneer & Indian Gravy Curries",
            category = "Indian Curries & Dal",
            drawableRes = R.drawable.food_curry,
            emoji = "🍲",
            searchKeywords = listOf("curry", "paneer", "paneer butter masala", "shahi paneer", "dal makhani", "dal tadka", "palak paneer", "korma", "rogan josh", "butter chicken", "malai kofta", "chole", "rajma")
        ),
        FoodPhotoItem(
            id = "biryani",
            title = "Royal Dum Biryani & Spiced Rice",
            category = "Biryani & Rice Specialties",
            drawableRes = R.drawable.food_biryani,
            emoji = "🥘",
            searchKeywords = listOf("biryani", "rice", "pulao", "dum biryani", "hyderabadi biryani", "subz biryani", "veg biryani", "chicken biryani", "mutton biryani", "jeera rice", "pulao", "saffron rice")
        ),
        FoodPhotoItem(
            id = "kebab",
            title = "Charcoal Grill & Tandoori Skewers",
            category = "Grill & Kebab",
            drawableRes = R.drawable.food_kebab,
            emoji = "🍢",
            searchKeywords = listOf("kebab", "kabab", "tikka", "paneer tikka", "chicken tikka", "tandoori", "tandoori chicken", "seekh", "grill", "skewer", "bbq", "barbecue", "roast", "shawarma")
        ),
        FoodPhotoItem(
            id = "beverage",
            title = "Vibrant Chilled Mocktails & Coolers",
            category = "Beverages & Mocktails",
            drawableRes = R.drawable.food_beverage,
            emoji = "🍹",
            searchKeywords = listOf("beverage", "mocktail", "mojito", "virgin mojito", "blue lagoon", "cooler", "juice", "fresh juice", "shake", "milkshake", "smoothie", "lassi", "fresh lime soda", "drink")
        ),
        FoodPhotoItem(
            id = "coffee",
            title = "Specialty Espresso & Artisan Brews",
            category = "Coffee & Hot Brews",
            drawableRes = R.drawable.food_coffee,
            emoji = "☕",
            searchKeywords = listOf("coffee", "espresso", "latte", "cappuccino", "macchiato", "cold brew", "tea", "chai", "masala chai", "barista", "brew")
        ),
        FoodPhotoItem(
            id = "burger",
            title = "Artisanal Cheeseburgers & Loaded Fries",
            category = "Burgers & Sandwiches",
            drawableRes = R.drawable.food_burger,
            emoji = "🍔",
            searchKeywords = listOf("burger", "cheeseburger", "chicken burger", "sandwich", "club sandwich", "fries", "french fries", "slider", "sliders", "panini", "loaded fries")
        ),
        FoodPhotoItem(
            id = "noodles",
            title = "Wok-Tossed Hakka Noodles & Asian Bowls",
            category = "Asian Noodles & Wok",
            drawableRes = R.drawable.food_noodles,
            emoji = "🍜",
            searchKeywords = listOf("noodle", "noodles", "hakka noodles", "ramen", "chow mein", "pad thai", "fried rice", "stir fry", "schezwan noodles", "asian wok")
        ),
        FoodPhotoItem(
            id = "dim_sum",
            title = "Steamed Dim Sum & Dumplings",
            category = "Dim Sum & Asian Dumplings",
            drawableRes = R.drawable.food_dim_sum,
            emoji = "🥟",
            searchKeywords = listOf("dim sum", "dimsum", "dumpling", "dumplings", "bao", "momos", "asian", "wonton", "gyoza", "steamed", "spring roll")
        ),
        FoodPhotoItem(
            id = "pizza",
            title = "Wood-Fired Artisanal Pizza",
            category = "Italian Pizza",
            drawableRes = R.drawable.food_pizza,
            emoji = "🍕",
            searchKeywords = listOf("pizza", "margherita", "wood fired", "calzone", "pepperoni", "slice", "crust", "neapolitan", "pizza hearth")
        ),
        FoodPhotoItem(
            id = "pasta",
            title = "Handcrafted Truffle Pasta",
            category = "Italian Pasta",
            drawableRes = R.drawable.food_pasta,
            emoji = "🍝",
            searchKeywords = listOf("pasta", "spaghetti", "fettuccine", "lasagna", "penne", "macaroni", "ravioli", "carbonara", "alfredo", "italian")
        ),
        FoodPhotoItem(
            id = "sushi",
            title = "Japanese Sushi & Sashimi",
            category = "Japanese & Oriental",
            drawableRes = R.drawable.food_sushi,
            emoji = "🍣",
            searchKeywords = listOf("sushi", "japanese", "sashimi", "maki", "roll", "nigiri", "tempura", "salmon", "tuna", "asian bar", "seafood", "prawn")
        ),
        FoodPhotoItem(
            id = "soup",
            title = "Gourmet Soups & Warm Velouté",
            category = "Soups & Starters",
            drawableRes = R.drawable.food_soup,
            emoji = "🥣",
            searchKeywords = listOf("soup", "broth", "tomato soup", "minestrone", "sweet corn soup", "hot and sour", "mushroom soup", "bisque", "chowder")
        ),
        FoodPhotoItem(
            id = "salad",
            title = "Fresh Organic Salad Bar",
            category = "Salads & Wellness",
            drawableRes = R.drawable.food_salad,
            emoji = "🥗",
            searchKeywords = listOf("salad", "healthy", "greens", "caesar", "greek salad", "organic", "raw", "slaw", "vegan", "wellness", "dietary", "salad bar")
        ),
        FoodPhotoItem(
            id = "chaat",
            title = "Artisanal Street Chaat Counter",
            category = "Indian Live Street",
            drawableRes = R.drawable.food_chaat,
            emoji = "🥣",
            searchKeywords = listOf("chaat", "pani puri", "golgappa", "sev", "bhel", "samosa", "kachori", "dahi puri", "street", "chaat station", "street food", "vada pav", "pav bhaji")
        ),
        FoodPhotoItem(
            id = "dessert",
            title = "Gourmet Patisserie & Sweets",
            category = "Desserts & Bakery",
            drawableRes = R.drawable.food_dessert,
            emoji = "🍰",
            searchKeywords = listOf("dessert", "sweet", "sweets", "kulfi", "cake", "pastry", "pastries", "mithai", "ice cream", "gulab jamun", "rasmalai", "tiramisu", "chocolate", "brownie")
        ),
        FoodPhotoItem(
            id = "breakfast",
            title = "Artisanal Morning Eggs & Breakfast",
            category = "Breakfast & Eggs",
            drawableRes = R.drawable.food_breakfast,
            emoji = "🍳",
            searchKeywords = listOf("egg", "eggs", "omelette", "omelet", "scrambled eggs", "eggs benedict", "poached egg", "sunny side up", "breakfast platter")
        ),
        FoodPhotoItem(
            id = "bakery",
            title = "Artisanal Breads & Viennoiserie",
            category = "Bakery & Breads",
            drawableRes = R.drawable.food_bakery,
            emoji = "🥐",
            searchKeywords = listOf("bakery", "bread", "croissant", "waffle", "waffles", "pancake", "pancakes", "toast", "bagel", "naan", "roti", "paratha")
        ),
        FoodPhotoItem(
            id = "mexican",
            title = "Sizzling Mexican Fajitas & Tacos",
            category = "Global Street Food",
            drawableRes = R.drawable.food_mexican,
            emoji = "🌮",
            searchKeywords = listOf("mexican", "wrap", "taco", "tacos", "fajita", "burrito", "quesadilla", "nachos", "tortilla")
        )
    )

    /**
     * Professional counter demand presets for hotel buffets and fine-dining operations.
     */
    val COUNTER_DEMAND_OPTIONS = listOf(
        CounterDemandOption(
            id = "live_tandoor",
            title = "Live Tandoor & Grill",
            subtitle = "Charcoal Skewers, Tikkas & Fresh Naans",
            iconEmoji = "🍢",
            defaultStationTag = "GRILL & TANDOOR",
            sampleDishes = listOf("Paneer Tikka", "Tandoori Murgh", "Seekh Kebab", "Amritsari Fish", "Butter Naan"),
            defaultDietary = "Chef's Live Selection",
            recommendedTheme = FoodVisualTheme.CHARCOAL_MATTE,
            accentColorHex = "#E05A2B"
        ),
        CounterDemandOption(
            id = "live_dosa",
            title = "Live Dosa & South Indian",
            subtitle = "Crispy Dosas, Steamed Idlis & Appams",
            iconEmoji = "🥞",
            defaultStationTag = "DOSA STATION",
            sampleDishes = listOf("Masala Dosa", "Mysore Masala Dosa", "Medu Vada", "Appam & Stew", "Ghee Roast"),
            defaultDietary = "🟢 100% Pure Veg",
            recommendedTheme = FoodVisualTheme.BRONZE_TEXTURE,
            accentColorHex = "#C67D26"
        ),
        CounterDemandOption(
            id = "live_pasta_pizza",
            title = "Live Pasta & Pizza Hearth",
            subtitle = "Handcrafted Pastas & Wood-Fired Slices",
            iconEmoji = "🍝",
            defaultStationTag = "PASTA & PIZZA",
            sampleDishes = listOf("Truffle Penne", "Wood-Fired Margherita", "Fettuccine Alfredo", "Ravioli Pomodoro"),
            defaultDietary = "Chef's Live Selection",
            recommendedTheme = FoodVisualTheme.BRUSHED_GOLD,
            accentColorHex = "#D4A017"
        ),
        CounterDemandOption(
            id = "live_chaat",
            title = "Live Street Chaat",
            subtitle = "Zesty Pani Puri, Dahi Puri & Samosas",
            iconEmoji = "🥣",
            defaultStationTag = "CHAAT COUNTER",
            sampleDishes = listOf("Pani Puri", "Dahi Puri", "Sev Papdi Chaat", "Samosa Chaat", "Raj Kachori"),
            defaultDietary = "🟢 Pure Sattvic Veg",
            recommendedTheme = FoodVisualTheme.EMERALD_VELVET,
            accentColorHex = "#2E7D32"
        ),
        CounterDemandOption(
            id = "live_dim_sum",
            title = "Live Dim Sum & Asian Wok",
            subtitle = "Steamed Dim Sum Baskets & Wok Tossed Bowls",
            iconEmoji = "🥟",
            defaultStationTag = "DIM SUM BAR",
            sampleDishes = listOf("Steamed Dim Sum", "Crystal Dumplings", "Bao Buns", "Hakka Noodles", "Chicken Gyoza"),
            defaultDietary = "Artisanal Specialty",
            recommendedTheme = FoodVisualTheme.MIDNIGHT_OBSIDIAN,
            accentColorHex = "#C59B27"
        ),
        CounterDemandOption(
            id = "live_egg_breakfast",
            title = "Live Egg & Morning Grill",
            subtitle = "Fluffy Omelettes, Eggs Benedict & Poached Eggs",
            iconEmoji = "🍳",
            defaultStationTag = "EGG STATION",
            sampleDishes = listOf("Cheese Omelette", "Eggs Benedict", "Sunny Side Up", "Scrambled Sourdough"),
            defaultDietary = "Chef's Morning Live",
            recommendedTheme = FoodVisualTheme.PEARL_GLAZE,
            accentColorHex = "#D97706"
        ),
        CounterDemandOption(
            id = "live_waffles",
            title = "Live Waffles & Crepes",
            subtitle = "Belgian Waffles, Pancakes & Nutella Crepes",
            iconEmoji = "🧇",
            defaultStationTag = "WAFFLE CORNER",
            sampleDishes = listOf("Belgian Waffles", "Maple Pancakes", "Nutella Banana Crepes", "French Toast"),
            defaultDietary = "Artisanal Sweets",
            recommendedTheme = FoodVisualTheme.ROSE_GOLD,
            accentColorHex = "#B45309"
        ),
        CounterDemandOption(
            id = "chafing_veg",
            title = "Chafing Line (Veg Mains)",
            subtitle = "Dal Makhani, Shahi Paneer & Subz Biryani",
            iconEmoji = "🍲",
            defaultStationTag = "VEG MAIN COURSE",
            sampleDishes = listOf("Dal Makhani", "Paneer Butter Masala", "Subz Dum Biryani", "Palak Paneer", "Malai Kofta"),
            defaultDietary = "🟢 100% Pure Veg",
            recommendedTheme = FoodVisualTheme.IVORY_SILK,
            accentColorHex = "#15803D"
        ),
        CounterDemandOption(
            id = "chafing_non_veg",
            title = "Chafing Line (Non-Veg Mains)",
            subtitle = "Butter Chicken, Rogan Josh & Murgh Biryani",
            iconEmoji = "🥘",
            defaultStationTag = "NON-VEG MAINS",
            sampleDishes = listOf("Butter Chicken", "Mutton Rogan Josh", "Royal Murgh Biryani", "Goan Fish Curry"),
            defaultDietary = "🔴 Non-Vegetarian",
            recommendedTheme = FoodVisualTheme.ROYAL_BURGUNDY,
            accentColorHex = "#B91C1C"
        ),
        CounterDemandOption(
            id = "salad_wellness",
            title = "Salad & Cold Appetizers",
            subtitle = "Farm Fresh Greens, Caesar, Greek Feta & Mezze",
            iconEmoji = "🥗",
            defaultStationTag = "HEALTHY SALAD BAR",
            sampleDishes = listOf("Caesar Salad", "Greek Feta Salad", "Hummus & Warm Pita", "Organic Quinoa Bowl"),
            defaultDietary = "🌿 Wellness & Vegan",
            recommendedTheme = FoodVisualTheme.SAPPHIRE_GRADIENT,
            accentColorHex = "#0284C7"
        ),
        CounterDemandOption(
            id = "dessert_patisserie",
            title = "Dessert & Patisserie Island",
            subtitle = "Artisanal Sweets, Tiramisu, Lava Cakes & Mithai",
            iconEmoji = "🍰",
            defaultStationTag = "SWEET ISLAND",
            sampleDishes = listOf("Gulab Jamun", "Rasmalai", "Chocolate Lava Cake", "Tiramisu Cup", "Saffron Kulfi"),
            defaultDietary = "✨ Sweet Delights",
            recommendedTheme = FoodVisualTheme.CHAMPAGNE_SPARKLE,
            accentColorHex = "#D97706"
        ),
        CounterDemandOption(
            id = "beverage_bar",
            title = "Beverage & Mocktail Bar",
            subtitle = "Virgin Mojitos, Artisan Teas & Espresso Brews",
            iconEmoji = "🍹",
            defaultStationTag = "BEVERAGE BAR",
            sampleDishes = listOf("Virgin Mint Mojito", "Blue Lagoon Cooler", "Masala Chai", "Espresso Macchiato"),
            defaultDietary = "Chilled & Refreshing",
            recommendedTheme = FoodVisualTheme.FROSTED_GLASS,
            accentColorHex = "#0D9488"
        )
    )

    // Online extended catalog for popular international & Indian dishes
    private val ONLINE_FOOD_EXTENSIONS = mapOf(
        "burger" to "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=900&q=80",
        "cheeseburger" to "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=900&q=80",
        "ramen" to "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=900&q=80",
        "noodles" to "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=900&q=80",
        "steak" to "https://images.unsplash.com/photo-1600891964092-4316c288032e?w=900&q=80",
        "beef" to "https://images.unsplash.com/photo-1600891964092-4316c288032e?w=900&q=80",
        "seafood" to "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?w=900&q=80",
        "lobster" to "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?w=900&q=80",
        "salmon" to "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?w=900&q=80",
        "waffles" to "https://images.unsplash.com/photo-1562376552-0d160a2f238d?w=900&q=80",
        "pancakes" to "https://images.unsplash.com/photo-1528207776546-365bb710ee93?w=900&q=80",
        "shawarma" to "https://images.unsplash.com/photo-1529006557810-274b9b2fc783?w=900&q=80",
        "cocktail" to "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=900&q=80",
        "mojito" to "https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=900&q=80",
        "butter chicken" to "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=900&q=80",
        "paneer" to "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=900&q=80",
        "dosa" to "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=900&q=80",
        "idli" to "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=900&q=80",
        "samosa" to "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=900&q=80",
        "biryani" to "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=900&q=80",
        "curry" to "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=900&q=80",
        "tacos" to "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=900&q=80",
        "sushi" to "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=900&q=80",
        "dim sum" to "https://images.unsplash.com/photo-1496116218417-1a781b1c416c?w=900&q=80",
        "dessert" to "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=900&q=80",
        "ice cream" to "https://images.unsplash.com/photo-1560008511-11c63416e52d?w=900&q=80",
        "tiramisu" to "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=900&q=80",
        "coffee" to "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=900&q=80"
    )

    /**
     * Resolves the user-facing title for a drawable resource.
     */
    fun getFoodTitleForDrawable(@DrawableRes resId: Int): String {
        return CURATED_FOOD_PHOTOS.firstOrNull { it.drawableRes == resId }?.title ?: "Authentic Culinary Plate"
    }

    /**
     * Matches a dish name directly against culinary categories with highest precedence.
     * Never confuses side counter demand with the actual dish name.
     */
    fun matchDishNameToDrawable(dishName: String): Int? {
        val clean = dishName.lowercase().trim()
        if (clean.isBlank()) return null

        // 1. Dosa & South Indian Specialties (Crisp Dosas, Idlis, Vadas, Appams)
        if (clean.contains("dosa") || clean.contains("masala dosa") || clean.contains("mysore masala") ||
            clean.contains("rava dosa") || clean.contains("ghee roast") || clean.contains("idli") ||
            clean.contains("idly") || clean.contains("medu vada") || clean.contains("sambar vada") ||
            clean.contains("vada") || clean.contains("appam") || clean.contains("uttapam") ||
            clean.contains("upma") || clean.contains("pongal") || clean.contains("paniyaram") ||
            clean.contains("pesrattu") || clean.contains("south indian")) {
            return R.drawable.food_dosa
        }

        // 2. Refreshing Chilled Mocktails, Juices, Shakes & Coolers
        if (clean.contains("mojito") || clean.contains("virgin mojito") || clean.contains("blue lagoon") ||
            clean.contains("cooler") || clean.contains("lemonade") || clean.contains("fresh lime") ||
            clean.contains("mocktail") || clean.contains("cocktail") || clean.contains("punch") ||
            clean.contains("spritz") || clean.contains("iced tea") || clean.contains("smoothie") ||
            clean.contains("shake") || clean.contains("milkshake") || clean.contains("lassi") ||
            clean.contains("chaas") || clean.contains("buttermilk") || clean.contains("juice") ||
            clean.contains("orange juice") || clean.contains("fresh juice") || clean.contains("sharbat") ||
            clean.contains("cold drink")) {
            return R.drawable.food_beverage
        }

        // 3. Breakfast & Morning Eggs
        if (clean.contains("omelette") || clean.contains("omelet") || clean.contains("cheese omelette") ||
            clean.contains("scrambled egg") || clean.contains("scrambled eggs") || clean.contains("poached egg") ||
            clean.contains("eggs benedict") || clean.contains("sunny side up") || clean.contains("boiled egg") ||
            clean.contains("egg bhurji") || clean.contains("breakfast platter")) {
            return R.drawable.food_breakfast
        }

        // 4. Burgers, Sandwiches & Crispy Fries
        if (clean.contains("burger") || clean.contains("cheeseburger") || clean.contains("chicken burger") ||
            clean.contains("veggie burger") || clean.contains("hamburger") || clean.contains("slider") ||
            clean.contains("sliders") || clean.contains("sandwich") || clean.contains("club sandwich") ||
            clean.contains("grilled sandwich") || clean.contains("panini") || clean.contains("fries") ||
            clean.contains("french fries") || clean.contains("loaded fries") || clean.contains("potato wedges") ||
            clean.contains("onion rings") || clean.contains("hot dog")) {
            return R.drawable.food_burger
        }

        // 5. Asian Noodles, Stir-Fry & Ramen
        if (clean.contains("hakka noodle") || clean.contains("hakka noodles") || clean.contains("noodle") ||
            clean.contains("noodles") || clean.contains("ramen") || clean.contains("chow mein") ||
            clean.contains("pad thai") || clean.contains("lo mein") || clean.contains("soba") ||
            clean.contains("udon") || clean.contains("stir fry") || clean.contains("stir-fry") ||
            clean.contains("schezwan noodle") || clean.contains("schezwan noodles") || clean.contains("khao suey")) {
            return R.drawable.food_noodles
        }

        // 6. Gourmet Soups & Warm Broths
        if (clean.contains("soup") || clean.contains("broth") || clean.contains("tomato soup") ||
            clean.contains("minestrone") || clean.contains("cream of mushroom") || clean.contains("sweet corn soup") ||
            clean.contains("hot and sour") || clean.contains("manchow") || clean.contains("bisque") ||
            clean.contains("chowder") || clean.contains("veloute") || clean.contains("french onion") ||
            clean.contains("clear soup")) {
            return R.drawable.food_soup
        }

        // 7. Biryani, Pulao & Saffron Spiced Rice (Check before general gravies)
        if (clean.contains("biryani") || clean.contains("dum biryani") || clean.contains("hyderabadi biryani") ||
            clean.contains("awadhi biryani") || clean.contains("subz biryani") || clean.contains("veg biryani") ||
            clean.contains("chicken biryani") || clean.contains("mutton biryani") || clean.contains("murgh biryani") ||
            clean.contains("gosht biryani") || clean.contains("pulao") || clean.contains("kashmiri pulao") ||
            clean.contains("jeera rice") || clean.contains("peas pulao") || clean.contains("saffron rice") ||
            clean.contains("khichdi") || (clean.contains("rice") && !clean.contains("fried rice"))) {
            return R.drawable.food_biryani
        }

        // 8. Tandoor, Charcoal Grills, Skewers & Kebabs
        if (clean.contains("paneer tikka") || clean.contains("chicken tikka") || clean.contains("malai tikka") ||
            clean.contains("seekh kebab") || clean.contains("tandoori murgh") || clean.contains("tandoori chicken") ||
            clean.contains("tandoor") || clean.contains("tandoori") || clean.contains("amritsari fish tikka") ||
            clean.contains("galouti") || clean.contains("shami kebab") || clean.contains("boti kebab") ||
            clean.contains("charcoal grill") || clean.contains("bbq") || clean.contains("barbecue") ||
            clean.contains("skewer") || clean.contains("grilled chicken") || clean.contains("steak") ||
            clean.contains("roast chicken") || clean.contains("roast beef") || clean.contains("kebab") ||
            clean.contains("kabab") || clean.contains("shawarma")) {
            return R.drawable.food_kebab
        }

        // 9. Indian Curries, Dal, Paneer Gravies & Rich Mains
        if (clean.contains("paneer butter") || clean.contains("shahi paneer") || clean.contains("palak paneer") ||
            clean.contains("kadai paneer") || clean.contains("kadhai paneer") || clean.contains("matar paneer") ||
            clean.contains("paneer lababdar") || clean.contains("paneer tikka masala") || clean.contains("paneer bhurji") ||
            clean.contains("paneer pasanda") || clean.contains("paneer") || clean.contains("dal makhani") ||
            clean.contains("dal tadka") || clean.contains("yellow dal") || clean.contains("dal fry") ||
            clean.contains("dal") || clean.contains("chana masala") || clean.contains("chole") ||
            clean.contains("rajma") || clean.contains("malai kofta") || clean.contains("subz handi") ||
            clean.contains("subz korma") || clean.contains("dum aloo") || clean.contains("baingan bharta") ||
            clean.contains("aloo gobi") || clean.contains("bhindi do pyaza") || clean.contains("makhani") ||
            clean.contains("korma") || clean.contains("butter chicken") || clean.contains("murgh makhani") ||
            clean.contains("chicken tikka masala") || clean.contains("chicken curry") || clean.contains("mutton curry") ||
            clean.contains("rogan josh") || clean.contains("bhuna gosht") || clean.contains("fish curry") ||
            clean.contains("goan fish") || clean.contains("amritsari fish") || clean.contains("prawn curry") ||
            clean.contains("egg curry") || clean.contains("curry") || clean.contains("gravy") ||
            clean.contains("handi") || clean.contains("masala") || clean.contains("saag")) {
            return R.drawable.food_curry
        }

        // 10. Street Chaat
        if (clean.contains("pani puri") || clean.contains("panipuri") || clean.contains("golgappa") ||
            clean.contains("puchka") || clean.contains("dahi puri") || clean.contains("sev puri") ||
            clean.contains("papdi chaat") || clean.contains("samosa chaat") || clean.contains("bhel puri") ||
            clean.contains("bhel") || clean.contains("raj kachori") || clean.contains("vada pav") ||
            clean.contains("pav bhaji") || clean.contains("dahi bhalla") || clean.contains("aloo tikki") ||
            clean.contains("samosa") || clean.contains("kachori") || clean.contains("chaat") ||
            clean.contains("pakora") || clean.contains("bhaji")) {
            return R.drawable.food_chaat
        }

        // 11. Pizza
        if (clean.contains("margherita") || clean.contains("pepperoni pizza") || clean.contains("wood-fired pizza") ||
            clean.contains("wood fired pizza") || clean.contains("four cheese pizza") || clean.contains("quattro formaggi") ||
            clean.contains("pizza") || clean.contains("calzone") || clean.contains("flatbread")) {
            return R.drawable.food_pizza
        }

        // 12. Pasta & Italian
        if (clean.contains("truffle penne") || clean.contains("fettuccine alfredo") || clean.contains("spaghetti carbonara") ||
            clean.contains("penne arrabiata") || clean.contains("lasagna") || clean.contains("ravioli") ||
            clean.contains("aglio e olio") || clean.contains("pasta") || clean.contains("spaghetti") ||
            clean.contains("penne") || clean.contains("fettuccine") || clean.contains("macaroni") ||
            clean.contains("mac and cheese") || clean.contains("cannelloni") || clean.contains("gnocchi") ||
            clean.contains("linguine") || clean.contains("risotto")) {
            return R.drawable.food_pasta
        }

        // 13. Asian Dim Sum & Dumplings
        if (clean.contains("steamed dim sum") || clean.contains("crystal dumpling") || clean.contains("bao bun") ||
            clean.contains("chicken gyoza") || clean.contains("spring roll") || clean.contains("steamed momo") ||
            clean.contains("wonton") || clean.contains("dim sum") || clean.contains("dimsum") ||
            clean.contains("dumpling") || clean.contains("dumplings") || clean.contains("bao") ||
            clean.contains("momo") || clean.contains("momos") || clean.contains("gyoza") ||
            clean.contains("manchurian") || clean.contains("chilli chicken") || clean.contains("fried rice") ||
            clean.contains("potsticker")) {
            return R.drawable.food_dim_sum
        }

        // 14. Sushi & Japanese Seafood
        if (clean.contains("salmon sushi") || clean.contains("california roll") || clean.contains("sashimi") ||
            clean.contains("tuna roll") || clean.contains("nigiri") || clean.contains("tempura") ||
            clean.contains("sushi") || clean.contains("maki") || clean.contains("poke bowl") ||
            clean.contains("lobster") || clean.contains("prawn") || clean.contains("shrimp") ||
            clean.contains("crab") || clean.contains("seafood platter") || clean.contains("raw fish")) {
            return R.drawable.food_sushi
        }

        // 15. Salads & Wellness
        if (clean.contains("caesar salad") || clean.contains("greek salad") || clean.contains("quinoa bowl") ||
            clean.contains("hummus") || clean.contains("tabbouleh") || clean.contains("fruit salad") ||
            clean.contains("salad") || clean.contains("caesar") || clean.contains("greens") ||
            clean.contains("raw") || clean.contains("organic") || clean.contains("slaw") ||
            clean.contains("coleslaw") || clean.contains("sprout") || clean.contains("vegan") ||
            clean.contains("wellness") || clean.contains("mezze")) {
            return R.drawable.food_salad
        }

        // 16. Desserts, Sweets & Mithai
        if (clean.contains("gulab jamun") || clean.contains("rasmalai") || clean.contains("rasgulla") ||
            clean.contains("jalebi") || clean.contains("lava cake") || clean.contains("chocolate cake") ||
            clean.contains("tiramisu") || clean.contains("cheesecake") || clean.contains("ice cream") ||
            clean.contains("saffron kulfi") || clean.contains("gajar halwa") || clean.contains("moong dal halwa") ||
            clean.contains("shahi tukda") || clean.contains("apple pie") || clean.contains("brownie") ||
            clean.contains("baklava") || clean.contains("dessert") || clean.contains("sweet") ||
            clean.contains("sweets") || clean.contains("kulfi") || clean.contains("cake") ||
            clean.contains("pastry") || clean.contains("pudding") || clean.contains("halwa") ||
            clean.contains("chocolate") || clean.contains("tart") || clean.contains("pie") ||
            clean.contains("custard") || clean.contains("sundae") || clean.contains("panna cotta") ||
            clean.contains("creme brulee") || clean.contains("mousse")) {
            return R.drawable.food_dessert
        }

        // 17. Coffee & Specialty Hot Brews
        if (clean.contains("masala chai") || clean.contains("espresso macchiato") || clean.contains("cappuccino") ||
            clean.contains("iced latte") || clean.contains("latte") || clean.contains("coffee") ||
            clean.contains("espresso") || clean.contains("cold brew") || clean.contains("chai") ||
            clean.contains("tea") || clean.contains("barista") || clean.contains("hot chocolate") ||
            clean.contains("brew")) {
            return R.drawable.food_coffee
        }

        // 18. Artisanal Breads, Viennoiserie, Waffles & Breakfast Bakes
        if (clean.contains("belgian waffle") || clean.contains("waffle") || clean.contains("waffles") ||
            clean.contains("maple pancake") || clean.contains("pancake") || clean.contains("pancakes") ||
            clean.contains("crepe") || clean.contains("crepes") || clean.contains("croissant") ||
            clean.contains("croissants") || clean.contains("french toast") || clean.contains("garlic naan") ||
            clean.contains("butter naan") || clean.contains("naan") || clean.contains("tandoori roti") ||
            clean.contains("roti") || clean.contains("paratha") || clean.contains("lachha paratha") ||
            clean.contains("kulcha") || clean.contains("chole bhature") || clean.contains("bhature") ||
            clean.contains("poori") || clean.contains("puri") || clean.contains("bread") ||
            clean.contains("toast") || clean.contains("bagel") || clean.contains("donut") ||
            clean.contains("muffin") || clean.contains("bakery")) {
            return R.drawable.food_bakery
        }

        // 19. Mexican & Tex-Mex
        if (clean.contains("chicken fajita") || clean.contains("beef taco") || clean.contains("chicken burrito") ||
            clean.contains("cheese quesadilla") || clean.contains("loaded nachos") || clean.contains("mexican") ||
            clean.contains("taco") || clean.contains("tacos") || clean.contains("fajita") ||
            clean.contains("fajitas") || clean.contains("burrito") || clean.contains("quesadilla") ||
            clean.contains("nacho") || clean.contains("nachos") || clean.contains("tortilla") ||
            clean.contains("wrap") || clean.contains("enchilada") || clean.contains("guacamole") ||
            clean.contains("salsa")) {
            return R.drawable.food_mexican
        }

        // Catch-all general food keywords
        if (clean.contains("chicken") || clean.contains("mutton") || clean.contains("meat")) return R.drawable.food_curry
        if (clean.contains("fish")) return R.drawable.food_curry
        if (clean.contains("egg")) return R.drawable.food_breakfast
        if (clean.contains("drink") || clean.contains("beverage")) return R.drawable.food_beverage

        return null
    }

    /**
     * Resolves default fallback plate for a counter demand when no dish name is entered.
     */
    fun matchDemandToDrawable(demandName: String): Int {
        val clean = demandName.lowercase().trim()
        return when {
            clean.contains("tandoor") || clean.contains("grill") -> R.drawable.food_kebab
            clean.contains("dosa") || clean.contains("south indian") -> R.drawable.food_dosa
            clean.contains("pasta") -> R.drawable.food_pasta
            clean.contains("pizza") -> R.drawable.food_pizza
            clean.contains("chaat") || clean.contains("street") -> R.drawable.food_chaat
            clean.contains("dim sum") || clean.contains("asian") || clean.contains("wok") -> R.drawable.food_dim_sum
            clean.contains("egg") || clean.contains("morning") -> R.drawable.food_breakfast
            clean.contains("waffle") || clean.contains("crepe") -> R.drawable.food_bakery
            clean.contains("veg mains") -> R.drawable.food_curry
            clean.contains("non-veg mains") || clean.contains("chafing") -> R.drawable.food_curry
            clean.contains("biryani") -> R.drawable.food_biryani
            clean.contains("salad") || clean.contains("wellness") -> R.drawable.food_salad
            clean.contains("dessert") || clean.contains("sweet") || clean.contains("patisserie") -> R.drawable.food_dessert
            clean.contains("beverage") || clean.contains("mocktail") || clean.contains("drink") || clean.contains("bar") -> R.drawable.food_beverage
            clean.contains("coffee") || clean.contains("tea") -> R.drawable.food_coffee
            clean.contains("burger") || clean.contains("sandwich") -> R.drawable.food_burger
            clean.contains("soup") -> R.drawable.food_soup
            else -> R.drawable.food_curry
        }
    }

    /**
     * Resolves the best matching real food photograph for any query or food mentioned by the user.
     * Always prioritizes the specific dish name before consulting any counter demand or station tags.
     */
    fun findMatchingFoodDrawable(dishName: String, fallbackDemand: String = ""): Int {
        val fromDish = matchDishNameToDrawable(dishName)
        if (fromDish != null) return fromDish

        // Check fallback demand ONLY if dish name had no culinary match or was empty
        if (fallbackDemand.isNotBlank()) {
            val fromDemand = matchDishNameToDrawable(fallbackDemand)
            if (fromDemand != null) return fromDemand
            return matchDemandToDrawable(fallbackDemand)
        }

        return R.drawable.food_curry
    }

    /**
     * Overload for multiple text tokens.
     */
    fun findMatchingFoodDrawable(vararg textMentions: String): Int {
        for (mention in textMentions) {
            val match = matchDishNameToDrawable(mention)
            if (match != null) return match
        }
        val lastFallback = textMentions.lastOrNull() ?: ""
        return matchDemandToDrawable(lastFallback)
    }

    /**
     * Gets or decodes a cached bitmap for a local drawable resource.
     */
    fun getBitmapForDrawable(context: Context, @DrawableRes resId: Int): Bitmap? {
        bitmapCache[resId]?.let {
            if (!it.isRecycled) return it
        }

        return try {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val bmp = BitmapFactory.decodeResource(context.resources, resId, options)
            if (bmp != null) {
                bitmapCache[resId] = bmp
            }
            bmp
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Finds and loads the real food bitmap for any text mentioned in the artwork.
     */
    fun loadRealFoodBitmap(context: Context, vararg textMentions: String): Bitmap? {
        val resId = findMatchingFoodDrawable(*textMentions)
        return getBitmapForDrawable(context, resId)
    }

    /**
     * Asynchronously generates or fetches a real food photograph automatically by dish name,
     * checking against the user counter demand.
     */
    suspend fun generateDishPhotoByDemand(
        dishName: String,
        counterDemand: String = "",
        context: Context? = null
    ): DishPhotoResult = withContext(Dispatchers.IO) {
        val cleanDish = dishName.trim().ifBlank { "Signature Dish" }
        val cleanDemand = counterDemand.trim()
        val cacheKey = cleanDish.lowercase()

        // 1. Cache lookup
        onlineBitmapCache[cacheKey]?.let {
            if (!it.isRecycled) {
                return@withContext DishPhotoResult(
                    bitmap = it,
                    sourceDescription = "Cached Real Photo: $cleanDish",
                    dishName = cleanDish,
                    matchedDemand = cleanDemand
                )
            }
        }

        // 2. High-Priority Direct Dish Name Match against our 19 Verified Authentic Culinary Plates
        if (context != null) {
            val directDishMatchRes = matchDishNameToDrawable(cleanDish)
            if (directDishMatchRes != null) {
                val localBmp = getBitmapForDrawable(context, directDishMatchRes)
                if (localBmp != null) {
                    val categoryTitle = getFoodTitleForDrawable(directDishMatchRes)
                    return@withContext DishPhotoResult(
                        bitmap = localBmp,
                        sourceDescription = "Verified Culinary Plate ($categoryTitle)",
                        dishName = cleanDish,
                        matchedDemand = cleanDemand
                    )
                }
            }
        }

        // 3. Check if query matches our online curated photo dictionary
        val queryLower = cleanDish.lowercase()
        for ((key, url) in ONLINE_FOOD_EXTENSIONS) {
            if (queryLower.contains(key)) {
                val downloaded = downloadBitmapFromUrl(url)
                if (downloaded != null) {
                    onlineBitmapCache[cacheKey] = downloaded
                    return@withContext DishPhotoResult(
                        bitmap = downloaded,
                        sourceDescription = "Curated High-Res: $cleanDish",
                        dishName = cleanDish,
                        matchedDemand = cleanDemand
                    )
                }
            }
        }

        // 4. Match authentic local culinary plate with fallback counter demand
        if (context != null) {
            val localRes = findMatchingFoodDrawable(cleanDish, cleanDemand)
            val localBmp = getBitmapForDrawable(context, localRes)
            if (localBmp != null) {
                val categoryTitle = getFoodTitleForDrawable(localRes)
                return@withContext DishPhotoResult(
                    bitmap = localBmp,
                    sourceDescription = "Verified Culinary Plate ($categoryTitle)",
                    dishName = cleanDish,
                    matchedDemand = cleanDemand
                )
            }
        }

        // 5. Safe fallback
        val placeholder = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        DishPhotoResult(
            bitmap = placeholder,
            sourceDescription = "Culinary Backdrop Plate",
            dishName = cleanDish,
            matchedDemand = cleanDemand
        )
    }

    /**
     * Asynchronously fetches a real food photo for an arbitrary query (e.g. "Steak", "Ramen", "Shawarma", "Waffles")
     * checks online catalog first, then generates dedicated real food photo via AI.
     */
    suspend fun fetchOnlineFoodBitmap(query: String): Bitmap? = withContext(Dispatchers.IO) {
        val clean = query.trim().lowercase()
        if (clean.isBlank()) return@withContext null

        onlineBitmapCache[clean]?.let {
            if (!it.isRecycled) return@withContext it
        }

        // Check if query matches our online curated photo dictionary
        for ((key, url) in ONLINE_FOOD_EXTENSIONS) {
            if (clean.contains(key)) {
                val bmp = downloadBitmapFromUrl(url)
                if (bmp != null) {
                    onlineBitmapCache[clean] = bmp
                    return@withContext bmp
                }
            }
        }

        // Dynamic AI photo generation by the name of the dish
        val promptText = "award winning authentic food photography of $clean, culinary presentation, 4k ultra realistic"
        val encodedPrompt = java.net.URLEncoder.encode(promptText, "UTF-8")
        val aiUrl = "https://image.pollinations.ai/prompt/$encodedPrompt?width=800&height=800&nologo=true"
        val bmp = downloadBitmapFromUrl(aiUrl)
        if (bmp != null) {
            onlineBitmapCache[clean] = bmp
            return@withContext bmp
        }

        null
    }

    private fun downloadBitmapFromUrl(urlStr: String): Bitmap? {
        return try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 4000
            conn.readTimeout = 5000
            conn.instanceFollowRedirects = true
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile) CulinaryStudio/1.0")
            conn.connect()
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val bytes = conn.inputStream.use { it.readBytes() }
                if (bytes.isNotEmpty()) {
                    val options = BitmapFactory.Options().apply {
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

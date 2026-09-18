package com.example.data.model

import com.example.data.remote.DigitalTagSize
import com.example.data.remote.DigitalTagTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Represents a connected electronic digital tag (ESL) actively synced to a buffet counter.
 */
data class ConnectedDigitalTag(
    val tagId: String,
    val macAddress: String,
    val counterCode: String,
    val counterName: String,
    val counterZone: String,
    val counterCategory: String,
    val dishId: Long,
    val dishName: String,
    val dishDesc: String = "",
    val isVeg: Boolean = true,
    val calories: String = "",
    val allergens: List<String> = emptyList(),
    val batteryPct: Int = 95,
    val signalDbm: Int = -45,
    val screenSize: DigitalTagSize = DigitalTagSize.COMPACT_4_2,
    val theme: DigitalTagTheme = DigitalTagTheme.EPAPER_TRICOLOR,
    val isSoldOut: Boolean = false,
    val isFlashingLed: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis() - 180_000,
    val syncStatus: String = "ONLINE_SYNCED" // "ONLINE_SYNCED", "TRANSMITTING", "OFFLINE"
) {
    val batteryHealth: String
        get() = when {
            batteryPct >= 80 -> "Optimal"
            batteryPct >= 50 -> "Healthy"
            batteryPct >= 25 -> "Moderate"
            else -> "Critical Low"
        }

    val estimatedRemainingLife: String
        get() = when {
            batteryPct >= 80 -> "4.5+ Years (Ultra-low E-Paper draw)"
            batteryPct >= 50 -> "2.8 Years remaining"
            batteryPct >= 25 -> "1.2 Years remaining"
            else -> "Replace CR2450 cell in ~14 days"
        }

    val batteryVoltage: String
        get() = when {
            batteryPct >= 80 -> "3.20 V"
            batteryPct >= 50 -> "3.05 V"
            batteryPct >= 25 -> "2.88 V"
            else -> "2.65 V (Low Voltage)"
        }

    val formattedLastUpdated: String
        get() = formatRelativeTimestamp(lastUpdatedTimestamp)
}

/**
 * Aggregated status representation for an individual buffet counter station.
 */
data class BuffetCounterGroup(
    val counterCode: String,
    val counterName: String,
    val counterZone: String,
    val counterCategory: String,
    val iconEmoji: String,
    val tags: List<ConnectedDigitalTag>
) {
    val totalTags: Int get() = tags.size
    val onlineTags: Int get() = tags.count { it.syncStatus != "OFFLINE" }
    val avgBattery: Int get() = if (tags.isNotEmpty()) tags.map { it.batteryPct }.average().toInt() else 100
    val minBattery: Int get() = tags.minOfOrNull { it.batteryPct } ?: 100
    val hasLowBattery: Boolean get() = tags.any { it.batteryPct < 25 }
    val soldOutCount: Int get() = tags.count { it.isSoldOut }
    val lastUpdatedTimestamp: Long get() = tags.maxOfOrNull { it.lastUpdatedTimestamp } ?: 0L
    val formattedLastUpdated: String get() = formatRelativeTimestamp(lastUpdatedTimestamp)
}

fun formatRelativeTimestamp(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val mins = diff / (60 * 1000)
    val hours = diff / (60 * 60 * 1000)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(timestamp))
    return when {
        diff < 60 * 1000 -> "Just now ($timeStr)"
        mins < 60 -> "${mins}m ago ($timeStr)"
        hours < 24 -> "${hours}h ago ($timeStr)"
        else -> SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(timestamp))
    }
}

/**
 * Canonical default fleet of connected digital tags mapped across 7 distinct buffet counters.
 */
fun generateDefaultTagFleet(now: Long = System.currentTimeMillis()): List<ConnectedDigitalTag> {
    return listOf(
        // Counter 1: Hot Chafing & Carvery Mains (Zone A - Central Island)
        ConnectedDigitalTag(
            tagId = "ESL-BF01-42",
            macAddress = "7A:9B:4C:12:34:01",
            counterCode = "CTR-01",
            counterName = "Counter 1: Hot Chafing & Carvery Mains",
            counterZone = "Zone A - Central Island",
            counterCategory = "Hot Mains",
            dishId = 6,
            dishName = "Chicken Butter Masala",
            dishDesc = "Tender chicken chunks in rich buttery tomato and cashew gravy.",
            isVeg = false,
            calories = "295 kcal / 100 g",
            allergens = listOf("Milk", "Nuts"),
            batteryPct = 98,
            signalDbm = -42,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (3 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF02-42",
            macAddress = "7A:9B:4C:12:34:02",
            counterCode = "CTR-01",
            counterName = "Counter 1: Hot Chafing & Carvery Mains",
            counterZone = "Zone A - Central Island",
            counterCategory = "Hot Mains",
            dishId = 7,
            dishName = "Paneer Zafrani Korma",
            dishDesc = "Fresh artisanal cottage cheese infused with saffron and roasted spices.",
            isVeg = true,
            calories = "235 kcal / 100 g",
            allergens = listOf("Milk", "Nuts"),
            batteryPct = 94,
            signalDbm = -44,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (3 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF03-42",
            macAddress = "7A:9B:4C:12:34:03",
            counterCode = "CTR-01",
            counterName = "Counter 1: Hot Chafing & Carvery Mains",
            counterZone = "Zone A - Central Island",
            counterCategory = "Hot Mains",
            dishId = 8,
            dishName = "Dal Makhani Grand Heritage",
            dishDesc = "Black lentils slow-cooked overnight over live charcoal with churned white butter.",
            isVeg = true,
            calories = "185 kcal / 100 g",
            allergens = listOf("Milk"),
            batteryPct = 91,
            signalDbm = -46,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (3 * 60 * 1000)
        ),

        // Counter 2: Live Tandoor & Grill Station (Zone B - Open Kitchen Promenade)
        ConnectedDigitalTag(
            tagId = "ESL-BF04-42",
            macAddress = "7A:9B:4C:12:34:04",
            counterCode = "CTR-02",
            counterName = "Counter 2: Live Tandoor & Grill Station",
            counterZone = "Zone B - Open Kitchen Promenade",
            counterCategory = "Live Grill",
            dishId = 101,
            dishName = "Tandoori Paneer Tikka",
            dishDesc = "Charcoal-grilled cottage cheese marinated in mustard oil, ajwain, and hung curd.",
            isVeg = true,
            calories = "210 kcal / 100 g",
            allergens = listOf("Milk", "Mustard"),
            batteryPct = 88,
            signalDbm = -48,
            screenSize = DigitalTagSize.DELUXE_7_5,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (8 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF05-42",
            macAddress = "7A:9B:4C:12:34:05",
            counterCode = "CTR-02",
            counterName = "Counter 2: Live Tandoor & Grill Station",
            counterZone = "Zone B - Open Kitchen Promenade",
            counterCategory = "Live Grill",
            dishId = 102,
            dishName = "Murgh Malai Seekh Kebab",
            dishDesc = "Minced chicken skewers seasoned with cardamom, green chilies, and clotted cream.",
            isVeg = false,
            calories = "240 kcal / 100 g",
            allergens = listOf("Milk", "Eggs"),
            batteryPct = 82,
            signalDbm = -50,
            screenSize = DigitalTagSize.DELUXE_7_5,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (8 * 60 * 1000)
        ),

        // Counter 3: Soup Kettle & Artisanal Bread Bar (Zone C - Soup & Bread Island)
        ConnectedDigitalTag(
            tagId = "ESL-BF06-42",
            macAddress = "7A:9B:4C:12:34:06",
            counterCode = "CTR-03",
            counterName = "Counter 3: Soup Kettle & Artisanal Bread Bar",
            counterZone = "Zone C - Soup & Bread Island",
            counterCategory = "Soups & Bakery",
            dishId = 5,
            dishName = "Cream Of Sweet Corn Soup",
            dishDesc = "Silky sweet corn velouté garnished with fresh herbs and cracked black pepper.",
            isVeg = true,
            calories = "92 kcal / 100 ml",
            allergens = listOf("Milk", "Celery"),
            batteryPct = 96,
            signalDbm = -39,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.IVORY_LUXURY,
            lastUpdatedTimestamp = now - (12 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF07-42",
            macAddress = "7A:9B:4C:12:34:07",
            counterCode = "CTR-03",
            counterName = "Counter 3: Soup Kettle & Artisanal Bread Bar",
            counterZone = "Zone C - Soup & Bread Island",
            counterCategory = "Soups & Bakery",
            dishId = 103,
            dishName = "Sourdough & Garlic Baguettes",
            dishDesc = "Wild-fermented sourdough loaves and fresh roasted garlic butter herb ficelles.",
            isVeg = true,
            calories = "160 kcal / 100 g",
            allergens = listOf("Gluten", "Milk"),
            batteryPct = 92,
            signalDbm = -41,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.IVORY_LUXURY,
            lastUpdatedTimestamp = now - (12 * 60 * 1000)
        ),

        // Counter 4: Chilled Salad Bar & Deli Charcuterie (Zone D - Cold Island)
        ConnectedDigitalTag(
            tagId = "ESL-BF08-42",
            macAddress = "7A:9B:4C:12:34:08",
            counterCode = "CTR-04",
            counterName = "Counter 4: Chilled Salad Bar & Deli Charcuterie",
            counterZone = "Zone D - Cold Island",
            counterCategory = "Salad & Cold Cuts",
            dishId = 3,
            dishName = "Caesar Salad with Shaved Parmesan",
            dishDesc = "Crisp romaine hearts tossed with aged Parmigiano-Reggiano and brioche croutons.",
            isVeg = true,
            calories = "145 kcal / 100 g",
            allergens = listOf("Milk", "Gluten", "Eggs"),
            batteryPct = 86,
            signalDbm = -52,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (15 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF09-42",
            macAddress = "7A:9B:4C:12:34:09",
            counterCode = "CTR-04",
            counterName = "Counter 4: Chilled Salad Bar & Deli Charcuterie",
            counterZone = "Zone D - Cold Island",
            counterCategory = "Salad & Cold Cuts",
            dishId = 4,
            dishName = "Curd, Pickle And Crisp Papad",
            dishDesc = "Churned organic yoghurt with house mango pickle and spiced lentil crisps.",
            isVeg = true,
            calories = "125 kcal / 100 g",
            allergens = listOf("Milk", "Mustard"),
            batteryPct = 89,
            signalDbm = -54,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (15 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF10-42",
            macAddress = "7A:9B:4C:12:34:10",
            counterCode = "CTR-04",
            counterName = "Counter 4: Chilled Salad Bar & Deli Charcuterie",
            counterZone = "Zone D - Cold Island",
            counterCategory = "Salad & Cold Cuts",
            dishId = 104,
            dishName = "Mediterranean Mezze & Hummus",
            dishDesc = "Silky chickpea purée, kalamata olives, marinated feta, and toasted sesame pita.",
            isVeg = true,
            calories = "175 kcal / 100 g",
            allergens = listOf("Sesame", "Milk", "Gluten"),
            batteryPct = 74,
            signalDbm = -58,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (15 * 60 * 1000)
        ),

        // Counter 5: Live Wok & Asian Noodle Counter (Zone E - Live Action Station)
        ConnectedDigitalTag(
            tagId = "ESL-BF11-42",
            macAddress = "7A:9B:4C:12:34:11",
            counterCode = "CTR-05",
            counterName = "Counter 5: Live Wok & Asian Noodle Counter",
            counterZone = "Zone E - Live Action Station",
            counterCategory = "Asian Wok",
            dishId = 2,
            dishName = "Crisp Vegetable Spring Rolls",
            dishDesc = "Thin pastry cigars stuffed with glass noodles and wok-tossed julienned vegetables.",
            isVeg = true,
            calories = "185 kcal / 100 g",
            allergens = listOf("Gluten", "Soya"),
            batteryPct = 68,
            signalDbm = -62,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (22 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF12-42",
            macAddress = "7A:9B:4C:12:34:12",
            counterCode = "CTR-05",
            counterName = "Counter 5: Live Wok & Asian Noodle Counter",
            counterZone = "Zone E - Live Action Station",
            counterCategory = "Asian Wok",
            dishId = 105,
            dishName = "Stir-Fried Asian Greens with Tofu",
            dishDesc = "Bok choy, shiitake mushrooms, and organic tofu in ginger soy reduction.",
            isVeg = true,
            calories = "135 kcal / 100 g",
            allergens = listOf("Soya", "Sesame"),
            batteryPct = 79,
            signalDbm = -64,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (22 * 60 * 1000)
        ),

        // Counter 6: Dessert & Patisserie Station (Zone F - Sweet Galleria)
        ConnectedDigitalTag(
            tagId = "ESL-BF13-42",
            macAddress = "7A:9B:4C:12:34:13",
            counterCode = "CTR-06",
            counterName = "Counter 6: Dessert & Patisserie Station",
            counterZone = "Zone F - Sweet Galleria",
            counterCategory = "Desserts",
            dishId = 11,
            dishName = "Warm Shahi Gulab Jamun",
            dishDesc = "Golden mawa dumplings soaked in cardamom rose syrup, garnished with pistachios.",
            isVeg = true,
            calories = "280 kcal / 100 g",
            allergens = listOf("Milk", "Nuts", "Gluten"),
            batteryPct = 95,
            signalDbm = -45,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (5 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF14-42",
            macAddress = "7A:9B:4C:12:34:14",
            counterCode = "CTR-06",
            counterName = "Counter 6: Dessert & Patisserie Station",
            counterZone = "Zone F - Sweet Galleria",
            counterCategory = "Desserts",
            dishId = 12,
            dishName = "Vanilla Bean Gelato with Belgian Fudge",
            dishDesc = "Madagascar bourbon vanilla gelato paired with warm 70% dark Belgian chocolate fudge.",
            isVeg = true,
            calories = "210 kcal / 100 g",
            allergens = listOf("Milk"),
            batteryPct = 42,
            signalDbm = -48,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (5 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF15-42",
            macAddress = "7A:9B:4C:12:34:15",
            counterCode = "CTR-06",
            counterName = "Counter 6: Dessert & Patisserie Station",
            counterZone = "Zone F - Sweet Galleria",
            counterCategory = "Desserts",
            dishId = 106,
            dishName = "Moong Dal Halwa with Silver Vark",
            dishDesc = "Desi ghee slow-roasted golden lentil pudding scented with saffron and nutmeg.",
            isVeg = true,
            calories = "320 kcal / 100 g",
            allergens = listOf("Milk", "Nuts"),
            batteryPct = 18, // LOW BATTERY DEMO
            signalDbm = -58,
            screenSize = DigitalTagSize.COMPACT_4_2,
            theme = DigitalTagTheme.EPAPER_TRICOLOR,
            lastUpdatedTimestamp = now - (5 * 60 * 1000)
        ),

        // Counter 7: Beverage & Mocktail Dispensary (Zone G - Beverage Lounge)
        ConnectedDigitalTag(
            tagId = "ESL-BF16-42",
            macAddress = "7A:9B:4C:12:34:16",
            counterCode = "CTR-07",
            counterName = "Counter 7: Beverage & Mocktail Dispensary",
            counterZone = "Zone G - Beverage Lounge",
            counterCategory = "Beverages",
            dishId = 1,
            dishName = "Virgin Mojito with Fresh Mint",
            dishDesc = "Muddled garden spearmint, Persian limes, and sparkling soda with cane reduction.",
            isVeg = true,
            calories = "45 kcal / 100 ml",
            allergens = emptyList(),
            batteryPct = 97,
            signalDbm = -38,
            screenSize = DigitalTagSize.DELUXE_7_5,
            theme = DigitalTagTheme.OLED_DARK,
            lastUpdatedTimestamp = now - (2 * 60 * 1000)
        ),
        ConnectedDigitalTag(
            tagId = "ESL-BF17-42",
            macAddress = "7A:9B:4C:12:34:17",
            counterCode = "CTR-07",
            counterName = "Counter 7: Beverage & Mocktail Dispensary",
            counterZone = "Zone G - Beverage Lounge",
            counterCategory = "Beverages",
            dishId = 107,
            dishName = "Royal Mango Saffron Lassi",
            dishDesc = "Alphonso mango pulp whipped with creamy curd, infused with Kashmir saffron.",
            isVeg = true,
            calories = "140 kcal / 100 ml",
            allergens = listOf("Milk"),
            batteryPct = 93,
            signalDbm = -40,
            screenSize = DigitalTagSize.DELUXE_7_5,
            theme = DigitalTagTheme.OLED_DARK,
            lastUpdatedTimestamp = now - (2 * 60 * 1000)
        )
    )
}

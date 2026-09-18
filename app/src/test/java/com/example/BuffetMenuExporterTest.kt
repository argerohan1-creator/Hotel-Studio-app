package com.example

import com.example.data.model.BuffetMenuItemEntity
import com.example.util.BuffetMenuExporter
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BuffetMenuExporterTest {

    private val sampleItems = listOf(
        BuffetMenuItemEntity(
            id = 1,
            dayOfWeek = "Friday",
            mealSession = "Dinner",
            courseSection = "Starters & Live Counter",
            name = "Paneer Tikka",
            desc = "Charcoal roasted cottage cheese marinated in aromatic Indian spices",
            cals = "210 kcal",
            type = "veg",
            station = "Live Tandoor Counter",
            allergens = listOf("Milk")
        ),
        BuffetMenuItemEntity(
            id = 2,
            dayOfWeek = "Friday",
            mealSession = "Dinner",
            courseSection = "Main Course (Non-Veg)",
            name = "Chicken Butter Masala",
            desc = "Tender tandoor-roasted chicken simmered in a velvety tomato-fenugreek gravy",
            cals = "320 kcal",
            type = "nonveg",
            station = "Chafing Dish #3",
            allergens = listOf("Milk", "Nuts")
        )
    )

    @Test
    fun `generatePrinterFriendlyHtml includes statutory calorie and allergen disclaimers`() {
        val html = BuffetMenuExporter.generatePrinterFriendlyHtml(
            day = "Friday",
            session = "Dinner",
            paxCount = 80,
            establishmentName = "Grand Horizon Culinary Suite",
            fssaiLicense = "10021011000892",
            items = sampleItems,
            includeCalories = true,
            includeAllergens = true,
            includeStations = true,
            theme = "ink_saver",
            paperSize = "A4"
        )

        // Verify establishment and service details
        assertTrue("Contains establishment name", html.contains("Grand Horizon Culinary Suite"))
        assertTrue("Contains day and session", html.contains("Friday Dinner"))
        assertTrue("Contains pax count", html.contains("80 Pax"))

        // Verify dish items and FSSAI symbols
        assertTrue("Contains Paneer Tikka", html.contains("Paneer Tikka"))
        assertTrue("Contains Chicken Butter Masala", html.contains("Chicken Butter Masala"))
        assertTrue("Contains veg box", html.contains("veg-box"))
        assertTrue("Contains non-veg box", html.contains("nonveg-box"))

        // Verify mandatory regulatory compliance disclaimers
        assertTrue("Contains calorie disclaimer", html.contains("2,000 kcal energy per day"))
        assertTrue("Contains allergen warning", html.contains("Food Allergens & Intolerance Warning"))
        assertTrue("Contains FSSAI license", html.contains("10021011000892"))
        assertTrue("Contains HACCP temperature standards", html.contains("63°C"))
    }

    @Test
    fun `generatePrintablePlainText includes full structured menu with disclaimers`() {
        val text = BuffetMenuExporter.generatePrintablePlainText(
            day = "Friday",
            session = "Dinner",
            paxCount = 80,
            establishmentName = "Grand Horizon Culinary Suite",
            fssaiLicense = "10021011000892",
            items = sampleItems
        )

        assertTrue(text.contains("GRAND HORIZON CULINARY SUITE"))
        assertTrue(text.contains("EXECUTIVE DAILY BUFFET MENU • Friday Dinner"))
        assertTrue(text.contains("[VEG] Paneer Tikka"))
        assertTrue(text.contains("[NON-VEG] Chicken Butter Masala"))
        assertTrue(text.contains("STATUTORY CALORIE ADVISORY"))
        assertTrue(text.contains("ALLERGEN DECLARATION & SENSITIVITY WARNING"))
        assertTrue(text.contains("FSSAI License No: 10021011000892"))
    }
}

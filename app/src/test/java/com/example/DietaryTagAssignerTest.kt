package com.example

import com.example.ui.components.DietaryTagCategory
import com.example.ui.components.DietaryTagRegistry
import com.example.ui.components.STANDARD_DIETARY_TAGS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DietaryTagAssignerTest {

    @Test
    fun `standard dietary tags list contains allergen-free, vegan, gluten-free tags`() {
        val tagNames = STANDARD_DIETARY_TAGS.map { it.name }

        // Must include all key dietary lifestyle and free-from tags
        assertTrue("Contains Allergen-Free", tagNames.contains("Allergen-Free"))
        assertTrue("Contains Vegan", tagNames.contains("Vegan"))
        assertTrue("Contains Gluten-Free", tagNames.contains("Gluten-Free"))
        assertTrue("Contains Dairy-Free", tagNames.contains("Dairy-Free"))
        assertTrue("Contains Nut-Free", tagNames.contains("Nut-Free"))
        assertTrue("Contains Jain", tagNames.contains("Jain"))
        assertTrue("Contains Halal", tagNames.contains("Halal"))
        assertTrue("Contains Organic", tagNames.contains("Organic"))
    }

    @Test
    fun `standard dietary tags contain 14 standard regulatory allergens`() {
        val regulatoryTags = STANDARD_DIETARY_TAGS.filter { it.category == DietaryTagCategory.ALLERGENS }
        val names = regulatoryTags.map { it.name }

        assertTrue("Contains Gluten allergen", names.contains("Gluten"))
        assertTrue("Contains Milk allergen", names.contains("Milk"))
        assertTrue("Contains Eggs allergen", names.contains("Eggs"))
        assertTrue("Contains Fish allergen", names.contains("Fish"))
        assertTrue("Contains Peanuts allergen", names.contains("Peanuts"))
        assertTrue("Contains Soya allergen", names.contains("Soya"))
        assertTrue("Contains Mustard allergen", names.contains("Mustard"))
        assertTrue("Contains Sesame allergen", names.contains("Sesame"))
        assertTrue("Contains Celery allergen", names.contains("Celery"))
        assertTrue("Contains Sulphites allergen", names.contains("Sulphites"))
        assertTrue("Contains Lupin allergen", names.contains("Lupin"))
        assertTrue("Contains Molluscs allergen", names.contains("Molluscs"))
        assertTrue("Contains Crustaceans allergen", names.contains("Crustaceans"))
    }

    @Test
    fun `dietary tag registry resolves tag information accurately`() {
        val veganTag = DietaryTagRegistry.findTag("Vegan")
        assertNotNull(veganTag)
        assertEquals(DietaryTagCategory.LIFESTYLE, veganTag?.category)

        val allergenFreeTag = DietaryTagRegistry.findTag("allergen-free")
        assertNotNull(allergenFreeTag)
        assertEquals(DietaryTagCategory.FREE_FROM, allergenFreeTag?.category)

        val glutenFreeTag = DietaryTagRegistry.findTag("Gluten Free")
        assertNotNull(glutenFreeTag)
        assertEquals(DietaryTagCategory.FREE_FROM, glutenFreeTag?.category)
    }

    @Test
    fun `dietary tag registry groups tags correctly by category`() {
        val grouped = DietaryTagRegistry.getAllTagsByCategory()

        assertTrue(grouped.containsKey(DietaryTagCategory.FREE_FROM))
        assertTrue(grouped.containsKey(DietaryTagCategory.LIFESTYLE))
        assertTrue(grouped.containsKey(DietaryTagCategory.ALLERGENS))
        assertTrue(grouped.containsKey(DietaryTagCategory.WELLNESS))

        val freeFrom = grouped[DietaryTagCategory.FREE_FROM] ?: emptyList()
        assertTrue(freeFrom.any { it.name == "Allergen-Free" })
        assertTrue(freeFrom.any { it.name == "Gluten-Free" })
        assertTrue(freeFrom.any { it.name == "Dairy-Free" })
    }
}

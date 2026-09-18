package com.example.util

import com.example.data.model.RecipeEntity

object RecipeImporter {

    /**
     * Parses a structured CSV/TSV text block into a list of RecipeEntities.
     * Expected columns: Item name, category, ingredients, prep time, cost per pax, portion, steps to follow
     */
    fun parseStructuredRecipeData(rawText: String): List<RecipeEntity> {
        val lines = rawText.trim().lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()

        val parsedRecipes = mutableListOf<RecipeEntity>()
        
        // We will do a simple CSV parser that respects quotes, or just TSV if tabs are present
        val isTsv = lines.first().contains('\t')
        
        for (line in lines) {
            // Skip apparent header rows
            if (line.contains("Item", ignoreCase = true) && (line.contains("category", ignoreCase = true) || line.contains("ingredients", ignoreCase = true))) {
                continue
            }
            
            val columns = if (isTsv) line.split('\t') else line.split(Regex(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).map { it.removeSurrounding("\"") }
            
            if (columns.isNotEmpty()) {
                val name = columns.getOrNull(0)?.trim() ?: "Unknown Recipe"
                val rawCategory = columns.getOrNull(1)?.trim() ?: ""
                val ingredientsStr = columns.getOrNull(2)?.trim() ?: ""
                val prepTime = columns.getOrNull(3)?.trim() ?: "30 mins"
                val costPerPax = columns.getOrNull(4)?.trim()?.toDoubleOrNull() ?: 0.0
                val portions = columns.getOrNull(5)?.trim() ?: "1 Person"
                val stepsStr = columns.getOrNull(6)?.trim() ?: ""
                
                // Smart Category Mapping
                val mappedCategory = mapToExistingCategory(rawCategory, name)
                
                // Parse ingredients and steps (supporting both comma and semicolon)
                val ingredients = ingredientsStr.split(Regex("[;,]")).map { it.trim() }.filter { it.isNotBlank() }
                val steps = stepsStr.split(Regex("[;,]")).map { it.trim() }.filter { it.isNotBlank() }
                
                parsedRecipes.add(
                    RecipeEntity(
                        name = name,
                        category = mappedCategory,
                        prepTime = prepTime,
                        yieldPortions = portions,
                        cals = "Est. 180 kcal", // Defaulting as requested columns removed it
                        desc = "Establishment standard recipe.",
                        story = "Imported establishment data.",
                        ingredients = if (ingredients.isNotEmpty()) ingredients else listOf("Standard ingredients apply"),
                        steps = if (steps.isNotEmpty()) steps else listOf("Prepare as per establishment SOP."),
                        costPerPax = costPerPax,
                        isEstablishmentData = true
                    )
                )
            }
        }
        
        return parsedRecipes
    }

    private fun mapToExistingCategory(raw: String, name: String): String {
        val searchStr = "$raw $name".lowercase()
        return when {
            searchStr.contains("soup") || searchStr.contains("broth") || searchStr.contains("shorba") -> "Soups"
            searchStr.contains("salad") || searchStr.contains("greens") || searchStr.contains("chaat") -> "Salads"
            searchStr.contains("starter") || searchStr.contains("appetizer") || searchStr.contains("tikka") || searchStr.contains("kebab") -> "Starters & Appetizers"
            searchStr.contains("sweet") || searchStr.contains("dessert") || searchStr.contains("cake") || searchStr.contains("ice cream") -> "Sweets & Confectionery"
            searchStr.contains("beverage") || searchStr.contains("drink") || searchStr.contains("mocktail") || searchStr.contains("juice") -> "Beverages & Mocktails"
            else -> "Main Course"
        }
    }
}

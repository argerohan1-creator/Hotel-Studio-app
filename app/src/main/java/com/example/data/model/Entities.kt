package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val desc: String,
    val cals: String,
    val type: String, // "veg" or "nonveg"
    val category: String,
    val allergens: List<String> = emptyList(),
    val isSample: Boolean = false
)

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val prepTime: String,
    val yieldPortions: String,
    val cals: String,
    val desc: String,
    val story: String,
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val costPerPax: Double = 0.0,
    val isEstablishmentData: Boolean = false
)

@Entity(tableName = "buffet_closing")
data class BuffetClosingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: String,
    val session: String,
    val totalCoversSold: Int,
    val buffetPrice: Double,
    val totalRevenue: Double,
    val totalFoodCost: Double,
    val wastageCost: Double,
    val foodCostPercentage: Double,
    val wastagePercentage: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Guest User",
    val establishment: String = "Global Culinary Suite",
    val emailOrPhone: String = "user@hotelstudio.com",
    val avatarUri: String? = null,
    val isLogoLocked: Boolean = true,
    val customLogoBase64: String? = null,
    val activeTheme: String = "taj", // "midnight", "luxury", "ivory", "kitchen"
    val activeLanguage: String = "en",
    val activeTemplate: String = "template-heritage-gold",
    val country: String = "United States"
)

@Entity(tableName = "help_desk_messages")
data class HelpDeskMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" or "ai"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "callback_requests")
data class CallbackRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phone: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "buffet_menu_items")
data class BuffetMenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String = "Monday", // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    val mealSession: String = "Lunch", // Breakfast, Brunch, Lunch, High Tea, Dinner
    val courseSection: String = "Main Course (Veg)", // Welcome Beverages, Starters & Live Counter, Soups & Breads, Salad Bar, Main Course (Veg), Main Course (Non-Veg), Accompaniments & Rices, Desserts & Sweets
    val recipeId: Long? = null,
    val name: String,
    val desc: String = "",
    val cals: String = "",
    val type: String = "veg", // "veg" or "nonveg"
    val prepTime: String = "20 mins",
    val portions: String = "80 Pax",
    val station: String = "Main Buffet Line",
    val allergens: List<String> = emptyList(),
    val sortOrder: Int = 0,
    val isReady: Boolean = false
)

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listType)

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.let { adapter.toJson(it) } ?: "[]"
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return if (value.isNullOrBlank()) emptyList() else adapter.fromJson(value) ?: emptyList()
    }
}

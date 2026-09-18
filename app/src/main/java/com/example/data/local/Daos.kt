package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.CallbackRequestEntity
import com.example.data.model.DishEntity
import com.example.data.model.HelpDeskMessageEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes ORDER BY id ASC")
    fun getAllDishes(): Flow<List<DishEntity>>

    @Query("SELECT * FROM dishes WHERE id = :id")
    suspend fun getDishById(id: Long): DishEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: DishEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<DishEntity>)

    @Update
    suspend fun updateDish(dish: DishEntity)

    @Delete
    suspend fun deleteDish(dish: DishEntity)

    @Query("DELETE FROM dishes")
    suspend fun clearDishes()

    @Query("SELECT COUNT(*) FROM dishes")
    suspend fun getDishCount(): Int
}

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Long): RecipeEntity?

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' OR `desc` LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE category = :category")
    fun getRecipesByCategory(category: String): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET activeTheme = :theme WHERE id = 1")
    suspend fun updateTheme(theme: String)

    @Query("UPDATE user_profile SET activeLanguage = :lang WHERE id = 1")
    suspend fun updateLanguage(lang: String)

    @Query("UPDATE user_profile SET activeTemplate = :template WHERE id = 1")
    suspend fun updateTemplate(template: String)

    @Query("UPDATE user_profile SET customLogoBase64 = :logo, isLogoLocked = :isLocked WHERE id = 1")
    suspend fun updateLogo(logo: String?, isLocked: Boolean)
}

@Dao
interface HelpDeskDao {
    @Query("SELECT * FROM help_desk_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<HelpDeskMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: HelpDeskMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallback(callback: CallbackRequestEntity): Long

    @Query("SELECT * FROM callback_requests ORDER BY timestamp DESC")
    fun getAllCallbacks(): Flow<List<CallbackRequestEntity>>
}

@Dao
interface BuffetMenuDao {
    @Query("SELECT * FROM buffet_menu_items ORDER BY sortOrder ASC, id ASC")
    fun getAllMenuItems(): Flow<List<BuffetMenuItemEntity>>

    @Query("SELECT * FROM buffet_menu_items WHERE dayOfWeek = :day AND mealSession = :session ORDER BY sortOrder ASC, id ASC")
    fun getMenuItems(day: String, session: String): Flow<List<BuffetMenuItemEntity>>

    @Query("SELECT * FROM buffet_menu_items WHERE dayOfWeek = :day AND mealSession = :session ORDER BY sortOrder ASC, id ASC")
    suspend fun getMenuItemsList(day: String, session: String): List<BuffetMenuItemEntity>

    @Query("SELECT * FROM buffet_menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Long): BuffetMenuItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: BuffetMenuItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(items: List<BuffetMenuItemEntity>)

    @Update
    suspend fun updateMenuItem(item: BuffetMenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(item: BuffetMenuItemEntity)

    @Query("DELETE FROM buffet_menu_items WHERE id = :id")
    suspend fun deleteMenuItemById(id: Long)

    @Query("DELETE FROM buffet_menu_items WHERE dayOfWeek = :day AND mealSession = :session")
    suspend fun clearSessionMenu(day: String, session: String)

    @Query("SELECT COUNT(*) FROM buffet_menu_items")
    suspend fun getMenuItemCount(): Int
}

@Dao
interface BuffetClosingDao {
    @Query("SELECT * FROM buffet_closing ORDER BY timestamp DESC")
    fun getAllClosingRecords(): Flow<List<com.example.data.model.BuffetClosingEntity>>

    @Query("SELECT * FROM buffet_closing WHERE day = :day AND session = :session ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestClosingRecord(day: String, session: String): com.example.data.model.BuffetClosingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClosingRecord(record: com.example.data.model.BuffetClosingEntity): Long

    @Query("DELETE FROM buffet_closing")
    suspend fun clearAllRecords()
}

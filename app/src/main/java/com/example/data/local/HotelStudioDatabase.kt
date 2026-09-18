package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.BuffetClosingEntity
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.CallbackRequestEntity
import com.example.data.model.Converters
import com.example.data.model.DishEntity
import com.example.data.model.HelpDeskMessageEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.UserProfileEntity

@Database(
    entities = [
        DishEntity::class,
        RecipeEntity::class,
        UserProfileEntity::class,
        HelpDeskMessageEntity::class,
        CallbackRequestEntity::class,
        BuffetMenuItemEntity::class,
        BuffetClosingEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HotelStudioDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun recipeDao(): RecipeDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun helpDeskDao(): HelpDeskDao
    abstract fun buffetMenuDao(): BuffetMenuDao
    abstract fun buffetClosingDao(): BuffetClosingDao

    companion object {
        @Volatile
        private var INSTANCE: HotelStudioDatabase? = null

        fun getInstance(context: Context): HotelStudioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HotelStudioDatabase::class.java,
                    "hotel_studio_enterprise.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

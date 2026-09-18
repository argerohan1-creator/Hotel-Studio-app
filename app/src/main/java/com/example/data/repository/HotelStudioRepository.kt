package com.example.data.repository

import com.example.data.local.BuffetClosingDao
import com.example.data.local.BuffetMenuDao
import com.example.data.local.DishDao
import com.example.data.local.HelpDeskDao
import com.example.data.local.RecipeDao
import com.example.data.local.UserProfileDao
import com.example.data.model.BuffetClosingEntity
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.CallbackRequestEntity
import com.example.data.model.DishEntity
import com.example.data.model.HelpDeskMessageEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class HotelStudioRepository(
    private val dishDao: DishDao,
    private val recipeDao: RecipeDao,
    private val userProfileDao: UserProfileDao,
    private val helpDeskDao: HelpDeskDao,
    private val buffetMenuDao: BuffetMenuDao,
    private val buffetClosingDao: BuffetClosingDao
) {
    val allDishes: Flow<List<DishEntity>> = dishDao.getAllDishes()
    val allRecipes: Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()
    val helpDeskMessages: Flow<List<HelpDeskMessageEntity>> = helpDeskDao.getAllMessages()
    val callbackRequests: Flow<List<CallbackRequestEntity>> = helpDeskDao.getAllCallbacks()
    val allBuffetMenuItems: Flow<List<BuffetMenuItemEntity>> = buffetMenuDao.getAllMenuItems()
    val allBuffetClosingRecords: Flow<List<BuffetClosingEntity>> = buffetClosingDao.getAllClosingRecords()

    suspend fun initializeDefaultData() {
        if (dishDao.getDishCount() == 0) {
            dishDao.insertDishes(defaultDishes)
        }
        
        val currentRecipes = recipeDao.getRecipeCount()
        val hasPremiumStarters = recipeDao.searchRecipes("Classic Italian Bruschetta").firstOrNull()?.isNotEmpty() == true
        val hasPremiumSoups = recipeDao.searchRecipes("Classic French Onion Soup").firstOrNull()?.isNotEmpty() == true
        
        if (currentRecipes == 0) {
            recipeDao.insertRecipes(defaultRecipes + extendedMocktails + extendedSoups + extendedSalads + extendedStarters + extendedMainCourses + extendedDesserts)
        } else if (!hasPremiumStarters || !hasPremiumSoups || currentRecipes < 100) {
            // Force upgrade existing database with the new extensive libraries to ensure user sees them
            recipeDao.insertRecipes(extendedMocktails + extendedSoups + extendedSalads + extendedStarters + extendedMainCourses + extendedDesserts)
        }
        
        if (buffetMenuDao.getMenuItemCount() == 0) {
            buffetMenuDao.insertMenuItems(defaultBuffetMenuItems)
        }
        val currentProfile = userProfileDao.getUserProfile().firstOrNull()
        if (currentProfile == null) {
            userProfileDao.insertOrUpdateProfile(UserProfileEntity())
        }
    }

    suspend fun insertDish(dish: DishEntity): Long = dishDao.insertDish(dish)

    suspend fun insertRecipes(recipes: List<RecipeEntity>) = recipeDao.insertRecipes(recipes)
    suspend fun updateDish(dish: DishEntity) = dishDao.updateDish(dish)
    suspend fun deleteDish(dish: DishEntity) = dishDao.deleteDish(dish)
    suspend fun clearDishes() = dishDao.clearDishes()
    suspend fun insertDishes(dishes: List<DishEntity>) = dishDao.insertDishes(dishes)

    suspend fun insertRecipe(recipe: RecipeEntity): Long = recipeDao.insertRecipe(recipe)
    fun searchRecipes(query: String): Flow<List<RecipeEntity>> = recipeDao.searchRecipes(query)
    fun getRecipesByCategory(category: String): Flow<List<RecipeEntity>> = recipeDao.getRecipesByCategory(category)

    fun getBuffetMenuItems(day: String, session: String): Flow<List<BuffetMenuItemEntity>> =
        buffetMenuDao.getMenuItems(day, session)

    suspend fun insertBuffetMenuItem(item: BuffetMenuItemEntity): Long =
        buffetMenuDao.insertMenuItem(item)

    suspend fun insertBuffetMenuItems(items: List<BuffetMenuItemEntity>) =
        buffetMenuDao.insertMenuItems(items)

    suspend fun updateBuffetMenuItem(item: BuffetMenuItemEntity) =
        buffetMenuDao.updateMenuItem(item)

    suspend fun deleteBuffetMenuItem(item: BuffetMenuItemEntity) =
        buffetMenuDao.deleteMenuItem(item)

    suspend fun deleteBuffetMenuItemById(id: Long) =
        buffetMenuDao.deleteMenuItemById(id)

    suspend fun clearBuffetMenu(day: String, session: String) =
        buffetMenuDao.clearSessionMenu(day, session)

    suspend fun duplicateBuffetMenu(fromDay: String, fromSession: String, toDay: String, toSession: String) {
        val existing = buffetMenuDao.getMenuItemsList(fromDay, fromSession)
        val copies = existing.map { it.copy(id = 0, dayOfWeek = toDay, mealSession = toSession) }
        buffetMenuDao.insertMenuItems(copies)
    }

    suspend fun reorderBuffetMenuItem(item: BuffetMenuItemEntity, moveUp: Boolean) {
        val list = buffetMenuDao.getMenuItemsList(item.dayOfWeek, item.mealSession).toMutableList()
        val index = list.indexOfFirst { it.id == item.id }
        if (index == -1) return
        val targetIndex = if (moveUp) index - 1 else index + 1
        if (targetIndex in list.indices) {
            val other = list[targetIndex]
            val newSortOrderOther = item.sortOrder
            val newSortOrderItem = other.sortOrder
            buffetMenuDao.updateMenuItem(item.copy(sortOrder = if (newSortOrderItem == newSortOrderOther) (if (moveUp) newSortOrderOther - 1 else newSortOrderOther + 1) else newSortOrderItem))
            buffetMenuDao.updateMenuItem(other.copy(sortOrder = newSortOrderOther))
        }
    }

    suspend fun updateProfile(profile: UserProfileEntity) = userProfileDao.insertOrUpdateProfile(profile)
    suspend fun updateTheme(theme: String) = userProfileDao.updateTheme(theme)
    suspend fun updateLanguage(lang: String) = userProfileDao.updateLanguage(lang)
    suspend fun updateTemplate(template: String) = userProfileDao.updateTemplate(template)
    suspend fun updateLogo(logo: String?, isLocked: Boolean) = userProfileDao.updateLogo(logo, isLocked)

    suspend fun sendHelpDeskMessage(sender: String, message: String): Long {
        return helpDeskDao.insertMessage(HelpDeskMessageEntity(sender = sender, message = message))
    }

    suspend fun requestCallback(phone: String): Long {
        return helpDeskDao.insertCallback(CallbackRequestEntity(phone = phone))
    }

    suspend fun getLatestClosingRecord(day: String, session: String): BuffetClosingEntity? =
        buffetClosingDao.getLatestClosingRecord(day, session)

    suspend fun insertClosingRecord(record: BuffetClosingEntity): Long =
        buffetClosingDao.insertClosingRecord(record)

    suspend fun clearClosingRecords() = buffetClosingDao.clearAllRecords()

    companion object {
        val defaultDishes = listOf(
            DishEntity(
                name = "Virgin Mojito",
                desc = "Refreshing mint and lime mocktail.",
                cals = "45 kcal per 100 ml",
                type = "veg",
                category = "Mocktails / Non-Alcoholic Beverages",
                allergens = emptyList(),
                isSample = true
            ),
            DishEntity(
                name = "Crisp Vegetable Spring Rolls",
                desc = "Golden fried rolls with sweet chili dip.",
                cals = "185 kcal per 100 g",
                type = "veg",
                category = "Starters / Pass Arounds",
                allergens = listOf("Gluten", "Soya"),
                isSample = true
            ),
            DishEntity(
                name = "Caesar Salad",
                desc = "Crisp romaine lettuce with Caesar dressing, garlic croutons, and parmesan.",
                cals = "145 kcal per 100 g",
                type = "veg",
                category = "Salads & Accompaniments",
                allergens = listOf("Milk", "Gluten", "Eggs"),
                isSample = true
            ),
            DishEntity(
                name = "Curd, Pickle And Papad",
                desc = "Cooling curd with traditional pickle and crisp papad.",
                cals = "125 kcal per 100 g",
                type = "veg",
                category = "Salads & Accompaniments",
                allergens = listOf("Milk", "Mustard", "Gluten"),
                isSample = true
            ),
            DishEntity(
                name = "Cream Of Sweet Corn Soup",
                desc = "Creamy sweet corn soup with gentle seasoning.",
                cals = "92 kcal per 100 ml",
                type = "veg",
                category = "Soup",
                allergens = listOf("Milk", "Celery"),
                isSample = true
            ),
            DishEntity(
                name = "Chicken Butter Masala",
                desc = "Tender chicken chunks in a rich buttery tomato gravy.",
                cals = "295 kcal per 100 g",
                type = "nonveg",
                category = "Main Course",
                allergens = listOf("Milk", "Nuts"),
                isSample = true
            ),
            DishEntity(
                name = "Paneer Zafrani Korma",
                desc = "Cottage cheese in a saffron-scented creamy gravy.",
                cals = "235 kcal per 100 g",
                type = "veg",
                category = "Main Course",
                allergens = listOf("Milk", "Nuts"),
                isSample = true
            ),
            DishEntity(
                name = "Jeera Rice",
                desc = "Basmati rice with cumin and butter.",
                cals = "190 kcal per 100 g",
                type = "veg",
                category = "Main Course",
                allergens = listOf("Milk"),
                isSample = true
            ),
            DishEntity(
                name = "Chapati",
                desc = "Soft whole-wheat flatbread cooked on the griddle.",
                cals = "275 kcal per 100 g",
                type = "veg",
                category = "Main Course",
                allergens = listOf("Gluten", "Milk"),
                isSample = true
            ),
            DishEntity(
                name = "Baked Mihidana",
                desc = "Traditional mihidana baked with sweetened milk.",
                cals = "285 kcal per 100 g",
                type = "veg",
                category = "Desserts",
                allergens = listOf("Milk", "Gluten"),
                isSample = true
            )
        )

        val defaultRecipes = listOf(
            RecipeEntity(
                name = "Caesar Salad",
                category = "Salads",
                prepTime = "15 mins",
                yieldPortions = "2 Portions",
                cals = "210 kcal / serving",
                desc = "Crisp romaine lettuce tossed in a creamy garlic and parmesan dressing with crunchy golden croutons.",
                story = "Invented in Tijuana, Mexico, in 1924 by Italian immigrant restaurateur Caesar Cardini during a bustling Independence Day weekend when supplies ran low and he improvised with ingredients on hand.",
                ingredients = listOf(
                    "1 Head Romaine Lettuce chopped",
                    "1 cup Garlic Croutons",
                    "1/2 cup Grated Parmesan cheese",
                    "1/4 cup Caesar dressing",
                    "Freshly cracked black pepper"
                ),
                steps = listOf(
                    "1. Wash and dry romaine lettuce leaves thoroughly, tearing into bite-sized pieces.",
                    "2. Toss lettuce in a chilled bowl with creamy Caesar dressing until evenly coated.",
                    "3. Top generously with garlic croutons and shaved parmesan cheese.",
                    "4. Finish with freshly ground black pepper and serve immediately."
                )
            ),
            RecipeEntity(
                name = "Chicken Butter Masala",
                category = "Main Course",
                prepTime = "35 mins",
                yieldPortions = "2 Portions",
                cals = "310 kcal / serving",
                desc = "Tender tandoori chicken pieces simmered in a luscious, velvety spiced tomato and butter sauce.",
                story = "Invented in the legendary Moti Mahal restaurant in Delhi by Kundan Lal Gujral in the late 1940s to repurpose leftover tandoori chicken into a rich, velvety tomato sauce.",
                ingredients = listOf(
                    "400g Boneless Chicken Thighs",
                    "200g Fresh Tomato Puree",
                    "50g Butter",
                    "1/2 cup Heavy Cream",
                    "1 tbsp Kasuri Methi (Fenugreek)",
                    "Ginger-garlic paste & Garam Masala"
                ),
                steps = listOf(
                    "1. Marinate chicken chunks with yogurt and spices, then roast until tender.",
                    "2. In a heavy pan, melt butter and sauté ginger-garlic paste with tomato puree until glossy.",
                    "3. Stir in cream, kasuri methi, and simmer for 15 minutes.",
                    "4. Fold in the roasted chicken and simmer for 5 minutes before serving for 2."
                )
            ),
            RecipeEntity(
                name = "Pasta Carbonara",
                category = "Main Course",
                prepTime = "20 mins",
                yieldPortions = "2 Portions",
                cals = "380 kcal / serving",
                desc = "Classic Roman pasta with crispy pancetta, eggs, pecorino romano cheese, and black pepper.",
                story = "Originating in Rome, Carbonara became internationally famous for its silky, rich sauce formed entirely from eggs, cheese, and pasta water without using cream.",
                ingredients = listOf(
                    "200g Spaghetti",
                    "100g Pancetta or Guanciale",
                    "2 Large Egg yolks",
                    "1/2 cup Pecorino Romano cheese grated",
                    "Black pepper"
                ),
                steps = listOf(
                    "1. Boil spaghetti in salted water until al dente.",
                    "2. Crisp pancetta in a skillet until golden.",
                    "3. Whisk egg yolks and pecorino cheese in a bowl.",
                    "4. Toss hot pasta with pancetta and remove from heat; rapidly stir in egg and cheese mixture to create a glossy sauce."
                )
            ),
            RecipeEntity(
                name = "Aloo Gobi",
                category = "Main Course",
                prepTime = "25 mins",
                yieldPortions = "2 Portions",
                cals = "175 kcal / serving",
                desc = "Stir-fried potatoes and cauliflower florets seasoned with turmeric, ginger, and cumin.",
                story = "A staple vegetarian dish across North India and global Indian restaurants, celebrated for its tender-crisp texture and warm spices.",
                ingredients = listOf(
                    "2 Potatoes cubed",
                    "1 Small Cauliflower cut into florets",
                    "1 tsp Cumin seeds & Turmeric",
                    "1 Onion chopped",
                    "Fresh cilantro"
                ),
                steps = listOf(
                    "1. Lightly pan-fry potato and cauliflower florets in oil until edges turn golden.",
                    "2. Sauté cumin seeds, onions, ginger, and turmeric in a separate pan.",
                    "3. Combine potatoes and cauliflower with spices, cover, and steam on low heat until tender.",
                    "4. Garnish with fresh cilantro and serve warm."
                )
            ),
            RecipeEntity(
                name = "Thai Green Curry",
                category = "Main Course",
                prepTime = "30 mins",
                yieldPortions = "2 Portions",
                cals = "290 kcal / serving",
                desc = "Fragrant Thai green curry paste simmered in coconut milk with bamboo shoots and basil.",
                story = "A classic Thai masterpiece blending fiery green chilies, galangal, lemongrass, and rich coconut milk for an aromatic explosion of flavor.",
                ingredients = listOf(
                    "2 tbsp Green Curry Paste",
                    "1 cup Coconut Milk",
                    "200g Chicken breast or Tofu",
                    "1/2 cup Bamboo shoots & Eggplant",
                    "Fresh Thai basil leaves"
                ),
                steps = listOf(
                    "1. Fry green curry paste in a pan with a splash of coconut milk until fragrant.",
                    "2. Add sliced chicken or tofu and sear until coated.",
                    "3. Pour in remaining coconut milk, bamboo shoots, and eggplant; simmer until tender.",
                    "4. Stir in fresh Thai basil leaves and serve hot with jasmine rice."
                )
            ),
            RecipeEntity(
                name = "Samosa",
                category = "Starters & Appetizers",
                prepTime = "30 mins",
                yieldPortions = "2 Portions (4 Pieces)",
                cals = "220 kcal / serving",
                desc = "Crispy golden triangular pastry filled with spiced potatoes and green peas.",
                story = "Tracing back to Central Asia before the 10th century, samosas travelled along trade routes to become one of the most famous hotel starters worldwide.",
                ingredients = listOf(
                    "200g All-purpose flour",
                    "3 Potatoes boiled and cubed",
                    "1/2 cup Green peas",
                    "1 tsp Cumin and Coriander seeds",
                    "Oil for frying"
                ),
                steps = listOf(
                    "1. Knead flour with carom seeds, salt, and oil into a firm dough.",
                    "2. Prepare a spiced potato and pea filling with cumin and coriander.",
                    "3. Roll dough into cones, stuff with filling, and seal edges securely.",
                    "4. Deep fry on medium height until golden brown and crispy."
                )
            ),
            RecipeEntity(
                name = "Naan",
                category = "Main Course",
                prepTime = "20 mins",
                yieldPortions = "2 Portions (4 Naans)",
                cals = "260 kcal / serving",
                desc = "Soft and pillowy leavened flatbread baked in a traditional clay tandoor oven.",
                story = "An indispensable accompaniment in Indian and Middle Eastern dining, traditionally baked against the blazing hot walls of a tandoor oven.",
                ingredients = listOf(
                    "250g Maida / Refined flour",
                    "1/2 cup Warm milk or yogurt",
                    "1 tsp Yeast or baking powder",
                    "1 tsp Sugar",
                    "Melted butter & garlic"
                ),
                steps = listOf(
                    "1. Mix flour, yeast, sugar, yogurt, and warm water to form a soft dough. Rest for 1 hour.",
                    "2. Divide into equal portions and roll out into teardrop shapes.",
                    "3. Cook on a hot cast-iron skillet or tandoor until bubbles form and edges char.",
                    "4. Brush generously with garlic butter before serving."
                )
            ),
            RecipeEntity(
                name = "Apple Pie",
                category = "Sweets & Confectionery",
                prepTime = "50 mins",
                yieldPortions = "2 Portions",
                cals = "350 kcal / serving",
                desc = "Classic baked pastry crust packed with spiced, tender sliced apples and cinnamon.",
                story = "A timeless symbol of comfort baking, combining sweet orchard fruits encased in a flaky, buttery crust.",
                ingredients = listOf(
                    "2 Pre-made pie crust sheets",
                    "4 Tart Apples peeled and sliced",
                    "1/3 cup Brown sugar",
                    "1 tsp Cinnamon powder",
                    "1 tbsp Butter"
                ),
                steps = listOf(
                    "1. Toss apple slices with brown sugar, cinnamon, and lemon juice.",
                    "2. Line a baking dish with bottom pie crust and fill with spiced apples.",
                    "3. Top with second crust, crimp edges, and cut vents on top.",
                    "4. Bake at 190°C for 35 minutes until golden brown."
                )
            ),
            RecipeEntity(
                name = "Gulab Jamun",
                category = "Sweets & Confectionery",
                prepTime = "40 mins",
                yieldPortions = "2 Portions (4 Pieces)",
                cals = "150 kcal / piece",
                desc = "Deep-fried khoya dumplings soaked in fragrant rose and cardamom sugar syrup.",
                story = "Tracing its roots to medieval Persian pastry brought to India, Gulab Jamun evolved using reduced milk solids (khoya).",
                ingredients = listOf(
                    "100g Khoya / Mawa",
                    "1.5 tbsp All-Purpose Flour",
                    "A pinch of Baking Soda",
                    "1 cup Sugar",
                    "1 cup Water",
                    "Cardamom powder & Rose Water"
                ),
                steps = listOf(
                    "1. Knead khoya, flour and baking soda into a velvety smooth dough.",
                    "2. Divide into 4 equal small balls ensuring no cracks on the surface.",
                    "3. Deep fry on low-medium ghee until deeply golden.",
                    "4. Submerge in warm sugar syrup infused with cardamom and rose water for 30 minutes."
                )
            ),
            RecipeEntity(
                name = "Dal Makhani",
                category = "Main Course",
                prepTime = "45 mins",
                yieldPortions = "2 Portions",
                cals = "210 kcal / serving",
                desc = "Slow-simmered whole black lentils with tomatoes, butter, and cream.",
                story = "Originated in Punjab, slow cooked overnight on charcoal embers to achieve its signature velvety texture and smokiness.",
                ingredients = listOf("1 cup Black Urad dal soaked", "1/4 cup Kidney beans", "2 tbsp Butter & Cream", "Pureed tomatoes & Ginger-garlic"),
                steps = listOf("1. Pressure cook soaked dal until velvety.", "2. Sauté ginger-garlic and tomato puree in butter.", "3. Simmer dal for 40 minutes on low flame with cream.")
            ),
            RecipeEntity(
                name = "Paneer Tikka",
                category = "Starters & Appetizers",
                prepTime = "25 mins",
                yieldPortions = "2 Portions",
                cals = "225 kcal / serving",
                desc = "Marinated cottage cheese cubes skewered with bell peppers and grilled in tandoor.",
                story = "An Indian culinary crown jewel, bringing smoky char and tangy spices to velvety fresh paneer.",
                ingredients = listOf("250g Paneer cubes", "Bell peppers and onions", "1/2 cup Hung curd", "Mustard oil, ajwain & chili"),
                steps = listOf("1. Marinate paneer in spiced yogurt marinade.", "2. Skewer with veggies.", "3. Roast in tandoor or high-heat grill until charred.")
            ),
            RecipeEntity(
                name = "Virgin Mojito",
                category = "Beverages & Mocktails",
                prepTime = "5 mins",
                yieldPortions = "1 Glass",
                cals = "45 kcal / serving",
                desc = "Crisp muddled fresh garden mint, Persian lime juice, raw cane syrup, and sparkling soda.",
                story = "A refreshing Cuban-inspired classic mocktail that cleanses the palate before grand banquets.",
                ingredients = listOf("Fresh mint leaves", "Lime wedges", "Sugar syrup", "Sparkling soda & Crushed ice"),
                steps = listOf("1. Gently muddle mint and lime wedges in highball glass.", "2. Fill with crushed ice, add syrup and top with sparkling soda.")
            ),
            RecipeEntity(
                name = "Cream Of Sweet Corn Soup",
                category = "Soups",
                prepTime = "20 mins",
                yieldPortions = "2 Portions",
                cals = "92 kcal / serving",
                desc = "Velvety sweet corn soup infused with vegetable broth and gentle white pepper.",
                story = "A premier banquet staple beloved for its comforting creamy warmth and subtle sweet notes.",
                ingredients = listOf("1 can Cream-style sweet corn", "2 cups Vegetable stock", "Finely minced carrots & celery", "White pepper & cornstarch"),
                steps = listOf("1. Bring stock and corn to a simmer.", "2. Stir in minced vegetables.", "3. Thicken with light cornstarch slurry and season.")
            ),
            RecipeEntity(
                name = "Vegetable Dum Biryani",
                category = "Main Course",
                prepTime = "40 mins",
                yieldPortions = "2 Portions",
                cals = "280 kcal / serving",
                desc = "Aromatic aged basmati rice layered with garden vegetables, saffron milk, and fried onions.",
                story = "The royal slow-steamed 'Dum' technique seals in fragrances of saffron, rose water, and whole cardamom.",
                ingredients = listOf("2 cups Aged Basmati rice", "Mixed garden vegetables", "Saffron infused milk", "Biryani masala & Ghee", "Fried onions & Mint"),
                steps = listOf("1. Parboil basmati rice with whole spices.", "2. Layer with spiced cooked vegetables.", "3. Seal pot and cook on low dum for 25 minutes.")
            )
        )

        val defaultBuffetMenuItems = listOf(
            // Welcome Beverages
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Welcome Beverages",
                name = "Virgin Mint Mojito",
                desc = "Freshly muddled garden mint with Persian lime and chilled soda.",
                cals = "45 kcal / 100 ml",
                type = "veg",
                prepTime = "10 mins",
                portions = "80 Pax",
                station = "Mocktail Dispensary",
                allergens = emptyList(),
                sortOrder = 1
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Welcome Beverages",
                name = "Spiced Buttermilk (Masala Chaas)",
                desc = "Cooling churned curd with roasted cumin, ginger, and curry leaves.",
                cals = "35 kcal / 100 ml",
                type = "veg",
                prepTime = "15 mins",
                portions = "80 Pax",
                station = "Beverage Counter",
                allergens = listOf("Milk"),
                sortOrder = 2
            ),
            // Starters & Live Counter
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Starters & Live Counter",
                name = "Crisp Vegetable Spring Rolls",
                desc = "Wok-tossed julienne vegetables encased in crisp golden pastry.",
                cals = "185 kcal / 100 g",
                type = "veg",
                prepTime = "25 mins",
                portions = "80 Pax",
                station = "Live Fry Station",
                allergens = listOf("Gluten", "Soya"),
                sortOrder = 3
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Starters & Live Counter",
                name = "Tandoori Paneer Tikka",
                desc = "Smoked cottage cheese cubes skewered with bell peppers and tandoor grilled.",
                cals = "225 kcal / 100 g",
                type = "veg",
                prepTime = "30 mins",
                portions = "80 Pax",
                station = "Live Tandoor Counter",
                allergens = listOf("Milk", "Mustard"),
                sortOrder = 4
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Starters & Live Counter",
                name = "Crispy Golden Samosa",
                desc = "Handcrafted flaky cones stuffed with spiced potatoes and green peas.",
                cals = "220 kcal / 100 g",
                type = "veg",
                prepTime = "35 mins",
                portions = "80 Pax",
                station = "Chafing Dish #1",
                allergens = listOf("Gluten"),
                sortOrder = 5
            ),
            // Soups & Breads
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Soups & Breads",
                name = "Cream Of Sweet Corn Soup",
                desc = "Velvety sweet corn soup served with seasoned herb croutons.",
                cals = "92 kcal / 100 ml",
                type = "veg",
                prepTime = "20 mins",
                portions = "80 Pax",
                station = "Soup Kettle #1",
                allergens = listOf("Milk", "Celery"),
                sortOrder = 6
            ),
            // Salad Bar
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Salad Bar",
                name = "Classic Caesar Salad",
                desc = "Crisp romaine hearts tossed with parmesan shavings and garlic bread crisps.",
                cals = "145 kcal / 100 g",
                type = "veg",
                prepTime = "15 mins",
                portions = "80 Pax",
                station = "Chilled Salad Bar",
                allergens = listOf("Milk", "Gluten"),
                sortOrder = 7
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Salad Bar",
                name = "Curd, Pickle & Papad Assortment",
                desc = "Artisan accompaniments with spiced mango relish and roasted papad.",
                cals = "125 kcal / 100 g",
                type = "veg",
                prepTime = "10 mins",
                portions = "80 Pax",
                station = "Condiment Island",
                allergens = listOf("Milk", "Mustard"),
                sortOrder = 8
            ),
            // Main Course (Veg)
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Main Course (Veg)",
                name = "Paneer Zafrani Korma",
                desc = "Cottage cheese chunks cooked in fragrant saffron cashew gravy.",
                cals = "235 kcal / 100 g",
                type = "veg",
                prepTime = "35 mins",
                portions = "80 Pax",
                station = "Chafing Dish #3",
                allergens = listOf("Milk", "Nuts"),
                sortOrder = 9
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Main Course (Veg)",
                name = "Slow-Simmered Dal Makhani",
                desc = "Whole black lentils slow cooked overnight with churned butter and cream.",
                cals = "210 kcal / 100 g",
                type = "veg",
                prepTime = "45 mins",
                portions = "80 Pax",
                station = "Chafing Dish #4",
                allergens = listOf("Milk"),
                sortOrder = 10
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Main Course (Veg)",
                name = "Aloo Gobi Adraki",
                desc = "Tender cauliflower and potatoes tempered with fresh ginger and cumin.",
                cals = "175 kcal / 100 g",
                type = "veg",
                prepTime = "25 mins",
                portions = "80 Pax",
                station = "Chafing Dish #5",
                allergens = emptyList(),
                sortOrder = 11
            ),
            // Main Course (Non-Veg)
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Main Course (Non-Veg)",
                name = "Chicken Butter Masala",
                desc = "Chargrilled tandoori chicken cooked in rich satin tomato and makhani gravy.",
                cals = "295 kcal / 100 g",
                type = "nonveg",
                prepTime = "35 mins",
                portions = "80 Pax",
                station = "Chafing Dish #6",
                allergens = listOf("Milk", "Nuts"),
                sortOrder = 12
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Main Course (Non-Veg)",
                name = "Thai Green Curry Chicken",
                desc = "Succulent chicken and crisp bamboo shoots in aromatic coconut and basil broth.",
                cals = "290 kcal / 100 g",
                type = "nonveg",
                prepTime = "30 mins",
                portions = "80 Pax",
                station = "Live Wok Station",
                allergens = emptyList(),
                sortOrder = 13
            ),
            // Accompaniments & Rices
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Accompaniments & Rices",
                name = "Fragrant Jeera Basmati Rice",
                desc = "Long grain basmati steamed with roasted cumin and dairy ghee.",
                cals = "190 kcal / 100 g",
                type = "veg",
                prepTime = "20 mins",
                portions = "80 Pax",
                station = "Rice Warmer",
                allergens = listOf("Milk"),
                sortOrder = 14
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Accompaniments & Rices",
                name = "Tandoori Garlic Butter Naan",
                desc = "Pillowy clay oven flatbread brushed with garlic butter.",
                cals = "260 kcal / 100 g",
                type = "veg",
                prepTime = "15 mins",
                portions = "80 Pax",
                station = "Live Bread Basket",
                allergens = listOf("Gluten", "Milk"),
                sortOrder = 15
            ),
            // Desserts & Sweets
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Desserts & Sweets",
                name = "Gulab Jamun with Rose Syrup",
                desc = "Warm caramelized khoya dumplings soaked in fragrant cardamom sugar syrup.",
                cals = "150 kcal / piece",
                type = "veg",
                prepTime = "40 mins",
                portions = "80 Pax",
                station = "Hot Dessert Chafing",
                allergens = listOf("Milk", "Gluten"),
                sortOrder = 16
            ),
            BuffetMenuItemEntity(
                dayOfWeek = "Monday",
                mealSession = "Lunch",
                courseSection = "Desserts & Sweets",
                name = "Baked Mihidana with Cream",
                desc = "Delicate heritage Bengal sweet baked with thickened saffron cream.",
                cals = "285 kcal / 100 g",
                type = "veg",
                prepTime = "35 mins",
                portions = "80 Pax",
                station = "Cold Pastry Showcase",
                allergens = listOf("Milk", "Gluten"),
                sortOrder = 17
            )
        )
    }
}

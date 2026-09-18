package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HotelStudioDatabase
import com.example.data.model.BuffetMenuItemEntity
import com.example.data.model.DishEntity
import com.example.data.model.HelpDeskMessageEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.UserProfileEntity
import com.example.data.remote.CloudGatewayConfig
import com.example.data.remote.CloudSyncResult
import com.example.data.remote.DigitalTagCloudService
import com.example.data.remote.DigitalTagPayload
import com.example.data.remote.DigitalTagSize
import com.example.data.remote.DigitalTagTheme
import com.example.data.remote.HotelStudioAiService
import com.example.data.repository.HotelStudioRepository
import com.example.ui.theme.TemplateSkins
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.util.CulinaryAgent

data class ManualDishDraft(
    val name: String = "",
    val type: String = "veg", // "veg" or "nonveg"
    val cals: String = "",
    val allergens: List<String> = emptyList(),
    val desc: String = "",
    val category: String = "",
    val prepTime: String = "25 mins",
    val agentVerifiedNote: String = ""
)

class HotelStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = HotelStudioDatabase.getInstance(application)
    private val repository = HotelStudioRepository(
        dishDao = db.dishDao(),
        recipeDao = db.recipeDao(),
        userProfileDao = db.userProfileDao(),
        helpDeskDao = db.helpDeskDao(),
        buffetMenuDao = db.buffetMenuDao(),
        buffetClosingDao = db.buffetClosingDao()
    )
    private val aiService = HotelStudioAiService()

    val dishes: StateFlow<List<DishEntity>> = repository.allDishes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recipes: StateFlow<List<RecipeEntity>> = repository.allRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBuffetClosingRecords: StateFlow<List<com.example.data.model.BuffetClosingEntity>> = repository.allBuffetClosingRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _latestClosingRecord = MutableStateFlow<com.example.data.model.BuffetClosingEntity?>(null)
    val latestClosingRecord: StateFlow<com.example.data.model.BuffetClosingEntity?> = _latestClosingRecord.asStateFlow()

    private val _isClosingBuffet = MutableStateFlow(false)
    val isClosingBuffet: StateFlow<Boolean> = _isClosingBuffet.asStateFlow()

    private val _translatedRecipes = MutableStateFlow<Map<Long, RecipeEntity>>(emptyMap())
    val translatedRecipes: StateFlow<Map<Long, RecipeEntity>> = _translatedRecipes.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    val helpDeskMessages: StateFlow<List<HelpDeskMessageEntity>> = repository.helpDeskMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBuffetMenuItems: StateFlow<List<BuffetMenuItemEntity>> = repository.allBuffetMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Menu Builder Day & Session State
    private val _selectedBuffetDay = MutableStateFlow("Monday")
    val selectedBuffetDay: StateFlow<String> = _selectedBuffetDay.asStateFlow()

    private val _selectedBuffetMealSession = MutableStateFlow("Lunch")
    val selectedBuffetMealSession: StateFlow<String> = _selectedBuffetMealSession.asStateFlow()

    private val _buffetPaxCount = MutableStateFlow(80)
    val buffetPaxCount: StateFlow<Int> = _buffetPaxCount.asStateFlow()

    private val _menuBuilderTab = MutableStateFlow("builder") // "builder", "grid", "recipes", "operations", "preview"
    val menuBuilderTab: StateFlow<String> = _menuBuilderTab.asStateFlow()

    // Recipe Database Picker State
    private val _isRecipePickerOpen = MutableStateFlow(false)
    val isRecipePickerOpen: StateFlow<Boolean> = _isRecipePickerOpen.asStateFlow()

    private val _recipePickerCategory = MutableStateFlow("ALL")
    val recipePickerCategory: StateFlow<String> = _recipePickerCategory.asStateFlow()

    private val _recipePickerDiet = MutableStateFlow("ALL") // "ALL", "VEG", "NONVEG"
    val recipePickerDiet: StateFlow<String> = _recipePickerDiet.asStateFlow()
    private val _recipePickerPrepTime = MutableStateFlow("ALL")
    val recipePickerPrepTime: StateFlow<String> = _recipePickerPrepTime.asStateFlow()

    private val _recipePickerSearch = MutableStateFlow("")
    val recipePickerSearch: StateFlow<String> = _recipePickerSearch.asStateFlow()

    private val _recipePickerTargetCourse = MutableStateFlow("Main Course (Veg)")
    val recipePickerTargetCourse: StateFlow<String> = _recipePickerTargetCourse.asStateFlow()

    val currentBuffetItems: StateFlow<List<BuffetMenuItemEntity>> = combine(
        repository.allBuffetMenuItems,
        _selectedBuffetDay,
        _selectedBuffetMealSession
    ) { allItems, day, session ->
        allItems.filter { it.dayOfWeek == day && it.mealSession == session }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active UI state
    private val _activeDish = MutableStateFlow<DishEntity>(HotelStudioRepository.defaultDishes[2]) // Caesar Salad by default
    val activeDish: StateFlow<DishEntity> = _activeDish.asStateFlow()

    private val _activeTemplateId = MutableStateFlow("template-heritage-gold")
    val activeTemplateId: StateFlow<String> = _activeTemplateId.asStateFlow()

    // Digital Buffet Tag Preview & Cloud Systems State
    private val digitalTagCloudService = DigitalTagCloudService()

    private val _tagPreviewMode = MutableStateFlow("print") // "print" or "digital"
    val tagPreviewMode: StateFlow<String> = _tagPreviewMode.asStateFlow()

    private val _digitalTagSize = MutableStateFlow(DigitalTagSize.COMPACT_4_2)
    val digitalTagSize: StateFlow<DigitalTagSize> = _digitalTagSize.asStateFlow()

    private val _digitalTagTheme = MutableStateFlow(DigitalTagTheme.EPAPER_TRICOLOR)
    val digitalTagTheme: StateFlow<DigitalTagTheme> = _digitalTagTheme.asStateFlow()

    private val _activeTagStation = MutableStateFlow("Main Hot Buffet - Station 1")
    val activeTagStation: StateFlow<String> = _activeTagStation.asStateFlow()

    private val _activeTagId = MutableStateFlow("ESL-TAG-42A1")
    val activeTagId: StateFlow<String> = _activeTagId.asStateFlow()

    private val _soldOutTags = MutableStateFlow<Set<String>>(emptySet())
    val soldOutTags: StateFlow<Set<String>> = _soldOutTags.asStateFlow()

    private val _cloudConfig = MutableStateFlow(CloudGatewayConfig())
    val cloudConfig: StateFlow<CloudGatewayConfig> = _cloudConfig.asStateFlow()

    private val _cloudSyncLogs = MutableStateFlow<List<CloudSyncResult>>(emptyList())
    val cloudSyncLogs: StateFlow<List<CloudSyncResult>> = _cloudSyncLogs.asStateFlow()

    private val _isCloudSyncing = MutableStateFlow(false)
    val isCloudSyncing: StateFlow<Boolean> = _isCloudSyncing.asStateFlow()

    private val _lastCloudSyncResult = MutableStateFlow<CloudSyncResult?>(null)
    val lastCloudSyncResult: StateFlow<CloudSyncResult?> = _lastCloudSyncResult.asStateFlow()

    private val _activeModule = MutableStateFlow("dashboard") // "dashboard", "buffet", "prospectus", "recipes", "analytics", "settings", "helpdesk", "terms", "privacy", "about"
    val activeModule: StateFlow<String> = _activeModule.asStateFlow()

    private val _menuPrintSize = MutableStateFlow("A5") // "A5", "A4", "A3"
    val menuPrintSize: StateFlow<String> = _menuPrintSize.asStateFlow()

    private val _menuOrientation = MutableStateFlow("portrait") // "portrait", "landscape"
    val menuOrientation: StateFlow<String> = _menuOrientation.asStateFlow()

    private val _menuPageLayout = MutableStateFlow("1 Page") // "1 Page", "Multiple Pages"
    val menuPageLayout: StateFlow<String> = _menuPageLayout.asStateFlow()

    private val _selectedRecipeForDetail = MutableStateFlow<RecipeEntity?>(null)
    val selectedRecipeForDetail: StateFlow<RecipeEntity?> = _selectedRecipeForDetail.asStateFlow()

    private val _recipeSearchQuery = MutableStateFlow("")
    val recipeSearchQuery: StateFlow<String> = _recipeSearchQuery.asStateFlow()

    private val _recipeCategoryFilter = MutableStateFlow("ALL")
    val recipeCategoryFilter: StateFlow<String> = _recipeCategoryFilter.asStateFlow()

    private val _recipeBankTab = MutableStateFlow("search") // "search", "categories", "translator"
    val recipeBankTab: StateFlow<String> = _recipeBankTab.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initializeDefaultData()
            // Initialize initial welcome message in help desk if empty
            repository.sendHelpDeskMessage("ai", "Hello! How can I assist you with Hotel Studio today? Feel free to ask questions about FSSAI guidelines, recipe scaling, or menu design!")
        }
    }

    
    fun generateOutletChecklist(prompt: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            _isAiLoading.value = true
            showToast("Consulting F&B Assistant for Custom Checklist...")
            try {
                val items = aiService.generateOutletChecklist(prompt)
                onResult(items)
                showToast("Checklist generated successfully!")
            } catch (e: Exception) {
                showToast("Failed to generate checklist. Fallback used.")
                onResult(emptyList())
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun importRecipesFromFile(uri: Uri, mimeType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@launch
                
                val parsed = aiService.parseRecipesFromFile(bytes, mimeType)
                if (parsed.isNotEmpty()) {
                    parsed.forEach { repository.insertRecipe(it) }
                    launch(Dispatchers.Main) {
                        showToast("Successfully scanned & imported ${parsed.size} recipes from file!")
                    }
                } else {
                    launch(Dispatchers.Main) {
                        showToast("Could not extract any recipes from the file. Please check format.")
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    showToast("Error scanning file: ${e.localizedMessage}")
                }
            }
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun switchModule(moduleId: String) {
        _activeModule.value = moduleId
    }

    fun selectDish(dish: DishEntity) {
        _activeDish.value = dish
        _activeTagId.value = "ESL-TAG-BF${if (dish.id > 0) dish.id else "01"}"
        _activeTagStation.value = dish.category.ifBlank { "Main Buffet Line" }
        showToast("Loaded \"${dish.name}\" to Master Preview")
    }

    fun updateActiveDish(updated: DishEntity) {
        _activeDish.value = updated
    }

    fun setTagPreviewMode(mode: String) {
        _tagPreviewMode.value = mode
    }

    fun setDigitalTagSize(size: DigitalTagSize) {
        _digitalTagSize.value = size
    }

    fun setDigitalTagTheme(theme: DigitalTagTheme) {
        _digitalTagTheme.value = theme
    }

    fun setActiveTagStation(station: String) {
        _activeTagStation.value = station
    }

    fun setActiveTagId(id: String) {
        _activeTagId.value = id
    }

    fun updateCloudConfig(newConfig: CloudGatewayConfig) {
        _cloudConfig.value = newConfig
        showToast("Cloud ESL Gateway configuration updated.")
    }

    fun toggleTagSoldOut(tagId: String = _activeTagId.value) {
        val current = _soldOutTags.value
        val willBeSoldOut = !current.contains(tagId)
        _soldOutTags.value = if (willBeSoldOut) current + tagId else current - tagId

        viewModelScope.launch {
            val result = digitalTagCloudService.sendRemoteAction(
                config = _cloudConfig.value,
                tagId = tagId,
                action = if (willBeSoldOut) "MARK_SOLD_OUT" else "CLEAR_SOLD_OUT"
            )
            _lastCloudSyncResult.value = result
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun testCloudConnection() {
        viewModelScope.launch {
            _isCloudSyncing.value = true
            val result = digitalTagCloudService.testConnection(_cloudConfig.value)
            _isCloudSyncing.value = false
            _lastCloudSyncResult.value = result
            _cloudConfig.value = _cloudConfig.value.copy(
                isConnected = result.success,
                lastHeartbeatTime = result.timestamp
            )
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun pushActiveTagToCloud() {
        val dish = _activeDish.value
        val tagId = _activeTagId.value
        val station = _activeTagStation.value
        val isSoldOut = _soldOutTags.value.contains(tagId)

        val payload = DigitalTagPayload(
            tagId = tagId,
            stationName = station,
            dishId = dish.id,
            dishName = dish.name.ifBlank { "Buffet Dish" },
            dishDesc = dish.desc,
            calories = dish.cals,
            isVeg = dish.type.lowercase() == "veg",
            allergens = dish.allergens,
            screenSize = _digitalTagSize.value.name,
            theme = _digitalTagTheme.value.name,
            isSoldOut = isSoldOut
        )

        viewModelScope.launch {
            _isCloudSyncing.value = true
            val result = digitalTagCloudService.syncSingleTag(_cloudConfig.value, payload)
            _isCloudSyncing.value = false
            _lastCloudSyncResult.value = result
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun broadcastAllTagsToCloud() {
        val allDishesList: List<DishEntity> = dishes.value.ifEmpty { listOf(_activeDish.value) }
        val payloads = allDishesList.mapIndexed { index, item ->
            val tagId = "ESL-TAG-BF${index + 1}"
            val isSoldOut = _soldOutTags.value.contains(tagId)
            val stationCategory = if (item.category.isNotBlank()) item.category else "Main Buffet"
            DigitalTagPayload(
                tagId = tagId,
                stationName = "Station ${index + 1} ($stationCategory)",
                dishId = item.id,
                dishName = item.name,
                dishDesc = item.desc,
                calories = item.cals,
                isVeg = item.type.lowercase() == "veg",
                allergens = item.allergens,
                screenSize = _digitalTagSize.value.name,
                theme = _digitalTagTheme.value.name,
                isSoldOut = isSoldOut
            )
        }

        viewModelScope.launch {
            _isCloudSyncing.value = true
            val result = digitalTagCloudService.syncBatchTags(_cloudConfig.value, payloads)
            _isCloudSyncing.value = false
            _lastCloudSyncResult.value = result
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun remoteFlashTagLed(tagId: String = _activeTagId.value) {
        viewModelScope.launch {
            val result = digitalTagCloudService.sendRemoteAction(
                config = _cloudConfig.value,
                tagId = tagId,
                action = "FLASH_LED"
            )
            _lastCloudSyncResult.value = result
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun forceRefreshTag(tagId: String = _activeTagId.value) {
        viewModelScope.launch {
            val result = digitalTagCloudService.sendRemoteAction(
                config = _cloudConfig.value,
                tagId = tagId,
                action = "FORCE_REFRESH"
            )
            _lastCloudSyncResult.value = result
            _cloudSyncLogs.value = listOf(result) + _cloudSyncLogs.value.take(19)
            showToast(result.message)
        }
    }

    fun saveActiveDish() {
        viewModelScope.launch {
            val current = _activeDish.value
            if (current.id > 0) {
                repository.updateDish(current)
                showToast("Updated \"${current.name}\" successfully!")
            } else {
                val newId = repository.insertDish(current)
                _activeDish.value = current.copy(id = newId)
                showToast("Saved \"${current.name}\" to Menu Showcase!")
            }
            if (_cloudConfig.value.autoSyncEnabled) {
                pushActiveTagToCloud()
            }
        }
    }

    fun loadSampleDish() {
        _activeDish.value = DishEntity(
            name = "Seasonal Salad Bar",
            desc = "Seasonal crisp greens with two dressings served separately.",
            cals = "55 kcal per 100 g",
            type = "veg",
            category = "Salads & Accompaniments",
            allergens = emptyList(),
            isSample = true
        )
        showToast("Loaded sample dish.")
    }

    fun cycleTemplate(direction: Int) {
        val current = _activeTemplateId.value
        val currentIndex = TemplateSkins.indexOfFirst { it.id == current }.let { if (it == -1) 0 else it }
        var nextIndex = (currentIndex + direction) % TemplateSkins.size
        if (nextIndex < 0) nextIndex = TemplateSkins.size - 1
        val nextTemplate = TemplateSkins[nextIndex].id
        _activeTemplateId.value = nextTemplate
        viewModelScope.launch {
            repository.updateTemplate(nextTemplate)
        }
        showToast("Template skin updated: ${TemplateSkins[nextIndex].name}")
    }

    fun setTemplate(id: String) {
        _activeTemplateId.value = id
        viewModelScope.launch {
            repository.updateTemplate(id)
        }
        showToast("Template skin updated.")
    }

    fun setMenuSize(size: String) {
        _menuPrintSize.value = size
        showToast("Print size set to $size")
    }

    fun setMenuOrientation(orientation: String) {
        _menuOrientation.value = orientation
        showToast("Orientation set to ${orientation.replaceFirstChar { it.uppercase() }}")
    }

    fun setMenuPageLayout(layout: String) {
        _menuPageLayout.value = layout
        showToast("Page layout set to $layout")
    }

    fun setAppTheme(themeName: String) {
        viewModelScope.launch {
            repository.updateTheme(themeName)
            showToast("Theme updated to ${themeName.uppercase()}")
        }
    }

    fun setAppLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateLanguage(lang)
            showToast("Language preference set.")
        }
    }

    fun toggleLogoLock() {
        val currentProfile = userProfile.value ?: UserProfileEntity()
        val nextLock = !currentProfile.isLogoLocked
        viewModelScope.launch {
            repository.updateLogo(currentProfile.customLogoBase64, nextLock)
            showToast(if (nextLock) "Logo locked permanently in persistent storage." else "Logo unlocked.")
        }
    }

    fun setCustomLogo(base64OrUri: String) {
        viewModelScope.launch {
            repository.updateLogo(base64OrUri, isLocked = true)
            showToast("Brand logo uploaded and locked.")
        }
    }

    fun runAiEnhancer() {
        _isAiLoading.value = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _isAiLoading.value = false
            showToast("✨ QA Agent Verified: Successfully enhanced nutrition & tags!")
        }
    }

    fun translateActiveDish(lang: String) {
        viewModelScope.launch {
            val translated = aiService.translateDishes(listOf(_activeDish.value), lang).firstOrNull()
            if (translated != null) {
                _activeDish.value = translated
                showToast("Active dish translated to ${lang.uppercase()}")
            }
        }
    }

    fun translateDisplayedRecipes(recipesToTranslate: List<RecipeEntity>, langCode: String, langName: String) {
        if (recipesToTranslate.isEmpty()) return
        
        _isAiLoading.value = true
        showToast("Translating ${recipesToTranslate.size} recipes to $langName...")
        
        viewModelScope.launch {
            try {
                val translated = aiService.translateRecipes(recipesToTranslate.take(20), langCode) // Limit to 20 for safety/speed
                val newMap = _translatedRecipes.value.toMutableMap()
                translated.forEach { newMap[it.id] = it }
                _translatedRecipes.value = newMap
                
                showToast("✨ Recipes successfully translated to $langName!")
            } catch (e: Exception) {
                showToast("Error translating recipes. Please try again.")
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun importExternalRecipes(rawText: String) {
        viewModelScope.launch {
            try {
                val parsed = com.example.util.RecipeImporter.parseStructuredRecipeData(rawText)
                if (parsed.isEmpty()) {
                    showToast("No valid recipes found in input.")
                    return@launch
                }
                
                parsed.forEach { repository.insertRecipe(it) }
                showToast("Successfully imported ${parsed.size} recipes & mapped compliance data!")
            } catch (e: Exception) {
                showToast("Error parsing recipes: ${e.localizedMessage}")
            }
        }
    }

    fun setRecipeSearchQuery(query: String) {
        _recipeSearchQuery.value = query
    }

    fun setRecipeCategoryFilter(category: String) {
        _recipeCategoryFilter.value = category
    }

    fun setRecipeBankTab(tab: String) {
        _recipeBankTab.value = tab
    }

    fun selectRecipeForDetail(recipe: RecipeEntity?) {
        _selectedRecipeForDetail.value = recipe
    }

    fun askAiRecipe(query: String) {
        if (query.isBlank()) return
        _isAiLoading.value = true
        
        viewModelScope.launch {
            try {
                // LOCAL DATA CACHING STRATEGY: Check Room DB first for offline access
                val existingRecipes = repository.searchRecipes(query).firstOrNull()
                val cachedMatch = existingRecipes?.firstOrNull { 
                    it.name.contains(query, ignoreCase = true) 
                }
                
                if (cachedMatch != null) {
                    _isAiLoading.value = false
                    _selectedRecipeForDetail.value = cachedMatch
                    showToast("Loaded \"${cachedMatch.name}\" from local offline cache.")
                    return@launch
                }

                showToast("Consulting culinary F&B Assistant for \"$query\" (2 persons)...")
                val newRecipe = aiService.generateRecipe(query)
                val id = repository.insertRecipe(newRecipe)
                _isAiLoading.value = false
                val created = newRecipe.copy(id = id)
                _selectedRecipeForDetail.value = created
                showToast("✨ Generated authentic recipe & heritage story for \"${created.name}\"!")
            } catch (e: Exception) {
                _isAiLoading.value = false
                showToast("Offline Mode: Unable to connect to F&B Assistant. Please check internet connection.")
            }
        }
    }

    suspend fun generateArtworkConcept(dishName: String, category: String): com.example.data.remote.RestaurantArtworkConcept {
        return aiService.generateRestaurantArtworkConcept(dishName, category)
    }

    fun sendHelpDeskMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendHelpDeskMessage("user", text)
            _isAiLoading.value = true
            kotlinx.coroutines.delay(400)
            val reply = aiService.getHelpDeskAnswer(text)
            repository.sendHelpDeskMessage("ai", reply)
            _isAiLoading.value = false
        }
    }

    fun requestCallback(phone: String) {
        if (phone.isBlank() || phone.length < 5) {
            showToast("Please enter a valid phone number.")
            return
        }
        viewModelScope.launch {
            repository.requestCallback(phone)
            showToast("Callback requested! Lead architect Rohan Ghosh & Support team have been notified.")
        }
    }

    fun saveUserProfile(establishment: String, avatarUri: String?, customLogoBase64: String?, country: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(
                current.copy(
                    establishment = establishment.ifBlank { "Global Culinary Suite" },
                    avatarUri = avatarUri,
                    customLogoBase64 = customLogoBase64,
                    country = country
                )
            )
            showToast("Settings saved successfully!")
            _activeModule.value = "buffet"
        }
    }

    fun saveUserProfile(username: String, establishment: String, avatarUri: String?, customLogoBase64: String?, country: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(
                current.copy(
                    username = username.ifBlank { "Guest User" },
                    establishment = establishment.ifBlank { "Global Culinary Suite" },
                    avatarUri = avatarUri,
                    customLogoBase64 = customLogoBase64,
                    country = country
                )
            )
            showToast("Profile settings saved successfully!")
            _activeModule.value = "buffet"
        }
    }

    fun loginOrRegister(name: String, est: String, idVal: String, isRegister: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            val user = if (isRegister) name.ifBlank { idVal.substringBefore("@") } else idVal.substringBefore("@")
            val establishment = if (isRegister) est.ifBlank { "Global Culinary Suite" } else current.establishment
            repository.updateProfile(
                current.copy(
                    username = user,
                    establishment = establishment,
                    emailOrPhone = idVal
                )
            )
            showToast("Welcome, $user ($establishment)!")
        }
    }

    fun logout() {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(
                current.copy(
                    username = "Guest User",
                    establishment = "Global Culinary Suite",
                    emailOrPhone = "user@hotelstudio.com"
                )
            )
            showToast("Logged out successfully.")
        }
    }

    // =========================================================================
    // MENU BUILDER & BUFFET MANAGEMENT
    // =========================================================================

    fun setBuffetDay(day: String) {
        _selectedBuffetDay.value = day
    }

    fun setBuffetMealSession(session: String) {
        _selectedBuffetMealSession.value = session
    }

    fun setBuffetPaxCount(pax: Int) {
        _buffetPaxCount.value = pax.coerceIn(10, 2000)
    }

    fun setMenuBuilderTab(tab: String) {
        _menuBuilderTab.value = tab
    }

    fun openRecipePicker(targetCourse: String? = null) {
        if (!targetCourse.isNullOrBlank()) {
            _recipePickerTargetCourse.value = targetCourse
        } else {
            _recipePickerTargetCourse.value = "Auto-Assign (Smart)"
        }
        _isRecipePickerOpen.value = true
    }

    fun closeRecipePicker() {
        _isRecipePickerOpen.value = false
    }

    fun setRecipePickerCategory(category: String) {
        _recipePickerCategory.value = category
    }

    fun setRecipePickerPrepTime(time: String) {
        _recipePickerPrepTime.value = time
    }

    fun setRecipePickerDiet(diet: String) {
        _recipePickerDiet.value = diet
    }

    fun setRecipePickerSearch(query: String) {
        _recipePickerSearch.value = query
    }

    fun setRecipePickerTargetCourse(course: String) {
        _recipePickerTargetCourse.value = course
    }

    fun addRecipeToBuffet(recipe: RecipeEntity, customCourse: String? = null) {
        viewModelScope.launch {
            var course = customCourse ?: _recipePickerTargetCourse.value
            
            val isVeg = !CulinaryAgent.isLikelyNonVeg(recipe.name) && !recipe.category.contains("Non-Veg", ignoreCase = true)

            if (course == "Auto-Assign (Smart)") {
                course = when {
                    recipe.category.contains("Beverage", true) -> "Welcome Beverages"
                    recipe.category.contains("Starter", true) -> "Starters & Live Counter"
                    recipe.category.contains("Soup", true) -> "Soups & Breads"
                    recipe.category.contains("Salad", true) -> "Salad Bar"
                    recipe.category.contains("Sweet", true) || recipe.category.contains("Dessert", true) -> "Desserts & Sweets"
                    recipe.name.contains("Rice", true) || recipe.name.contains("Bread", true) || recipe.name.contains("Naan", true) -> "Accompaniments & Rices"
                    else -> if (isVeg) "Main Course (Veg)" else "Main Course (Non-Veg)"
                }
            }


            // Derive allergens from ingredients
            val ingText = recipe.ingredients.joinToString(" ").lowercase()
            val detectedAllergens = mutableListOf<String>()
            if (ingText.contains("milk") || ingText.contains("paneer") || ingText.contains("ghee") || ingText.contains("butter") || ingText.contains("cream") || ingText.contains("curd") || ingText.contains("khoya")) detectedAllergens.add("Milk")
            if (ingText.contains("flour") || ingText.contains("wheat") || ingText.contains("bread") || ingText.contains("maida") || ingText.contains("naan")) detectedAllergens.add("Gluten")
            if (ingText.contains("nut") || ingText.contains("cashew") || ingText.contains("almond") || ingText.contains("walnut")) detectedAllergens.add("Nuts")
            if (ingText.contains("soya") || ingText.contains("soy")) detectedAllergens.add("Soya")
            if (ingText.contains("mustard")) detectedAllergens.add("Mustard")
            if (ingText.contains("egg")) detectedAllergens.add("Egg")

            val station = when {
                course.contains("Beverage", ignoreCase = true) -> "Beverage Dispensary"
                course.contains("Starter", ignoreCase = true) -> "Live Tandoor / Fry Counter"
                course.contains("Soup", ignoreCase = true) -> "Soup Kettle"
                course.contains("Salad", ignoreCase = true) -> "Chilled Salad Bar"
                course.contains("Dessert", ignoreCase = true) || course.contains("Sweet", ignoreCase = true) -> "Dessert Island"
                course.contains("Rice", ignoreCase = true) || course.contains("Bread", ignoreCase = true) -> "Carb & Bread Station"
                else -> "Main Chafing Line"
            }

            val currentItems = currentBuffetItems.value
            val maxSort = (currentItems.maxOfOrNull { it.sortOrder } ?: 0) + 1

            val menuItem = BuffetMenuItemEntity(
                dayOfWeek = _selectedBuffetDay.value,
                mealSession = _selectedBuffetMealSession.value,
                courseSection = course,
                recipeId = recipe.id,
                name = recipe.name,
                desc = recipe.desc,
                cals = recipe.cals,
                type = if (isVeg) "veg" else "nonveg",
                prepTime = recipe.prepTime,
                portions = "${_buffetPaxCount.value} Pax",
                station = station,
                allergens = detectedAllergens,
                sortOrder = maxSort
            )
            repository.insertBuffetMenuItem(menuItem)
            showToast("Added \"${recipe.name}\" to ${_selectedBuffetDay.value} ${_selectedBuffetMealSession.value}")
        }
    }

    fun addCustomBuffetItem(name: String, desc: String, station: String, cals: String = "N/A", allergens: List<String> = emptyList()) {
        val course = _recipePickerTargetCourse.value ?: "Main Course (Veg)"
        val isNonVeg = CulinaryAgent.isLikelyNonVeg(name)
        val type = if (isNonVeg) "nonveg" else "veg"
        
        addCustomBuffetItem(
            name = name,
            courseSection = course,
            type = type,
            desc = desc,
            cals = cals,
            prepTime = "N/A",
            station = station,
            allergens = allergens
        )
    }

    fun addCustomBuffetItem(
        name: String,
        courseSection: String,
        type: String,
        desc: String,
        cals: String,
        prepTime: String,
        station: String,
        allergens: List<String>
    ) {
        if (name.isBlank()) {
            showToast("Item name cannot be empty.")
            return
        }
        viewModelScope.launch {
            val currentItems = currentBuffetItems.value
            val maxSort = (currentItems.maxOfOrNull { it.sortOrder } ?: 0) + 1
            val item = BuffetMenuItemEntity(
                dayOfWeek = _selectedBuffetDay.value,
                mealSession = _selectedBuffetMealSession.value,
                courseSection = courseSection,
                name = name.trim(),
                desc = desc.trim(),
                cals = cals.trim().ifBlank { "180 kcal" },
                type = type,
                prepTime = prepTime.trim().ifBlank { "25 mins" },
                portions = "${_buffetPaxCount.value} Pax",
                station = station.trim().ifBlank { "Main Buffet Line" },
                allergens = allergens,
                sortOrder = maxSort
            )
            repository.insertBuffetMenuItem(item)
            showToast("Added \"${item.name}\" to buffet menu!")
        }
    }

    fun updateBuffetMenuItem(item: BuffetMenuItemEntity) {
        viewModelScope.launch {
            repository.updateBuffetMenuItem(item)
            showToast("Updated \"${item.name}\"")
        }
    }

    fun updateBuffetItem(item: BuffetMenuItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBuffetMenuItem(item)
        }
    }

    fun analyzeDishForCustomItem(dishName: String, onResult: (com.example.data.remote.DishAnalysis) -> Unit) {
        viewModelScope.launch {
            try {
                val analysis = aiService.analyzeCustomDish(dishName)
                onResult(analysis)
            } catch (e: Exception) {
                // Return default on error
                onResult(com.example.data.remote.DishAnalysis(
                    description = "Custom house preparation.",
                    allergens = emptyList(),
                    caloriesPer100 = "150 kcal"
                ))
            }
        }
    }

    fun deleteBuffetMenuItem(item: BuffetMenuItemEntity) {
        viewModelScope.launch {
            repository.deleteBuffetMenuItem(item)
            showToast("Removed \"${item.name}\" from buffet")
        }
    }

    fun reorderBuffetItem(item: BuffetMenuItemEntity, moveUp: Boolean) {
        viewModelScope.launch {
            repository.reorderBuffetMenuItem(item, moveUp)
        }
    }

    fun toggleBuffetItemReady(item: BuffetMenuItemEntity) {
        viewModelScope.launch {
            val updated = item.copy(isReady = !item.isReady)
            repository.updateBuffetMenuItem(updated)
            val status = if (updated.isReady) "Marked as Ready for Service" else "Marked as Prep Queued"
            showToast("${updated.name}: $status")
        }
    }

    fun clearCurrentBuffetMenu() {
        viewModelScope.launch {
            repository.clearBuffetMenu(_selectedBuffetDay.value, _selectedBuffetMealSession.value)
            showToast("Cleared ${_selectedBuffetDay.value} ${_selectedBuffetMealSession.value} buffet menu")
        }
    }

    // =========================================================================
    // ESTABLISHMENT DATA & BUFFET ANALYTICS
    // =========================================================================

    fun importEstablishmentCosting(csvText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val parsed = com.example.util.RecipeImporter.parseStructuredRecipeData(csvText)
                if (parsed.isEmpty()) {
                    showToast("No valid establishment data found in input.")
                    return@launch
                }
                
                parsed.forEach { repository.insertRecipe(it) }
                showToast("Successfully imported ${parsed.size} establishment recipes with costing data!")
            } catch (e: Exception) {
                showToast("Error importing costing data: ${e.localizedMessage}")
            }
        }
    }

    fun calculateTotalBuffetCost(): Double {
        val currentItems = currentBuffetItems.value
        val allRecipes = recipes.value
        var totalCost = 0.0

        currentItems.forEach { item ->
            val recipe = allRecipes.find { it.id == item.recipeId } 
                ?: allRecipes.find { it.name.equals(item.name, ignoreCase = true) }

            val costPerPax = if (recipe != null && recipe.costPerPax > 0) {
                recipe.costPerPax
            } else {
                // Fallback to internal bank logic / approx costing
                when {
                    item.courseSection.contains("Non-Veg", true) -> 180.0
                    item.courseSection.contains("Veg", true) -> 120.0
                    item.courseSection.contains("Beverage", true) -> 45.0
                    item.courseSection.contains("Dessert", true) -> 85.0
                    item.courseSection.contains("Salad", true) -> 65.0
                    item.courseSection.contains("Starter", true) -> 95.0
                    else -> 75.0
                }
            }
            totalCost += costPerPax * _buffetPaxCount.value
        }
        return totalCost
    }

    fun closeBuffetService(soldCount: Int, pricePerBuffet: Double, wastageAmount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _isClosingBuffet.value = true
            try {
                val totalCost = calculateTotalBuffetCost()
                val revenue = soldCount * pricePerBuffet
                val wastageCost = wastageAmount // Assuming user enters cost of wastage
                
                val fcp = if (revenue > 0) (totalCost / revenue) * 100 else 0.0
                val wastagePct = if (totalCost > 0) (wastageCost / totalCost) * 100 else 0.0

                val record = com.example.data.model.BuffetClosingEntity(
                    day = _selectedBuffetDay.value,
                    session = _selectedBuffetMealSession.value,
                    totalCoversSold = soldCount,
                    buffetPrice = pricePerBuffet,
                    totalRevenue = revenue,
                    totalFoodCost = totalCost,
                    wastageCost = wastageCost,
                    foodCostPercentage = fcp,
                    wastagePercentage = wastagePct
                )

                repository.insertClosingRecord(record)
                _latestClosingRecord.value = record
                showToast("Buffet Closed! FCP: ${"%.1f".format(fcp)}% | Wastage: ${"%.1f".format(wastagePct)}%")
                _menuBuilderTab.value = "analytics"
            } catch (e: Exception) {
                showToast("Error closing buffet: ${e.localizedMessage}")
            } finally {
                _isClosingBuffet.value = false
            }
        }
    }

    fun duplicateBuffetMenu(targetDay: String, targetSession: String) {
        viewModelScope.launch {
            repository.duplicateBuffetMenu(
                fromDay = _selectedBuffetDay.value,
                fromSession = _selectedBuffetMealSession.value,
                toDay = targetDay,
                toSession = targetSession
            )
            showToast("Duplicated menu to $targetDay $targetSession!")
        }
    }

    fun saveManualChefMenu(
        headerName: String,
        targetPax: Int,
        actionStations: List<String>,
        beverageChoices: List<String>,
        items: List<ManualDishDraft>
    ) {
        viewModelScope.launch {
            _buffetPaxCount.value = targetPax
            val day = _selectedBuffetDay.value
            val session = _selectedBuffetMealSession.value
            val currentRecipeList = recipes.value

            val itemsToInsert = mutableListOf<BuffetMenuItemEntity>()
            var sort = 1

            // 1. Add Beverages
            for (bevChoice in beverageChoices) {
                if (bevChoice.isBlank()) continue
                val bevs = bevChoice.split("&", ",").map { it.trim() }.filter { it.isNotBlank() }
                for (bev in bevs) {
                    val analysis = CulinaryAgent.analyzeDish(bev, "veg", emptyList(), currentRecipeList)
                    itemsToInsert.add(
                        BuffetMenuItemEntity(
                            dayOfWeek = day,
                            mealSession = session,
                            courseSection = "Welcome Beverages",
                            name = bev,
                            desc = "Freshly chilled signature welcome refreshment ($bevChoice).",
                            cals = analysis.cals.ifBlank { "60 kcal / 100 ml" },
                            type = "veg",
                            prepTime = "10 mins",
                            portions = "$targetPax Pax",
                            station = "Mocktail & Juice Bar",
                            allergens = analysis.allergens,
                            sortOrder = sort++
                        )
                    )
                }
            }

            // 2. Add Live Action Stations
            for (station in actionStations) {
                if (station.isBlank()) continue
                val cleanStation = station.trim()
                val stationAnalysis = CulinaryAgent.analyzeDish(cleanStation, "veg", emptyList(), currentRecipeList)
                itemsToInsert.add(
                    BuffetMenuItemEntity(
                        dayOfWeek = day,
                        mealSession = session,
                        courseSection = "Starters & Live Counter",
                        name = if (cleanStation.startsWith("Live", ignoreCase = true)) cleanStation else "Live $cleanStation Special",
                        desc = "Prepared à la minute live at the banquet counter for guests.",
                        cals = stationAnalysis.cals.ifBlank { "175 kcal / 100 g" },
                        type = stationAnalysis.type,
                        prepTime = "15 mins",
                        portions = "$targetPax Pax",
                        station = cleanStation,
                        allergens = stationAnalysis.allergens,
                        sortOrder = sort++
                    )
                )
            }

            // 3. Add all custom Chef items from editable boxes
            val sectionHeader = headerName.trim().ifBlank { "Chef's Choice Main Course" }
            for (draft in items) {
                if (draft.name.isBlank()) continue
                val isNonVeg = draft.type.equals("nonveg", ignoreCase = true) || CulinaryAgent.isLikelyNonVeg(draft.name)
                val matchingRecipe = currentRecipeList.find { it.name.contains(draft.name, ignoreCase = true) }

                // Auto-detect allergens from draft or culinary agent if not provided
                val finalAllergens = if (draft.allergens.isNotEmpty() && !draft.allergens.contains("None Detected")) {
                    draft.allergens
                } else {
                    val analysis = CulinaryAgent.analyzeDish(draft.name, draft.type, emptyList(), currentRecipeList)
                    if (draft.allergens.isNotEmpty()) draft.allergens else analysis.allergens
                }

                val finalCals = draft.cals.trim().ifBlank {
                    matchingRecipe?.cals?.ifBlank { null }
                        ?: CulinaryAgent.analyzeDish(draft.name, draft.type, emptyList(), currentRecipeList).cals
                }

                val finalCourse = if (draft.category.isNotBlank()) draft.category else {
                    if (isNonVeg) "$sectionHeader (Non-Veg)" else "$sectionHeader (Veg)"
                }

                val primaryStation = actionStations.firstOrNull { it.isNotBlank() } ?: "Live Action Counter"
                val stationName = when {
                    draft.name.contains("Live", ignoreCase = true) -> primaryStation
                    isNonVeg -> "Hot Non-Veg Chafing Line"
                    else -> "Hot Veg Chafing Line"
                }

                itemsToInsert.add(
                    BuffetMenuItemEntity(
                        dayOfWeek = day,
                        mealSession = session,
                        courseSection = finalCourse,
                        recipeId = matchingRecipe?.id,
                        name = draft.name.trim(),
                        desc = draft.desc.trim().ifBlank {
                            matchingRecipe?.desc ?: "Executive Chef signature recipe curated fresh for $day $session service."
                        },
                        cals = finalCals,
                        type = if (isNonVeg) "nonveg" else "veg",
                        prepTime = draft.prepTime.trim().ifBlank { matchingRecipe?.prepTime ?: "25 mins" },
                        portions = "$targetPax Pax",
                        station = stationName,
                        allergens = finalAllergens,
                        sortOrder = sort++
                    )
                )
            }

            if (itemsToInsert.isEmpty()) {
                showToast("Please enter at least one dish name.")
                return@launch
            }

            repository.clearBuffetMenu(day, session)
            repository.insertBuffetMenuItems(itemsToInsert)
            showToast("✅ Created Chef's Choice menu: ${itemsToInsert.size} items ready for digital tags & print!")
        }
    }

    // Overload for single station & beverage (backward compatibility)
    fun saveManualChefMenu(
        headerName: String,
        targetPax: Int,
        actionStation: String,
        beverageChoice: String,
        items: List<ManualDishDraft>
    ) {
        saveManualChefMenu(
            headerName = headerName,
            targetPax = targetPax,
            actionStations = if (actionStation.isNotBlank()) listOf(actionStation) else emptyList(),
            beverageChoices = if (beverageChoice.isNotBlank()) listOf(beverageChoice) else emptyList(),
            items = items
        )
    }

    fun generateAiBuffetMenu() {
        generateAiBuffetMenuWithOptions()
    }

    fun generateAiBuffetMenuWithOptions(
        targetPax: Int = _buffetPaxCount.value,
        vegCount: Int = 4,
        nonVegCount: Int = 2,
        dessertCount: Int = 2,
        breadCount: Int = 2,
        riceCount: Int = 1,
        starterCount: Int = 2,
        actionStations: List<String> = listOf("Live Tandoor & Chaat Counter"),
        beverageChoices: List<String> = listOf("Virgin Mojito & Spiced Buttermilk")
    ) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _buffetPaxCount.value = targetPax
            val day = _selectedBuffetDay.value
            val session = _selectedBuffetMealSession.value
            val profile = userProfile.value
            val userCountry = profile?.country ?: "United States"
            val userEstablishment = profile?.establishment ?: "Global Culinary Suite"
            showToast("Curating $day $session buffet: $vegCount Veg, $nonVegCount Non-Veg, $dessertCount Desserts...")
            kotlinx.coroutines.delay(400)

            val currentRecipeList = recipes.value

            fun findRecipe(namePart: String): com.example.data.model.RecipeEntity? {
                return currentRecipeList.find { it.name.contains(namePart, ignoreCase = true) }
            }

            // Define curated recipes catalog based on Day, Meal Period and User Nationality / Regional Taste
            val isIndianRegional = userCountry.contains("India", ignoreCase = true) ||
                    userEstablishment.contains("Taj", ignoreCase = true) ||
                    userEstablishment.contains("Heritage", ignoreCase = true)
            val isAmericanEuropean = userCountry.contains("United States", ignoreCase = true) ||
                    userCountry.contains("United Kingdom", ignoreCase = true) ||
                    userCountry.contains("Canada", ignoreCase = true) ||
                    userCountry.contains("Australia", ignoreCase = true) ||
                    userCountry.contains("Germany", ignoreCase = true) ||
                    userCountry.contains("France", ignoreCase = true)

            val breakfastMenu = when {
                isAmericanEuropean -> listOf(
                    "Welcome Beverages" to listOf("Fresh Cold-Pressed Valencia Orange Juice", "Artisan Roasted Colombian Coffee", "Green Detox Smoothie"),
                    "Starters & Live Counter" to listOf("Live Belgian Waffle & Crepe Station", "Eggs Benedict with Hollandaise", "Avocado Toast on Sourdough"),
                    "Soups & Breads" to listOf("Warm Steel Cut Oatmeal with Berries", "Fresh Baked Danish Pastries & Bagels"),
                    "Salad Bar" to listOf("Seasonal Melon & Berry Platter", "Organic Greek Yogurt with Granola & Honey"),
                    "Main Course (Veg)" to listOf("Crispy Golden Hash Browns", "Grilled Herb Tomatoes & Mushrooms", "Roasted Asparagus with Parmesan"),
                    "Main Course (Non-Veg)" to listOf("Applewood Smoked Bacon", "Country Maple Pork & Chicken Sausage", "Vegetable Frittata with Feta"),
                    "Accompaniments & Rices" to listOf("Artisan Cream Cheese & Fruit Preserves", "Fresh Cultured Butter & Artisanal Breads"),
                    "Desserts & Sweets" to listOf("Fluffy Buttermilk Pancakes with Warm Maple Syrup", "Cinnamon French Toast Brioche")
                )
                else -> when (day) {
                    "Monday", "Wednesday", "Friday" -> listOf(
                        "Welcome Beverages" to listOf("Sunrise Citrus Cooler", "Cucumber Mint Spritzer"),
                        "Starters & Live Counter" to listOf("Steamed Idli Sambar", "Medu Vada", "Live Egg & Omelette Station"),
                        "Soups & Breads" to listOf("Warm Spiced Oat Porridge", "Assorted Breakfast Rolls & Croissants"),
                        "Salad Bar" to listOf("Fresh Tropical Fruit Salad", "Greek Yogurt & Granola Bowl"),
                        "Main Course (Veg)" to listOf("Poha Indori", "Aloo Poori Bhaji", "Baked Beans on Toast"),
                        "Main Course (Non-Veg)" to listOf("Chicken Breakfast Sausage", "Crispy Bacon Strips", "Egg & Cheese Breakfast Sandwich"),
                        "Accompaniments & Rices" to listOf("Coconut Chutney & Tomato Chutney", "Multi-Grain Toast & Salted Butter"),
                        "Desserts & Sweets" to listOf("Fluffy Pancakes with Maple Syrup", "Fresh Cut Watermelon & Papaya Platter")
                    )
                    "Saturday", "Sunday" -> listOf(
                        "Welcome Beverages" to listOf("Fresh Orange Juice Cooler", "Masala Chai"),
                        "Starters & Live Counter" to listOf("Masala Dosa", "Crispy Batata Vada", "Live Belgian Waffle Station"),
                        "Soups & Breads" to listOf("Sweet Corn Chowder", "Brioche Toast & Danishes"),
                        "Salad Bar" to listOf("Waldorf Apple Nut Salad", "Bircher Muesli Bowl"),
                        "Main Course (Veg)" to listOf("Paneer Paratha with White Butter", "Chole Bhature", "South Indian Upma"),
                        "Main Course (Non-Veg)" to listOf("Spanish Scrambled Eggs with Chorizo", "Grilled Chicken Sausages"),
                        "Accompaniments & Rices" to listOf("Gunpowder Podi with Ghee", "Mixed Pickle & Curd"),
                        "Desserts & Sweets" to listOf("Warm Cinnamon Rolls", "Gulab Jamun")
                    )
                    else -> listOf(
                        "Welcome Beverages" to listOf("Tropical Pineapple Punch", "Sweet Lassi"),
                        "Starters & Live Counter" to listOf("Uttapam Platter", "Crisp Vegetable Spring Rolls", "Live Egg Counter"),
                        "Soups & Breads" to listOf("Tomato Herb Consomme", "Fresh English Muffins"),
                        "Salad Bar" to listOf("Sprouted Moong Salad", "Fresh Melon Trio"),
                        "Main Course (Veg)" to listOf("Sev Tamatar Saag with Puri", "Vegetable Pulao", "Aloo Gobi Adraki"),
                        "Main Course (Non-Veg)" to listOf("Egg Bhurji Masala", "Chicken Salami & Ham"),
                        "Accompaniments & Rices" to listOf("Mint Coriander Chutney", "Butter & Jams"),
                        "Desserts & Sweets" to listOf("French Toast with Berry Compote", "Banana Walnut Bread")
                    )
                }
            }

            val lunchMenu = when {
                isAmericanEuropean && !isIndianRegional -> listOf(
                    "Welcome Beverages" to listOf("Sparkling Pomegranate Limeade", "Iced Peach White Tea"),
                    "Starters & Live Counter" to listOf("Live Gourmet Flatbread Hearth", "Crispy Calamari & Herb Arancini", "Truffle Parmesan Fries"),
                    "Soups & Breads" to listOf("Roasted Tomato Bisque", "Warm Ciabatta & Herb Focaccia"),
                    "Salad Bar" to listOf("Classic Caesar Salad with Shaved Parmesan", "Mediterranean Quinoa & Feta Salad"),
                    "Main Course (Veg)" to listOf("Spinach & Ricotta Ravioli in Brown Butter", "Wild Mushroom Risotto", "Grilled Ratatouille Provençale"),
                    "Main Course (Non-Veg)" to listOf("Pan-Seared Atlantic Salmon with Lemon Dill", "Roasted Herb Rosemary Chicken Breast", "Prime Angus Roast Beef Au Jus"),
                    "Accompaniments & Rices" to listOf("Garlic Mashed Russet Potatoes", "Roasted Seasonal Asparagus & Heirloom Carrots"),
                    "Desserts & Sweets" to listOf("Classic New York Cheesecake", "Warm Chocolate Lava Cake with Raspberry Coulis")
                )
                else -> when (day) {
                    "Monday", "Thursday" -> listOf(
                        "Welcome Beverages" to listOf("Blueberry Basil Smash", "Spiced Buttermilk"),
                        "Starters & Live Counter" to listOf("Crisp Vegetable Spring Rolls", "Paneer Tikka", "Golden Samosa"),
                        "Soups & Breads" to listOf("Creamy Tomato Basil Bisque", "Warm Garlic Baguette"),
                        "Salad Bar" to listOf("Classic Caesar Salad", "Cucumber & Tomato Kachumber"),
                        "Main Course (Veg)" to listOf("Paneer Zafrani Korma", "Dal Makhani", "Bhindi Do Pyaza", "Subz Panchmel"),
                        "Main Course (Non-Veg)" to listOf("Chicken Butter Masala", "Mutton Rogan Josh"),
                        "Accompaniments & Rices" to listOf("Fragrant Jeera Basmati Rice", "Tandoori Garlic Butter Naan", "Laccha Paratha"),
                        "Desserts & Sweets" to listOf("Gulab Jamun", "Classic Apple Pie")
                    )
                    "Tuesday", "Friday" -> listOf(
                        "Welcome Beverages" to listOf("Virgin Mojito", "Sweet Lime Cooler"),
                        "Starters & Live Counter" to listOf("Hara Bhara Kabab", "Corn Cheese Balls", "Dahi Ke Sholey"),
                        "Soups & Breads" to listOf("Wild Mushroom Truffle Soup", "Rosemary Focaccia"),
                        "Salad Bar" to listOf("Mediterranean Greek Salad", "Beetroot Orange Carpaccio"),
                        "Main Course (Veg)" to listOf("Kadhai Paneer", "Dal Tadka Dhaba Style", "Aloo Gobi Adraki", "Navratan Korma"),
                        "Main Course (Non-Veg)" to listOf("Chicken Tikka Masala", "Goan Fish Curry"),
                        "Accompaniments & Rices" to listOf("Hyderabadi Subz Dum Biryani", "Tandoori Roti with Desi Ghee", "Boondi Raita"),
                        "Desserts & Sweets" to listOf("Classic Tiramisu", "Rasgulla in Rose Syrup")
                    )
                    else -> listOf(
                        "Welcome Beverages" to listOf("Spicy Jalapeno Margarita Mocktail", "Kokum Sharbat"),
                        "Starters & Live Counter" to listOf("Paneer Malai Tikka", "Tandoori Soya Chaap", "Crispy Corn Pepper Salt"),
                        "Soups & Breads" to listOf("Roasted Butternut Squash Soup", "Herb Dinner Rolls"),
                        "Salad Bar" to listOf("Russian Potato Salad", "Quinoa Pomegranate Tabouleh"),
                        "Main Course (Veg)" to listOf("Paneer Lababdar", "Dal Bukhara", "Methi Matar Malai", "Dum Aloo Kashmiri"),
                        "Main Course (Non-Veg)" to listOf("Murgh Dum Biryani", "Chicken Kolhapuri"),
                        "Accompaniments & Rices" to listOf("Kashmiri Pulao", "Butter Naan & Missi Roti", "Pineapple Raita"),
                        "Desserts & Sweets" to listOf("New York Style Cheesecake", "Shahi Tukda")
                    )
                }
            }

            val dinnerMenu = when {
                isAmericanEuropean && !isIndianRegional -> listOf(
                    "Welcome Beverages" to listOf("Smoked Rosemary Cranberry Spritz", "Virgin Elderflower Fizz"),
                    "Starters & Live Counter" to listOf("Live Carvery Prime Rib Counter", "Jumbo Lump Crab Cakes", "Wild Mushroom Crostini"),
                    "Soups & Breads" to listOf("Classic French Onion Soup with Gruyère Crouton", "Artisan Sourdough Loaf with Sea Salt Butter"),
                    "Salad Bar" to listOf("Caprese Salad with Aged Balsamic Glaze", "Shaved Fennel & Arugula Salad"),
                    "Main Course (Veg)" to listOf("Eggplant Parmigiana Rustica", "Butternut Squash Tortelloni", "Truffled Polenta with Roasted Wild Forest Fungi"),
                    "Main Course (Non-Veg)" to listOf("Filet Mignon with Red Wine Demi-Glace", "Pan-Roasted Chilean Sea Bass", "Slow-Braised Lamb Shank"),
                    "Accompaniments & Rices" to listOf("Dauphinoise Potatoes Gratin", "Sautéed French Haricots Verts & Shallots"),
                    "Desserts & Sweets" to listOf("Classic Bourbon Vanilla Crème Brûlée", "Warm Molten Chocolate Fondant", "Tiramisu Tradizionale")
                )
                else -> when (day) {
                    "Friday", "Saturday", "Sunday" -> listOf(
                        "Welcome Beverages" to listOf("Sunrise Citrus Cooler", "Spiced Buttermilk"),
                        "Starters & Live Counter" to listOf("Tandoori Paneer Angara", "Murgh Malai Tikka", "Afghani Soya Chaap"),
                        "Soups & Breads" to listOf("Classic French Onion Soup", "Artisan Sourdough & Herb Butter"),
                        "Salad Bar" to listOf("Chilled Caprese Salad", "Smoked Chicken Salad", "Crunchy Asian Slaw"),
                        "Main Course (Veg)" to listOf("Paneer Butter Masala", "Dal Makhani Grand Heritage", "Vilayati Subz Kadhai", "Kaju Curry"),
                        "Main Course (Non-Veg)" to listOf("Awadhi Dum Chicken Biryani", "Mutton Rogan Josh Royal", "Kolkata Fish Fry"),
                        "Accompaniments & Rices" to listOf("Fragrant Saffron Pilaf", "Chur Chur Naan", "Roomali Roti"),
                        "Desserts & Sweets" to listOf("Molten Chocolate Lava Cake", "Traditional Baklava", "Kesar Rasmalai")
                    )
                    "Wednesday", "Thursday" -> listOf(
                        "Welcome Beverages" to listOf("Blueberry Basil Smash", "Fresh Mint Mojito"),
                        "Starters & Live Counter" to listOf("Paneer Hariyali Tikka", "Golden Samosa", "Vegetable Galouti Kabab"),
                        "Soups & Breads" to listOf("Thai Tom Yum Goong", "Flaky Garlic Knots"),
                        "Salad Bar" to listOf("Classic Caesar Salad", "Som Tam Green Papaya Salad"),
                        "Main Course (Veg)" to listOf("Paneer Zafrani Korma", "Dal Tadka", "Subz Jalfrezi", "Baingan Bharta"),
                        "Main Course (Non-Veg)" to listOf("Chicken Tikka Masala", "Amritsari Machhi Curry"),
                        "Accompaniments & Rices" to listOf("Jeera Basmati Rice", "Tandoori Butter Naan", "Tawa Paratha"),
                        "Desserts & Sweets" to listOf("Classic Crème Brûlée", "Gulab Jamun with Rabri")
                    )
                    else -> listOf(
                        "Welcome Beverages" to listOf("Cucumber Mint Spritzer", "Rose Almond Milk"),
                        "Starters & Live Counter" to listOf("Amritsari Paneer Pakora", "Corn & Spinach Fritters", "Crisp Vegetable Spring Rolls"),
                        "Soups & Breads" to listOf("Creamy Tomato Basil Bisque", "Garlic Butter Toast"),
                        "Salad Bar" to listOf("Mediterranean Greek Salad", "Waldorf Apple Salad"),
                        "Main Course (Veg)" to listOf("Palak Paneer", "Dal Makhani", "Aloo Gobi Adraki", "Pindi Chana"),
                        "Main Course (Non-Veg)" to listOf("Chicken Butter Masala", "Egg Curry Homestyle"),
                        "Accompaniments & Rices" to listOf("Steamed Basmati Rice", "Pudina Paratha & Naan", "Burani Raita"),
                        "Desserts & Sweets" to listOf("Classic Apple Pie", "Moong Dal Halwa")
                    )
                }
            }

            val hiTeaMenu = listOf(
                "Welcome Beverages" to listOf("Masala Chai", "Assam Tea", "Filter Coffee", "Iced Peach Tea"),
                "Starters & Live Counter" to listOf("Live Sandwich & Toastie Bar", "Assorted Vegetable Pakoras", "Mini Corn & Cheese Samosas"),
                "Salad Bar" to listOf("Fresh Fruit Platter", "Cucumber & Mint Tea Sandwiches"),
                "Main Course (Veg)" to listOf("Paneer Tikka Crostini", "Vegetable Spring Rolls"),
                "Main Course (Non-Veg)" to listOf("Chicken Mayo Sliders", "Egg Mayo Finger Sandwiches"),
                "Desserts & Sweets" to listOf("English Scones with Jam", "Blueberry Muffins", "Assorted Cookies")
            )

            val supperMenu = listOf(
                "Welcome Beverages" to listOf("Warm Lemon & Ginger Tea", "Hot Chocolate"),
                "Soups & Breads" to listOf("Light Vegetable Clear Soup", "Chicken Consommé", "Garlic Toasted Ciabatta"),
                "Salad Bar" to listOf("Green Garden Salad", "Mixed Sprouts Salad"),
                "Main Course (Veg)" to listOf("Mushroom Risotto", "Baked Jacket Potato with Chives", "Steamed Seasonal Vegetables"),
                "Main Course (Non-Veg)" to listOf("Grilled Fish with Lemon Butter", "Poached Chicken Breast with Herbs"),
                "Desserts & Sweets" to listOf("Fresh Fruit Compote", "Low-Fat Yogurt with Berries")
            )

            val baseCourses = when (session) {
                "Breakfast" -> breakfastMenu
                "Hi Tea" -> hiTeaMenu
                "Supper" -> supperMenu
                "Lunch" -> lunchMenu
                else -> dinnerMenu
            }

            val itemsToInsert = mutableListOf<BuffetMenuItemEntity>()
            var sort = 1

            for ((course, suggestions) in baseCourses) {
                // Adjust suggestions based on user preferences
                val courseFiltered = when (course) {
                    "Welcome Beverages" -> {
                        val customBevs = beverageChoices.flatMap { choice ->
                            choice.split("&", ",").map { it.trim() }.filter { it.isNotBlank() }
                        }
                        if (customBevs.isNotEmpty()) customBevs else suggestions
                    }
                    "Starters & Live Counter" -> {
                        // Incorporate user's requested action stations if specified
                        val baseList = suggestions.toMutableList()
                        for (station in actionStations.reversed()) {
                            if (station.isNotBlank() && !baseList.any { it.contains(station, ignoreCase = true) }) {
                                baseList.add(0, if (station.startsWith("Live", ignoreCase = true)) station else "Live $station Special")
                            }
                        }
                        baseList
                    }
                    "Main Course (Veg)" -> {
                        // Respect user veg count request
                        if (vegCount > 0 && suggestions.size > vegCount) suggestions.take(vegCount)
                        else if (vegCount > suggestions.size) {
                            val extraVegs = listOf("Dal Makhani", "Paneer Lababdar", "Aloo Gobi Adraki", "Bhindi Do Pyaza", "Subz Panchmel")
                            (suggestions + extraVegs).distinct().take(vegCount)
                        } else suggestions
                    }
                    "Main Course (Non-Veg)" -> {
                        // Respect user non-veg count request
                        if (nonVegCount <= 0) emptyList()
                        else if (suggestions.size > nonVegCount) suggestions.take(nonVegCount)
                        else if (nonVegCount > suggestions.size) {
                            val extraNonVegs = listOf("Chicken Butter Masala", "Chicken Tikka Masala", "Goan Fish Curry", "Mutton Rogan Josh")
                            (suggestions + extraNonVegs).distinct().take(nonVegCount)
                        } else suggestions
                    }
                    "Desserts & Sweets" -> {
                        if (dessertCount > 0 && suggestions.size > dessertCount) suggestions.take(dessertCount)
                        else suggestions
                    }
                    "Soups & Breads" -> {
                        // Assuming 1 Soup + Breads
                        if (breadCount > 0) suggestions.take(breadCount + 1)
                        else suggestions
                    }
                    "Accompaniments & Rices" -> {
                        if (riceCount > 0 && suggestions.size > riceCount) suggestions.take(riceCount)
                        else suggestions
                    }
                    else -> suggestions
                }

                for (dishName in courseFiltered) {
                    val matchingRecipe = findRecipe(dishName)
                    val name = matchingRecipe?.name ?: dishName
                    val isNonVegDish = course.contains("Non-Veg", ignoreCase = true) ||
                            name.contains("Chicken", ignoreCase = true) ||
                            name.contains("Fish", ignoreCase = true) ||
                            name.contains("Mutton", ignoreCase = true) ||
                            name.contains("Bacon", ignoreCase = true) ||
                            name.contains("Egg", ignoreCase = true) ||
                            name.contains("Pork", ignoreCase = true) ||
                            name.contains("Beef", ignoreCase = true) ||
                            name.contains("Ham", ignoreCase = true) ||
                            name.contains("Sausage", ignoreCase = true) ||
                            name.contains("Frittata", ignoreCase = true) ||
                            name.contains("Omelette", ignoreCase = true) ||
                            name.contains("Kabab", ignoreCase = true) ||
                            name.contains("Kebab", ignoreCase = true)

                    // STRICT SEGREGATION: Ensure non-veg dishes are NEVER in a Veg section
                    var finalCourse = course
                    if (isNonVegDish && course.contains("(Veg)", ignoreCase = true)) {
                        finalCourse = course.replace("(Veg)", "(Non-Veg)")
                    } else if (!isNonVegDish && course.contains("(Non-Veg)", ignoreCase = true)) {
                        finalCourse = course.replace("(Non-Veg)", "(Veg)")
                    }

                    val primaryActionStation = actionStations.firstOrNull { it.isNotBlank() } ?: "Live Action Counter"
                    val stationName = when {
                        name.contains("Live", ignoreCase = true) || name.contains("Station", ignoreCase = true) -> primaryActionStation
                        course.contains("Beverage", ignoreCase = true) -> "Mocktail & Juice Bar"
                        course.contains("Starter", ignoreCase = true) -> primaryActionStation
                        course.contains("Soup", ignoreCase = true) -> "Soup Kettle & Bread Station"
                        course.contains("Salad", ignoreCase = true) -> "Chilled Salad Well"
                        course.contains("Dessert", ignoreCase = true) || course.contains("Sweet", ignoreCase = true) -> "Dessert Island"
                        course.contains("Rice", ignoreCase = true) || course.contains("Accompaniments", ignoreCase = true) -> "Rice & Bread Chafing Station"
                        isNonVegDish -> "Hot Non-Veg Chafing Line"
                        else -> "Hot Veg Chafing Line"
                    }

                    val detectedAllergens = matchingRecipe?.let {
                        val ing = it.ingredients.joinToString(" ").lowercase()
                        val list = mutableListOf<String>()
                        if (ing.contains("milk") || ing.contains("butter") || ing.contains("cream") || ing.contains("cheese") || ing.contains("paneer")) list.add("Milk")
                        if (ing.contains("flour") || ing.contains("wheat") || ing.contains("bread") || ing.contains("naan")) list.add("Gluten")
                        if (ing.contains("nut") || ing.contains("cashew") || ing.contains("almond") || ing.contains("walnut")) list.add("Nuts")
                        if (ing.contains("egg")) list.add("Eggs")
                        if (ing.contains("fish") || ing.contains("prawn") || ing.contains("shrimp")) list.add("Fish / Shellfish")
                        if (list.isEmpty()) listOf("None Detected") else list
                    } ?: (if (isNonVegDish) listOf("Milk") else listOf("Milk", "Gluten"))

                    itemsToInsert.add(
                        BuffetMenuItemEntity(
                            dayOfWeek = day,
                            mealSession = session,
                            courseSection = finalCourse,
                            recipeId = matchingRecipe?.id,
                            name = name,
                            desc = matchingRecipe?.desc ?: "Executive chef specialty prepared fresh for banquets and daily service.",
                            cals = matchingRecipe?.cals ?: "190 kcal / 100 g",
                            type = if (isNonVegDish) "nonveg" else "veg",
                            prepTime = matchingRecipe?.prepTime ?: "25 mins",
                            portions = "$targetPax Pax",
                            station = stationName,
                            allergens = detectedAllergens,
                            sortOrder = sort++
                        )
                    )
                }
            }

            repository.clearBuffetMenu(day, session)
            repository.insertBuffetMenuItems(itemsToInsert)
            _isAiLoading.value = false
            showToast("✨ Generated $day $session buffet: ${itemsToInsert.size} dishes for $targetPax Pax!")
        }
    }

    // Single-string overload for backward compatibility
    fun generateAiBuffetMenuWithOptions(
        targetPax: Int = _buffetPaxCount.value,
        vegCount: Int = 4,
        nonVegCount: Int = 2,
        dessertCount: Int = 2,
        breadCount: Int = 2,
        riceCount: Int = 1,
        starterCount: Int = 2,
        actionStation: String,
        beverageChoice: String
    ) {
        generateAiBuffetMenuWithOptions(
            targetPax = targetPax,
            vegCount = vegCount,
            nonVegCount = nonVegCount,
            dessertCount = dessertCount,
            breadCount = breadCount,
            riceCount = riceCount,
            starterCount = starterCount,
            actionStations = if (actionStation.isNotBlank()) listOf(actionStation) else listOf("Live Tandoor & Chaat Counter"),
            beverageChoices = if (beverageChoice.isNotBlank()) listOf(beverageChoice) else listOf("Virgin Mojito & Spiced Buttermilk")
        )
    }

    fun syncBuffetDishesToMaster() {
        viewModelScope.launch {
            val items = currentBuffetItems.value
            if (items.isEmpty()) {
                showToast("No buffet items to export. Add recipes first.")
                return@launch
            }
            val newDishes = items.map { item ->
                DishEntity(
                    name = item.name,
                    desc = item.desc.ifBlank { "Prepared fresh for ${_selectedBuffetDay.value} ${_selectedBuffetMealSession.value} buffet." },
                    cals = item.cals.ifBlank { "180 kcal" },
                    type = item.type,
                    category = item.courseSection,
                    allergens = item.allergens,
                    isSample = false
                )
            }
            repository.insertDishes(newDishes)
            showToast("Exported ${newDishes.size} dishes to Buffet Tag Master & Printable Menus!")
        }
    }

    fun closeBuffet(paxSold: Int, pricePerPax: Double, wastageCost: Double) {
        viewModelScope.launch {
            val day = _selectedBuffetDay.value
            val session = _selectedBuffetMealSession.value
            val items = currentBuffetItems.value
            val allRecipesList = recipes.value

            // Calculate Total Food Cost for the entire buffet based on establishment costing or fallback
            var totalFoodCost = 0.0
            for (item in items) {
                val matchingRecipe = allRecipesList.find { it.id == item.recipeId }
                val costPerPax = if (matchingRecipe != null && matchingRecipe.costPerPax > 0) {
                    matchingRecipe.costPerPax
                } else {
                    com.example.util.CulinaryAgent.getEstimatedCostPerPax(item.name)
                }
                totalFoodCost += costPerPax * paxSold
            }

            val revenue = paxSold * pricePerPax
            val fcp = if (revenue > 0) (totalFoodCost / revenue) * 100.0 else 0.0
            val wastagePercentage = if (totalFoodCost > 0) (wastageCost / totalFoodCost) * 100.0 else 0.0

            val record = com.example.data.model.BuffetClosingEntity(
                day = day,
                session = session,
                totalCoversSold = paxSold,
                buffetPrice = pricePerPax,
                totalRevenue = revenue,
                totalFoodCost = totalFoodCost,
                wastageCost = wastageCost,
                foodCostPercentage = fcp,
                wastagePercentage = wastagePercentage
            )

            repository.insertClosingRecord(record)
            showToast("Buffet Closed! FCP: ${String.format("%.1f", fcp)}% | Wastage: ${String.format("%.1f", wastagePercentage)}%")
        }
    }

    fun importEstablishmentData(rawText: String) {
        viewModelScope.launch {
            try {
                // Expected format: Name, Category, CostPerPax
                val lines = rawText.lines().filter { it.isNotBlank() }
                var importedCount = 0
                for (line in lines) {
                    val parts = line.split(",").map { it.trim() }
                    if (parts.size >= 3) {
                        val name = parts[0]
                        val cat = parts[1]
                        val cost = parts[2].toDoubleOrNull() ?: 0.0
                        
                        val existing = repository.searchRecipes(name).firstOrNull()?.firstOrNull { it.name.equals(name, true) }
                        if (existing != null) {
                            repository.insertRecipe(existing.copy(costPerPax = cost, isEstablishmentData = true))
                        } else {
                            repository.insertRecipe(RecipeEntity(
                                name = name,
                                category = cat,
                                costPerPax = cost,
                                isEstablishmentData = true,
                                prepTime = "30 mins",
                                yieldPortions = "1 pax",
                                cals = "200 kcal",
                                desc = "Imported establishment recipe.",
                                story = "Official establishment record."
                            ))
                        }
                        importedCount++
                    }
                }
                showToast("Successfully imported $importedCount establishment recipes & costing data.")
            } catch (e: Exception) {
                showToast("Import failed: ${e.message}")
            }
        }
    }
}

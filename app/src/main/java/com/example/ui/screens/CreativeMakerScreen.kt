package com.example.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.R
import com.example.util.CounterDemandOption
import com.example.util.FoodImageProvider
import com.example.util.FoodPhotoItem
import com.example.util.FoodStationPreset
import com.example.util.FoodVisualTheme
import com.example.util.ModernArtRenderer
import com.example.viewmodel.HotelStudioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Smartphone-Optimized Creative Maker for Restaurant, Banquet, and Buffet Station Signage.
 * Features:
 * - Automatic real food photo generation by dish name matching user counter demand.
 * - 12 User Counter Demand Check presets (Live Tandoor, Live Dosa, Pasta/Pizza, Chaat, Dim Sum, Chafing Mains, etc.).
 * - Instant 1-tap dish suggestion chips that auto-generate authentic photos.
 * - Live interactive preview with orientation toggle and full-screen view.
 * - Zero-permission Android Photo Picker for real kitchen dish uploads.
 * - 300 DPI vector PDF and high-res PNG export for printing and sharing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreativeMakerScreen(viewModel: HotelStudioViewModel? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // 12 User Counter Demand Options
    val counterDemands = FoodImageProvider.COUNTER_DEMAND_OPTIONS
    var selectedCounterDemand by remember { mutableStateOf(counterDemands[0]) }

    // 15 Food Station Presets
    val presets = ModernArtRenderer.FOOD_STATION_PRESETS
    var selectedPresetId by remember { mutableStateOf("tandoor") }

    // Core Signage Content State
    var scriptTitle by remember { mutableStateOf("Paneer Tikka") }
    var stationTag by remember { mutableStateOf("GRILL & TANDOOR") }
    var dietaryTag by remember { mutableStateOf("Chef's Live Selection") }
    var selectedTheme by remember { mutableStateOf(FoodVisualTheme.CHARCOAL_MATTE) }
    var customFoodBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var activeFoodPhotoTitle by remember { mutableStateOf("Charcoal Grill & Kebabs") }
    var activePhotoSourceNote by remember { mutableStateOf("Verified Culinary Plate") }
    var isUserUploadedPhoto by remember { mutableStateOf(false) }
    var autoMatchFoodPhoto by remember { mutableStateOf(true) }
    var isGeneratingDishPhoto by remember { mutableStateOf(false) }
    var onlineFoodSearchQuery by remember { mutableStateOf("") }

    // Design & Layout Styling
    var bannerStyle by remember { mutableStateOf("Classic White Ribbon") }
    var selectedAccentHex by remember { mutableStateOf("#E05A2B") }
    var selectedSize by remember { mutableStateOf("A4 Portrait") }
    var includeLogo by remember { mutableStateOf(false) }

    // UI Navigation & Dialog State
    var activeEditorTab by remember { mutableStateOf(0) } // 0: Counter & Dish, 1: Real Photo Studio, 2: Ribbon & Style
    var showFullScreenDialog by remember { mutableStateOf(false) }
    var isRenderingPreview by remember { mutableStateOf(false) }
    var isExportingPdf by remember { mutableStateOf(false) }
    var isExportingImage by remember { mutableStateOf(false) }
    var isAiGenerating by remember { mutableStateOf(false) }
    var aiPromptInput by remember { mutableStateOf("") }

    // Generated Live Bitmap
    var liveSignageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Establishment logo from user profile
    val userProfileState = viewModel?.userProfile?.collectAsState()
    val rawLogo = userProfileState?.value?.customLogoBase64
    val establishmentLogo: Bitmap? = remember(rawLogo) {
        if (!rawLogo.isNullOrEmpty()) {
            try {
                val decodedBytes = android.util.Base64.decode(rawLogo, android.util.Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    // Photo Picker from smartphone gallery/camera (Zero-permission Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                try {
                    val stream = context.contentResolver.openInputStream(uri)
                    val decoded = BitmapFactory.decodeStream(stream)
                    stream?.close()
                    withContext(Dispatchers.Main) {
                        customFoodBitmap = decoded
                        isUserUploadedPhoto = true
                        activeFoodPhotoTitle = "Uploaded Custom Dish Photo"
                        activePhotoSourceNote = "Camera / Gallery Photo"
                        viewModel?.showToast("Loaded custom kitchen dish photo!")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Could not load image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Primary function: generates real dish photo by dish name checking counter demand
    fun generateAndApplyDishPhoto(dishName: String, counterDemandTitle: String = selectedCounterDemand.title) {
        val targetDish = dishName.trim().ifBlank { scriptTitle.trim().ifBlank { "Signature Dish" } }
        scope.launch {
            isGeneratingDishPhoto = true
            viewModel?.showToast("Finding photo for $targetDish...")
            try {
                val result = FoodImageProvider.generateDishPhotoByDemand(
                    dishName = targetDish,
                    counterDemand = counterDemandTitle,
                    context = context
                )
                customFoodBitmap = result.bitmap
                isUserUploadedPhoto = false
                val matchedRes = FoodImageProvider.findMatchingFoodDrawable(targetDish, counterDemandTitle)
                activeFoodPhotoTitle = FoodImageProvider.getFoodTitleForDrawable(matchedRes)
                activePhotoSourceNote = result.sourceDescription
                viewModel?.showToast("Applied photo for $targetDish")
            } catch (e: Exception) {
                viewModel?.showToast("Could not generate photo: ${e.message}")
            } finally {
                isGeneratingDishPhoto = false
            }
        }
    }

    // Auto-load initial authentic real food photo on screen launch
    LaunchedEffect(Unit) {
        if (customFoodBitmap == null) {
            val resId = FoodImageProvider.findMatchingFoodDrawable(scriptTitle, selectedCounterDemand.title)
            val initialBmp = FoodImageProvider.getBitmapForDrawable(context, resId)
            if (initialBmp != null) {
                customFoodBitmap = initialBmp
                activeFoodPhotoTitle = FoodImageProvider.getFoodTitleForDrawable(resId)
                activePhotoSourceNote = "Verified Culinary Plate"
            }
        }
    }

    // Auto-match real food photo when user finishes typing a dish name (debounced by 600ms)
    LaunchedEffect(scriptTitle) {
        if (autoMatchFoodPhoto && !isUserUploadedPhoto && scriptTitle.isNotBlank()) {
            delay(600)
            generateAndApplyDishPhoto(scriptTitle, selectedCounterDemand.title)
        }
    }

    // Live Render Trigger
    fun updateLivePreview() {
        scope.launch(Dispatchers.Default) {
            isRenderingPreview = true
            val isPortrait = selectedSize.contains("Portrait")
            val w = if (isPortrait) 600 else 840
            val h = if (isPortrait) 840 else 600

            val parsedColor = try {
                AndroidColor.parseColor(selectedAccentHex)
            } catch (e: Exception) {
                AndroidColor.parseColor("#9C7A4A")
            }

            val bmp = ModernArtRenderer.renderFoodStationSignBitmap(
                scriptTitle = scriptTitle.ifBlank { "Signature" },
                stationTag = stationTag.ifBlank { "STATION" },
                dietaryTag = dietaryTag,
                theme = selectedTheme,
                customFoodBitmap = customFoodBitmap,
                bannerStyle = bannerStyle,
                accentColorInt = parsedColor,
                width = w,
                height = h,
                establishmentLogo = establishmentLogo,
                includeLogo = includeLogo
            )

            withContext(Dispatchers.Main) {
                liveSignageBitmap = bmp
                isRenderingPreview = false
            }
        }
    }

    // Re-render preview whenever visual parameters change
    LaunchedEffect(
        scriptTitle,
        stationTag,
        dietaryTag,
        selectedTheme,
        customFoodBitmap,
        bannerStyle,
        selectedAccentHex,
        selectedSize,
        includeLogo
    ) {
        updateLivePreview()
    }

    Box(modifier = Modifier.fillMaxSize().testTag("creative_maker_screen")) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp)
                .padding(top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "FOOD STATION SIGNAGE",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Real Dish Photo Generator & Art Director",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "300 DPI VECTOR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // ==========================================
            // 1. HERO LIVE PREVIEW CARD
            // ==========================================
            val isPortrait = selectedSize.contains("Portrait")
            val previewCardWidth = if (isPortrait) 260.dp else 320.dp
            val previewCardHeight = if (isPortrait) 350.dp else 230.dp

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Preview Card Header with Quick Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.size(8.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIVE SIGN PREVIEW",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Toggle Orientation
                            IconButton(
                                onClick = {
                                    selectedSize = if (selectedSize.contains("Portrait")) {
                                        selectedSize.replace("Portrait", "Landscape")
                                    } else {
                                        selectedSize.replace("Landscape", "Portrait")
                                    }
                                },
                                modifier = Modifier.size(32.dp).testTag("rotate_sign_button")
                            ) {
                                Icon(
                                    Icons.Default.ScreenRotation,
                                    contentDescription = "Rotate Sign",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Full Screen Modal
                            IconButton(
                                onClick = { showFullScreenDialog = true },
                                modifier = Modifier.size(32.dp).testTag("fullscreen_sign_button")
                            ) {
                                Icon(
                                    Icons.Default.Fullscreen,
                                    contentDescription = "Full Screen Preview",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    // Rendered Visual Poster Card
                    Box(
                        modifier = Modifier
                            .width(previewCardWidth)
                            .height(previewCardHeight)
                            .shadow(12.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFF9C7A4A))
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E1E1E))
                            .clickable { showFullScreenDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (liveSignageBitmap != null) {
                            Image(
                                bitmap = liveSignageBitmap!!.asImageBitmap(),
                                contentDescription = "Live Food Station Signage",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            CircularProgressIndicator(
                                color = Color(0xFF9C7A4A),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Rendering overlay spinner
                        if (isRenderingPreview) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.35f),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ==============================================================
                    // REAL PHOTO ACTIVE STATUS & INSTANT GENERATE / REGENERATE BAR
                    // ==============================================================
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Thumbnail preview of current food bitmap
                                    if (customFoodBitmap != null && !customFoodBitmap!!.isRecycled) {
                                        Image(
                                            bitmap = customFoodBitmap!!.asImageBitmap(),
                                            contentDescription = "Active Food Photo",
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Surface(
                                            modifier = Modifier.size(38.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.Restaurant,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (customFoodBitmap != null) "$scriptTitle ($activeFoodPhotoTitle)" else "Procedural Backdrop",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${selectedCounterDemand.iconEmoji} ${selectedCounterDemand.title} • $activePhotoSourceNote",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            // Quick Action Buttons for the Photo
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = {
                                        generateAndApplyDishPhoto(scriptTitle, selectedCounterDemand.title)
                                    },
                                    modifier = Modifier.weight(1.3f).height(36.dp).testTag("auto_generate_dish_photo_button"),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    enabled = !isGeneratingDishPhoto
                                ) {
                                    if (isGeneratingDishPhoto) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(14.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Generating...", fontSize = 11.sp)
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Auto-Generate Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.height(36.dp).testTag("upload_phone_photo_button"),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Phone Photo", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // ==============================================================
            // 2. USER COUNTER DEMAND CHECK (12 BUFFET / RESTAURANT STATIONS)
            // ==============================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "USER COUNTER DEMAND CHECK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = selectedCounterDemand.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "Select your hotel counter demand. We automatically configure the typography, theme, and generate authentic real food photos.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Horizontal Scrollable Counter Demands
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        counterDemands.forEach { demand ->
                            val isSelected = selectedCounterDemand.id == demand.id
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .clickable {
                                        selectedCounterDemand = demand
                                        stationTag = demand.defaultStationTag
                                        dietaryTag = demand.defaultDietary
                                        selectedTheme = demand.recommendedTheme
                                        selectedAccentHex = demand.accentColorHex
                                        // Auto-generate photo for first sample dish
                                        val firstDish = demand.sampleDishes.firstOrNull() ?: demand.title
                                        scriptTitle = firstDish
                                        generateAndApplyDishPhoto(firstDish, demand.title)
                                    }
                                    .testTag("counter_demand_${demand.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(demand.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(modifier = Modifier.widthIn(max = 120.dp)) {
                                        Text(
                                            text = demand.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = demand.subtitle,
                                            fontSize = 9.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 1-Tap Popular Dishes for this Selected Counter Demand
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "1-Tap Dishes for ${selectedCounterDemand.title} (Generates Authentic Photo):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedCounterDemand.sampleDishes.forEach { dish ->
                                AssistChip(
                                    onClick = {
                                        scriptTitle = dish
                                        generateAndApplyDishPhoto(dish, selectedCounterDemand.title)
                                    },
                                    label = {
                                        Text(
                                            text = dish,
                                            fontSize = 11.sp,
                                            fontWeight = if (scriptTitle.equals(dish, ignoreCase = true)) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    modifier = Modifier.testTag("sample_dish_$dish")
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 3. SEGMENTED TABS FOR CREATIVE MAKER
            // ==========================================
            ScrollableTabRow(
                selectedTabIndex = activeEditorTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                edgePadding = 0.dp
            ) {
                Tab(
                    selected = activeEditorTab == 0,
                    onClick = { activeEditorTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1. Dish & Text", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("tab_dish_text")
                )
                Tab(
                    selected = activeEditorTab == 1,
                    onClick = { activeEditorTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("2. Photo Studio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("tab_photo_studio")
                )
                Tab(
                    selected = activeEditorTab == 2,
                    onClick = { activeEditorTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("3. Ribbon & Style", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("tab_ribbon_style")
                )
            }

            // ==========================================
            // TAB 0: DISH & TYPOGRAPHY SETTINGS
            // ==========================================
            if (activeEditorTab == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Station Dual-Typography Settings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        // Script Calligraphy Title (Dish Name) with Inline Generate Button
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Dish Name (Cursive Calligraphy Script)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                TextButton(
                                    onClick = { generateAndApplyDishPhoto(scriptTitle, selectedCounterDemand.title) },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                    enabled = !isGeneratingDishPhoto
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Generate Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            OutlinedTextField(
                                value = scriptTitle,
                                onValueChange = { scriptTitle = it },
                                placeholder = { Text("e.g. Butter Chicken, Truffle Penne, Masala Dosa") },
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(
                                        onClick = { generateAndApplyDishPhoto(scriptTitle, selectedCounterDemand.title) },
                                        enabled = !isGeneratingDishPhoto
                                    ) {
                                        if (isGeneratingDishPhoto) {
                                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        } else {
                                            Icon(Icons.Default.AutoAwesome, contentDescription = "Generate photo for this dish", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("dish_name_input")
                            )

                            // Quick Global Cuisines Chips
                            val quickFoodChips = listOf(
                                Pair("🥘 Butter Chicken", "Butter Chicken"),
                                Pair("🥞 Masala Dosa", "Masala Dosa"),
                                Pair("🍔 Cheeseburger", "Cheeseburger"),
                                Pair("🍜 Hakka Noodles", "Hakka Noodles"),
                                Pair("🍲 Tomato Soup", "Tomato Soup"),
                                Pair("🌮 Mexican Tacos", "Mexican Tacos"),
                                Pair("🍹 Virgin Mojito", "Virgin Mojito"),
                                Pair("☕ Cappuccino", "Cappuccino"),
                                Pair("🍳 Cheese Omelette", "Cheese Omelette"),
                                Pair("🍝 Truffle Penne", "Truffle Penne"),
                                Pair("🍕 Wood-Fired Pizza", "Margherita"),
                                Pair("🍢 Paneer Tikka", "Paneer Tikka"),
                                Pair("🥟 Steamed Dim Sum", "Dim Sum"),
                                Pair("🍲 Dal Makhani", "Dal Makhani"),
                                Pair("🥣 Pani Puri", "Pani Puri"),
                                Pair("🍣 Salmon Sushi", "Sushi"),
                                Pair("🧇 Belgian Waffles", "Belgian Waffles"),
                                Pair("🍰 Gulab Jamun", "Gulab Jamun"),
                                Pair("🥗 Caesar Salad", "Caesar Salad")
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                quickFoodChips.forEach { (label, name) ->
                                    AssistChip(
                                        onClick = {
                                            scriptTitle = name
                                            generateAndApplyDishPhoto(name, selectedCounterDemand.title)
                                        },
                                        label = { Text(label, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        // Station Tag Word (Gold Serif)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Station Tag Word (Gold Serif)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            OutlinedTextField(
                                value = stationTag,
                                onValueChange = { stationTag = it },
                                placeholder = { Text("e.g. BAR, PIZZA, STATION, CORNER, COUNTER, BUFFET") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("station_tag_input")
                            )

                            val quickTags = listOf("STATION", "BAR", "CORNER", "COUNTER", "PIZZA", "BUFFET", "LOVERS", "HEARTH", "GRILL")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                quickTags.forEach { tag ->
                                    AssistChip(
                                        onClick = { stationTag = tag },
                                        label = { Text(tag, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                    )
                                }
                            }
                        }

                        // Dietary / Chef Subtitle Note
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Dietary / Chef Subtitle Note",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            OutlinedTextField(
                                value = dietaryTag,
                                onValueChange = { dietaryTag = it },
                                placeholder = { Text("e.g. 🟢 100% Pure Veg, 🔴 Non-Veg, Chef's Special") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("dietary_tag_input")
                            )

                            val dietaryPresets = listOf("🟢 100% Pure Veg", "🔴 Non-Vegetarian", "🌿 Jain Food", "✨ Chef's Special", "🌶️ Spicy Live", "Clear")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                dietaryPresets.forEach { tag ->
                                    AssistChip(
                                        onClick = {
                                            dietaryTag = if (tag == "Clear") "" else tag
                                        },
                                        label = { Text(tag, fontSize = 10.sp) }
                                    )
                                }
                            }
                        }

                        // Auto-match toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Auto-generate photo as I type dish name", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("Automatically matches photo to dish & counter demand", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = autoMatchFoodPhoto,
                                onCheckedChange = { autoMatchFoodPhoto = it },
                                modifier = Modifier.testTag("auto_match_switch")
                            )
                        }
                    }
                }
            }

            // ==========================================
            // TAB 1: REAL PHOTO STUDIO & GALLERY
            // ==========================================
            if (activeEditorTab == 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Real Food Photography Studio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        // 1. DEDICATED SEARCH & AI GENERATOR FOR ANY DISH
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generate Real Photo for Any Dish", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text(
                                    "Enter any dish name. The AI generator will synthesize or match an authentic professional food photograph aligned with your counter demand.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                OutlinedTextField(
                                    value = onlineFoodSearchQuery,
                                    onValueChange = { onlineFoodSearchQuery = it },
                                    placeholder = { Text("e.g. Lobster Thermidor, Shahi Tukda, Chicken Shawarma") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("custom_dish_photo_query")
                                )

                                Button(
                                    onClick = {
                                        val q = onlineFoodSearchQuery.ifBlank { scriptTitle }
                                        scriptTitle = q
                                        generateAndApplyDishPhoto(q, selectedCounterDemand.title)
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("generate_custom_dish_button"),
                                    enabled = !isGeneratingDishPhoto
                                ) {
                                    if (isGeneratingDishPhoto) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Generating Authentic Dish Photo...")
                                    } else {
                                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Apply Real Photo for Dish")
                                    }
                                }
                            }
                        }

                        // 2. UPLOAD FROM PHONE CAMERA / GALLERY
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Upload Plated Dish from Smartphone", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text(
                                    "Use photos taken by your executive chef or culinary team directly from your smartphone camera or gallery.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                OutlinedButton(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Choose Dish Photo from Phone")
                                }
                            }
                        }

                        // 3. CURATED REAL FOOD PHOTOGRAPHY GALLERY (1-TAP)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Curated Verified Cuisine Gallery", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Text(
                                        "${FoodImageProvider.CURATED_FOOD_PHOTOS.size} Cuisines",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    FoodImageProvider.CURATED_FOOD_PHOTOS.chunked(2).forEach { rowItems ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            rowItems.forEach { item ->
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = MaterialTheme.colorScheme.surface,
                                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            val bmp = FoodImageProvider.getBitmapForDrawable(context, item.drawableRes)
                                                            if (bmp != null) {
                                                                customFoodBitmap = bmp
                                                                isUserUploadedPhoto = false
                                                                activeFoodPhotoTitle = item.title
                                                                activePhotoSourceNote = "Curated Library"
                                                                viewModel?.showToast("Loaded ${item.title}")
                                                            }
                                                        }
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(item.emoji, fontSize = 20.sp)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Column {
                                                            Text(
                                                                text = item.title,
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.SemiBold,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                            Text(
                                                                text = item.category,
                                                                fontSize = 9.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 4. PROCEDURAL THEMED BACKDROP (OPTIONAL ABSTRACT ALTERNATIVE)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Or Use Themed Abstract Backdrop (No Food Photo):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            val themeOptions = listOf(
                                Pair("⬛ Midnight Obsidian", FoodVisualTheme.MIDNIGHT_OBSIDIAN),
                                Pair("⚪ Ivory Silk", FoodVisualTheme.IVORY_SILK),
                                Pair("🟡 Brushed Gold", FoodVisualTheme.BRUSHED_GOLD),
                                Pair("🟢 Emerald Velvet", FoodVisualTheme.EMERALD_VELVET),
                                Pair("🔵 Sapphire Gradient", FoodVisualTheme.SAPPHIRE_GRADIENT),
                                Pair("🔴 Crimson Damask", FoodVisualTheme.CRIMSON_DAMASK),
                                Pair("⚫ Charcoal Matte", FoodVisualTheme.CHARCOAL_MATTE),
                                Pair("🌸 Rose Gold", FoodVisualTheme.ROSE_GOLD),
                                Pair("🧊 Frosted Glass", FoodVisualTheme.FROSTED_GLASS),
                                Pair("🤍 Platinum Mesh", FoodVisualTheme.PLATINUM_MESH),
                                Pair("🟤 Bronze Texture", FoodVisualTheme.BRONZE_TEXTURE),
                                Pair("✨ Pearl Glaze", FoodVisualTheme.PEARL_GLAZE),
                                Pair("🍷 Royal Burgundy", FoodVisualTheme.ROYAL_BURGUNDY),
                                Pair("⚙️ Titanium Weave", FoodVisualTheme.TITANIUM_WEAVE),
                                Pair("🥂 Champagne Sparkle", FoodVisualTheme.CHAMPAGNE_SPARKLE)
                            )

                            themeOptions.forEach { (label, theme) ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selectedTheme == theme && customFoodBitmap == null) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else Color.Transparent,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedTheme = theme
                                            customFoodBitmap = null
                                            viewModel?.showToast("Selected $label abstract backdrop")
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedTheme == theme && customFoodBitmap == null,
                                            onClick = {
                                                selectedTheme = theme
                                                customFoodBitmap = null
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // TAB 2: RIBBON, STYLING & PAPER FRAMING
            // ==========================================
            if (activeEditorTab == 2) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Ribbon & Display Framing",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        // Center Banner Style
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Center Ribbon Banner Style", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            val bannerStyles = listOf("Classic White Ribbon", "Gold Framed Ribbon", "Frosted Glass", "Midnight Luxury")
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                bannerStyles.forEach { style ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (bannerStyle == style) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { bannerStyle = style }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = bannerStyle == style,
                                                onClick = { bannerStyle = style }
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(style, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Serif Accent Color
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Serif Station Tag Accent Tone", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            val accentTones = listOf(
                                Pair("Imperial Gold", "#9C7A4A"),
                                Pair("Champagne Gold", "#C5A059"),
                                Pair("Tandoor Flame", "#E05A2B"),
                                Pair("Antique Bronze", "#8B5A2B"),
                                Pair("Emerald Green", "#2E7D32"),
                                Pair("Rose Gold", "#B76E79"),
                                Pair("Obsidian Slate", "#2B2B2B")
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                accentTones.forEach { (name, hex) ->
                                    val isSelected = selectedAccentHex.equals(hex, ignoreCase = true)
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                                        modifier = Modifier.clickable { selectedAccentHex = hex }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(android.graphics.Color.parseColor(hex)),
                                                modifier = Modifier.size(16.dp)
                                            ) {}
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Paper Dimensions
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Print Stand & Paper Dimensions", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            val sizes = listOf("A4 Portrait", "A4 Landscape", "A3 Portrait", "A3 Landscape")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                sizes.forEach { size ->
                                    FilterChip(
                                        selected = selectedSize == size,
                                        onClick = { selectedSize = size },
                                        label = { Text(size, fontSize = 11.sp) },
                                        leadingIcon = if (selectedSize == size) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                        } else null
                                    )
                                }
                            }
                        }

                        HorizontalDivider()

                        // Establishment Logo Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Include Establishment Logo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    if (establishmentLogo != null) "Using active hotel brand badge" else "No logo uploaded (Can upload in Settings)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = includeLogo && establishmentLogo != null,
                                onCheckedChange = { includeLogo = it },
                                enabled = establishmentLogo != null
                            )
                        }
                    }
                }
            }
        }

        // ==============================================================
        // 4. STICKY BOTTOM ACTION BAR (Print Vector PDF & Share Image)
        // ==============================================================
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Export PDF Button
                Button(
                    onClick = {
                        scope.launch {
                            isExportingPdf = true
                            viewModel?.showToast("Generating print-ready vector PDF for $selectedSize...")
                            try {
                                val parsedColor = try {
                                    AndroidColor.parseColor(selectedAccentHex)
                                } catch (e: Exception) {
                                    AndroidColor.parseColor("#9C7A4A")
                                }

                                val pdfFile = withContext(Dispatchers.IO) {
                                    ModernArtRenderer.exportFoodStationPdf(
                                        context = context,
                                        scriptTitle = scriptTitle,
                                        stationTag = stationTag,
                                        dietaryTag = dietaryTag,
                                        theme = selectedTheme,
                                        customFoodBitmap = customFoodBitmap,
                                        bannerStyle = bannerStyle,
                                        accentColorInt = parsedColor,
                                        paperSize = selectedSize,
                                        logo = establishmentLogo,
                                        includeLogo = includeLogo
                                    )
                                }

                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    pdfFile
                                )

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }

                                try {
                                    context.startActivity(intent)
                                    viewModel?.showToast("Opened food station signage PDF!")
                                } catch (e: Exception) {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "application/pdf"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Buffet Signage PDF"))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isExportingPdf = false
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(48.dp)
                        .testTag("export_pdf_button"),
                    enabled = !isExportingPdf && !isExportingImage,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (isExportingPdf) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Rendering PDF...", fontSize = 12.sp)
                    } else {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export PDF (300 DPI)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Share Image (PNG) Button
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isExportingImage = true
                            viewModel?.showToast("Rendering high-res PNG image...")
                            try {
                                val parsedColor = try {
                                    AndroidColor.parseColor(selectedAccentHex)
                                } catch (e: Exception) {
                                    AndroidColor.parseColor("#9C7A4A")
                                }

                                val imageFile = withContext(Dispatchers.IO) {
                                    ModernArtRenderer.exportFoodStationImage(
                                        context = context,
                                        scriptTitle = scriptTitle,
                                        stationTag = stationTag,
                                        dietaryTag = dietaryTag,
                                        theme = selectedTheme,
                                        customFoodBitmap = customFoodBitmap,
                                        bannerStyle = bannerStyle,
                                        accentColorInt = parsedColor,
                                        paperSize = selectedSize,
                                        logo = establishmentLogo,
                                        includeLogo = includeLogo
                                    )
                                }

                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    imageFile
                                )

                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "image/png"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Food Station Signage"))
                                viewModel?.showToast("Sharing high-res food sign!")
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error exporting image: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isExportingImage = false
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("export_png_button"),
                    enabled = !isExportingPdf && !isExportingImage
                ) {
                    if (isExportingImage) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Saving...", fontSize = 12.sp)
                    } else {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share PNG", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // ==========================================
        // 5. FULL SCREEN PREVIEW DIALOG
        // ==========================================
        if (showFullScreenDialog && liveSignageBitmap != null) {
            Dialog(onDismissRequest = { showFullScreenDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$scriptTitle $stationTag",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { showFullScreenDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = liveSignageBitmap!!.asImageBitmap(),
                                contentDescription = "Full Screen Food Station Signage",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Button(
                            onClick = { showFullScreenDialog = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Editor")
                        }
                    }
                }
            }
        }
    }
}

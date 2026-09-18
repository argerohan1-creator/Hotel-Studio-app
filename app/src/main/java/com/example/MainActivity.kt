package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppDrawerContent
import com.example.ui.components.AppHeader
import com.example.ui.components.HotelStudioBottomNav
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AuditNutritionScreen
import com.example.ui.screens.OutletChecklistScreen
import com.example.ui.screens.BuffetTagMasterScreen
import com.example.ui.screens.HelpDeskScreen
import com.example.ui.screens.MenuBuilderScreen
import com.example.ui.screens.MenuMakerScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.RecipeBankScreen
import com.example.ui.screens.SettingsHubScreen
import com.example.ui.screens.SignageBuilderScreen
import com.example.ui.screens.CreativeMakerScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.TermsOfServiceScreen
import com.example.ui.theme.HotelStudioTheme
import com.example.viewmodel.HotelStudioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private val viewModel: HotelStudioViewModel by viewModels()
    private var globalError by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            runOnUiThread {
                globalError = exception.stackTraceToString()
            }
            // oldHandler?.uncaughtException(thread, exception)
        }
        
        
        enableEdgeToEdge()
        setContent {
            val error = globalError
            if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Red).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn {
                        item {
                            Text(text = "App Crashed:", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = error, color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            } else {
                val userProfile by viewModel.userProfile.collectAsState()
                val activeTheme = userProfile?.activeTheme ?: "midnight"
                HotelStudioTheme(activeTheme = activeTheme) {
                    HotelStudioApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun HotelStudioApp(viewModel: HotelStudioViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activeModule by viewModel.activeModule.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val toastMsg by viewModel.toastMessage.collectAsState()

    // Auto-dismiss toast
    LaunchedEffect(toastMsg) {
        if (toastMsg != null) {
            delay(3000)
            viewModel.clearToast()
        }
    }

    val moduleTitles = mapOf(
        "dashboard" to "Operations Dashboard",
        "buffet" to "Buffet Tag Master",
        "prospectus" to "Daily Buffet Menu Builder",
        "menu_builder" to "Daily Buffet Menu Builder",
        "recipes" to "Global Recipe Bank (100k+)",
        "analytics" to "Audit & Nutrition Hub",
        "signage" to "Board to Read (Signage)",
        "creative_maker" to "Creative Maker",
        "name_tag_creator" to "Name Tag Creator",
        "settings" to "Settings Hub",
        "helpdesk" to "Help Desk & F&B Assistant Support",
        "terms" to "Terms of Service & Disclaimer",
        "privacy" to "Privacy Policy & Legal",
        "about" to "About Us (Rohan Ghosh)"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerContent(
                    activeModule = activeModule,
                    userProfile = userProfile,
                    onNavigate = { moduleId ->
                        viewModel.switchModule(moduleId)
                    },
                    onLoginOrRegister = { name, est, emailOrPhone, isReg ->
                        viewModel.loginOrRegister(name, est, emailOrPhone, isReg)
                    },
                    onLogout = {
                        viewModel.logout()
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    activeModuleTitle = moduleTitles[activeModule] ?: "Buffet Tag Master",
                    activeModule = activeModule,
                    userProfile = userProfile,
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    },
                    onOpenSettings = {
                        viewModel.switchModule("settings")
                    },
                    onNavigate = { targetModule ->
                        viewModel.switchModule(targetModule)
                    }
                )
            },
            bottomBar = {
                HotelStudioBottomNav(
                    activeModule = activeModule,
                    onTabSelected = { moduleId ->
                        viewModel.switchModule(moduleId)
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Active module screen
                when (activeModule) {
                    "dashboard" -> DashboardScreen(viewModel = viewModel)
                    "buffet" -> BuffetTagMasterScreen(viewModel = viewModel)
                    "prospectus", "menu_builder" -> MenuBuilderScreen(viewModel = viewModel)
                    "recipes" -> RecipeBankScreen(viewModel = viewModel)
                    "analytics" -> AuditNutritionScreen(viewModel = viewModel)
                    "signage" -> SignageBuilderScreen(viewModel = viewModel)
                    "creative_maker" -> CreativeMakerScreen(viewModel = viewModel)
                    "name_tag_creator" -> com.example.ui.screens.NameTagCreatorScreen(viewModel = viewModel)
                    "settings" -> SettingsHubScreen(viewModel = viewModel)
                    "helpdesk" -> HelpDeskScreen(viewModel = viewModel)
                    "terms" -> TermsOfServiceScreen()
                    "privacy" -> PrivacyPolicyScreen()
                    "checklists" -> OutletChecklistScreen(viewModel = viewModel)
                    "about" -> AboutUsScreen()
                    else -> DashboardScreen(viewModel = viewModel)
                }

                // Toast notification pill
                AnimatedVisibility(
                    visible = toastMsg != null,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .shadow(12.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF10B981))
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = toastMsg ?: "",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
